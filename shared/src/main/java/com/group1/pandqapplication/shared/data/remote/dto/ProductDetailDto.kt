package com.group1.pandqapplication.shared.data.remote.dto

data class ProductDetailDto(
    val id: String,
    val name: String,
    val description: String?,
    val price: Double,
    val thumbnailUrl: String?,
    val categoryId: String?,
    val categoryName: String?,
    val status: String?,
    val averageRating: Double?,
    val reviewCount: Int?,
    val stockQuantity: Int?,
    val images: List<ProductImageDto>?,
    val specifications: List<ProductSpecificationDto>?,
    val relatedProducts: List<RelatedProductDto>?
)

data class ProductImageDto(
    val id: String,
    val imageUrl: String,
    val displayOrder: Int?
)

data class ProductSpecificationDto(
    val specKey: String,
    val specValue: String
)

data class RelatedProductDto(
    val id: String,
    val name: String,
    val thumbnailUrl: String?,
    val price: Double
)
