package com.group1.pandqapplication.admin.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.group1.pandqapplication.shared.data.remote.AppApiService
import com.group1.pandqapplication.shared.data.remote.VerifyAdminResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of AdminAuthRepository.
 * Uses Firebase Auth for authentication and backend API for role verification.
 */
@Singleton
class AdminAuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val apiService: AppApiService
) : AdminAuthRepository {

    override suspend fun loginAndVerifyAdmin(email: String, password: String): Flow<AdminAuthResult> = flow {
        emit(AdminAuthResult.Loading)

        try {
            // Step 1: Login with Firebase
            val authResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            
            if (authResult.user == null) {
                emit(AdminAuthResult.Error("Đăng nhập thất bại. Vui lòng thử lại."))
                return@flow
            }

            // Step 2: Verify admin role via backend
            val verifyResponse = apiService.verifyAdmin()
            
            if (verifyResponse.isAdmin) {
                emit(AdminAuthResult.Success(verifyResponse))
            } else {
                // Not an admin - sign out
                firebaseAuth.signOut()
                emit(AdminAuthResult.Error(verifyResponse.message))
            }
        } catch (e: Exception) {
            // Sign out on error
            firebaseAuth.signOut()
            val errorMessage = when {
                e.message?.contains("INVALID_LOGIN_CREDENTIALS") == true -> 
                    "Email hoặc mật khẩu không đúng."
                e.message?.contains("network") == true -> 
                    "Lỗi kết nối mạng. Vui lòng kiểm tra internet."
                e.message?.contains("too-many-requests") == true -> 
                    "Quá nhiều lần thử. Vui lòng đợi một lúc."
                else -> e.message ?: "Đã xảy ra lỗi không xác định."
            }
            emit(AdminAuthResult.Error(errorMessage))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun verifyCurrentUserIsAdmin(): Flow<AdminAuthResult> = flow {
        emit(AdminAuthResult.Loading)

        try {
            if (firebaseAuth.currentUser == null) {
                emit(AdminAuthResult.Error("Chưa đăng nhập. Vui lòng đăng nhập lại."))
                return@flow
            }

            // Verify admin role via backend
            val verifyResponse = apiService.verifyAdmin()
            
            if (verifyResponse.isAdmin) {
                emit(AdminAuthResult.Success(verifyResponse))
            } else {
                emit(AdminAuthResult.Error(verifyResponse.message))
            }
        } catch (e: Exception) {
            val errorMessage = when {
                e.message?.contains("401") == true -> 
                    "Phiên đăng nhập đã hết hạn. Vui lòng đăng nhập lại."
                e.message?.contains("network") == true -> 
                    "Lỗi kết nối mạng."
                else -> e.message ?: "Không thể xác minh quyền admin."
            }
            emit(AdminAuthResult.Error(errorMessage))
        }
    }.flowOn(Dispatchers.IO)

    override fun hasExistingSession(): Boolean {
        return firebaseAuth.currentUser != null
    }

    override fun getCurrentSessionEmail(): String? {
        return firebaseAuth.currentUser?.email
    }

    override fun getCurrentSessionDisplayName(): String? {
        return firebaseAuth.currentUser?.displayName
    }

    override fun logout() {
        firebaseAuth.signOut()
    }
}
