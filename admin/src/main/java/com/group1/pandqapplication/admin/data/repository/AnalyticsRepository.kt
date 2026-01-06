package com.group1.pandqapplication.admin.data.repository

import com.group1.pandqapplication.admin.data.remote.AdminApiService
import com.group1.pandqapplication.admin.data.remote.dto.CategorySalesDto
import com.group1.pandqapplication.admin.data.remote.dto.FullAnalyticsDto
import com.group1.pandqapplication.admin.data.remote.dto.RevenueChartDto
import com.group1.pandqapplication.admin.data.remote.dto.SalesOverviewDto
import com.group1.pandqapplication.admin.data.remote.dto.TopProductsDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for Analytics data.
 * Following Repository pattern - abstracts data source from UI layer.
 */
@Singleton
class AnalyticsRepository @Inject constructor(
    private val apiService: AdminApiService
) {

    /**
     * Get full analytics data for the specified date range
     */
    fun getFullAnalytics(range: String): Flow<Result<FullAnalyticsDto>> = flow {
        try {
            val response = apiService.getFullAnalytics(range)
            emit(Result.success(response))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    /**
     * Get sales overview KPIs
     */
    fun getSalesOverview(range: String): Flow<Result<SalesOverviewDto>> = flow {
        try {
            val response = apiService.getSalesOverview(range)
            emit(Result.success(response))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    /**
     * Get revenue chart data
     */
    fun getRevenueChart(range: String): Flow<Result<RevenueChartDto>> = flow {
        try {
            val response = apiService.getRevenueChart(range)
            emit(Result.success(response))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    /**
     * Get top selling products
     */
    fun getTopProducts(limit: Int = 4, range: String = "30d", sortBy: String = "quantity"): Flow<Result<TopProductsDto>> = flow {
        try {
            val response = apiService.getTopProducts(limit, range, sortBy)
            emit(Result.success(response))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    /**
     * Get category sales distribution
     */
    fun getCategorySales(range: String = "30d", sortBy: String = "revenue"): Flow<Result<CategorySalesDto>> = flow {
        try {
            val response = apiService.getCategorySales(range, sortBy)
            emit(Result.success(response))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    /**
     * Get daily analytics detail for a specific date
     */
    suspend fun getDailyAnalyticsDetail(date: String): com.group1.pandqapplication.admin.data.remote.dto.DailyAnalyticsDetailDto {
        return apiService.getDailyAnalyticsDetail(date)
    }
}
