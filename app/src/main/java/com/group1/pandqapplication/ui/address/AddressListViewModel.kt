package com.group1.pandqapplication.ui.address

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group1.pandqapplication.shared.data.remote.dto.AddressDto
import com.group1.pandqapplication.shared.data.repository.AddressRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AddressListUiState(
    val isLoading: Boolean = false,
    val addresses: List<AddressDto> = emptyList(),
    val error: String? = null,
    val showDeleteDialog: Boolean = false,
    val addressToDelete: AddressDto? = null
)

@HiltViewModel
class AddressListViewModel @Inject constructor(
    private val addressRepository: AddressRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddressListUiState())
    val uiState: StateFlow<AddressListUiState> = _uiState.asStateFlow()

    // Hardcoded userId for demo - should come from auth
    private val currentUserId = "550e8400-e29b-41d4-a716-446655440000"

    init {
        loadAddresses()
    }

    fun loadAddresses() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            addressRepository.getAddressesByUserId(currentUserId)
                .onSuccess { addresses ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            addresses = addresses
                        )
                    }
                }
                .onFailure { exception ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = exception.message ?: "Không thể tải danh sách địa chỉ"
                        )
                    }
                }
        }
    }

    fun showDeleteDialog(address: AddressDto) {
        _uiState.update {
            it.copy(
                showDeleteDialog = true,
                addressToDelete = address
            )
        }
    }

    fun hideDeleteDialog() {
        _uiState.update {
            it.copy(
                showDeleteDialog = false,
                addressToDelete = null
            )
        }
    }

    fun deleteAddress() {
        val addressToDelete = _uiState.value.addressToDelete ?: return
        
        viewModelScope.launch {
            addressRepository.deleteAddress(addressToDelete.id)
                .onSuccess {
                    hideDeleteDialog()
                    loadAddresses() // Reload list
                }
                .onFailure { exception ->
                    _uiState.update {
                        it.copy(
                            error = exception.message ?: "Không thể xóa địa chỉ",
                            showDeleteDialog = false,
                            addressToDelete = null
                        )
                    }
                }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
