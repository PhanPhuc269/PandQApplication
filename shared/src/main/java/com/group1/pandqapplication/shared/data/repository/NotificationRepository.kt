package com.group1.pandqapplication.shared.data.repository

import com.group1.pandqapplication.shared.data.remote.ApiService
import com.group1.pandqapplication.shared.data.remote.dto.FcmTokenByEmailRequest
import com.group1.pandqapplication.shared.data.remote.dto.FcmTokenRequest
import com.group1.pandqapplication.shared.data.remote.dto.NotificationDto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationRepository @Inject constructor(
    private val apiService: ApiService,
    private val preferenceRepository: PreferenceRepository
) {
    suspend fun getNotifications(userId: String, type: String? = null): Result<List<NotificationDto>> {
        return try {
            val response = apiService.getNotifications(userId, type)
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
    
    suspend fun getPreferences(userId: String): Result<com.group1.pandqapplication.shared.data.remote.dto.NotificationPreferenceResponse> {
        return try {
            val response = apiService.getNotificationPreferences(userId)
            if (response.isSuccessful && response.body() != null) {
                val prefs = response.body()!!
                preferenceRepository.saveNotificationPreferences(prefs)
                Result.success(prefs)
            } else {
                Result.failure(Exception("Failed to get preferences: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updatePreferences(userId: String, request: com.group1.pandqapplication.shared.data.remote.dto.NotificationPreferenceRequest): Result<Unit> {
        return try {
            val response = apiService.updateNotificationPreferences(userId, request)
            if (response.isSuccessful) {
                // Determine new state based on request. 
                // Since request fields are optional, we need current state to merge properly.
                // However, for simplicity and ensuring sync, we probably should re-fetch or assume successful update matches request.
                // Or better: Let's assume the UI passes the full state or we just update what we have.
                // Actually, to be safe and consistent, we should re-fetch or use the request values to update local cache ONLY if we have the full object.
                // But the request has nullable fields. 
                // Let's re-fetch preferences to ensure we have the authoritative state from server + side effect of caching it.
                // OR: read local flow -> merge with request -> save.
                
                // Let's try reading from repository flow first if possible, but it's a Flow.
                // Simplest robust way: Fetch latest from server again.
                // Optimization: update locally optimistically if needed.
                
                // For now, let's just trigger a re-fetch if we want to be 100% sure, OR rely on the ViewModel to re-call getPreferences.
                // BUT, the goal is to have the Service have the data.
                
                // Let's implement optimistic update logic here using the previous value from DataStore if possible, OR just update the specific fields.
                // Since we can't easily "patch" the datastore object without reading it, and we are inside a Result wrapper...
                
                // A better approach often used: Returns the updated object from server. 
                // Since our API returns Unit (Void), we must guess or re-fetch.
                
                // Let's trigger a background refresh:
                getPreferences(userId) // This will fetch and cache.
                
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to update preferences: ${response.code()}"))
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

    suspend fun getNotificationsByEmail(email: String): Result<List<NotificationDto>> {
        return try {
            val response = apiService.getNotificationsByEmail(email)
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Failed to get notifications: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}


