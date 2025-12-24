package com.group1.pandqapplication.ui.ordertracking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group1.pandqapplication.data.repository.OrderRepository
import com.group1.pandqapplication.shared.data.remote.dto.OrderHistoryDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class OrderDetailUiState(
    val isLoading: Boolean = false,
    val order: OrderHistoryDto? = null,
    val error: String? = null,
    val actionSuccess: String? = null
)

class OrderDetailViewModel(
    private val orderRepository: OrderRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(OrderDetailUiState())
    val uiState: StateFlow<OrderDetailUiState> = _uiState.asStateFlow()

    fun loadOrderDetail(orderId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                // Note: This assumes OrderRepository has a getOrderDetail method
                // If not, you'll need to add it
                val order = orderRepository.getOrderDetail(orderId)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    order = order,
                    error = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    order = null,
                    error = e.message ?: "Không thể tải thông tin đơn hàng"
                )
            }
        }
    }

    fun cancelOrder() {
        val orderId = _uiState.value.order?.id ?: return
        viewModelScope.launch {
            try {
                // Note: Need to add cancelOrder method to OrderRepository
                // orderRepository.cancelOrder(orderId)
                _uiState.value = _uiState.value.copy(
                    actionSuccess = "Đơn hàng đã được hủy"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Không thể hủy đơn hàng: ${e.message}"
                )
            }
        }
    }

    fun submitReview(rating: Int, comment: String) {
        val orderId = _uiState.value.order?.id ?: return
        viewModelScope.launch {
            try {
                // Note: Need to add submitReview method to OrderRepository
                // orderRepository.submitReview(orderId, rating, comment)
                _uiState.value = _uiState.value.copy(
                    actionSuccess = "Cảm ơn bạn đã đánh giá"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Không thể gửi đánh giá: ${e.message}"
                )
            }
        }
    }

    fun reorder() {
        viewModelScope.launch {
            try {
                val orderId = _uiState.value.order?.id ?: return@launch
                // Note: Need to add reorder method to OrderRepository
                // orderRepository.reorder(orderId)
                _uiState.value = _uiState.value.copy(
                    actionSuccess = "Đơn hàng đã được thêm vào giỏ hàng"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Không thể thực hiện lại đơn hàng: ${e.message}"
                )
            }
        }
    }

    fun requestReturn() {
        val orderId = _uiState.value.order?.id ?: return
        viewModelScope.launch {
            try {
                // Note: Need to add requestReturn method to OrderRepository
                // orderRepository.requestReturn(orderId)
                _uiState.value = _uiState.value.copy(
                    actionSuccess = "Yêu cầu trả hàng đã được gửi"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Không thể gửi yêu cầu trả hàng: ${e.message}"
                )
            }
        }
    }

    fun clearMessage() {
        _uiState.value = _uiState.value.copy(
            error = null,
            actionSuccess = null
        )
    }
}
