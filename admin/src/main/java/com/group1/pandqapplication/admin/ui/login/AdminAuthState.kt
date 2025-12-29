package com.group1.pandqapplication.admin.ui.login

/**
 * Represents the state of admin authentication.
 */
sealed class AdminAuthState {
    /**
     * Initial state, no authentication attempt made.
     */
    data object Idle : AdminAuthState()

    /**
     * Authentication in progress.
     */
    data object Loading : AdminAuthState()

    /**
     * Already authenticated from previous session.
     * Can use biometric to unlock.
     */
    data class Authenticated(
        val email: String,
        val displayName: String?
    ) : AdminAuthState()

    /**
     * Successfully verified as admin.
     */
    data class Success(
        val email: String,
        val displayName: String?,
        val role: String
    ) : AdminAuthState()

    /**
     * Authentication failed.
     */
    data class Error(val message: String) : AdminAuthState()
}
