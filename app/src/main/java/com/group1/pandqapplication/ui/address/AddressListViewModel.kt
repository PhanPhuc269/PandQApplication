package com.group1.pandqapplication.ui.address

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.group1.pandqapplication.shared.data.remote.dto.AddressDto
import com.group1.pandqapplication.shared.data.repository.AddressRepository
import com.group1.pandqapplication.shared.data.repository.UserRepository
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
    private val addressRepository: AddressRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddressListUiState())
    val uiState: StateFlow<AddressListUiState> = _uiState.asStateFlow()

    // Get current user ID from Firebase Auth + backend
    private var currentUserId: String? = null
    
    // Get email from Firebase Auth
    private val currentUserEmail: String?
        get() = FirebaseAuth.getInstance().currentUser?.email

    init {
        loadCurrentUserAndAddresses()
    }
    
    private fun loadCurrentUserAndAddresses() {
        val email = currentUserEmail
        if (email == null) {
            _uiState.update { it.copy(isLoading = false, error = "Vui lòng đăng nhập để xem địa chỉ") }
            return
        }
        
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            userRepository.getUserByEmail(email)
                .onSuccess { user ->
                    currentUserId = user.id
                    loadAddresses()
                }
                .onFailure { exception ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = exception.message ?: "Không thể tải thông tin người dùng"
                        )
                    }
                }
        }
    }

    fun loadAddresses() {
        val userId = currentUserId ?: return
        
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            addressRepository.getAddressesByUserId(userId)
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

    /**
     * Set an address as default for checkout selection
     */
    fun setDefaultAddress(addressId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            addressRepository.setDefaultAddress(addressId)
                .onSuccess {
                    // Reload addresses to reflect the change
                    loadAddresses()
                }
                .onFailure { exception ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = exception.message ?: "Không thể đặt địa chỉ mặc định"
                        )
                    }
                }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
