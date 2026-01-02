package com.group1.pandqapplication.shared.data.remote.dto

data class CreateProductRequest(
    val categoryId: String,
    val name: String,
    val description: String?,
    val price: Double,
    val costPrice: Double?,
    val thumbnailUrl: String?,
    val status: String,
    val images: List<String>?,
    val specifications: List<ProductSpecificationDto>?
)

data class UpdateProductRequest(
    val categoryId: String,
    val name: String,
    val description: String?,
    val price: Double,
    val costPrice: Double?,
    val thumbnailUrl: String?,
    val status: String,
    val images: List<String>?,
    val specifications: List<ProductSpecificationDto>?
)


