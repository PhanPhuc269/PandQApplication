package com.group1.pandqapplication.admin.data.remote.dto

/**
 * DTO for admin user data
 */
data class AdminUserDto(
    val id: String,
    val email: String,
    val fullName: String?,
    val phone: String?,
    val avatarUrl: String?,
    val role: String,
    val status: String
)

data class CreateAdminUserRequest(
    val email: String,
    val fullName: String,
    val phone: String? = null,
    val avatarUrl: String? = null,
    val role: String = "ADMIN"
)
