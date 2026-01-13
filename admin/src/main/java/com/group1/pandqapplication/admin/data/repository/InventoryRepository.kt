package com.group1.pandqapplication.admin.data.repository

import com.group1.pandqapplication.shared.data.remote.AppApiService
import com.group1.pandqapplication.shared.data.remote.dto.InventoryStatsDto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InventoryRepository @Inject constructor(
    private val apiService: AppApiService
) {
    suspend fun getInventoryStats(): Result<InventoryStatsDto> {
        return try {
            val stats = apiService.getInventoryStats()
            Result.success(stats)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
