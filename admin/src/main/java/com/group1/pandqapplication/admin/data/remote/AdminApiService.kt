package com.group1.pandqapplication.admin.data.remote

import com.group1.pandqapplication.admin.data.remote.dto.CreateNotificationTemplateRequest
import com.group1.pandqapplication.admin.data.remote.dto.NotificationTemplateDto
import com.group1.pandqapplication.admin.data.remote.dto.UpdateNotificationTemplateRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

/**
 * Admin-specific API service for notification templates.
 */
interface AdminApiService {

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
}
