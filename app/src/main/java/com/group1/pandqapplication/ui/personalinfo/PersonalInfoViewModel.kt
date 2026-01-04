package com.group1.pandqapplication.ui.personalinfo

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.group1.pandqapplication.shared.data.remote.dto.UserDto
import com.group1.pandqapplication.shared.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PersonalInfoUiState(
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val isUploadingImage: Boolean = false,
    val user: UserDto? = null,
    val fullName: String = "",
    val phone: String = "",
    val avatarUrl: String = "",
    val selectedImageUri: Uri? = null,
    val error: String? = null,
    val successMessage: String? = null
)

@HiltViewModel
class PersonalInfoViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PersonalInfoUiState())
    val uiState: StateFlow<PersonalInfoUiState> = _uiState.asStateFlow()

    // Get current user email from Firebase Auth
    private val currentUserEmail: String?
        get() = FirebaseAuth.getInstance().currentUser?.email

    init {
        loadUserData()
    }

    fun loadUserData() {
        val email = currentUserEmail
        if (email == null) {
            _uiState.update { 
                it.copy(
                    isLoading = false,
                    error = "Vui lòng đăng nhập để xem thông tin cá nhân"
                ) 
            }
            return
        }
        
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            userRepository.getUserByEmail(email)
                .onSuccess { user ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            user = user,
                            fullName = user.fullName ?: "",
                            phone = user.phone ?: "",
                            avatarUrl = user.avatarUrl ?: ""
                        )
                    }
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

    fun updateFullName(value: String) {
        _uiState.update { it.copy(fullName = value) }
    }

    fun updatePhone(value: String) {
        _uiState.update { it.copy(phone = value) }
    }

    fun updateAvatarUrl(value: String) {
        _uiState.update { it.copy(avatarUrl = value, selectedImageUri = null) }
    }
    
    fun setUploadingImage(isUploading: Boolean) {
        _uiState.update { it.copy(isUploadingImage = isUploading) }
    }
    
    /**
     * Upload avatar URL and immediately save to backend
     */
    fun updateAvatarAndSave(url: String) {
        val currentUser = _uiState.value.user ?: run {
            _uiState.update { it.copy(isUploadingImage = false, error = "Không tìm thấy thông tin người dùng") }
            return
        }
        
        viewModelScope.launch {
            _uiState.update { it.copy(avatarUrl = url, selectedImageUri = null) }
            
            userRepository.updateUser(
                id = currentUser.id,
                fullName = _uiState.value.fullName,
                phone = _uiState.value.phone,
                avatarUrl = url
            )
                .onSuccess { updatedUser ->
                    _uiState.update {
                        it.copy(
                            isUploadingImage = false,
                            user = updatedUser
                        )
                    }
                }
                .onFailure { exception ->
                    _uiState.update {
                        it.copy(
                            isUploadingImage = false,
                            error = exception.message ?: "Không thể cập nhật ảnh đại diện"
                        )
                    }
                }
        }
    }

    fun selectImage(uri: Uri) {
        _uiState.update { 
            it.copy(
                selectedImageUri = uri,
                avatarUrl = uri.toString() // Temporary preview
            ) 
        }
    }

    fun saveChanges() {
        val currentUser = _uiState.value.user ?: return
        
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, error = null, successMessage = null) }
            
            userRepository.updateUser(
                id = currentUser.id,
                fullName = _uiState.value.fullName,
                phone = _uiState.value.phone,
                avatarUrl = _uiState.value.avatarUrl
            )
                .onSuccess { updatedUser ->
                    _uiState.update {
                        it.copy(
                            isSaving = false,
                            user = updatedUser,
                            successMessage = "Cập nhật thông tin thành công!"
                        )
                    }
                }
                .onFailure { exception ->
                    _uiState.update {
                        it.copy(
                            isSaving = false,
                            error = exception.message ?: "Không thể cập nhật thông tin"
                        )
                    }
                }
        }
    }

    fun clearMessages() {
        _uiState.update { it.copy(error = null, successMessage = null) }
    }
}
