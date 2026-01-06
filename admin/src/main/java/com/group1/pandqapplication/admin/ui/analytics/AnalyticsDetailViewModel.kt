package com.group1.pandqapplication.admin.ui.analytics

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group1.pandqapplication.admin.data.remote.dto.CategorySaleItemDto
import com.group1.pandqapplication.admin.data.remote.dto.TopProductDto
import com.group1.pandqapplication.admin.data.repository.AnalyticsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnalyticsDetailViewModel @Inject constructor(
    private val analyticsRepository: AnalyticsRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(AnalyticsDetailUiState())
    val uiState: StateFlow<AnalyticsDetailUiState> = _uiState.asStateFlow()

    private val reportType: String = savedStateHandle["reportType"] ?: "Top Sản phẩm (Doanh thu)"
    private val range: String = savedStateHandle["range"] ?: "30d"

    init {
        _uiState.update { it.copy(reportType = reportType) }
        loadData()
    }

    fun loadData() {
        _uiState.update { it.copy(isLoading = true, error = null) }
        
        viewModelScope.launch {
            if (reportType.contains("danh mục")) {
                loadCategories()
            } else {
                loadProducts()
            }
        }
    }

    private suspend fun loadCategories() {
        val sortBy = if (reportType.contains("Doanh thu")) "revenue" else "quantity"
        analyticsRepository.getCategorySales(range = range, sortBy = sortBy).collect { result ->
            result.onSuccess { data ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        categories = data.categories ?: emptyList()
                    )
                }
            }.onFailure { error ->
                _uiState.update { it.copy(isLoading = false, error = error.message) }
            }
        }
    }

    private suspend fun loadProducts() {
        val sortBy = if (reportType.contains("Doanh thu")) "revenue" else "quantity"
        
        // Fetch top 50 products for "View All"
        analyticsRepository.getTopProducts(limit = 50, range = range, sortBy = sortBy).collect { result ->
            result.onSuccess { data ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        products = data.products ?: emptyList()
                    )
                }
            }.onFailure { error ->
                _uiState.update { it.copy(isLoading = false, error = error.message) }
            }
        }
    }
}

data class AnalyticsDetailUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val reportType: String = "",
    val products: List<TopProductDto> = emptyList(),
    val categories: List<CategorySaleItemDto> = emptyList()
)
