package com.group1.pandqapplication.admin.data.remote.dto

data class AdminNotificationResponse(
    val content: List<AdminNotificationItem>,
    val totalElements: Int,
    val totalPages: Int,
    val number: Int
)

data class AdminNotificationItem(
    val id: String,
    val type: String,
    val title: String,
    val body: String,
    val targetData: String?,
    val isRead: Boolean,
    val createdAt: String
)
