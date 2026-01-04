package com.group1.pandqapplication.shared.data.remote.dto

data class CreateReviewDto(
    val userId: String,
    val productId: String,
    val rating: Int,
    val comment: String?,
    val imageUrls: List<String>?
)
