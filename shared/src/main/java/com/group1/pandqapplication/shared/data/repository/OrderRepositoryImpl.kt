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

    override suspend fun confirmDelivery(orderId: String): Result<OrderDto> {
        return try {
            val order = apiService.confirmDelivery(orderId)
            Result.success(order)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun assignCarrier(
        orderId: String,
        shippingProvider: String,
        trackingNumber: String?
    ): Result<OrderDto> {
        return try {
            val request = com.group1.pandqapplication.shared.data.remote.dto.AssignCarrierRequest(
                shippingProvider = shippingProvider,
                trackingNumber = trackingNumber
            )
            val order = apiService.assignCarrier(orderId, request)
            Result.success(order)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateOrderStatus(orderId: String, status: String): Result<OrderDto> {
        return try {
            val request = com.group1.pandqapplication.shared.data.remote.dto.UpdateStatusRequest(
                status = status
            )
            val order = apiService.updateOrderStatus(orderId, request)
            Result.success(order)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
