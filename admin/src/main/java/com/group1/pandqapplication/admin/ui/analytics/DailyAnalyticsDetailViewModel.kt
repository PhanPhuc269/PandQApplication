package com.group1.pandqapplication.admin.ui.analytics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group1.pandqapplication.admin.data.remote.dto.CategoryRevenueDetailDto
import com.group1.pandqapplication.admin.data.remote.dto.DailyAnalyticsDetailDto
import com.group1.pandqapplication.admin.data.remote.dto.ProductRevenueDetailDto
import com.group1.pandqapplication.admin.data.repository.AnalyticsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class DailyAnalyticsDetailViewModel @Inject constructor(
    private val analyticsRepository: AnalyticsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DailyAnalyticsDetailUiState())
    val uiState: StateFlow<DailyAnalyticsDetailUiState> = _uiState.asStateFlow()

    fun loadDailyDetail(date: String) {
        _uiState.update { it.copy(isLoading = true, error = null) }
        
        viewModelScope.launch {
            try {
                val response = analyticsRepository.getDailyAnalyticsDetail(date)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        date = response.date,
                        totalRevenue = response.totalRevenue,
                        orderCount = response.orderCount,
                        categories = response.categories,
                        selectedCategory = if (response.categories.isNotEmpty()) response.categories[0] else null,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load daily analytics"
                    )
                }
            }
        }
    }

    fun selectCategory(category: CategoryRevenueDetailDto?) {
        _uiState.update { it.copy(selectedCategory = category) }
    }
}

data class DailyAnalyticsDetailUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val date: String = "",
    val totalRevenue: BigDecimal = BigDecimal.ZERO,
    val orderCount: Long = 0,
    val categories: List<CategoryRevenueDetailDto> = emptyList(),
    val selectedCategory: CategoryRevenueDetailDto? = null
)
