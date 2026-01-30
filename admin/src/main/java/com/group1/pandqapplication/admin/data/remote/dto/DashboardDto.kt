package com.group1.pandqapplication.admin.data.remote.dto

import java.math.BigDecimal

data class DashboardSummaryResponse(
    val totalRevenue: BigDecimal?,
    val revenueTrend: String?,
    val revenueSubtitle: String?,
    val totalOrders: Long?,
    val pendingOrders: Long?,
    val completedOrders: Long?,
    val lowStockAlerts: Long?,
    val recentActivities: List<RecentActivityResponse>?
)

data class RecentActivityResponse(
    val id: String?,
    val title: String?,
    val subtitle: String?,
    val status: String?,
    val statusColor: String?,
    val time: String?,
    val imageUrl: String?,
    val isAlert: Boolean?
)
