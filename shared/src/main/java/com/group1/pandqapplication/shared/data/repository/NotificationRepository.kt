package com.group1.pandqapplication.shared.data.repository

import com.group1.pandqapplication.shared.data.remote.ApiService
import com.group1.pandqapplication.shared.data.remote.dto.FcmTokenByEmailRequest
import com.group1.pandqapplication.shared.data.remote.dto.FcmTokenRequest
import com.group1.pandqapplication.shared.data.remote.dto.NotificationDto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getNotifications(userId: String): Result<List<NotificationDto>> {
        return try {
            val response = apiService.getNotifications(userId)
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Failed to get notifications: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun markAsRead(notificationId: String): Result<Unit> {
        return try {
            val response = apiService.markNotificationAsRead(notificationId)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to mark notification as read: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateFcmToken(userId: String, token: String): Result<Unit> {
        return try {
            val response = apiService.updateFcmToken(FcmTokenRequest(userId, token))
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to update FCM token: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateFcmTokenByEmail(email: String, token: String, firebaseUid: String? = null): Result<Unit> {
        return try {
            val response = apiService.updateFcmTokenByEmail(FcmTokenByEmailRequest(email, token, firebaseUid))
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to update FCM token: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

