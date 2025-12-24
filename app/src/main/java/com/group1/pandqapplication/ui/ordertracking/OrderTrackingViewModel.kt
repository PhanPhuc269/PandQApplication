package com.group1.pandqapplication.ui.ordertracking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group1.pandqapplication.data.repository.OrderRepository
import com.group1.pandqapplication.shared.data.remote.dto.OrderHistoryDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import java.time.LocalDateTime

data class TrackingUpdate(
    val timestamp: LocalDateTime,
    val title: String,
    val description: String,
    val isCompleted: Boolean = false
)

data class TrackingStep(
    val status: String,
    val label: String,
    val isCompleted: Boolean = false,
    val isCurrent: Boolean = false
)

data class OrderTrackingUiState(
    val isLoading: Boolean = false,
    val order: OrderHistoryDto? = null,
    val error: String? = null,
    val trackingSteps: List<TrackingStep> = emptyList(),
    val trackingUpdates: List<TrackingUpdate> = emptyList()
)

@HiltViewModel
class OrderTrackingViewModel @Inject constructor(
    private val orderRepository: OrderRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(OrderTrackingUiState())
    val uiState: StateFlow<OrderTrackingUiState> = _uiState.asStateFlow()

    fun loadOrderTracking(orderId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val order = orderRepository.getOrderDetail(orderId)
                val trackingSteps = generateTrackingSteps(order.status)
                val trackingUpdates = generateTrackingUpdates(order.status)
                
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    order = order,
                    trackingSteps = trackingSteps,
                    trackingUpdates = trackingUpdates,
                    error = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Không thể tải thông tin theo dõi"
                )
            }
        }
    }

    private fun generateTrackingSteps(status: String): List<TrackingStep> {
        val allSteps = listOf(
            TrackingStep("PENDING", "Đã đặt hàng"),
            TrackingStep("CONFIRMED", "Đã xác nhận"),
            TrackingStep("SHIPPING", "Đang vận chuyển"),
            TrackingStep("DELIVERED", "Đã giao hàng")
        )

        val statusOrder = listOf("PENDING", "CONFIRMED", "SHIPPING", "DELIVERED")
        val currentIndex = statusOrder.indexOf(status.uppercase())

        return allSteps.mapIndexed { index, step ->
            step.copy(
                isCompleted = index < currentIndex,
                isCurrent = index == currentIndex
            )
        }
    }

    private fun generateTrackingUpdates(status: String): List<TrackingUpdate> {
        return when (status.uppercase()) {
            "DELIVERED" -> listOf(
                TrackingUpdate(
                    timestamp = LocalDateTime.now(),
                    title = "Đã giao hàng",
                    description = "Đơn hàng của bạn đã được giao thành công",
                    isCompleted = true
                ),
                TrackingUpdate(
                    timestamp = LocalDateTime.now().minusHours(2),
                    title = "Tài xế đang giao hàng",
                    description = "Đơn hàng của bạn đang trên đường đến",
                    isCompleted = true
                ),
                TrackingUpdate(
                    timestamp = LocalDateTime.now().minusHours(4),
                    title = "Đơn hàng đã rời kho",
                    description = "Bắt đầu vận chuyển từ kho phân loại",
                    isCompleted = true
                ),
                TrackingUpdate(
                    timestamp = LocalDateTime.now().minusHours(6),
                    title = "Đơn hàng đã được xác nhận",
                    description = "Chúng tôi đang chuẩn bị hàng",
                    isCompleted = true
                )
            )
            "SHIPPING" -> listOf(
                TrackingUpdate(
                    timestamp = LocalDateTime.now(),
                    title = "Tài xế đang giao hàng",
                    description = "Đơn hàng của bạn sẽ được giao trong hôm nay",
                    isCompleted = true
                ),
                TrackingUpdate(
                    timestamp = LocalDateTime.now().minusHours(2),
                    title = "Đơn hàng đã rời kho",
                    description = "Kho phân loại tại TP. Hồ Chí Minh",
                    isCompleted = true
                ),
                TrackingUpdate(
                    timestamp = LocalDateTime.now().minusHours(4),
                    title = "Đơn hàng đã đến kho",
                    description = "Kho tổng tại Hà Nội",
                    isCompleted = true
                ),
                TrackingUpdate(
                    timestamp = LocalDateTime.now().minusHours(6),
                    title = "Đơn hàng được xác nhận",
                    description = "Chúng tôi đang chuẩn bị hàng",
                    isCompleted = true
                )
            )
            "CONFIRMED" -> listOf(
                TrackingUpdate(
                    timestamp = LocalDateTime.now().minusHours(1),
                    title = "Đơn hàng được xác nhận",
                    description = "Chúng tôi đang chuẩn bị hàng của bạn",
                    isCompleted = true
                ),
                TrackingUpdate(
                    timestamp = LocalDateTime.now().minusHours(2),
                    title = "Đơn hàng nhận được",
                    description = "Đơn hàng của bạn đã được tiếp nhận",
                    isCompleted = true
                )
            )
            else -> listOf(
                TrackingUpdate(
                    timestamp = LocalDateTime.now(),
                    title = "Đơn hàng nhận được",
                    description = "Đơn hàng của bạn đã được tiếp nhận",
                    isCompleted = true
                )
            )
        }
    }
}
