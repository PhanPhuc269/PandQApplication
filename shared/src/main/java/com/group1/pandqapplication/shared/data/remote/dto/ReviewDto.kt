package com.group1.pandqapplication.shared.data.remote.dto

data class ReviewDto(
    val id: String,
    val userId: String,
    val userName: String?,
    val userAvatar: String?,
    val productId: String,
    val rating: Int,
    val comment: String?,
    val imageUrls: List<String>?,
    val createdAt: String
)
