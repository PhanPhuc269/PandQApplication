package com.group1.pandqapplication.admin.ui.profile

import com.group1.pandqapplication.shared.data.remote.AdminUserInfo

/**
 * Represents the UI state of the Admin Profile screen.
 */
sealed class AdminProfileState {
    /**
     * Loading state - fetching profile data.
     */
    data object Loading : AdminProfileState()

    /**
     * Success state - profile data loaded.
     */
    data class Success(
        val user: AdminUserInfo
    ) : AdminProfileState()

    /**
     * Error state - failed to load profile.
     */
    data class Error(val message: String) : AdminProfileState()
}
