package com.group1.pandqapplication.admin.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

sealed class ChangePasswordState {
    data object Idle : ChangePasswordState()
    data object Loading : ChangePasswordState()
    data object Success : ChangePasswordState()
    data class Error(val message: String) : ChangePasswordState()
}

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _state = MutableStateFlow<ChangePasswordState>(ChangePasswordState.Idle)
    val state: StateFlow<ChangePasswordState> = _state.asStateFlow()

    fun changePassword(currentPassword: String, newPassword: String, confirmPassword: String) {
        // Validation
        when {
            currentPassword.isBlank() -> {
                _state.value = ChangePasswordState.Error("Vui lòng nhập mật khẩu hiện tại")
                return
            }
            newPassword.isBlank() -> {
                _state.value = ChangePasswordState.Error("Vui lòng nhập mật khẩu mới")
                return
            }
            newPassword.length < 6 -> {
                _state.value = ChangePasswordState.Error("Mật khẩu mới phải có ít nhất 6 ký tự")
                return
            }
            newPassword != confirmPassword -> {
                _state.value = ChangePasswordState.Error("Xác nhận mật khẩu không khớp")
                return
            }
            currentPassword == newPassword -> {
                _state.value = ChangePasswordState.Error("Mật khẩu mới phải khác mật khẩu hiện tại")
                return
            }
        }

        viewModelScope.launch {
            _state.value = ChangePasswordState.Loading
            try {
                val user = firebaseAuth.currentUser
                if (user == null || user.email == null) {
                    _state.value = ChangePasswordState.Error("Phiên đăng nhập đã hết hạn")
                    return@launch
                }

                // Re-authenticate with current password
                val credential = EmailAuthProvider.getCredential(user.email!!, currentPassword)
                user.reauthenticate(credential).await()

                // Update to new password
                user.updatePassword(newPassword).await()

                _state.value = ChangePasswordState.Success
            } catch (e: Exception) {
                val errorMessage = when {
                    e.message?.contains("wrong-password") == true ||
                    e.message?.contains("invalid-credential") == true -> 
                        "Mật khẩu hiện tại không đúng"
                    e.message?.contains("requires-recent-login") == true -> 
                        "Vui lòng đăng nhập lại để thay đổi mật khẩu"
                    e.message?.contains("weak-password") == true -> 
                        "Mật khẩu mới quá yếu"
                    else -> "Lỗi: ${e.message ?: "Không thể đổi mật khẩu"}"
                }
                _state.value = ChangePasswordState.Error(errorMessage)
            }
        }
    }

    fun resetState() {
        _state.value = ChangePasswordState.Idle
    }
}
