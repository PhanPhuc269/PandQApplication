package com.group1.pandqapplication.shared.data.remote.dto

import java.time.LocalDateTime

data class NotificationDto(
    val id: String,
    val userId: String,
    val type: String, // ORDER_UPDATE, PROMOTION, SYSTEM
    val title: String,
    val body: String,
    val targetUrl: String?,
    val isRead: Boolean,
    val createdAt: String
)

data class FcmTokenRequest(
    val userId: String,
    val fcmToken: String
)

data class FcmTokenByEmailRequest(
    val email: String,
    val fcmToken: String,
    val firebaseUid: String? = null
)


