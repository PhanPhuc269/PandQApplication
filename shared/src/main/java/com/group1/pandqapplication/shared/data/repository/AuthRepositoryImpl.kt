package com.group1.pandqapplication.shared.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.GoogleAuthProvider
import com.group1.pandqapplication.shared.util.Result
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth
) : AuthRepository {

    /**
     * Chuyển đổi mã lỗi Firebase sang thông báo tiếng Việt
     */
    private fun getVietnameseErrorMessage(exception: Exception?): String {
        if (exception is FirebaseAuthException) {
            return when (exception.errorCode) {
                "ERROR_INVALID_EMAIL" -> "Email không hợp lệ"
                "ERROR_WRONG_PASSWORD" -> "Email hoặc mật khẩu không đúng"
                "ERROR_USER_NOT_FOUND" -> "Email hoặc mật khẩu không đúng"
                "ERROR_USER_DISABLED" -> "Tài khoản đã bị vô hiệu hóa"
                "ERROR_TOO_MANY_REQUESTS" -> "Quá nhiều lần thử. Vui lòng thử lại sau"
                "ERROR_EMAIL_ALREADY_IN_USE" -> "Email đã được sử dụng"
                "ERROR_WEAK_PASSWORD" -> "Mật khẩu quá yếu. Vui lòng chọn mật khẩu mạnh hơn"
                "ERROR_OPERATION_NOT_ALLOWED" -> "Đăng nhập bằng email chưa được kích hoạt"
                "ERROR_INVALID_CREDENTIAL" -> "Email hoặc mật khẩu không đúng"
                else -> exception.message ?: "Đã xảy ra lỗi"
            }
        }
        return exception?.message ?: "Đã xảy ra lỗi"
    }

    override suspend fun login(email: String, password: String): Flow<Result<Boolean>> = callbackFlow {
        trySend(Result.Loading)
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    trySend(Result.Success(true))
                } else {
                    trySend(Result.Error(getVietnameseErrorMessage(task.exception)))
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
                    trySend(Result.Error(getVietnameseErrorMessage(task.exception)))
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
                    trySend(Result.Error(getVietnameseErrorMessage(task.exception)))
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

    override fun getCurrentFirebaseUid(): String? {
        return auth.currentUser?.uid
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
