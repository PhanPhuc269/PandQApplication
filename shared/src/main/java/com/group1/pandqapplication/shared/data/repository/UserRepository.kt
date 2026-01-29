package com.group1.pandqapplication.shared.data.repository

import com.group1.pandqapplication.shared.data.remote.dto.UpdateUserRequest
import com.group1.pandqapplication.shared.data.remote.dto.UserDto

interface UserRepository {
    suspend fun getUserById(id: String): Result<UserDto>
    suspend fun getUserByEmail(email: String): Result<UserDto>
    suspend fun updateUser(id: String, fullName: String?, phone: String?, avatarUrl: String?): Result<UserDto>
    suspend fun closeAccount(email: String, reason: String?): Result<Unit>
}
