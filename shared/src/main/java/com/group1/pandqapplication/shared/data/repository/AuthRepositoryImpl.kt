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
}
