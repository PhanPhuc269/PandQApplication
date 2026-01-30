package com.group1.pandqapplication.admin.ui.analytics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group1.pandqapplication.admin.data.remote.dto.CategorySaleItemDto
import com.group1.pandqapplication.admin.data.remote.dto.DailyRevenueDto
import com.group1.pandqapplication.admin.data.remote.dto.FullAnalyticsDto
import com.group1.pandqapplication.admin.data.remote.dto.SalesOverviewDto
import com.group1.pandqapplication.admin.data.remote.dto.TopProductDto
import com.group1.pandqapplication.admin.data.repository.AnalyticsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

/**
 * ViewModel for AdminAnalyticsScreen (Báo Cáo & Phân Tích).
 */
@HiltViewModel
class AdminAnalyticsViewModel @Inject constructor(
    private val analyticsRepository: AnalyticsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminAnalyticsUiState())

    val uiState: StateFlow<AdminAnalyticsUiState> = _uiState.asStateFlow()

    init {
        loadAnalytics()
    }

    fun loadAnalytics(range: String = "7d") {
        _uiState.update { it.copy(isLoading = true, error = null, selectedRange = range) }

        viewModelScope.launch {
            analyticsRepository.getFullAnalytics(range).collect { result ->
                result.onSuccess { data ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            totalRevenue = data.overview?.totalRevenue ?: BigDecimal.ZERO,
                            revenueChangePercent = data.overview?.revenueChangePercent ?: 0.0,
                            totalOrders = data.overview?.totalOrders ?: 0,
                            ordersChangePercent = data.overview?.ordersChangePercent ?: 0.0,
                            totalProductsSold = data.overview?.totalProductsSold ?: 0,
                            productsChangePercent = data.overview?.productsChangePercent ?: 0.0,
                            newCustomers = data.overview?.newCustomers ?: 0,
                            customersChangePercent = data.overview?.newCustomersChangePercent ?: 0.0,
                            dailyRevenues = data.revenueChart?.dailyRevenues ?: emptyList(),
                            topProducts = data.topProducts?.products ?: emptyList(),
                            categorySales = data.categorySales?.categories ?: emptyList(),
                            error = null
                        )
                    }
                }.onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = error.message ?: "Failed to load analytics"
                        )
                    }
                }
            }
        }
    }

    fun setDateRange(range: String) {
        if (range != _uiState.value.selectedRange) {
            loadAnalytics(range)
        }
    }
    
    fun setReportType(type: String) {
        val range = _uiState.value.selectedRange
        // Parse type to determine what to fetch
        // Format: "Top Sản phẩm (Doanh thu)", "Top Danh mục (Số lượng)"
        val isCategory = type.contains("Danh mục", ignoreCase = true)
        val isRevenue = type.contains("Doanh thu", ignoreCase = true)
        val sortBy = if (isRevenue) "revenue" else "quantity"

        // Clear old data and set loading state
        if (isCategory) {
            _uiState.update { it.copy(categorySales = emptyList(), isLoadingReportData = true) }
        } else {
            _uiState.update { it.copy(topProducts = emptyList(), isLoadingReportData = true) }
        }

        viewModelScope.launch {
            if (isCategory) {
                analyticsRepository.getCategorySales(range, sortBy).collect { result ->
                    result.onSuccess { data ->
                        _uiState.update { it.copy(categorySales = data.categories ?: emptyList(), isLoadingReportData = false) }
                    }.onFailure {
                        _uiState.update { it.copy(isLoadingReportData = false) }
                    }
                }
            } else {
                analyticsRepository.getTopProducts(limit = 4, range = range, sortBy = sortBy).collect { result ->
                    result.onSuccess { data ->
                        _uiState.update { it.copy(topProducts = data.products ?: emptyList(), isLoadingReportData = false) }
                    }.onFailure {
                        _uiState.update { it.copy(isLoadingReportData = false) }
                    }
                }
            }
        }
    }

    fun retry() {
        loadAnalytics(_uiState.value.selectedRange)
    }
}

data class AdminAnalyticsUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val selectedRange: String = "7d",
    val totalRevenue: BigDecimal = BigDecimal.ZERO,
    val revenueChangePercent: Double = 0.0,
    val totalOrders: Long = 0,
    val ordersChangePercent: Double = 0.0,
    val totalProductsSold: Long = 0,
    val productsChangePercent: Double = 0.0,
    val newCustomers: Long = 0,
    val customersChangePercent: Double = 0.0,
    val dailyRevenues: List<DailyRevenueDto> = emptyList(),
    val topProducts: List<TopProductDto> = emptyList(),
    val categorySales: List<CategorySaleItemDto> = emptyList(),
    val isLoadingReportData: Boolean = false
)
