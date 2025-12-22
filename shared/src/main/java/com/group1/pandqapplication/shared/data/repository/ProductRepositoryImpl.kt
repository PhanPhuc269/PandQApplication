package com.group1.pandqapplication.shared.data.repository

import com.group1.pandqapplication.shared.data.local.entity.CategoryEntity
import com.group1.pandqapplication.shared.data.remote.AppApiService
import com.group1.pandqapplication.shared.data.remote.dto.PaginationResponseDto
import com.group1.pandqapplication.shared.data.remote.dto.ProductSearchDto
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val apiService: AppApiService,
    private val realm: Realm
) : ProductRepository {

    override suspend fun searchProducts(
        query: String?,
        categoryId: String?,
        minPrice: Double?,
        maxPrice: Double?,
        minRating: Double?,
        inStockOnly: Boolean,
        sortBy: String,
        page: Int,
        size: Int
    ): Result<PaginationResponseDto<ProductSearchDto>> {
        return try {
            val response = apiService.searchProducts(
                query = query,
                categoryId = categoryId,
                minPrice = minPrice,
                maxPrice = maxPrice,
                minRating = minRating,
                inStockOnly = inStockOnly,
                sortBy = sortBy,
                page = page,
                size = size
            )
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getCategories(): List<CategoryItem> {
        return realm.query<CategoryEntity>().find().map { entity ->
            CategoryItem(
                id = entity.id,
                name = entity.name,
                iconUrl = entity.iconUrl
            )
        }
    }
}
