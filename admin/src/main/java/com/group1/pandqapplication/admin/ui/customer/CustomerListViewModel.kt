package com.group1.pandqapplication.admin.ui.customer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group1.pandqapplication.admin.data.remote.dto.*
import com.group1.pandqapplication.admin.data.repository.CustomerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// Sort options
enum class SortOption(val label: String) {
    NAME_ASC("Tên A-Z"),
    NAME_DESC("Tên Z-A"),
    SPENT_HIGH("Chi tiêu cao nhất"),
    SPENT_LOW("Chi tiêu thấp nhất"),
    NEWEST("Mới nhất"),
    OLDEST("Cũ nhất")
}

data class CustomerListUiState(
    val isLoading: Boolean = false,
    val customers: List<CustomerListItemDto> = emptyList(),
    val stats: CustomerStatsDto? = null,
    val error: String? = null,
    val currentPage: Int = 0,
    val totalPages: Int = 0,
    val searchQuery: String = "",
    val selectedTier: CustomerTier? = null,
    val selectedStatus: AccountStatus? = null,
    val sortOption: SortOption = SortOption.NEWEST
)

@HiltViewModel
class CustomerListViewModel @Inject constructor(
    private val repository: CustomerRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CustomerListUiState())
    val uiState: StateFlow<CustomerListUiState> = _uiState.asStateFlow()

    init {
        loadCustomers()
        loadStats()
    }

    fun loadCustomers(page: Int = 0) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            repository.getCustomers(
                page = page,
                size = 20,
                search = _uiState.value.searchQuery.takeIf { it.isNotEmpty() },
                tier = _uiState.value.selectedTier,
                status = _uiState.value.selectedStatus
            ).collect { result ->
                result.onSuccess { response ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            customers = response.customers,
                            currentPage = response.currentPage,
                            totalPages = response.totalPages
                        )
                    }
                }.onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = error.message ?: "Unknown error"
                        )
                    }
                }
            }
        }
    }

    fun loadStats() {
        viewModelScope.launch {
            repository.getCustomerStats().collect { result ->
                result.onSuccess { stats ->
                    _uiState.update { it.copy(stats = stats) }
                }
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun performSearch() {
        loadCustomers(page = 0)
    }

    fun filterByTier(tier: CustomerTier?) {
        _uiState.update { it.copy(selectedTier = tier) }
        loadCustomers(page = 0)
    }

    fun filterByStatus(status: AccountStatus?) {
        _uiState.update { it.copy(selectedStatus = status) }
        loadCustomers(page = 0)
    }

    fun setSortOption(option: SortOption) {
        _uiState.update { currentState ->
            val sorted = when (option) {
                SortOption.NAME_ASC -> currentState.customers.sortedBy { it.fullName?.lowercase() ?: "" }
                SortOption.NAME_DESC -> currentState.customers.sortedByDescending { it.fullName?.lowercase() ?: "" }
                SortOption.SPENT_HIGH -> currentState.customers.sortedByDescending { it.totalSpent ?: 0.0 }
                SortOption.SPENT_LOW -> currentState.customers.sortedBy { it.totalSpent ?: 0.0 }
                SortOption.NEWEST -> currentState.customers.sortedByDescending { it.createdAt ?: "" }
                SortOption.OLDEST -> currentState.customers.sortedBy { it.createdAt ?: "" }
            }
            currentState.copy(customers = sorted, sortOption = option)
        }
    }

    fun clearFilters() {
        _uiState.update {
            it.copy(
                searchQuery = "",
                selectedTier = null,
                selectedStatus = null,
                sortOption = SortOption.NEWEST
            )
        }
        loadCustomers(page = 0)
    }
}
