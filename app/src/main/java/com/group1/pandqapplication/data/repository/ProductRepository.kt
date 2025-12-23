package com.group1.pandqapplication.data.repository

import com.group1.pandqapplication.shared.data.remote.AppApiService
import com.group1.pandqapplication.shared.data.remote.dto.ProductDetailDto
import com.group1.pandqapplication.shared.data.remote.dto.ReviewDto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepository @Inject constructor(
    private val apiService: AppApiService
) {
    suspend fun getProductById(productId: String): Result<ProductDetailDto> {
        return try {
            val product = apiService.getProductById(productId)
            Result.success(product)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getReviewsByProductId(
        productId: String,
        filterByRating: Int? = null,
        sortBy: String? = "newest"
    ): Result<List<ReviewDto>> {
        return try {
            val reviews = apiService.getReviewsByProductId(productId, filterByRating, sortBy)
            Result.success(reviews)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createReview(
        productId: String,
        userId: String,
        rating: Int,
        comment: String?
    ): Result<ReviewDto> {
        return try {
            val request = com.group1.pandqapplication.shared.data.remote.dto.CreateReviewDto(
                userId = userId,
                productId = productId,
                rating = rating,
                comment = comment,
                imageUrls = emptyList()
            )
            val review = apiService.createReview(request)
            Result.success(review)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
