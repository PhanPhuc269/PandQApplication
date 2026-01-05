package com.group1.pandqapplication.admin.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group1.pandqapplication.admin.data.remote.AdminApiService
import com.group1.pandqapplication.admin.data.remote.dto.DashboardSummaryResponse
import com.group1.pandqapplication.admin.data.remote.dto.RecentActivityResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DashboardUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val summary: DashboardSummaryResponse? = null
)

@HiltViewModel
class AdminDashboardViewModel @Inject constructor(
    private val adminApiService: AdminApiService
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        loadDashboardData()
    }

    fun loadDashboardData() {
        viewModelScope.launch {
            _uiState.value = DashboardUiState(isLoading = true)
            try {
                val summary = adminApiService.getDashboardSummary()
                _uiState.value = DashboardUiState(isLoading = false, summary = summary)
            } catch (e: Exception) {
                _uiState.value = DashboardUiState(isLoading = false, error = e.message)
            }
        }
    }
}
