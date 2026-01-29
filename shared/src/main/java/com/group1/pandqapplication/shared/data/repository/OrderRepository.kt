package com.group1.pandqapplication.shared.data.repository

import com.group1.pandqapplication.shared.data.remote.dto.OrderDto

interface OrderRepository {
    suspend fun getOrdersByUserId(userId: String): Result<List<OrderDto>>
    suspend fun getOrderById(orderId: String): Result<OrderDto>
    suspend fun confirmDelivery(orderId: String): Result<OrderDto>
}
