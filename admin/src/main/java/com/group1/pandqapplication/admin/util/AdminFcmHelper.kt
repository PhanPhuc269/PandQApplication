package com.group1.pandqapplication.admin.util

import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

object AdminFcmHelper {
    private const val TAG = "AdminFCM"

    /**
     * Get the current FCM token.
     * This should be called after admin logs in to send the token to the backend.
     */
    suspend fun getToken(): String = suspendCancellableCoroutine { continuation ->
        FirebaseMessaging.getInstance().token
            .addOnSuccessListener { token ->
                Log.d(TAG, "Admin FCM Token retrieved: ${token.take(20)}...")
                continuation.resume(token)
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Failed to get Admin FCM token", exception)
                continuation.resumeWithException(exception)
            }
    }

    /**
     * Subscribe to admin-specific topic for admin notifications.
     */
    fun subscribeToAdminTopic() {
        FirebaseMessaging.getInstance().subscribeToTopic("admin_notifications")
            .addOnSuccessListener {
                Log.d(TAG, "Subscribed to admin_notifications topic")
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Failed to subscribe to admin_notifications topic", exception)
            }
    }
}
