package com.group1.pandqapplication.shared.data.repository

import com.group1.pandqapplication.shared.data.remote.dto.PaginationResponseDto
import com.group1.pandqapplication.shared.data.remote.dto.ProductSearchDto

data class CategoryItem(
    val id: String,
    val name: String,
    val iconUrl: String?
)

interface ProductRepository {
    suspend fun searchProducts(
        query: String? = null,
        categoryId: String? = null,
        minPrice: Double? = null,
        maxPrice: Double? = null,
        minRating: Double? = null,
        inStockOnly: Boolean = false,
        sortBy: String = "newest",
        page: Int = 0,
        size: Int = 20
    ): Result<PaginationResponseDto<ProductSearchDto>>
    
    suspend fun getCategories(): List<CategoryItem>
}
