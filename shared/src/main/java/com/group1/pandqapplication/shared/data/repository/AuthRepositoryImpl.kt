package com.group1.pandqapplication.shared.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.group1.pandqapplication.shared.util.Result
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth
) : AuthRepository {
    override suspend fun login(email: String, password: String): Flow<Result<Boolean>> = callbackFlow {
        trySend(Result.Loading)
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    trySend(Result.Success(true))
                } else {
                    trySend(Result.Error(task.exception?.message ?: "Login failed"))
                }
                close()
            }
        awaitClose { }
    }

    override suspend fun register(email: String, password: String): Flow<Result<Boolean>> = callbackFlow {
        trySend(Result.Loading)
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    trySend(Result.Success(true))
                } else {
                    trySend(Result.Error(task.exception?.message ?: "Registration failed"))
                }
                close()
            }
        awaitClose { }
    }

    override suspend fun signInWithGoogle(idToken: String): Flow<Result<Boolean>> = callbackFlow {
        trySend(Result.Loading)
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    trySend(Result.Success(true))
                } else {
                    trySend(Result.Error(task.exception?.message ?: "Google Sign-In failed"))
                }
                close()
            }
        awaitClose { }
    }

    override fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    override fun logout() {
        auth.signOut()
    }

    override fun getCurrentUserEmail(): String? {
        return auth.currentUser?.email
    }

    override fun getCurrentUserDisplayName(): String? {
        return auth.currentUser?.displayName
    }

    override fun getCurrentUserPhotoUrl(): String? {
        return auth.currentUser?.photoUrl?.toString()
    }

    override suspend fun sendEmailVerification(): Flow<Result<Boolean>> = callbackFlow {
        trySend(Result.Loading)
        val user = auth.currentUser
        if (user == null) {
            trySend(Result.Error("Chưa đăng nhập"))
            close()
        } else {
            user.sendEmailVerification()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        trySend(Result.Success(true))
                    } else {
                        trySend(Result.Error(task.exception?.message ?: "Gửi email xác thực thất bại"))
                    }
                    close()
                }
        }
        awaitClose { }
    }

    override fun isEmailVerified(): Boolean {
        return auth.currentUser?.isEmailVerified ?: false
    }

    override suspend fun reloadUser(): Flow<Result<Boolean>> = callbackFlow {
        trySend(Result.Loading)
        val user = auth.currentUser
        if (user == null) {
            trySend(Result.Error("Chưa đăng nhập"))
            close()
        } else {
            user.reload()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        trySend(Result.Success(auth.currentUser?.isEmailVerified ?: false))
                    } else {
                        trySend(Result.Error(task.exception?.message ?: "Không thể tải lại thông tin"))
                    }
                    close()
                }
        }
        awaitClose { }
    }
}
