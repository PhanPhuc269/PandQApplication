package com.group1.pandqapplication.admin.ui.orders

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
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

data class AdminOrderDetailsUiState(
    val order: OrderDto? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class AdminOrderDetailsViewModel @Inject constructor(
    private val orderRepository: OrderRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminOrderDetailsUiState())
    val uiState: StateFlow<AdminOrderDetailsUiState> = _uiState.asStateFlow()

    fun loadOrder(orderId: String?) {
        if (orderId.isNullOrBlank()) {
            _uiState.update { it.copy(error = "Order ID is required") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            orderRepository.getOrderById(orderId).fold(
                onSuccess = { order ->
                    _uiState.update { 
                        it.copy(
                            order = order,
                            isLoading = false,
                            error = null
                        )
                    }
                },
                onFailure = { exception ->
                    _uiState.update { 
                        it.copy(
                            isLoading = false,
                            error = exception.message ?: "Failed to load order"
                        )
                    }
                }
            )
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    fun assignCarrier(shippingProvider: String, trackingNumber: String? = null) {
        val currentOrder = _uiState.value.order ?: return
        
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            orderRepository.assignCarrier(currentOrder.id, shippingProvider, trackingNumber).fold(
                onSuccess = { updatedOrder ->
                    _uiState.update { 
                        it.copy(
                            order = updatedOrder,
                            isLoading = false,
                            error = null
                        )
                    }
                },
                onFailure = { exception ->
                    _uiState.update { 
                        it.copy(
                            isLoading = false,
                            error = exception.message ?: "Failed to assign carrier"
                        )
                    }
                }
            )
        }
    }

    fun updateOrderStatus(newStatus: String) {
        val currentOrder = _uiState.value.order ?: return
        
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            orderRepository.updateOrderStatus(currentOrder.id, newStatus).fold(
                onSuccess = { updatedOrder ->
                    _uiState.update { 
                        it.copy(
                            order = updatedOrder,
                            isLoading = false,
                            error = null
                        )
                    }
                },
                onFailure = { exception ->
                    _uiState.update { 
                        it.copy(
                            isLoading = false,
                            error = exception.message ?: "Failed to update order status"
                        )
                    }
                }
            )
        }
    }

    companion object {
        fun formatPrice(amount: Double): String {
            val formatter = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
            return formatter.format(amount).replace("₫", "đ")
        }

        fun formatDate(isoDate: String?): String {
            if (isoDate.isNullOrBlank()) return ""
            
            return try {
                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                val outputFormat = SimpleDateFormat("dd/MM/yyyy, HH:mm", Locale.getDefault())
                val date = inputFormat.parse(isoDate)
                date?.let { outputFormat.format(it) } ?: isoDate
            } catch (e: Exception) {
                // If parsing fails, try alternative format
                try {
                    val parts = isoDate.split("T")
                    if (parts.size >= 2) {
                        val dateParts = parts[0].split("-")
                        val timeParts = parts[1].split(":")
                        "${dateParts[2]}/${dateParts[1]}/${dateParts[0]}, ${timeParts[0]}:${timeParts[1]}"
                    } else {
                        isoDate
                    }
                } catch (e: Exception) {
                    isoDate
                }
            }
        }
        
        fun getStatusText(status: String?): String {
            return when (status?.uppercase()) {
                "PENDING" -> "Chờ xử lý"
                "CONFIRMED" -> "Đã xác nhận"
                "SHIPPING" -> "Đang giao"
                "DELIVERED" -> "Đã giao"
                "COMPLETED" -> "Hoàn thành"
                "CANCELLED" -> "Đã huỷ"
                else -> status ?: "Không xác định"
            }
        }
        
        fun getPaymentMethodText(method: String?): String {
            return when (method?.uppercase()) {
                "COD" -> "Thanh toán khi nhận hàng (COD)"
                "ZALOPAY" -> "ZaloPay"
                "MOMO" -> "MoMo"
                "SEPAY" -> "SePay"
                else -> method ?: "Chưa xác định"
            }
        }
    }
}
