package com.group1.pandqapplication.admin.data.repository

import com.group1.pandqapplication.admin.data.remote.AdminApiService
import com.group1.pandqapplication.admin.data.remote.dto.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PromotionRepository @Inject constructor(
    private val apiService: AdminApiService
) {
    /**
     * Lấy danh sách tất cả khuyến mãi
     */
    fun getPromotions(): Flow<Result<List<PromotionDto>>> = flow {
        try {
            val response = apiService.getPromotions()
            emit(Result.success(response))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    /**
     * Lấy chi tiết khuyến mãi theo ID
     */
    fun getPromotionById(id: String): Flow<Result<PromotionDto>> = flow {
        try {
            val response = apiService.getPromotionById(id)
            emit(Result.success(response))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    /**
     * Lấy khuyến mãi theo mã code
     */
    fun getPromotionByCode(code: String): Flow<Result<PromotionDto>> = flow {
        try {
            val response = apiService.getPromotionByCode(code)
            emit(Result.success(response))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    /**
     * Tạo khuyến mãi mới
     */
    fun createPromotion(request: CreatePromotionRequest): Flow<Result<PromotionDto>> = flow {
        try {
            val response = apiService.createPromotion(request)
            emit(Result.success(response))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    /**
     * Cập nhật khuyến mãi
     */
    fun updatePromotion(id: String, request: UpdatePromotionRequest): Flow<Result<PromotionDto>> = flow {
        try {
            val response = apiService.updatePromotion(id, request)
            emit(Result.success(response))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    /**
     * Xóa khuyến mãi
     */
    fun deletePromotion(id: String): Flow<Result<Unit>> = flow {
        try {
            apiService.deletePromotion(id)
            emit(Result.success(Unit))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    /**
     * Validate mã giảm giá
     */
    fun validatePromotion(request: ValidatePromotionRequest): Flow<Result<ValidatePromotionResponse>> = flow {
        try {
            val response = apiService.validatePromotion(request)
            emit(Result.success(response))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}
