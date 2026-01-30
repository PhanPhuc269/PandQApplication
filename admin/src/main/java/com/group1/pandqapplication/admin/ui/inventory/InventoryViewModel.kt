package com.group1.pandqapplication.admin.ui.inventory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group1.pandqapplication.admin.data.repository.InventoryRepository
import com.group1.pandqapplication.shared.data.remote.dto.InventoryItemDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale
import javax.inject.Inject

enum class InventoryFilter(val displayName: String) {
    ALL("Tất cả"),
    IN_STOCK("Còn hàng"),
    LOW_STOCK("Sắp hết"),
    OUT_OF_STOCK("Hết hàng"),
    HAS_RESERVED("Đang giữ")
}

enum class InventorySortBy(val displayName: String) {
    NAME_ASC("Tên A-Z"),
    NAME_DESC("Tên Z-A"),
    QUANTITY_ASC("SL tăng dần"),
    QUANTITY_DESC("SL giảm dần"),
    VALUE_ASC("Giá trị tăng"),
    VALUE_DESC("Giá trị giảm")
}

data class InventoryUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val totalInventoryValue: String = "0 ₫",
    val totalProductsInStock: String = "0",
    val lowStockCount: String = "0",
    val lowStockItems: List<InventoryItemDto> = emptyList(),
    val allItems: List<InventoryItemDto> = emptyList(),
    val filteredItems: List<InventoryItemDto> = emptyList(),
    val searchQuery: String = "",
    val selectedFilter: InventoryFilter = InventoryFilter.ALL,
    val selectedSortBy: InventorySortBy = InventorySortBy.NAME_ASC,
    val showFilterSheet: Boolean = false
)

@HiltViewModel
class InventoryViewModel @Inject constructor(
    private val repository: InventoryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(InventoryUiState())
    val uiState: StateFlow<InventoryUiState> = _uiState.asStateFlow()

    init {
        loadInventoryStats()
    }

    fun loadInventoryStats() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            repository.getInventoryStats()
                .onSuccess { stats ->
                    val formattedValue = formatCurrency(stats.totalInventoryValue)
                    val formattedProducts = formatNumber(stats.totalProductsInStock)
                    
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        totalInventoryValue = formattedValue,
                        totalProductsInStock = formattedProducts,
                        lowStockCount = stats.lowStockCount.toString(),
                        lowStockItems = stats.lowStockItems,
                        allItems = stats.allItems,
                        filteredItems = stats.allItems
                    )
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message ?: "Không thể tải dữ liệu tồn kho"
                    )
                }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
        filterAndSortItems()
    }

    fun onFilterChanged(filter: InventoryFilter) {
        _uiState.value = _uiState.value.copy(selectedFilter = filter)
        filterAndSortItems()
    }

    fun onSortByChanged(sortBy: InventorySortBy) {
        _uiState.value = _uiState.value.copy(selectedSortBy = sortBy)
        filterAndSortItems()
    }

    fun toggleFilterSheet() {
        _uiState.value = _uiState.value.copy(showFilterSheet = !_uiState.value.showFilterSheet)
    }

    fun hideFilterSheet() {
        _uiState.value = _uiState.value.copy(showFilterSheet = false)
    }

    private fun filterAndSortItems() {
        val query = _uiState.value.searchQuery.lowercase().trim()
        val filter = _uiState.value.selectedFilter
        val sortBy = _uiState.value.selectedSortBy

        // Step 1: Search filter
        var filtered = if (query.isEmpty()) {
            _uiState.value.allItems
        } else {
            _uiState.value.allItems.filter { item ->
                item.productName.lowercase().contains(query) ||
                item.productId.lowercase().contains(query) ||
                (item.productSku?.lowercase()?.contains(query) == true)
            }
        }

        // Step 2: Status filter
        filtered = when (filter) {
            InventoryFilter.ALL -> filtered
            InventoryFilter.IN_STOCK -> filtered.filter { it.quantity > 10 }
            InventoryFilter.LOW_STOCK -> filtered.filter { it.quantity in 1..10 }
            InventoryFilter.OUT_OF_STOCK -> filtered.filter { it.quantity == 0 }
            InventoryFilter.HAS_RESERVED -> filtered.filter { (it.reservedQuantity ?: 0) > 0 }
        }

        // Step 3: Sort
        filtered = when (sortBy) {
            InventorySortBy.NAME_ASC -> filtered.sortedBy { it.productName.lowercase() }
            InventorySortBy.NAME_DESC -> filtered.sortedByDescending { it.productName.lowercase() }
            InventorySortBy.QUANTITY_ASC -> filtered.sortedBy { it.quantity }
            InventorySortBy.QUANTITY_DESC -> filtered.sortedByDescending { it.quantity }
            InventorySortBy.VALUE_ASC -> filtered.sortedBy { (it.productPrice ?: 0.0) * it.quantity }
            InventorySortBy.VALUE_DESC -> filtered.sortedByDescending { (it.productPrice ?: 0.0) * it.quantity }
        }

        _uiState.value = _uiState.value.copy(filteredItems = filtered)
    }

    private fun filterItems() {
        filterAndSortItems()
    }

    private fun formatCurrency(value: Double): String {
        return when {
            value >= 1_000_000_000 -> String.format("%.1f tỷ ₫", value / 1_000_000_000)
            value >= 1_000_000 -> String.format("%.0f tr", value / 1_000_000)
            else -> {
                val format = NumberFormat.getNumberInstance(Locale("vi", "VN"))
                "${format.format(value)} ₫"
            }
        }
    }

    private fun formatNumber(value: Int): String {
        val format = NumberFormat.getNumberInstance(Locale("vi", "VN"))
        return format.format(value)
    }

    fun formatItemValue(quantity: Int, price: Double?): String {
        val value = (price ?: 0.0) * quantity
        return formatCurrency(value)
    }
}
