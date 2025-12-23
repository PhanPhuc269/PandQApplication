package com.group1.pandqapplication.ui.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group1.pandqapplication.data.repository.OrderRepository
import com.group1.pandqapplication.shared.data.remote.dto.OrderHistoryDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class OrderStatus(val displayName: String, val backendValue: String) {
    ALL("Tất cả", "all"),
    PENDING("Chờ xác nhận", "PENDING"),
    CONFIRMED("Đã xác nhận", "CONFIRMED"),
    SHIPPING("Đang giao", "SHIPPING"),
    DELIVERED("Đã giao", "DELIVERED"),
    CANCELLED("Đã hủy", "CANCELLED"),
    RETURNED("Trả hàng", "RETURNED"),
    FAILED("Thất bại", "FAILED")
}

data class OrderHistoryUiState(
    val orders: List<OrderHistoryDto> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedStatus: OrderStatus = OrderStatus.ALL,
    val searchQuery: String = "",
    val currentPage: Int = 0,
    val totalItems: Long = 0L,
    val pageSize: Int = 20
)

@HiltViewModel
class OrderHistoryViewModel @Inject constructor(
    private val orderRepository: OrderRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(OrderHistoryUiState())
    val uiState: StateFlow<OrderHistoryUiState> = _uiState.asStateFlow()

    fun setUserId(userId: String) {
        // Store userId for later use in search
        _userId = userId
        searchOrders(resetPage = true)
    }

    fun onStatusFilterChange(status: OrderStatus) {
        _uiState.update { it.copy(selectedStatus = status) }
        searchOrders(resetPage = true)
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        searchOrders(resetPage = true)
    }

    fun onClearSearch() {
        _uiState.update { it.copy(searchQuery = "") }
        searchOrders(resetPage = true)
    }

    fun loadNextPage() {
        val state = _uiState.value
        val nextPage = state.currentPage + 1
        searchOrders(page = nextPage)
    }

    private var _userId: String = ""

    private fun searchOrders(resetPage: Boolean = false, page: Int? = null) {
        if (_userId.isBlank()) return

        val state = _uiState.value
        val targetPage = when {
            resetPage -> 0
            page != null -> page
            else -> state.currentPage
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            val result = orderRepository.searchOrderHistory(
                userId = _userId,
                status = state.selectedStatus.backendValue,
                query = state.searchQuery.takeIf { it.isNotBlank() },
                page = targetPage,
                size = state.pageSize
            )

            result.onSuccess { response ->
                _uiState.update {
                    it.copy(
                        orders = if (resetPage || page == 0) response.data else it.orders + response.data,
                        currentPage = targetPage,
                        totalItems = response.meta.totalElements,
                        isLoading = false
                    )
                }
            }

            result.onFailure { exception ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = exception.message ?: "Lỗi tải đơn hàng"
                    )
                }
            }
        }
    }
}
