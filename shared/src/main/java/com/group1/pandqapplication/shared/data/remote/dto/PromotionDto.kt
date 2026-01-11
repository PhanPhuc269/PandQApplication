package com.group1.pandqapplication.shared.data.remote.dto

import java.math.BigDecimal

/**
 * DTOs cho Promotion API - dùng cho User app validate mã giảm giá
 */

// Request validate mã giảm giá
data class ValidatePromotionRequest(
    val promoCode: String,
    val orderTotal: BigDecimal? = null,
    val productIds: List<String>? = null,
    val categoryIds: List<String>? = null
)

// Response validate
data class ValidatePromotionResponse(
    val valid: Boolean,
    val message: String? = null,
    val discountAmount: BigDecimal? = null,
    val finalAmount: BigDecimal? = null,
    val promotion: PromotionResponse? = null
)

// Promotion response (simplified for user app)
data class PromotionResponse(
    val id: String,
    val code: String,
    val name: String,
    val description: String? = null,
    val type: String, // PERCENTAGE, FIXED_AMOUNT, FREE_SHIPPING
    val value: BigDecimal? = null,
    val minOrderValue: BigDecimal? = null,
    val endDate: String? = null
)
