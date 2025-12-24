package com.group1.pandqapplication.shared.data.repository

import com.group1.pandqapplication.shared.data.remote.AppApiService
import com.group1.pandqapplication.shared.data.remote.dto.OrderDto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderRepositoryImpl @Inject constructor(
    private val apiService: AppApiService
) : OrderRepository {

    override suspend fun getOrdersByUserId(userId: String): Result<List<OrderDto>> {
        return try {
            val orders = apiService.getOrdersByUserId(userId)
            Result.success(orders)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getOrderById(orderId: String): Result<OrderDto> {
        return try {
            val order = apiService.getOrderById(orderId)
            Result.success(order)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
