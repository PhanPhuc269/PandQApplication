package com.group1.pandqapplication.data.repository

import com.group1.pandqapplication.shared.data.remote.AppApiService
import com.group1.pandqapplication.shared.data.remote.dto.CategoryDto
import com.group1.pandqapplication.shared.data.remote.dto.PaginationResponseDto
import com.group1.pandqapplication.shared.data.remote.dto.ProductDto
import com.group1.pandqapplication.shared.data.remote.dto.PromotionDto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeRepository @Inject constructor(
    private val apiService: AppApiService
) {
    suspend fun getCategories(): Result<List<CategoryDto>> {
        return try {
            val categories = apiService.getCategories()
            Result.success(categories)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getProducts(): Result<List<ProductDto>> {
        return try {
            val products = apiService.getProducts()
            Result.success(products)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun searchProducts(
        page: Int = 0,
        size: Int = 10,
        categoryId: String? = null,
        query: String? = null
    ): Result<PaginationResponseDto<ProductDto>> {
        return try {
            val response = apiService.searchProductsHome(
                query = query,
                categoryId = categoryId,
                page = page,
                size = size
            )
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getPromotions(): Result<List<PromotionDto>> {
        return try {
            val promotions = apiService.getAllPromotions()
            Result.success(promotions)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
