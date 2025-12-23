package com.group1.pandqapplication.shared.data.remote.dto

import java.math.BigDecimal

data class OrderDto(
    val id: String,
    val userId: String,
    val totalAmount: Double,
    val shippingFee: Double,
    val discountAmount: Double,
    val finalAmount: Double,
    val paymentMethod: String?,
    val status: String,
    val shippingAddress: String?,
    val createdAt: String?,
    val items: List<OrderItemDto> = emptyList()
)

data class OrderItemDto(
    val productId: String,
    val productName: String,
    val quantity: Int,
    val price: Double,
    val totalPrice: Double,
    val imageUrl: String? = null
)

data class AddToCartRequest(
    val userId: String,
    val productId: String,
    val quantity: Int
)
