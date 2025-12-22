package com.group1.pandqapplication.util

import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

object FcmHelper {
    private const val TAG = "FCM"

    /**
     * Get the current FCM token.
     * This should be called after the user logs in to send the token to the backend.
     */
    suspend fun getToken(): String = suspendCancellableCoroutine { continuation ->
        FirebaseMessaging.getInstance().token
            .addOnSuccessListener { token ->
                Log.d(TAG, "FCM Token retrieved: $token")
                continuation.resume(token)
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Failed to get FCM token", exception)
                continuation.resumeWithException(exception)
            }
    }

    /**
     * Subscribe to a topic for targeted notifications.
     * For example: subscribe to "promotions" or "order_updates"
     */
    fun subscribeToTopic(topic: String) {
        FirebaseMessaging.getInstance().subscribeToTopic(topic)
            .addOnSuccessListener {
                Log.d(TAG, "Subscribed to topic: $topic")
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Failed to subscribe to topic: $topic", exception)
            }
    }

    /**
     * Unsubscribe from a topic.
     */
    fun unsubscribeFromTopic(topic: String) {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(topic)
            .addOnSuccessListener {
                Log.d(TAG, "Unsubscribed from topic: $topic")
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Failed to unsubscribe from topic: $topic", exception)
            }
    }
}
