package com.group1.pandqapplication.ui.account

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

import com.group1.pandqapplication.shared.data.repository.NotificationRepository
import com.group1.pandqapplication.shared.data.repository.UserRepository
import com.group1.pandqapplication.shared.data.remote.dto.NotificationPreferenceResponse
import com.group1.pandqapplication.shared.data.remote.dto.NotificationPreferenceRequest

data class AccountUiState(
    val isLoggedIn: Boolean = false,
    val isEmailVerified: Boolean = false,
    val displayName: String = "Khách",
    val email: String = "",
    val photoUrl: String? = null,
    val isLoading: Boolean = false,
    val message: String? = null,
    val preferences: NotificationPreferenceResponse? = null,
    val isClosingAccount: Boolean = false,
    val closeAccountSuccess: Boolean = false
)

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val notificationRepository: NotificationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AccountUiState())
    val uiState: StateFlow<AccountUiState> = _uiState.asStateFlow()
    
    private var currentUserId: String? = null

    init {
        loadUserInfo()
    }

    fun loadUserInfo() {
        val isLoggedIn = authRepository.isUserLoggedIn()
        if (isLoggedIn) {
            _uiState.update { 
                it.copy(
                    isLoggedIn = true,
                    isEmailVerified = authRepository.isEmailVerified(),
                    displayName = authRepository.getCurrentUserDisplayName() 
                        ?: authRepository.getCurrentUserEmail()?.substringBefore("@") 
                        ?: "Người dùng",
                    email = authRepository.getCurrentUserEmail() ?: "",
                    photoUrl = authRepository.getCurrentUserPhotoUrl()
                )
            }
            // Fetch User ID and Preferences
            loadPreferences()
        } else {
            _uiState.update { 
                AccountUiState(
                    isLoggedIn = false,
                    isEmailVerified = false,
                    displayName = "Khách",
                    email = "Chưa đăng nhập",
                    photoUrl = null
                )
            }
        }
    }
    
    fun loadPreferences() {
        viewModelScope.launch {
            try {
                val email = authRepository.getCurrentUserEmail() ?: return@launch
                
                // 1. Get User ID if not cached
                if (currentUserId == null) {
                    val userResult = userRepository.getUserByEmail(email)
                    userResult.onSuccess { user ->
                        currentUserId = user.id
                    }.onFailure {
                        return@launch
                    }
                }
                
                // 2. Get Preferences
                val userId = currentUserId ?: return@launch
                val prefResult = notificationRepository.getPreferences(userId)
                prefResult.onSuccess { prefs ->
                    _uiState.update { it.copy(preferences = prefs) }
                }
            } catch (e: Exception) {
               val defaultPrefs = NotificationPreferenceResponse(
                   enablePromotions = true, 
                   enableOrders = true, 
                   enableSystem = true, 
                   enableChat = true
               )
               _uiState.update { 
                   it.copy(
                       preferences = defaultPrefs,
                       message = "Không thể tải cài đặt (Server Error). Đã dùng mặc định."
                   ) 
               }
            }
        }
    }
    
    fun updatePreference(request: NotificationPreferenceRequest) {
        val userId = currentUserId ?: return
        viewModelScope.launch {
            val result = notificationRepository.updatePreferences(userId, request)
            if (result.isSuccess) {
                loadPreferences() // Reload to confirm
            } else {
                _uiState.update { it.copy(message = "Không thể cập nhật cài đặt thông báo") }
            }
        }
    }

    fun sendVerificationEmail() {
        viewModelScope.launch {
            authRepository.sendEmailVerification().collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                    is Result.Success -> {
                        _uiState.update { 
                            it.copy(
                                isLoading = false, 
                                message = "Đã gửi email xác thực! Vui lòng kiểm tra hộp thư."
                            ) 
                        }
                    }
                    is Result.Error -> {
                        _uiState.update { 
                            it.copy(isLoading = false, message = result.message) 
                        }
                    }
                }
            }
        }
    }

    fun refreshEmailVerificationStatus() {
        viewModelScope.launch {
            authRepository.reloadUser().collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                    is Result.Success -> {
                        _uiState.update { 
                            it.copy(
                                isLoading = false,
                                isEmailVerified = authRepository.isEmailVerified(),
                                message = if (authRepository.isEmailVerified()) 
                                    "Email đã được xác thực!" 
                                else 
                                    "Email chưa được xác thực. Vui lòng kiểm tra hộp thư."
                            )
                        }
                    }
                    is Result.Error -> {
                        _uiState.update { 
                            it.copy(isLoading = false, message = result.message) 
                        }
                    }
                }
            }
        }
    }

    fun closeAccount(reason: String?) {
        val email = authRepository.getCurrentUserEmail() ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(isClosingAccount = true) }
            try {
                val result = userRepository.closeAccount(email, reason)
                if (result.isSuccess) {
                    _uiState.update { 
                        it.copy(
                            isClosingAccount = false,
                            closeAccountSuccess = true
                        ) 
                    }
                } else {
                    _uiState.update { 
                        it.copy(
                            isClosingAccount = false, 
                            message = "Không thể đóng tài khoản. Vui lòng thử lại."
                        ) 
                    }
                }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isClosingAccount = false, 
                        message = "Lỗi: ${e.message}"
                    ) 
                }
            }
        }
    }

    fun clearMessage() {
        _uiState.update { it.copy(message = null) }
    }
}

