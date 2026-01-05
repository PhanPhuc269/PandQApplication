package com.group1.pandqapplication.admin.ui.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.group1.pandqapplication.admin.data.repository.AdminAuthRepository
import com.group1.pandqapplication.admin.data.repository.AdminAuthResult
import com.group1.pandqapplication.admin.util.AdminFcmHelper
import com.group1.pandqapplication.shared.data.repository.NotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/**
 * ViewModel for admin login screen.
 * Handles authentication state and login operations.
 */
@HiltViewModel
class AdminAuthViewModel @Inject constructor(
    private val authRepository: AdminAuthRepository,
    private val firebaseAuth: FirebaseAuth,
    private val notificationRepository: NotificationRepository
) : ViewModel() {

    private val _authState = MutableStateFlow<AdminAuthState>(AdminAuthState.Idle)
    val authState: StateFlow<AdminAuthState> = _authState.asStateFlow()

    // Separate state for password reset
    private val _passwordResetState = MutableStateFlow<PasswordResetState>(PasswordResetState.Idle)
    val passwordResetState: StateFlow<PasswordResetState> = _passwordResetState.asStateFlow()

    init {
        checkExistingSession()
    }

    /**
     * Check if there's an existing Firebase session.
     * If yes, transition to Authenticated state for biometric unlock.
     */
    private fun checkExistingSession() {
        if (authRepository.hasExistingSession()) {
            _authState.value = AdminAuthState.Authenticated(
                email = authRepository.getCurrentSessionEmail() ?: "",
                displayName = authRepository.getCurrentSessionDisplayName()
            )
        }
    }

    /**
     * Login with email and password.
     */
    fun login(email: String, password: String) {
        if (email.isBlank()) {
            _authState.value = AdminAuthState.Error("Vui lòng nhập email")
            return
        }
        if (password.isBlank()) {
            _authState.value = AdminAuthState.Error("Vui lòng nhập mật khẩu")
            return
        }

        viewModelScope.launch {
            authRepository.loginAndVerifyAdmin(email, password).collect { result ->
                _authState.value = when (result) {
                    is AdminAuthResult.Loading -> AdminAuthState.Loading
                    is AdminAuthResult.Success -> {
                        // Register FCM token after successful login
                        registerFcmToken(result.response.user?.email ?: email)
                        AdminAuthState.Success(
                            email = result.response.user?.email ?: "",
                            displayName = result.response.user?.fullName,
                            role = result.response.user?.role ?: "ADMIN"
                        )
                    }
                    is AdminAuthResult.Error -> AdminAuthState.Error(result.message)
                }
            }
        }
    }

    /**
     * Register FCM token with backend after successful login.
     * Uses GlobalScope to ensure completion even if ViewModel is destroyed.
     */
    private fun registerFcmToken(email: String) {
        GlobalScope.launch(Dispatchers.IO + NonCancellable) {
            try {
                val firebaseUid = firebaseAuth.currentUser?.uid
                val fcmToken = AdminFcmHelper.getToken()
                
                Log.d("AdminFCM", "Registering FCM token for admin: ${email}")
                Log.d("AdminFCM", "FCM Token: ${fcmToken.take(20)}...")
                Log.d("AdminFCM", "Firebase UID: $firebaseUid")

                val result = notificationRepository.updateFcmTokenByEmail(email, fcmToken, firebaseUid)
                
                if (result.isSuccess) {
                    Log.d("AdminFCM", "Admin FCM token registered successfully!")
                    // Also subscribe to admin topic for broadcast notifications
                    AdminFcmHelper.subscribeToAdminTopic()
                } else {
                    Log.e("AdminFCM", "Failed to register admin FCM token: ${result.exceptionOrNull()?.message}")
                }
            } catch (e: Exception) {
                Log.e("AdminFCM", "Error registering admin FCM token", e)
            }
        }
    }

    /**
     * Send password reset email via Firebase.
     */
    fun sendPasswordResetEmail(email: String) {
        if (email.isBlank()) {
            _passwordResetState.value = PasswordResetState.Error("Vui lòng nhập email")
            return
        }

        viewModelScope.launch {
            _passwordResetState.value = PasswordResetState.Loading
            try {
                firebaseAuth.sendPasswordResetEmail(email).await()
                _passwordResetState.value = PasswordResetState.Success(email)
            } catch (e: Exception) {
                val errorMessage = when {
                    e.message?.contains("no user record") == true ->
                        "Email không tồn tại trong hệ thống."
                    e.message?.contains("invalid-email") == true ->
                        "Email không hợp lệ."
                    else -> "Không thể gửi email. Vui lòng thử lại."
                }
                _passwordResetState.value = PasswordResetState.Error(errorMessage)
            }
        }
    }

    /**
     * Reset password reset state.
     */
    fun clearPasswordResetState() {
        _passwordResetState.value = PasswordResetState.Idle
    }

    /**
     * Verify admin after biometric unlock.
     * Called when user successfully authenticates via biometric.
     */
    fun verifyAfterBiometric() {
        viewModelScope.launch {
            authRepository.verifyCurrentUserIsAdmin().collect { result ->
                _authState.value = when (result) {
                    is AdminAuthResult.Loading -> AdminAuthState.Loading
                    is AdminAuthResult.Success -> AdminAuthState.Success(
                        email = result.response.user?.email ?: "",
                        displayName = result.response.user?.fullName,
                        role = result.response.user?.role ?: "ADMIN"
                    )
                    is AdminAuthResult.Error -> {
                        // If verification fails after biometric, log out
                        authRepository.logout()
                        AdminAuthState.Error(result.message)
                    }
                }
            }
        }
    }

    /**
     * Logout current user.
     */
    fun logout() {
        authRepository.logout()
        _authState.value = AdminAuthState.Idle
    }

    /**
     * Reset error state back to Idle or Authenticated.
     */
    fun clearError() {
        _authState.value = if (authRepository.hasExistingSession()) {
            AdminAuthState.Authenticated(
                email = authRepository.getCurrentSessionEmail() ?: "",
                displayName = authRepository.getCurrentSessionDisplayName()
            )
        } else {
            AdminAuthState.Idle
        }
    }
}

/**
 * State for password reset flow.
 */
sealed class PasswordResetState {
    data object Idle : PasswordResetState()
    data object Loading : PasswordResetState()
    data class Success(val email: String) : PasswordResetState()
    data class Error(val message: String) : PasswordResetState()
}
