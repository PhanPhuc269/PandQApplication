package com.group1.pandqapplication.ui.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group1.pandqapplication.shared.data.repository.AuthRepository
import com.group1.pandqapplication.shared.data.repository.NotificationRepository
import com.group1.pandqapplication.shared.util.Result
import com.group1.pandqapplication.util.FcmHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val notificationRepository: NotificationRepository
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
                        // Register FCM token after registration
                        registerFcmToken()
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
                // Register FCM token after successful login
                registerFcmToken()
            }
            is Result.Error -> {
                _uiState.update { it.copy(isLoading = false, errorMessage = result.message) }
            }
        }
    }

    /**
     * Register FCM token with backend after login/register.
     * Uses email and Firebase UID to link user, then sends FCM token.
     * Note: Uses GlobalScope with NonCancellable to ensure the API call completes
     * even after the ViewModel is destroyed during navigation.
     */
    private fun registerFcmToken() {
        kotlinx.coroutines.GlobalScope.launch(kotlinx.coroutines.Dispatchers.IO + kotlinx.coroutines.NonCancellable) {
            try {
                val email = authRepository.getCurrentUserEmail()
                val firebaseUid = authRepository.getCurrentFirebaseUid()
                
                if (email.isNullOrEmpty()) {
                    Log.w("FCM", "Cannot register FCM token: no email")
                    return@launch
                }

                // Get FCM token
                val fcmToken = FcmHelper.getToken()
                Log.d("FCM", "Got FCM token: ${fcmToken.take(20)}...")
                Log.d("FCM", "Firebase UID: $firebaseUid")

                // Send token to backend with Firebase UID for linking
                val result = notificationRepository.updateFcmTokenByEmail(email, fcmToken, firebaseUid)
                
                if (result.isSuccess) {
                    Log.d("FCM", "FCM token registered successfully")
                } else {
                    Log.e("FCM", "Failed to register FCM token: ${result.exceptionOrNull()?.message}")
                }
            } catch (e: Exception) {
                Log.e("FCM", "Error registering FCM token", e)
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

