package com.group1.pandqapplication.admin.data.remote.dto

import java.math.BigDecimal

/**
 * DTOs cho Promotion API
 */

// Các loại khuyến mãi
enum class DiscountType {
    PERCENTAGE,
    FIXED_AMOUNT,
    FREE_SHIPPING
}

// Trạng thái khuyến mãi
enum class PromotionStatus {
    ACTIVE,
    INACTIVE
}

// Response từ API get promotions
data class PromotionDto(
    val id: String,
    val code: String,
    val name: String,
    val description: String? = null,
    val type: DiscountType,
    val value: BigDecimal? = null,
    val maxDiscountAmount: BigDecimal? = null,
    val minOrderValue: BigDecimal? = null,
    val startDate: String? = null,
    val endDate: String? = null,
    val quantityLimit: Int? = null,
    val usageCount: Int? = null,
    val status: PromotionStatus? = null,
    val applicableCategoryIds: List<String>? = null,
    val applicableProductIds: List<String>? = null
)

// Request tạo khuyến mãi mới
data class CreatePromotionRequest(
    val code: String,
    val name: String,
    val description: String? = null,
    val type: DiscountType,
    val value: BigDecimal? = null,
    val maxDiscountAmount: BigDecimal? = null,
    val minOrderValue: BigDecimal? = null,
    val startDate: String? = null,
    val endDate: String? = null,
    val quantityLimit: Int? = null,
    val applicableCategoryIds: List<String>? = null,
    val applicableProductIds: List<String>? = null
)

// Request cập nhật khuyến mãi
data class UpdatePromotionRequest(
    val name: String? = null,
    val description: String? = null,
    val value: BigDecimal? = null,
    val maxDiscountAmount: BigDecimal? = null,
    val minOrderValue: BigDecimal? = null,
    val startDate: String? = null,
    val endDate: String? = null,
    val quantityLimit: Int? = null,
    val status: PromotionStatus? = null,
    val applicableCategoryIds: List<String>? = null,
    val applicableProductIds: List<String>? = null
)

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
    val promotion: PromotionDto? = null
)
