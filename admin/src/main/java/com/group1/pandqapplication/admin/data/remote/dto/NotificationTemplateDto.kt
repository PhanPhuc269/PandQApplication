package com.group1.pandqapplication.admin.data.remote.dto

import com.google.gson.annotations.SerializedName

data class NotificationTemplateDto(
    val id: String,
    val title: String,
    val body: String,
    val type: String,
    val targetUrl: String?,
    val isActive: Boolean,
    val scheduledAt: String?,
    val targetAudience: String?,
    val lastSentAt: String?,
    val sendCount: Int,
    val createdAt: String?,
    val updatedAt: String?
)

data class CreateNotificationTemplateRequest(
    val title: String,
    val body: String,
    val type: String = "SYSTEM",
    val targetUrl: String? = null,
    val isActive: Boolean = true,
    val scheduledAt: String? = null,
    val targetAudience: String = "ALL"
)

data class UpdateNotificationTemplateRequest(
    val title: String? = null,
    val body: String? = null,
    val type: String? = null,
    val targetUrl: String? = null,
    val isActive: Boolean? = null,
    val scheduledAt: String? = null,
    val targetAudience: String? = null
)
