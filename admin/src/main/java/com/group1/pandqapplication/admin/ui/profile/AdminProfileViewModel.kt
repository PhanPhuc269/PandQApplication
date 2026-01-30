package com.group1.pandqapplication.admin.ui.profile

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group1.pandqapplication.admin.data.AdminUserManager
import com.group1.pandqapplication.admin.data.repository.AdminAuthRepository
import com.group1.pandqapplication.admin.util.CloudinaryHelper
import com.group1.pandqapplication.shared.data.remote.AdminUserInfo
import com.group1.pandqapplication.shared.data.remote.AppApiService
import com.group1.pandqapplication.shared.data.remote.dto.UpdateUserRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

/**
 * ViewModel for Admin Profile screen.
 * Handles loading profile data from API, editing, and logout.
 */
@HiltViewModel
class AdminProfileViewModel @Inject constructor(
    private val authRepository: AdminAuthRepository,
    private val apiService: AppApiService,
    private val adminUserManager: AdminUserManager,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _profileState = MutableStateFlow<AdminProfileState>(AdminProfileState.Loading)
    val profileState: StateFlow<AdminProfileState> = _profileState.asStateFlow()

    // Editable fields
    private val _firstName = MutableStateFlow("")
    val firstName: StateFlow<String> = _firstName.asStateFlow()

    private val _lastName = MutableStateFlow("")
    val lastName: StateFlow<String> = _lastName.asStateFlow()

    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving.asStateFlow()

    private val _saveMessage = MutableStateFlow<String?>(null)
    val saveMessage: StateFlow<String?> = _saveMessage.asStateFlow()

    private var currentUserId: String = ""

    init {
        loadProfile()
    }

    /**
     * Load current user profile from API.
     */
    fun loadProfile() {
        viewModelScope.launch {
            _profileState.value = AdminProfileState.Loading
            try {
                val userInfo = apiService.getCurrentAuthUser()
                currentUserId = userInfo.id
                
                // Split full name into first and last
                val names = (userInfo.fullName ?: "").split(" ", limit = 2)
                _firstName.value = names.getOrElse(0) { "" }
                _lastName.value = names.getOrElse(1) { "" }
                
                _profileState.value = AdminProfileState.Success(userInfo)
            } catch (e: Exception) {
                val errorMessage = when {
                    e.message?.contains("401") == true -> 
                        "Phiên đăng nhập đã hết hạn. Vui lòng đăng nhập lại."
                    e.message?.contains("network") == true -> 
                        "Lỗi kết nối mạng."
                    else -> e.message ?: "Không thể tải thông tin hồ sơ."
                }
                _profileState.value = AdminProfileState.Error(errorMessage)
            }
        }
    }

    fun onFirstNameChange(value: String) {
        _firstName.value = value
    }

    fun onLastNameChange(value: String) {
        _lastName.value = value
    }

    /**
     * Save profile changes to API.
     */
    fun saveProfile() {
        if (currentUserId.isEmpty()) return
        
        viewModelScope.launch {
            _isSaving.value = true
            _saveMessage.value = null
            
            try {
                val fullName = "${_firstName.value} ${_lastName.value}".trim()
                val currentState = _profileState.value
                val currentAvatarUrl = if (currentState is AdminProfileState.Success) {
                    currentState.user.avatarUrl
                } else null
                
                val request = UpdateUserRequest(
                    fullName = fullName.ifEmpty { null },
                    phone = null,
                    avatarUrl = currentAvatarUrl
                )
                
                val updatedUser = apiService.updateUser(currentUserId, request)
                
                // Update state with new data
                val newUserInfo = AdminUserInfo(
                    id = updatedUser.id,
                    email = updatedUser.email,
                    fullName = updatedUser.fullName,
                    avatarUrl = updatedUser.avatarUrl,
                    role = updatedUser.role ?: "ADMIN"
                )
                _profileState.value = AdminProfileState.Success(newUserInfo)
                adminUserManager.updateUser(newUserInfo) // Sync to shared state
                _saveMessage.value = "Đã lưu thành công!"
            } catch (e: Exception) {
                _saveMessage.value = "Lỗi: ${e.message ?: "Không thể lưu thay đổi"}"
            } finally {
                _isSaving.value = false
            }
        }
    }

    /**
     * Upload avatar image to Cloudinary and update profile.
     */
    fun uploadAvatar(uri: Uri) {
        if (currentUserId.isEmpty()) return
        
        viewModelScope.launch {
            _isSaving.value = true
            _saveMessage.value = null
            
            try {
                // Copy URI to temp file
                val inputStream = context.contentResolver.openInputStream(uri)
                val tempFile = File(context.cacheDir, "avatar_${System.currentTimeMillis()}.jpg")
                inputStream?.use { input ->
                    FileOutputStream(tempFile).use { output ->
                        input.copyTo(output)
                    }
                }
                
                if (!tempFile.exists() || tempFile.length() == 0L) {
                    _saveMessage.value = "Lỗi: Không thể đọc file ảnh"
                    _isSaving.value = false
                    return@launch
                }
                
                // Upload to Cloudinary
                Log.d("AdminProfileViewModel", "Uploading to Cloudinary... file size: ${tempFile.length()} bytes")
                val result = CloudinaryHelper.uploadImage(tempFile)
                
                result.onSuccess { imageUrl ->
                    Log.d("AdminProfileViewModel", "Cloudinary upload success! URL: $imageUrl")
                    
                    // Update user with new avatar URL
                    val request = UpdateUserRequest(
                        fullName = "${_firstName.value} ${_lastName.value}".trim().ifEmpty { null },
                        phone = null,
                        avatarUrl = imageUrl
                    )
                    
                    val updatedUser = apiService.updateUser(currentUserId, request)
                    
                    // Update state with new data and sync to shared state
                    val newUserInfo = AdminUserInfo(
                        id = updatedUser.id,
                        email = updatedUser.email,
                        fullName = updatedUser.fullName,
                        avatarUrl = updatedUser.avatarUrl,
                        role = updatedUser.role ?: "ADMIN"
                    )
                    _profileState.value = AdminProfileState.Success(newUserInfo)
                    adminUserManager.updateUser(newUserInfo)
                    _saveMessage.value = "Đã cập nhật ảnh đại diện!"
                }
                
                result.onFailure { e ->
                    Log.e("AdminProfileViewModel", "Cloudinary upload failed", e)
                    _saveMessage.value = "Lỗi upload: ${e.message}"
                }
                
                // Clean up temp file
                tempFile.delete()
            } catch (e: Exception) {
                Log.e("AdminProfileViewModel", "Upload error", e)
                _saveMessage.value = "Lỗi: ${e.message ?: "Không thể tải ảnh lên"}"
            } finally {
                _isSaving.value = false
            }
        }
    }

    fun clearSaveMessage() {
        _saveMessage.value = null
    }

    /**
     * Logout current user.
     */
    fun logout() {
        authRepository.logout()
    }
}
