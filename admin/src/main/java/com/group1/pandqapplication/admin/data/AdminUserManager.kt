package com.group1.pandqapplication.admin.data

import com.group1.pandqapplication.shared.data.remote.AdminUserInfo
import com.group1.pandqapplication.shared.data.remote.AppApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Singleton manager to hold the current admin user data.
 * This allows sharing user data across multiple screens without re-fetching.
 */
@Singleton
class AdminUserManager @Inject constructor(
    private val apiService: AppApiService
) {
    private val _currentUser = MutableStateFlow<AdminUserInfo?>(null)
    val currentUser: StateFlow<AdminUserInfo?> = _currentUser.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    /**
     * Load current user data from API.
     * If already loaded, returns cached data.
     */
    suspend fun loadCurrentUser(forceRefresh: Boolean = false): AdminUserInfo? {
        if (!forceRefresh && _currentUser.value != null) {
            return _currentUser.value
        }
        
        _isLoading.value = true
        return try {
            val user = apiService.getCurrentAuthUser()
            _currentUser.value = user
            user
        } catch (e: Exception) {
            null
        } finally {
            _isLoading.value = false
        }
    }

    /**
     * Update cached user data after profile edits.
     */
    fun updateUser(user: AdminUserInfo) {
        _currentUser.value = user
    }

    /**
     * Clear user data on logout.
     */
    fun clearUser() {
        _currentUser.value = null
    }
}
