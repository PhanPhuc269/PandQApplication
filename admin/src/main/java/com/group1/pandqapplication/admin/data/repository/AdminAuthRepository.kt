package com.group1.pandqapplication.admin.data.repository

import com.group1.pandqapplication.shared.data.remote.VerifyAdminResponse
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for admin authentication operations.
 */
interface AdminAuthRepository {
    /**
     * Login with email and password via Firebase, then verify admin role via backend.
     */
    suspend fun loginAndVerifyAdmin(email: String, password: String): Flow<AdminAuthResult>

    /**
     * Verify if current Firebase user is an admin.
     * Used after biometric unlock.
     */
    suspend fun verifyCurrentUserIsAdmin(): Flow<AdminAuthResult>

    /**
     * Check if there's an existing Firebase session.
     */
    fun hasExistingSession(): Boolean

    /**
     * Get current session email if exists.
     */
    fun getCurrentSessionEmail(): String?

    /**
     * Get current session display name if exists.
     */
    fun getCurrentSessionDisplayName(): String?

    /**
     * Logout current user.
     */
    fun logout()
}

/**
 * Result of admin authentication operation.
 */
sealed class AdminAuthResult {
    data object Loading : AdminAuthResult()
    data class Success(val response: VerifyAdminResponse) : AdminAuthResult()
    data class Error(val message: String) : AdminAuthResult()
}
