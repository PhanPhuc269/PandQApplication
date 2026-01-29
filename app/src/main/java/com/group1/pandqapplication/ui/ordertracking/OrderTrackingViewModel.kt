package com.group1.pandqapplication.ui.ordertracking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group1.pandqapplication.shared.data.remote.dto.OrderDto
import com.group1.pandqapplication.shared.data.repository.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

import com.group1.pandqapplication.R

data class OrderTrackingUiState(
    val order: OrderDto? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val trackingSteps: List<TrackingStepData> = emptyList(),
    val statusUpdates: List<StatusUpdateData> = emptyList()
)

data class TrackingStepData(
    val titleRes: Int,
    val subtitleRes: Int,
    val isActive: Boolean,
    val iconType: TrackingIconType
)

enum class TrackingIconType {
    ORDER_PLACED,
    CONFIRMED,
    SHIPPING,
    DELIVERED
}

data class StatusUpdateData(
    val time: String,
    val titleRes: Int,
    val subtitleRes: Int? = null,
    val subtitleStr: String? = null,
    val isActive: Boolean
)

@HiltViewModel
class OrderTrackingViewModel @Inject constructor(
    private val orderRepository: OrderRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(OrderTrackingUiState())
    val uiState: StateFlow<OrderTrackingUiState> = _uiState.asStateFlow()

    fun loadOrder(orderId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            orderRepository.getOrderById(orderId)
                .onSuccess { order ->
                    val trackingSteps = generateTrackingSteps(order.status)
                    val statusUpdates = generateStatusUpdates(order)
                    
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            order = order,
                            trackingSteps = trackingSteps,
                            statusUpdates = statusUpdates
                        )
                    }
                }
                .onFailure { exception ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = exception.message ?: "Error loading order"
                        )
                    }
                }
        }
    }

    /**
     * Set order directly from navigation param (passed from OrdersScreen)
     */
    fun setOrder(order: OrderDto) {
        val trackingSteps = generateTrackingSteps(order.status)
        val statusUpdates = generateStatusUpdates(order)
        
        _uiState.update {
            it.copy(
                isLoading = false,
                order = order,
                trackingSteps = trackingSteps,
                statusUpdates = statusUpdates
            )
        }
    }

    private fun generateTrackingSteps(status: String): List<TrackingStepData> {
        val statusUpper = status.uppercase()
        
        val isOrderPlaced = true // Always true if order exists
        val isConfirmed = statusUpper in listOf("CONFIRMED", "SHIPPING", "DELIVERED", "COMPLETED")
        val isShipping = statusUpper in listOf("SHIPPING", "DELIVERED", "COMPLETED")
        val isDelivered = statusUpper in listOf("DELIVERED", "COMPLETED")
        
        return listOf(
            TrackingStepData(
                titleRes = R.string.track_step_placed_title,
                subtitleRes = R.string.track_step_placed_desc,
                isActive = isOrderPlaced,
                iconType = TrackingIconType.ORDER_PLACED
            ),
            TrackingStepData(
                titleRes = R.string.track_step_confirmed_title,
                subtitleRes = R.string.track_step_confirmed_desc,
                isActive = isConfirmed,
                iconType = TrackingIconType.CONFIRMED
            ),
            TrackingStepData(
                titleRes = R.string.track_step_shipping_title,
                subtitleRes = R.string.track_step_shipping_desc,
                isActive = isShipping,
                iconType = TrackingIconType.SHIPPING
            ),
            TrackingStepData(
                titleRes = R.string.track_step_delivered_title,
                subtitleRes = if (isDelivered) R.string.track_step_delivered_success else R.string.track_step_delivered_expected,
                isActive = isDelivered,
                iconType = TrackingIconType.DELIVERED
            )
        )
    }

    private fun generateStatusUpdates(order: OrderDto): List<StatusUpdateData> {
        val updates = mutableListOf<StatusUpdateData>()
        val dateFormat = SimpleDateFormat("HH:mm a", Locale.getDefault())
        val statusUpper = order.status.uppercase()
        
        // Parse order creation date or use current
        val calendar = Calendar.getInstance()
        
        if (statusUpper in listOf("SHIPPING", "DELIVERED", "COMPLETED")) {
            updates.add(
                StatusUpdateData(
                    time = dateFormat.format(calendar.time),
                    titleRes = R.string.status_update_shipping_title,
                    subtitleRes = R.string.status_update_shipping_desc,
                    isActive = statusUpper == "SHIPPING"
                )
            )
        }
        
        if (statusUpper in listOf("CONFIRMED", "SHIPPING", "DELIVERED", "COMPLETED")) {
            calendar.add(Calendar.HOUR, -2)
            updates.add(
                StatusUpdateData(
                    time = dateFormat.format(calendar.time),
                    titleRes = R.string.status_update_confirmed_title,
                    subtitleRes = R.string.status_update_confirmed_desc,
                    isActive = statusUpper == "CONFIRMED"
                )
            )
        }
        
        // Order placed is always shown
        calendar.add(Calendar.HOUR, -1)
        updates.add(
            StatusUpdateData(
                time = dateFormat.format(calendar.time),
                titleRes = R.string.status_update_placed_title,
                subtitleRes = if (order.shippingAddress == null) R.string.status_update_placed_desc_default else null,
                subtitleStr = order.shippingAddress,
                isActive = statusUpper == "PENDING"
            )
        )
        
        return updates
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    companion object {
        fun formatPrice(amount: Double): String {
            return String.format("%,.0f₫", amount).replace(',', '.')
        }
        
        fun formatDate(isoDate: String?): String {
            if (isoDate.isNullOrBlank()) return "N/A"
            return try {
                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val date = inputFormat.parse(isoDate)
                date?.let { outputFormat.format(it) } ?: "N/A"
            } catch (e: Exception) {
                try {
                    isoDate.substring(0, 10).split("-").reversed().joinToString("/")
                } catch (e2: Exception) {
                    "N/A"
                }
            }
        }
        
        fun calculateEstimatedDelivery(createdAt: String?): String {
            return try {
                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val date = inputFormat.parse(createdAt ?: "")
                val calendar = Calendar.getInstance()
                calendar.time = date ?: calendar.time
                calendar.add(Calendar.DAY_OF_MONTH, 3) // Estimated 3 days delivery
                outputFormat.format(calendar.time)
            } catch (e: Exception) {
                "N/A"
            }
        }
    }
}
