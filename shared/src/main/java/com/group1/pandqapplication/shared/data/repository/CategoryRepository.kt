package com.group1.pandqapplication.shared.data.repository

import com.group1.pandqapplication.shared.data.remote.dto.CategoryCreateRequest
import com.group1.pandqapplication.shared.data.remote.dto.CategoryDto
import com.group1.pandqapplication.shared.data.remote.dto.CategoryUpdateRequest
import java.util.UUID

interface CategoryRepository {
    suspend fun getAllCategories(): Result<List<CategoryDto>>
    suspend fun getCategoryById(id: UUID): Result<CategoryDto>
    suspend fun createCategory(request: CategoryCreateRequest): Result<CategoryDto>
    suspend fun updateCategory(id: UUID, request: CategoryUpdateRequest): Result<CategoryDto>
    suspend fun deleteCategory(id: UUID): Result<Unit>
}
