package com.group1.pandqapplication.admin.ui.shipping

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group1.pandqapplication.admin.data.remote.AdminApiService
import com.group1.pandqapplication.admin.data.remote.dto.AssignCarrierRequest
import com.group1.pandqapplication.admin.data.remote.dto.ShippingOrderDto
import com.group1.pandqapplication.admin.data.remote.dto.UpdateShippingStatusRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ShippingUiState(
    val orders: List<ShippingOrderDto> = emptyList(),
    val filteredOrders: List<ShippingOrderDto> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedTab: ShippingTab = ShippingTab.PENDING,
    val searchQuery: String = "",
    val pendingCount: Int = 0,
    val shippingCount: Int = 0,
    val completedCount: Int = 0,
    val cancelledCount: Int = 0,
    
    // Dialog states
    val showAssignCarrierDialog: Boolean = false,
    val showUpdateStatusDialog: Boolean = false,
    val selectedOrder: ShippingOrderDto? = null,
    val isProcessing: Boolean = false
)

enum class ShippingTab {
    PENDING,      // Chờ xử lý (PENDING + CONFIRMED)
    SHIPPING,     // Đang giao
    COMPLETED,    // Hoàn thành (DELIVERED + COMPLETED)
    CANCELLED     // Đã huỷ
}

@HiltViewModel
class ShippingViewModel @Inject constructor(
    private val apiService: AdminApiService
) : ViewModel() {

    private val _uiState = MutableStateFlow(ShippingUiState())
    val uiState: StateFlow<ShippingUiState> = _uiState.asStateFlow()

    init {
        loadOrders()
    }

    fun loadOrders() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val orders = apiService.getAllOrders()
                updateOrdersAndCounts(orders)
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    private fun updateOrdersAndCounts(orders: List<ShippingOrderDto>) {
        val pendingCount = orders.count { it.status == "PENDING" || it.status == "CONFIRMED" }
        val shippingCount = orders.count { it.status == "SHIPPING" }
        val completedCount = orders.count { it.status == "DELIVERED" || it.status == "COMPLETED" }
        val cancelledCount = orders.count { it.status == "CANCELLED" }

        _uiState.update { state ->
            state.copy(
                orders = orders,
                isLoading = false,
                pendingCount = pendingCount,
                shippingCount = shippingCount,
                completedCount = completedCount,
                cancelledCount = cancelledCount
            )
        }
        filterOrders()
    }

    fun selectTab(tab: ShippingTab) {
        _uiState.update { it.copy(selectedTab = tab) }
        filterOrders()
    }

    fun setSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        filterOrders()
    }

    private fun filterOrders() {
        val state = _uiState.value
        val filtered = state.orders.filter { order ->
            // Filter by tab
            val matchesTab = when (state.selectedTab) {
                ShippingTab.PENDING -> order.status == "PENDING" || order.status == "CONFIRMED"
                ShippingTab.SHIPPING -> order.status == "SHIPPING"
                ShippingTab.COMPLETED -> order.status == "DELIVERED" || order.status == "COMPLETED"
                ShippingTab.CANCELLED -> order.status == "CANCELLED"
            }

            // Filter by search query
            val matchesSearch = if (state.searchQuery.isBlank()) true else {
                order.id.contains(state.searchQuery, ignoreCase = true) ||
                parseCustomerName(order.shippingAddress).contains(state.searchQuery, ignoreCase = true)
            }

            matchesTab && matchesSearch
        }
        _uiState.update { it.copy(filteredOrders = filtered) }
    }

    // Parse customer name from JSON shippingAddress
    private fun parseCustomerName(shippingAddress: String?): String {
        if (shippingAddress.isNullOrBlank()) return ""
        return try {
            // Simple parse - look for "fullName" or "name" field
            val nameRegex = """"(?:fullName|name)"\s*:\s*"([^"]+)"""".toRegex()
            nameRegex.find(shippingAddress)?.groupValues?.get(1) ?: ""
        } catch (e: Exception) {
            ""
        }
    }

    // Show assign carrier dialog
    fun showAssignCarrierDialog(order: ShippingOrderDto) {
        _uiState.update { it.copy(showAssignCarrierDialog = true, selectedOrder = order) }
    }

    fun dismissAssignCarrierDialog() {
        _uiState.update { it.copy(showAssignCarrierDialog = false, selectedOrder = null) }
    }

    // Assign carrier to order
    fun assignCarrier(shippingProvider: String, trackingNumber: String? = null) {
        val orderId = _uiState.value.selectedOrder?.id ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(isProcessing = true) }
            try {
                val request = AssignCarrierRequest(shippingProvider, trackingNumber)
                apiService.assignCarrier(orderId, request)
                dismissAssignCarrierDialog()
                loadOrders() // Refresh
            } catch (e: Exception) {
                _uiState.update { it.copy(isProcessing = false, error = e.message) }
            }
        }
    }

    // Show update status dialog
    fun showUpdateStatusDialog(order: ShippingOrderDto) {
        _uiState.update { it.copy(showUpdateStatusDialog = true, selectedOrder = order) }
    }

    fun dismissUpdateStatusDialog() {
        _uiState.update { it.copy(showUpdateStatusDialog = false, selectedOrder = null) }
    }

    // Update shipping status
    fun updateStatus(newStatus: String) {
        val orderId = _uiState.value.selectedOrder?.id ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(isProcessing = true) }
            try {
                val request = UpdateShippingStatusRequest(newStatus)
                apiService.updateShippingStatus(orderId, request)
                dismissUpdateStatusDialog()
                loadOrders() // Refresh
            } catch (e: Exception) {
                _uiState.update { it.copy(isProcessing = false, error = e.message) }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
