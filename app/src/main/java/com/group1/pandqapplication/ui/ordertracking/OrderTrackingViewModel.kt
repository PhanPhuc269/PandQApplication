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
    val description: String = "",
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
                val trackingUpdates = generateTrackingUpdates(order.status, order.createdAt)
                
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

    // Set order directly from navigation parameter
    fun setOrder(order: OrderHistoryDto) {
        val trackingSteps = generateTrackingSteps(order.status)
        val trackingUpdates = generateTrackingUpdates(order.status, order.createdAt)
        
        _uiState.value = _uiState.value.copy(
            isLoading = false,
            order = order,
            trackingSteps = trackingSteps,
            trackingUpdates = trackingUpdates,
            error = null
        )
    }

    private fun generateTrackingSteps(status: String): List<TrackingStep> {
        val allSteps = listOf(
            TrackingStep("PENDING", "Đã đặt hàng", description = "Đơn hàng của bạn đã được tiếp nhận"),
            TrackingStep("CONFIRMED", "Đã xác nhận", description = "Chúng tôi đang chuẩn bị đơn hàng"),
            TrackingStep("SHIPPING", "Đang vận chuyển", description = "Đã giao cho đơn vị vận chuyển"),
            TrackingStep("DELIVERED", "Đã giao hàng", description = "Dự kiến giao trong hôm nay")
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

    private fun generateTrackingUpdates(status: String, orderDate: LocalDateTime): List<TrackingUpdate> {
        return when (status.uppercase()) {
            "DELIVERED" -> listOf(
                TrackingUpdate(
                    timestamp = orderDate.plusDays(4).withHour(14).withMinute(30),
                    title = "Đã giao hàng",
                    description = "Đơn hàng của bạn đã được giao thành công",
                    isCompleted = true
                ),
                TrackingUpdate(
                    timestamp = orderDate.plusDays(4).withHour(10).withMinute(0),
                    title = "Tài xế đang giao hàng",
                    description = "Đơn hàng của bạn sẽ sớm được giao",
                    isCompleted = true
                ),
                TrackingUpdate(
                    timestamp = orderDate.plusDays(3).withHour(9).withMinute(5),
                    title = "Đơn hàng đã đến kho",
                    description = "Kho phân loại tại TP. Hồ Chí Minh",
                    isCompleted = true
                ),
                TrackingUpdate(
                    timestamp = orderDate.plusDays(2).withHour(2).withMinute(0),
                    title = "Đơn hàng rời kho",
                    description = "Kho tổng tại Hà Nội",
                    isCompleted = true
                )
            )
            "SHIPPING" -> listOf(
                TrackingUpdate(
                    timestamp = orderDate.plusDays(3).withHour(10).withMinute(0),
                    title = "Tài xế đang giao hàng",
                    description = "Đơn hàng của bạn sẽ sớm được giao",
                    isCompleted = true
                ),
                TrackingUpdate(
                    timestamp = orderDate.plusDays(2).withHour(9).withMinute(5),
                    title = "Đơn hàng đã đến kho",
                    description = "Kho phân loại tại TP. Hồ Chí Minh",
                    isCompleted = true
                ),
                TrackingUpdate(
                    timestamp = orderDate.plusDays(1).withHour(2).withMinute(0),
                    title = "Đơn hàng rời kho",
                    description = "Kho tổng tại Hà Nội",
                    isCompleted = true
                )
            )
            "CONFIRMED" -> listOf(
                TrackingUpdate(
                    timestamp = orderDate.plusHours(2),
                    title = "Đơn hàng được xác nhận",
                    description = "Chúng tôi đang chuẩn bị hàng của bạn",
                    isCompleted = true
                ),
                TrackingUpdate(
                    timestamp = orderDate,
                    title = "Đặt hàng thành công",
                    description = "Đơn hàng của bạn đã được tiếp nhận",
                    isCompleted = true
                )
            )
            else -> listOf(
                TrackingUpdate(
                    timestamp = orderDate,
                    title = "Đặt hàng thành công",
                    description = "Đơn hàng của bạn đã được tiếp nhận",
                    isCompleted = true
                )
            )
        }
    }
}
