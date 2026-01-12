package com.group1.pandqapplication.admin.data.remote.dto

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

/**
 * DTO cho đơn hàng trong màn hình quản lý vận chuyển
 */
data class ShippingOrderDto(
    val id: String,
    val userId: String?,
    val totalAmount: BigDecimal?,
    val shippingFee: BigDecimal?,
    val discountAmount: BigDecimal?,
    val finalAmount: BigDecimal?,
    val paymentMethod: String?,
    val status: String?,
    val shippingAddress: String?,
    val shippingProvider: String?,
    val trackingNumber: String?,
    val createdAt: String?,
    val items: List<OrderItemDto>?
)

data class OrderItemDto(
    val productId: String?,
    val productName: String?,
    val quantity: Int?,
    val price: BigDecimal?,
    val totalPrice: BigDecimal?,
    val imageUrl: String?
)

/**
 * Request để gán đơn vị vận chuyển
 */
data class AssignCarrierRequest(
    val shippingProvider: String,
    val trackingNumber: String? = null
)

/**
 * Request để cập nhật trạng thái vận chuyển
 */
data class UpdateShippingStatusRequest(
    val status: String
)

/**
 * Order status enum cho Android
 */
enum class OrderStatus {
    @SerializedName("PENDING") PENDING,
    @SerializedName("CONFIRMED") CONFIRMED,
    @SerializedName("SHIPPING") SHIPPING,
    @SerializedName("DELIVERED") DELIVERED,
    @SerializedName("COMPLETED") COMPLETED,
    @SerializedName("CANCELLED") CANCELLED,
    @SerializedName("RETURNED") RETURNED,
    @SerializedName("FAILED") FAILED
}
