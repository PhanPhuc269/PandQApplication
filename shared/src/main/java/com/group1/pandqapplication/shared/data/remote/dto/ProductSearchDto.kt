package com.group1.pandqapplication.shared.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ProductSearchDto(
    val id: String,
    val categoryId: String,
    val categoryName: String,
    val name: String,
    val description: String?,
    val price: Double,
    val thumbnailUrl: String?,
    val averageRating: Double?,
    val reviewCount: Int?,
    @SerializedName("isBestSeller")
    val isBestSeller: Boolean?,
    val stockQuantity: Int?
)
