package com.group1.pandqapplication.shared.data.remote.dto

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

/**
 * Request để validate mã giảm giá
 */
data class ValidatePromotionRequest(
    val promoCode: String,
    val orderTotal: BigDecimal?,
    val productIds: List<String>? = null,
    val categoryIds: List<String>? = null
)

/**
 * Response từ validate promotion
 */
data class ValidatePromotionResponse(
    val valid: Boolean,
    val message: String?,
    val discountAmount: BigDecimal?,
    val finalAmount: BigDecimal?,
    val promotion: PromotionDto?
)

/**
 * DTO cho Promotion
 */
data class PromotionDto(
    val id: String,
    val code: String,
    val name: String,
    val type: String,
    val value: BigDecimal?,
    val maxDiscountAmount: BigDecimal?,
    val minOrderValue: BigDecimal?,
    val startDate: String?,
    val endDate: String?,
    val quantityLimit: Int?,
    val usageCount: Int?,
    val status: String?
)
