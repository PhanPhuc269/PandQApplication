package com.group1.pandqapplication.shared.data.remote.dto

data class UserDto(
    val id: String,
    val email: String,
    val fullName: String?,
    val phone: String?,
    val avatarUrl: String?,
    val role: String?,
    val status: String?
)

data class UpdateUserRequest(
    val fullName: String?,
    val phone: String?,
    val avatarUrl: String?
)
