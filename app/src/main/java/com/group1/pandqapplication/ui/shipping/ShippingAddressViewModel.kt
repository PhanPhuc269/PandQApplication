package com.group1.pandqapplication.ui.shipping

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group1.pandqapplication.shared.data.remote.ApiService
import com.group1.pandqapplication.shared.data.remote.dto.AddressDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ShippingAddressUiState(
    val addresses: List<AddressDto> = emptyList(),
    val selectedAddressId: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class ShippingAddressViewModel @Inject constructor(
    private val apiService: ApiService
) : ViewModel() {

    private val _uiState = MutableStateFlow(ShippingAddressUiState())
    val uiState: StateFlow<ShippingAddressUiState> = _uiState.asStateFlow()

    fun loadUserAddresses(userId: String) {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                
                // Fetch user data which includes addresses
                val response = apiService.getUserById(userId)
                
                _uiState.update { 
                    it.copy(
                        addresses = response.addresses ?: emptyList(),
                        selectedAddressId = response.addresses?.firstOrNull { addr -> addr.isDefault }?.id,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Failed to load addresses"
                    )
                }
            }
        }
    }

    fun selectAddress(addressId: String) {
        _uiState.update { it.copy(selectedAddressId = addressId) }
    }

    fun setDefaultAddress(addressId: String) {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }
                
                // Update the addresses list to mark the selected one as default
                val updatedAddresses = _uiState.value.addresses.map { address ->
                    address.copy(isDefault = address.id == addressId)
                }
                
                _uiState.update { 
                    it.copy(
                        addresses = updatedAddresses,
                        selectedAddressId = addressId,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Failed to set default address"
                    )
                }
            }
        }
    }

    fun addNewAddress(address: AddressDto) {
        val newAddresses = _uiState.value.addresses.toMutableList()
        
        // If this is set as default, unset other defaults
        if (address.isDefault) {
            newAddresses.replaceAll { it.copy(isDefault = false) }
        }
        
        newAddresses.add(address)
        _uiState.update { it.copy(addresses = newAddresses) }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
