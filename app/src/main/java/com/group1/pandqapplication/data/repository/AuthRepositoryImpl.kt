package com.group1.pandqapplication.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.group1.pandqapplication.util.Result
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
}
