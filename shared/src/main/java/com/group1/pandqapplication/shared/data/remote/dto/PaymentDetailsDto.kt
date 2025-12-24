package com.group1.pandqapplication.shared.data.remote.dto

data class PaymentDetailsDto(
    val orderId: String,
    val orderStatus: String,
    val paymentMethod: String?,
    
    // User info
    val userId: String,
    val userName: String,
    val userEmail: String,
    val userPhone: String,
    
    // Shipping address
    val shippingAddress: String,
    val shippingCity: String? = null,
    val shippingDistrict: String? = null,
    
    // Order items
    val items: List<PaymentOrderItemDto> = emptyList(),
    
    // Amounts
    val subtotal: Long,
    val shippingFee: Long,
    val discountAmount: Long,
    val finalAmount: Long,
    
    // Additional info
    val orderNote: String? = null,
    val createdAt: String? = null,
    val message: String? = null
)

data class PaymentOrderItemDto(
    val productId: String,
    val productName: String,
    val price: Long,
    val quantity: Int,
    val totalPrice: Long
)
