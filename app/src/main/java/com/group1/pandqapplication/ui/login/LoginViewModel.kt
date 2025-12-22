package com.group1.pandqapplication.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group1.pandqapplication.shared.data.repository.AuthRepository
import com.group1.pandqapplication.shared.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun login(email: String, password: String) {
        val trimmedEmail = email.trim()
        if (!validateInput(trimmedEmail, password)) return

        viewModelScope.launch {
            authRepository.login(trimmedEmail, password).collect { result ->
                processResult(result)
            }
        }
    }

    fun register(email: String, password: String) {
        val trimmedEmail = email.trim()
        if (!validateInput(trimmedEmail, password)) return

        viewModelScope.launch {
            authRepository.register(trimmedEmail, password).collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                    }
                    is Result.Success -> {
                        // Send verification email after successful registration
                        sendVerificationEmail()
                        _uiState.update { it.copy(isLoading = false, isLoggedIn = true) }
                    }
                    is Result.Error -> {
                        _uiState.update { it.copy(isLoading = false, errorMessage = result.message) }
                    }
                }
            }
        }
    }

    private fun sendVerificationEmail() {
        viewModelScope.launch {
            authRepository.sendEmailVerification().collect { result ->
                if (result is Result.Success) {
                    _uiState.update { it.copy(errorMessage = "Đã gửi email xác thực! Vui lòng kiểm tra hộp thư.") }
                }
            }
        }
    }

    private fun validateInput(email: String, password: String): Boolean {
        if (email.isEmpty() || password.isEmpty()) {
            _uiState.update { it.copy(errorMessage = "Vui lòng nhập đầy đủ email và mật khẩu.") }
            return false
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _uiState.update { it.copy(errorMessage = "Email không hợp lệ.") }
            return false
        }
        if (password.length < 6) {
             _uiState.update { it.copy(errorMessage = "Mật khẩu phải có ít nhất 6 ký tự.") }
             return false
        }
        return true
    }

    private fun processResult(result: Result<Boolean>) {
        when (result) {
            is Result.Loading -> {
                _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            }
            is Result.Success -> {
                _uiState.update { it.copy(isLoading = false, isLoggedIn = true) }
            }
            is Result.Error -> {
                _uiState.update { it.copy(isLoading = false, errorMessage = result.message) }
            }
        }
    }

    fun signInWithGoogle(idToken: String) {
        viewModelScope.launch {
            authRepository.signInWithGoogle(idToken).collect { result ->
                processResult(result)
            }
        }
    }

    fun onErrorMessageShown() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
