package com.group1.pandqapplication.data.repository

import com.group1.pandqapplication.util.Result
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(email: String, password: String): Flow<Result<Boolean>>
    suspend fun register(email: String, password: String): Flow<Result<Boolean>>
    fun isUserLoggedIn(): Boolean
}
