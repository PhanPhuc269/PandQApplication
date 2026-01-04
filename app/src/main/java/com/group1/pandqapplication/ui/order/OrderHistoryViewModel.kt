package com.group1.pandqapplication.ui.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group1.pandqapplication.shared.data.remote.dto.OrderDto
import com.group1.pandqapplication.shared.data.repository.AuthRepository
import com.group1.pandqapplication.shared.data.repository.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

data class OrderUiState(
    val isLoading: Boolean = true,
    val orders: List<OrderDto> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class OrderHistoryViewModel @Inject constructor(
    private val orderRepository: OrderRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(OrderUiState())
    val uiState: StateFlow<OrderUiState> = _uiState.asStateFlow()

    init {
        loadOrders()
    }

    fun loadOrders() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            val firebaseUid = authRepository.getCurrentFirebaseUid()
            if (firebaseUid == null) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Vui lòng đăng nhập để xem lịch sử đơn hàng"
                )
                return@launch
            }

            val result = orderRepository.getOrdersByUserId(firebaseUid)
            result.onSuccess { orders ->
                // Filter out PENDING orders (which are cart items)
                val completedOrders = orders.filter { it.status != "PENDING" }
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    orders = completedOrders
                )
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = error.message ?: "Không thể tải danh sách đơn hàng"
                )
            }
        }
    }

    fun refresh() {
        loadOrders()
    }

    companion object {
        // Map backend status to Vietnamese labels
        fun mapStatusToVietnamese(status: String): String {
            return when (status.uppercase()) {
                "PENDING" -> "Chờ xử lý"
                "CONFIRMED" -> "Đã xác nhận"
                "SHIPPING" -> "Đang giao"
                "DELIVERED" -> "Đã giao"
                "COMPLETED" -> "Hoàn thành"
                "CANCELLED" -> "Đã huỷ"
                "RETURNED" -> "Đã trả hàng"
                "FAILED" -> "Thất bại"
                else -> status
            }
        }

        // Format date from ISO to dd/MM/yyyy
        fun formatDate(isoDate: String?): String {
            if (isoDate.isNullOrBlank()) return "N/A"
            return try {
                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val date = inputFormat.parse(isoDate)
                date?.let { outputFormat.format(it) } ?: "N/A"
            } catch (e: Exception) {
                // Try parsing without time
                try {
                    val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    val date = inputFormat.parse(isoDate.substring(0, 10))
                    date?.let { outputFormat.format(it) } ?: "N/A"
                } catch (e2: Exception) {
                    "N/A"
                }
            }
        }
    }
}
