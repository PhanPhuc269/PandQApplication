package com.group1.pandqapplication.admin.data.remote

import com.group1.pandqapplication.admin.data.remote.dto.CreateNotificationTemplateRequest
import com.group1.pandqapplication.admin.data.remote.dto.NotificationTemplateDto
import com.group1.pandqapplication.admin.data.remote.dto.UpdateNotificationTemplateRequest
import com.group1.pandqapplication.admin.data.remote.dto.FullAnalyticsDto
import com.group1.pandqapplication.admin.data.remote.dto.SalesOverviewDto
import com.group1.pandqapplication.admin.data.remote.dto.RevenueChartDto
import com.group1.pandqapplication.admin.data.remote.dto.TopProductsDto
import com.group1.pandqapplication.admin.data.remote.dto.CategorySalesDto
import com.group1.pandqapplication.admin.data.remote.dto.DailyAnalyticsDetailDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

/**
 * Admin-specific API service for notification templates and analytics.
 */
interface AdminApiService {

    // ==================== Notification Templates ====================

    @GET("api/v1/admin/notification-templates")
    suspend fun getNotificationTemplates(): List<NotificationTemplateDto>

    @GET("api/v1/admin/notification-templates/{id}")
    suspend fun getNotificationTemplate(@Path("id") id: String): NotificationTemplateDto

    @POST("api/v1/admin/notification-templates")
    suspend fun createNotificationTemplate(@Body request: CreateNotificationTemplateRequest): NotificationTemplateDto

    @PUT("api/v1/admin/notification-templates/{id}")
    suspend fun updateNotificationTemplate(
        @Path("id") id: String, 
        @Body request: UpdateNotificationTemplateRequest
    ): NotificationTemplateDto

    @PUT("api/v1/admin/notification-templates/{id}/toggle")
    suspend fun toggleNotificationTemplate(@Path("id") id: String): NotificationTemplateDto

    @POST("api/v1/admin/notification-templates/{id}/send")
    suspend fun sendNotificationTemplate(@Path("id") id: String): NotificationTemplateDto

    @DELETE("api/v1/admin/notification-templates/{id}")
    suspend fun deleteNotificationTemplate(@Path("id") id: String)

    // Dashboard
    @GET("api/v1/admin/dashboard/summary")
    suspend fun getDashboardSummary(): com.group1.pandqapplication.admin.data.remote.dto.DashboardSummaryResponse

    @GET("api/v1/admin/notifications")
    suspend fun getAdminNotifications(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): com.group1.pandqapplication.admin.data.remote.dto.AdminNotificationResponse
    // ==================== Analytics ====================

    @GET("api/v1/analytics/full")
    suspend fun getFullAnalytics(@Query("range") range: String = "30d"): FullAnalyticsDto

    @GET("api/v1/analytics/sales-overview")
    suspend fun getSalesOverview(@Query("range") range: String = "30d"): SalesOverviewDto

    @GET("api/v1/analytics/revenue-chart")
    suspend fun getRevenueChart(@Query("range") range: String = "7d"): RevenueChartDto

    @GET("api/v1/analytics/top-products")
    suspend fun getTopProducts(
        @Query("limit") limit: Int = 4,
        @Query("range") range: String = "30d",
        @Query("sortBy") sortBy: String = "quantity"
    ): TopProductsDto

    @GET("api/v1/analytics/category-sales")
    suspend fun getCategorySales(
        @Query("range") range: String = "30d",
        @Query("sortBy") sortBy: String = "revenue"
    ): CategorySalesDto

    @GET("api/v1/analytics/daily/{date}")
    suspend fun getDailyAnalyticsDetail(@Path("date") date: String): DailyAnalyticsDetailDto

    // ==================== Customer Management ====================

    @GET("api/v1/customers")
    suspend fun getCustomers(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20,
        @Query("search") search: String? = null,
        @Query("tier") tier: String? = null,
        @Query("status") status: String? = null
    ): com.group1.pandqapplication.admin.data.remote.dto.CustomerListResponse

    @GET("api/v1/customers/{id}")
    suspend fun getCustomerDetail(@Path("id") id: String): com.group1.pandqapplication.admin.data.remote.dto.CustomerDetailDto

    @PUT("api/v1/customers/{id}/status")
    suspend fun updateCustomerStatus(
        @Path("id") id: String,
        @Body request: com.group1.pandqapplication.admin.data.remote.dto.UpdateStatusRequest
    )

    @GET("api/v1/customers/stats")
    suspend fun getCustomerStats(): com.group1.pandqapplication.admin.data.remote.dto.CustomerStatsDto

    // ==================== Promotion Management ====================

    @GET("api/v1/promotions")
    suspend fun getPromotions(): List<com.group1.pandqapplication.admin.data.remote.dto.PromotionDto>

    @GET("api/v1/promotions/{id}")
    suspend fun getPromotionById(@Path("id") id: String): com.group1.pandqapplication.admin.data.remote.dto.PromotionDto

    @GET("api/v1/promotions/code/{code}")
    suspend fun getPromotionByCode(@Path("code") code: String): com.group1.pandqapplication.admin.data.remote.dto.PromotionDto

    @POST("api/v1/promotions")
    suspend fun createPromotion(
        @Body request: com.group1.pandqapplication.admin.data.remote.dto.CreatePromotionRequest
    ): com.group1.pandqapplication.admin.data.remote.dto.PromotionDto

    @PUT("api/v1/promotions/{id}")
    suspend fun updatePromotion(
        @Path("id") id: String,
        @Body request: com.group1.pandqapplication.admin.data.remote.dto.UpdatePromotionRequest
    ): com.group1.pandqapplication.admin.data.remote.dto.PromotionDto

    @DELETE("api/v1/promotions/{id}")
    suspend fun deletePromotion(@Path("id") id: String)

    @POST("api/v1/promotions/validate")
    suspend fun validatePromotion(
        @Body request: com.group1.pandqapplication.admin.data.remote.dto.ValidatePromotionRequest
    ): com.group1.pandqapplication.admin.data.remote.dto.ValidatePromotionResponse
}
