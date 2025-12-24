package com.group1.pandqapplication.shared.data.remote.dto

data class UserResponseDto(
    val id: String,
    val email: String,
    val name: String?,
    val phone: String?,
    val avatar: String?,
    val addresses: List<AddressDto>? = emptyList()
)
