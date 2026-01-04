package com.group1.pandqapplication.shared.data.remote.dto

data class ProductDto(
    val id: String,
    val categoryId: String?,
    val categoryName: String?,
    val name: String,
    val description: String?,
    val price: Double,
    val thumbnailUrl: String?,
    val averageRating: Double?,
    val reviewCount: Int?,
    val status: String?
)
