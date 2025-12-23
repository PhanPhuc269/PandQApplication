package com.group1.pandqapplication.data.repository

import com.group1.pandqapplication.shared.data.remote.AppApiService
import com.group1.pandqapplication.shared.data.remote.dto.OrderHistoryDto
import com.group1.pandqapplication.shared.data.remote.dto.PaginationResponseDto
import javax.inject.Inject

class OrderRepository @Inject constructor(
    private val apiService: AppApiService
) {
    suspend fun searchOrderHistory(
        userId: String,
        status: String = "all",
        query: String? = null,
        page: Int = 0,
        size: Int = 20
    ): Result<PaginationResponseDto<OrderHistoryDto>> = try {
        val response = apiService.searchOrderHistory(userId, status, query, page, size)
        Result.success(response)
    } catch (e: Exception) {
        Result.failure(e)
    }
}
