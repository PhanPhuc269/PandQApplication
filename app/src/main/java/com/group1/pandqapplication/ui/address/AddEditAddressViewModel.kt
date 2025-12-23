package com.group1.pandqapplication.ui.address

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group1.pandqapplication.shared.data.remote.dto.CreateAddressRequest
import com.group1.pandqapplication.shared.data.remote.dto.UpdateAddressRequest
import com.group1.pandqapplication.shared.data.repository.AddressRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AddEditAddressUiState(
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val receiverName: String = "",
    val phone: String = "",
    val detailAddress: String = "",
    val ward: String = "",
    val district: String = "",
    val city: String = "",
    val isDefault: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null
)

@HiltViewModel
class AddEditAddressViewModel @Inject constructor(
    private val addressRepository: AddressRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddEditAddressUiState())
    val uiState: StateFlow<AddEditAddressUiState> = _uiState.asStateFlow()

    private val addressId: String? = savedStateHandle["addressId"]
    private val currentUserId = "550e8400-e29b-41d4-a716-446655440000"

    val isEditMode: Boolean get() = addressId != null

    init {
        addressId?.let { loadAddress(it) }
    }

    private fun loadAddress(id: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            addressRepository.getAddressById(id)
                .onSuccess { address ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            receiverName = address.receiverName,
                            phone = address.phone,
                            detailAddress = address.detailAddress,
                            ward = address.ward,
                            district = address.district,
                            city = address.city,
                            isDefault = address.isDefault
                        )
                    }
                }
                .onFailure { exception ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = exception.message ?: "Không thể tải thông tin địa chỉ"
                        )
                    }
                }
        }
    }

    fun updateReceiverName(value: String) {
        _uiState.update { it.copy(receiverName = value) }
    }

    fun updatePhone(value: String) {
        _uiState.update { it.copy(phone = value) }
    }

    fun updateDetailAddress(value: String) {
        _uiState.update { it.copy(detailAddress = value) }
    }

    fun updateWard(value: String) {
        _uiState.update { it.copy(ward = value) }
    }

    fun updateDistrict(value: String) {
        _uiState.update { it.copy(district = value) }
    }

    fun updateCity(value: String) {
        _uiState.update { it.copy(city = value) }
    }

    fun toggleDefault() {
        _uiState.update { it.copy(isDefault = !it.isDefault) }
    }

    fun fillFromMapAddress(address: String) {
        // Parse address from LocationIQ format
        // Example: "123 Nguyen Hue, Ward 1, District 1, Ho Chi Minh City, Vietnam"
        val parts = address.split(",").map { it.trim() }
        
        when {
            parts.size >= 4 -> {
                // Full address with detail, ward, district, city
                _uiState.update {
                    it.copy(
                        detailAddress = parts[0],
                        ward = parts.getOrNull(1) ?: "",
                        district = parts.getOrNull(2) ?: "",
                        city = parts.getOrNull(3) ?: ""
                    )
                }
            }
            parts.size == 3 -> {
                // Detail, district, city
                _uiState.update {
                    it.copy(
                        detailAddress = parts[0],
                        district = parts[1],
                        city = parts[2]
                    )
                }
            }
            parts.size == 2 -> {
                // Detail, city
                _uiState.update {
                    it.copy(
                        detailAddress = parts[0],
                        city = parts[1]
                    )
                }
            }
            else -> {
                // Just use as detail address
                _uiState.update {
                    it.copy(detailAddress = address)
                }
            }
        }
    }

    fun saveAddress(onSuccess: () -> Unit) {
        val state = _uiState.value
        
        // Validation
        if (state.receiverName.isBlank()) {
            _uiState.update { it.copy(error = "Vui lòng nhập tên người nhận") }
            return
        }
        if (state.phone.isBlank()) {
            _uiState.update { it.copy(error = "Vui lòng nhập số điện thoại") }
            return
        }
        if (state.detailAddress.isBlank()) {
            _uiState.update { it.copy(error = "Vui lòng nhập địa chỉ chi tiết") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, error = null) }
            
            val result = if (isEditMode) {
                addressRepository.updateAddress(
                    addressId!!,
                    UpdateAddressRequest(
                        receiverName = state.receiverName,
                        phone = state.phone,
                        detailAddress = state.detailAddress,
                        ward = state.ward,
                        district = state.district,
                        city = state.city,
                        isDefault = state.isDefault
                    )
                )
            } else {
                addressRepository.createAddress(
                    CreateAddressRequest(
                        userId = currentUserId,
                        receiverName = state.receiverName,
                        phone = state.phone,
                        detailAddress = state.detailAddress,
                        ward = state.ward,
                        district = state.district,
                        city = state.city,
                        isDefault = state.isDefault
                    )
                )
            }
            
            result
                .onSuccess {
                    _uiState.update {
                        it.copy(
                            isSaving = false,
                            successMessage = "Lưu địa chỉ thành công!"
                        )
                    }
                    onSuccess()
                }
                .onFailure { exception ->
                    _uiState.update {
                        it.copy(
                            isSaving = false,
                            error = exception.message ?: "Không thể lưu địa chỉ"
                        )
                    }
                }
        }
    }

    fun clearMessages() {
        _uiState.update { it.copy(error = null, successMessage = null) }
    }
}
