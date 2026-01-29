package com.group1.pandqapplication.admin.data.remote.dto

import com.google.gson.annotations.SerializedName

// Customer tier enum
enum class CustomerTier {
    @SerializedName("BRONZE") BRONZE,
    @SerializedName("SILVER") SILVER,
    @SerializedName("GOLD") GOLD,
    @SerializedName("PLATINUM") PLATINUM
}

// Account status enum
enum class AccountStatus {
    @SerializedName("ACTIVE") ACTIVE,
    @SerializedName("INACTIVE") INACTIVE,
    @SerializedName("BANNED") BANNED
}

// Customer list item
data class CustomerListItemDto(
    @SerializedName("id") val id: String,
    @SerializedName("fullName") val fullName: String?,
    @SerializedName("email") val email: String,
    @SerializedName("phone") val phone: String?,
    @SerializedName("avatarUrl") val avatarUrl: String?,
    @SerializedName("customerTier") val customerTier: CustomerTier?,
    @SerializedName("totalSpent") val totalSpent: Double?,
    @SerializedName("accountStatus") val accountStatus: AccountStatus?,
    @SerializedName("createdAt") val createdAt: String?,
    @SerializedName("orderCount") val orderCount: Long?
)

// Customer detail
data class CustomerDetailDto(
    @SerializedName("id") val id: String,
    @SerializedName("fullName") val fullName: String?,
    @SerializedName("email") val email: String,
    @SerializedName("phone") val phone: String?,
    @SerializedName("avatarUrl") val avatarUrl: String?,
    @SerializedName("customerTier") val customerTier: CustomerTier?,
    @SerializedName("totalSpent") val totalSpent: Double?,
    @SerializedName("accountStatus") val accountStatus: AccountStatus?,
    @SerializedName("createdAt") val createdAt: String?,
    @SerializedName("updatedAt") val updatedAt: String?,
    @SerializedName("orderCount") val orderCount: Long?,
    @SerializedName("recentOrders") val recentOrders: List<OrderSummaryDto>?
)

// Order summary
data class OrderSummaryDto(
    @SerializedName("orderId") val orderId: String,
    @SerializedName("orderDate") val orderDate: String,
    @SerializedName("totalAmount") val totalAmount: Double,
    @SerializedName("status") val status: String,
    @SerializedName("itemCount") val itemCount: Int
)

// Customer stats
data class CustomerStatsDto(
    @SerializedName("totalCustomers") val totalCustomers: Long,
    @SerializedName("activeCustomers") val activeCustomers: Long,
    @SerializedName("inactiveCustomers") val inactiveCustomers: Long,
    @SerializedName("bannedCustomers") val bannedCustomers: Long,
    @SerializedName("tierDistribution") val tierDistribution: Map<String, Long>?,
    @SerializedName("totalRevenue") val totalRevenue: Double?
)

// Paginated response
data class CustomerListResponse(
    @SerializedName("customers") val customers: List<CustomerListItemDto>,
    @SerializedName("totalElements") val totalElements: Long,
    @SerializedName("totalPages") val totalPages: Int,
    @SerializedName("currentPage") val currentPage: Int,
    @SerializedName("pageSize") val pageSize: Int
)

// Update status request
data class UpdateStatusRequest(
    @SerializedName("status") val status: AccountStatus
)

// ==================== Customer Tier Config DTOs ====================

// Tier configuration response
data class TierConfigDto(
    @SerializedName("id") val id: String,
    @SerializedName("tier") val tier: CustomerTier,
    @SerializedName("minSpent") val minSpent: Double,
    @SerializedName("maxSpent") val maxSpent: Double?,
    @SerializedName("displayName") val displayName: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("isActive") val isActive: Boolean?,
    @SerializedName("updatedAt") val updatedAt: String?,
    @SerializedName("updatedBy") val updatedBy: String?
)

// Tier config list response
data class TierConfigListResponse(
    @SerializedName("configs") val configs: List<TierConfigDto>
)

// Update tier config request
data class UpdateTierConfigRequest(
    @SerializedName("tier") val tier: CustomerTier,
    @SerializedName("minSpent") val minSpent: Double,
    @SerializedName("maxSpent") val maxSpent: Double?,
    @SerializedName("displayName") val displayName: String? = null,
    @SerializedName("description") val description: String? = null
)

// Update all tier configs request
data class UpdateAllTierConfigsRequest(
    @SerializedName("configs") val configs: List<UpdateTierConfigRequest>
)
