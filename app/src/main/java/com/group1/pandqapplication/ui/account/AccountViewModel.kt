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

data class AccountUiState(
    val isLoggedIn: Boolean = false,
    val isEmailVerified: Boolean = false,
    val displayName: String = "Khách",
    val email: String = "",
    val photoUrl: String? = null,
    val isLoading: Boolean = false,
    val message: String? = null
)

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AccountUiState())
    val uiState: StateFlow<AccountUiState> = _uiState.asStateFlow()

    init {
        loadUserInfo()
    }

    fun loadUserInfo() {
        val isLoggedIn = authRepository.isUserLoggedIn()
        if (isLoggedIn) {
            _uiState.value = AccountUiState(
                isLoggedIn = true,
                isEmailVerified = authRepository.isEmailVerified(),
                displayName = authRepository.getCurrentUserDisplayName() 
                    ?: authRepository.getCurrentUserEmail()?.substringBefore("@") 
                    ?: "Người dùng",
                email = authRepository.getCurrentUserEmail() ?: "",
                photoUrl = authRepository.getCurrentUserPhotoUrl()
            )
        } else {
            _uiState.value = AccountUiState(
                isLoggedIn = false,
                isEmailVerified = false,
                displayName = "Khách",
                email = "Chưa đăng nhập",
                photoUrl = null
            )
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

    fun clearMessage() {
        _uiState.update { it.copy(message = null) }
    }
}
