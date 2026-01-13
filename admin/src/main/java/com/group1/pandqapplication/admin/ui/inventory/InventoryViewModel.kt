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

data class InventoryUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val totalInventoryValue: String = "0 ₫",
    val totalProductsInStock: String = "0",
    val lowStockCount: String = "0",
    val lowStockItems: List<InventoryItemDto> = emptyList(),
    val allItems: List<InventoryItemDto> = emptyList(),
    val filteredItems: List<InventoryItemDto> = emptyList(),
    val searchQuery: String = ""
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
        filterItems()
    }

    private fun filterItems() {
        val query = _uiState.value.searchQuery.lowercase().trim()
        val filtered = if (query.isEmpty()) {
            _uiState.value.allItems
        } else {
            _uiState.value.allItems.filter { item ->
                item.productName.lowercase().contains(query) ||
                item.productId.lowercase().contains(query) ||
                (item.productSku?.lowercase()?.contains(query) == true)
            }
        }
        _uiState.value = _uiState.value.copy(filteredItems = filtered)
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
