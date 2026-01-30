package com.group1.pandqapplication.admin.ui.customer

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group1.pandqapplication.admin.data.remote.dto.AccountStatus
import com.group1.pandqapplication.admin.data.remote.dto.CustomerDetailDto
import com.group1.pandqapplication.admin.data.repository.CustomerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CustomerDetailUiState(
    val isLoading: Boolean = false,
    val customer: CustomerDetailDto? = null,
    val error: String? = null,
    val isStatusUpdating: Boolean = false
)

@HiltViewModel
class CustomerDetailViewModel @Inject constructor(
    private val repository: CustomerRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(CustomerDetailUiState())
    val uiState: StateFlow<CustomerDetailUiState> = _uiState.asStateFlow()

    val customerId: String? = savedStateHandle["customerId"]

    init {
        customerId?.let { loadCustomerDetail(it) }
    }

    fun loadCustomerDetail(id: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            repository.getCustomerDetail(id).collect { result ->
                result.onSuccess { customer ->
                    _uiState.update { it.copy(isLoading = false, customer = customer) }
                }.onFailure { error ->
                    _uiState.update { it.copy(isLoading = false, error = error.message) }
                }
            }
        }
    }

    fun updateStatus(status: AccountStatus) {
        val id = _uiState.value.customer?.id ?: return
        
        viewModelScope.launch {
            _uiState.update { it.copy(isStatusUpdating = true) }
            
            repository.updateCustomerStatus(id, status).collect { result ->
                result.onSuccess {
                    // Refresh data
                    loadCustomerDetail(id)
                    _uiState.update { it.copy(isStatusUpdating = false) }
                }.onFailure { error ->
                    _uiState.update { 
                        it.copy(
                            isStatusUpdating = false,
                            error = "Failed to update status: ${error.message}"
                        ) 
                    }
                }
            }
        }
    }
}
