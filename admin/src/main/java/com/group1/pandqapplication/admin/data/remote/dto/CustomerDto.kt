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
