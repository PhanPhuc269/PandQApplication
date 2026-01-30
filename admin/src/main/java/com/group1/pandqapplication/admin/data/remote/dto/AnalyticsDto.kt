package com.group1.pandqapplication.admin.data.remote.dto

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

/**
 * DTOs for Analytics API responses.
 * Matches backend AnalyticsDTO structure.
 */

data class SalesOverviewDto(
    @SerializedName("totalRevenue") val totalRevenue: BigDecimal?,
    @SerializedName("revenueChangePercent") val revenueChangePercent: Double?,
    @SerializedName("conversionRate") val conversionRate: Double?,
    @SerializedName("conversionChangePercent") val conversionChangePercent: Double?,
    @SerializedName("averageOrderValue") val averageOrderValue: BigDecimal?,
    @SerializedName("averageOrderChangePercent") val averageOrderChangePercent: Double?,
    @SerializedName("totalOrders") val totalOrders: Long?,
    @SerializedName("ordersChangePercent") val ordersChangePercent: Double?,
    @SerializedName("totalProductsSold") val totalProductsSold: Long?,
    @SerializedName("productsChangePercent") val productsChangePercent: Double?,
    @SerializedName("dateRange") val dateRange: String?,
    @SerializedName("message") val message: String?,
    @SerializedName("newCustomers") val newCustomers: Long?,
    @SerializedName("newCustomersChangePercent") val newCustomersChangePercent: Double?
)

data class RevenueChartDto(
    @SerializedName("totalRevenue") val totalRevenue: BigDecimal?,
    @SerializedName("changePercent") val changePercent: Double?,
    @SerializedName("dateRangeLabel") val dateRangeLabel: String?,
    @SerializedName("dailyRevenues") val dailyRevenues: List<DailyRevenueDto>?,
    @SerializedName("message") val message: String?
)

data class DailyRevenueDto(
    @SerializedName("date") val date: String?,
    @SerializedName("dayLabel") val dayLabel: String?,
    @SerializedName("revenue") val revenue: BigDecimal?,
    @SerializedName("percentage") val percentage: Double?,
    @SerializedName("isHighlighted") val isHighlighted: Boolean?
)

data class TopProductsDto(
    @SerializedName("products") val products: List<TopProductDto>?,
    @SerializedName("message") val message: String?
)

data class TopProductDto(
    @SerializedName("rank") val rank: Int?,
    @SerializedName("productId") val productId: String?,
    @SerializedName("productName") val productName: String?,
    @SerializedName("imageUrl") val imageUrl: String?,
    @SerializedName("price") val price: BigDecimal?,
    @SerializedName("quantitySold") val quantitySold: Long?,
    @SerializedName("totalRevenue") val totalRevenue: BigDecimal?
)

data class CategorySalesDto(
    @SerializedName("totalRevenue") val totalRevenue: BigDecimal?,
    @SerializedName("categories") val categories: List<CategorySaleItemDto>?,
    @SerializedName("message") val message: String?
)

data class CategorySaleItemDto(
    @SerializedName("categoryId") val categoryId: String? = null,
    @SerializedName("categoryName") val categoryName: String? = null,
    @SerializedName("revenue") val revenue: BigDecimal? = null,
    @SerializedName("percentage") val percentage: Double = 0.0,
    @SerializedName("colorHex") val colorHex: String = "#000000",
    @SerializedName("quantitySold") val quantitySold: Long = 0,
    @SerializedName("imageUrl") val imageUrl: String? = null
)

data class FullAnalyticsDto(
    @SerializedName("overview") val overview: SalesOverviewDto?,
    @SerializedName("revenueChart") val revenueChart: RevenueChartDto?,
    @SerializedName("topProducts") val topProducts: TopProductsDto?,
    @SerializedName("categorySales") val categorySales: CategorySalesDto?,
    @SerializedName("message") val message: String?
)

/**
 * Daily analytics detail response (for specific date drill-down)
 */
data class DailyAnalyticsDetailDto(
    @SerializedName("date") val date: String,
    @SerializedName("totalRevenue") val totalRevenue: BigDecimal,
    @SerializedName("orderCount") val orderCount: Long,
    @SerializedName("categories") val categories: List<CategoryRevenueDetailDto>,
    @SerializedName("message") val message: String?
)

/**
 * Category revenue detail for daily analytics
 */
data class CategoryRevenueDetailDto(
    @SerializedName("categoryId") val categoryId: String,
    @SerializedName("categoryName") val categoryName: String,
    @SerializedName("revenue") val revenue: BigDecimal,
    @SerializedName("percentage") val percentage: Double,
    @SerializedName("colorHex") val colorHex: String,
    @SerializedName("quantitySold") val quantitySold: Long,
    @SerializedName("imageUrl") val imageUrl: String?,
    @SerializedName("products") val products: List<ProductRevenueDetailDto>
)

/**
 * Product revenue detail within a category
 */
data class ProductRevenueDetailDto(
    @SerializedName("productId") val productId: String,
    @SerializedName("productName") val productName: String,
    @SerializedName("revenue") val revenue: BigDecimal,
    @SerializedName("quantitySold") val quantitySold: Long,
    @SerializedName("price") val price: BigDecimal,
    @SerializedName("imageUrl") val imageUrl: String?
)
