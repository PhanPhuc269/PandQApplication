package com.group1.pandqapplication.shared.data.remote.dto

import java.math.BigDecimal

data class VoucherResponseDto(
    val id: String,
    val code: String,
    val name: String,
    val description: String?,
    val discountType: String?,
    val value: BigDecimal?,
    val maxDiscountAmount: BigDecimal?,
    val minOrderValue: BigDecimal?,
    val startDate: String?,
    val endDate: String?,
    val quantityLimit: Int?,
    val usageCount: Int?,
    val isClaimed: Boolean = false,
    val isUsed: Boolean = false,
    val claimedAt: String? = null
)

data class VoucherListResponseDto(
    val vouchers: List<VoucherResponseDto>,
    val totalCount: Int
)

data class ClaimVoucherRequest(
    val promotionId: String
)

data class ClaimVoucherResponse(
    val success: Boolean,
    val message: String,
    val voucher: VoucherResponseDto? = null
)

data class ApplyPromotionRequest(
    val userId: String,
    val promotionId: String
)
