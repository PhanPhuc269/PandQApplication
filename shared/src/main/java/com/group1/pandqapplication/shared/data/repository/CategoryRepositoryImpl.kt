package com.group1.pandqapplication.shared.data.repository

import com.group1.pandqapplication.shared.data.remote.ApiService
import com.group1.pandqapplication.shared.data.remote.dto.CategoryCreateRequest
import com.group1.pandqapplication.shared.data.remote.dto.CategoryDto
import com.group1.pandqapplication.shared.data.remote.dto.CategoryUpdateRequest
import java.util.UUID
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : CategoryRepository {

    override suspend fun getAllCategories(): Result<List<CategoryDto>> = try {
        val response = apiService.getAllCategories()
        if (response.isSuccessful) {
            response.body()?.let {
                Result.success(it)
            } ?: Result.failure(Exception("Empty response"))
        } else {
            Result.failure(Exception("HTTP ${response.code()}: ${response.message()}"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getCategoryById(id: UUID): Result<CategoryDto> = try {
        val response = apiService.getCategoryById(id)
        if (response.isSuccessful) {
            response.body()?.let {
                Result.success(it)
            } ?: Result.failure(Exception("Empty response"))
        } else {
            Result.failure(Exception("HTTP ${response.code()}: ${response.message()}"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun createCategory(request: CategoryCreateRequest): Result<CategoryDto> = try {
        val response = apiService.createCategory(request)
        if (response.isSuccessful) {
            response.body()?.let {
                Result.success(it)
            } ?: Result.failure(Exception("Empty response"))
        } else {
            Result.failure(Exception("HTTP ${response.code()}: ${response.message()}"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun updateCategory(id: UUID, request: CategoryUpdateRequest): Result<CategoryDto> = try {
        val response = apiService.updateCategory(id, request)
        if (response.isSuccessful) {
            response.body()?.let {
                Result.success(it)
            } ?: Result.failure(Exception("Empty response"))
        } else {
            Result.failure(Exception("HTTP ${response.code()}: ${response.message()}"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun deleteCategory(id: UUID): Result<Unit> = try {
        val response = apiService.deleteCategory(id)
        if (response.isSuccessful) {
            Result.success(Unit)
        } else {
            Result.failure(Exception("HTTP ${response.code()}: ${response.message()}"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}
