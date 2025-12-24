package com.group1.pandqapplication.shared.data.remote.dto

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal
import java.time.LocalDateTime

data class OrderHistoryDto(
    val id: String,
    @SerializedName("user_id")
    val userId: String,
    @SerializedName("total_amount")
    val totalAmount: BigDecimal,
    @SerializedName("shipping_fee")
    val shippingFee: BigDecimal,
    @SerializedName("discount_amount")
    val discountAmount: BigDecimal,
    @SerializedName("final_amount")
    val finalAmount: BigDecimal,
    @SerializedName("payment_method")
    val paymentMethod: String,
    val status: String,
    @SerializedName("shipping_address")
    val shippingAddress: String?,
    @SerializedName("created_at")
    val createdAt: LocalDateTime,
    val items: List<OrderItemDto>
)

data class OrderItemDto(
    @SerializedName("product_id")
    val productId: String,
    @SerializedName("product_name")
    val productName: String,
    val quantity: Int,
    val price: BigDecimal,
    @SerializedName("total_price")
    val totalPrice: BigDecimal,
    @SerializedName("image_url")
    val imageUrl: String? = null
)
