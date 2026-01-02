package com.group1.pandqapplication.admin.data.repository

import com.group1.pandqapplication.shared.data.remote.AppApiService
import com.group1.pandqapplication.shared.data.remote.dto.ProductDetailDto
import com.group1.pandqapplication.shared.data.remote.dto.ReviewDto
import com.group1.pandqapplication.shared.data.remote.dto.ProductDto
import com.group1.pandqapplication.shared.data.remote.dto.CategoryDto
import com.group1.pandqapplication.shared.data.remote.dto.CreateProductRequest

import com.group1.pandqapplication.shared.data.remote.dto.UpdateProductRequest
import javax.inject.Inject
import javax.inject.Singleton
import java.io.File
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
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

    suspend fun getCategories(): List<CategoryDto> {
        return apiService.getCategories()
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
