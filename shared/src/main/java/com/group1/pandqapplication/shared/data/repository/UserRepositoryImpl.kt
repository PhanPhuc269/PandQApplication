package com.group1.pandqapplication.shared.data.repository

import com.group1.pandqapplication.shared.data.remote.AppApiService
import com.group1.pandqapplication.shared.data.remote.dto.UpdateUserRequest
import com.group1.pandqapplication.shared.data.remote.dto.UserDto
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val apiService: AppApiService
) : UserRepository {

    override suspend fun getUserById(id: String): Result<UserDto> {
        return try {
            val response = apiService.getUserById(id)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUserByEmail(email: String): Result<UserDto> {
        return try {
            val response = apiService.getUserByEmail(email)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateUser(
        id: String,
        fullName: String?,
        phone: String?,
        avatarUrl: String?
    ): Result<UserDto> {
        return try {
            val request = UpdateUserRequest(
                fullName = fullName,
                phone = phone,
                avatarUrl = avatarUrl
            )
            val response = apiService.updateUser(id, request)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun closeAccount(email: String, reason: String?): Result<Unit> {
        return try {
            val request = com.group1.pandqapplication.shared.data.remote.dto.CloseAccountRequest(
                email = email,
                reason = reason
            )
            val response = apiService.closeAccount(request)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to close account: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
