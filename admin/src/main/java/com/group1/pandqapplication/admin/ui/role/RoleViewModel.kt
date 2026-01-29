package com.group1.pandqapplication.admin.ui.role

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group1.pandqapplication.admin.data.remote.AdminApiService
import com.group1.pandqapplication.admin.data.remote.dto.AdminUserDto
import com.group1.pandqapplication.admin.data.remote.dto.CreateAdminUserRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RoleWithUsers(
    val role: String,
    val displayName: String,
    val users: List<AdminUserDto>
)

sealed class RoleUiState {
    object Loading : RoleUiState()
    data class Success(val roles: List<RoleWithUsers>) : RoleUiState()
    data class Error(val message: String) : RoleUiState()
}

@HiltViewModel
class RoleViewModel @Inject constructor(
    private val apiService: AdminApiService
) : ViewModel() {

    private val _uiState = MutableStateFlow<RoleUiState>(RoleUiState.Loading)
    val uiState: StateFlow<RoleUiState> = _uiState.asStateFlow()
    
    private val _isCreating = MutableStateFlow(false)
    val isCreating: StateFlow<Boolean> = _isCreating.asStateFlow()
    
    private val _createSuccess = MutableStateFlow(false)
    val createSuccess: StateFlow<Boolean> = _createSuccess.asStateFlow()
    
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        loadUsers()
    }

    fun loadUsers() {
        viewModelScope.launch {
            _uiState.value = RoleUiState.Loading
            try {
                val users = apiService.getAllUsers()
                
                // Group users by role - only ADMIN
                val adminUsers = users.filter { it.role == "ADMIN" }
                
                val roles = listOf(
                    RoleWithUsers("ADMIN", "Quản trị viên", adminUsers)
                )
                
                _uiState.value = RoleUiState.Success(roles)
            } catch (e: Exception) {
                _uiState.value = RoleUiState.Error(e.message ?: "Failed to load users")
            }
        }
    }
    
    fun createAdminUser(email: String, fullName: String, role: String) {
        viewModelScope.launch {
            _isCreating.value = true
            _createSuccess.value = false
            _errorMessage.value = null
            
            try {
                val request = CreateAdminUserRequest(
                    email = email,
                    fullName = fullName,
                    role = role
                )
                apiService.createUser(request)
                
                // Send password reset email using Firebase Client SDK
                try {
                    com.google.firebase.auth.FirebaseAuth.getInstance()
                        .sendPasswordResetEmail(email)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                android.util.Log.d("RoleViewModel", "Password reset email sent to $email")
                            } else {
                                android.util.Log.w("RoleViewModel", "Failed to send password reset email", task.exception)
                            }
                        }
                } catch (e: Exception) {
                    android.util.Log.w("RoleViewModel", "Error sending password reset email", e)
                }
                
                _createSuccess.value = true
                // Reload users list
                loadUsers()
            } catch (e: Exception) {
                val errorMsg = try {
                    if (e is retrofit2.HttpException) {
                        // Try to parse standard Spring Boot error response
                        // Format: {"code":"...", "message":"User already exists...", ...}
                        val errorBody = e.response()?.errorBody()?.string()
                        if (!errorBody.isNullOrEmpty() && errorBody.contains("\"message\":")) {
                            // Simple regex extraction to avoid bringing in Gson/Moshi just for this if not already present
                            // Or assuming Gson is available since Retrofit is used. 
                            // Let's stick to simple string parsing to be safe and dependency-free here.
                            val match = "\"message\":\"(.*?)\"".toRegex().find(errorBody)
                            match?.groupValues?.get(1) ?: e.message()
                        } else {
                            e.message()
                        }
                    } else {
                        e.message ?: "Failed to create user"
                    }
                } catch (parseEx: Exception) {
                    e.message ?: "Failed to create user"
                }
                _errorMessage.value = errorMsg
            } finally {
                _isCreating.value = false
            }
        }
    }
    
    fun clearError() {
        _errorMessage.value = null
    }
    
    fun clearCreateSuccess() {
        _createSuccess.value = false
    }

    fun demoteUser(email: String) {
        viewModelScope.launch {
            _uiState.value = RoleUiState.Loading // Or show specific loading state
            try {
                apiService.demoteUser(
                    com.group1.pandqapplication.admin.data.remote.dto.DemoteRequest(email)
                )
                // Reload list
                loadUsers()
            } catch (e: Exception) {
                 val errorMsg = try {
                    if (e is retrofit2.HttpException) {
                        val errorBody = e.response()?.errorBody()?.string()
                        if (!errorBody.isNullOrEmpty() && errorBody.contains("\"message\":")) {
                            val match = "\"message\":\"(.*?)\"".toRegex().find(errorBody)
                            match?.groupValues?.get(1) ?: e.message()
                        } else {
                            e.message()
                        }
                    } else {
                        e.message ?: "Failed to demote user"
                    }
                } catch (parseEx: Exception) {
                    e.message ?: "Failed to demote user"
                }
                _uiState.value = RoleUiState.Error(errorMsg)
            }
        }
    }
}
