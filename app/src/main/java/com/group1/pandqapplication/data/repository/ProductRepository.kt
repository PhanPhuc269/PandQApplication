package com.group1.pandqapplication.data.repository

import com.group1.pandqapplication.shared.data.remote.AppApiService
import com.group1.pandqapplication.shared.data.remote.dto.ProductDetailDto
import com.group1.pandqapplication.shared.data.remote.dto.ReviewDto
import com.group1.pandqapplication.shared.data.remote.dto.ProductDto
import com.group1.pandqapplication.shared.data.remote.dto.CreateProductRequest
import com.group1.pandqapplication.shared.data.remote.dto.UpdateProductRequest

import javax.inject.Inject
import javax.inject.Singleton
import java.io.File
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.HttpException
import org.json.JSONObject

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

    suspend fun getAllProducts(): Result<List<ProductDto>> {
        return try {
            val products = apiService.getProducts()
            Result.success(products)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createProduct(request: CreateProductRequest): Result<ProductDetailDto> {
        return try {
            val product = apiService.createProduct(request)
            Result.success(product)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateProduct(id: String, request: UpdateProductRequest): Result<ProductDetailDto> {
        return try {
            val product = apiService.updateProduct(id, request)
            Result.success(product)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteProduct(id: String): Result<Unit> {
        return try {
            apiService.deleteProduct(id)
            Result.success(Unit)
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
        comment: String?,
        imageUrls: List<String>? = null
    ): Result<ReviewDto> {
        return try {
            val request = com.group1.pandqapplication.shared.data.remote.dto.CreateReviewDto(
                userId = userId,
                productId = productId,
                rating = rating,
                comment = comment,
                imageUrls = imageUrls
            )
            val review = apiService.createReview(request)
            Result.success(review)
        } catch (e: HttpException) {
            // Parse error message from response body
            val errorBody = e.response()?.errorBody()?.string()
            val errorMessage = try {
                val json = JSONObject(errorBody ?: "{}")
                json.optString("error", "Không thể gửi đánh giá. Vui lòng thử lại.")
            } catch (ex: Exception) {
                "Không thể gửi đánh giá. Vui lòng thử lại."
            }
            Result.failure(Exception(errorMessage))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun uploadImage(file: File): Result<String> {
        return try {
            val mediaType = "image/*".toMediaTypeOrNull()
            val requestFile = file.asRequestBody(mediaType)
            val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
            val response = apiService.uploadImage(body)
            Result.success(response.string())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
