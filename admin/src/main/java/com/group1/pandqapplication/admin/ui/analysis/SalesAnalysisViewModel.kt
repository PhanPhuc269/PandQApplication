package com.group1.pandqapplication.admin.ui.analysis

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group1.pandqapplication.admin.data.remote.dto.CategorySalesDto
import com.group1.pandqapplication.admin.data.remote.dto.FullAnalyticsDto
import com.group1.pandqapplication.admin.data.remote.dto.RevenueChartDto
import com.group1.pandqapplication.admin.data.remote.dto.SalesOverviewDto
import com.group1.pandqapplication.admin.data.remote.dto.TopProductsDto
import com.group1.pandqapplication.admin.data.repository.AnalyticsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for SalesAnalysisScreen.
 * Manages UI state and data loading from backend.
 */
@HiltViewModel
class SalesAnalysisViewModel @Inject constructor(
    private val analyticsRepository: AnalyticsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SalesAnalysisUiState())
    val uiState: StateFlow<SalesAnalysisUiState> = _uiState.asStateFlow()

    init {
        loadAnalytics()
    }

    /**
     * Load all analytics data
     */
    fun loadAnalytics(range: String = "30d") {
        _uiState.update { it.copy(isLoading = true, error = null, selectedRange = range) }

        viewModelScope.launch {
            analyticsRepository.getFullAnalytics(range).collect { result ->
                result.onSuccess { data ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            overview = data.overview,
                            revenueChart = data.revenueChart,
                            topProducts = data.topProducts,
                            categorySales = data.categorySales,
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

    /**
     * Change date range filter
     */
    fun setDateRange(range: String) {
        if (range != _uiState.value.selectedRange) {
            loadAnalytics(range)
        }
    }

    /**
     * Retry loading after error
     */
    fun retry() {
        loadAnalytics(_uiState.value.selectedRange)
    }
}

/**
 * UI State for Sales Analysis screen
 */
data class SalesAnalysisUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val selectedRange: String = "30d",
    val overview: SalesOverviewDto? = null,
    val revenueChart: RevenueChartDto? = null,
    val topProducts: TopProductsDto? = null,
    val categorySales: CategorySalesDto? = null
)
