package com.group1.pandqapplication.admin.service

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.group1.pandqapplication.admin.AdminActivity
import com.group1.pandqapplication.admin.AdminApplication
import com.group1.pandqapplication.admin.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdminFirebaseMessagingService : FirebaseMessagingService() {

    companion object {
        private const val TAG = "AdminFCM"
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "=== ADMIN APP NEW FCM TOKEN ===")
        Log.d(TAG, "Token: $token")
        Log.d(TAG, "================================")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d(TAG, "=== ADMIN APP RECEIVED FCM MESSAGE ===")
        Log.d(TAG, "From: ${remoteMessage.from}")
        Log.d(TAG, "Message ID: ${remoteMessage.messageId}")

        // Handle notification payload (when app is in foreground)
        remoteMessage.notification?.let { notification ->
            Log.d(TAG, "Notification payload - Title: ${notification.title}")
            Log.d(TAG, "Notification payload - Body: ${notification.body}")
            showNotification(
                title = "[Admin] ${notification.title ?: "Thông báo"}",
                body = notification.body ?: ""
            )
        }

        // Handle data payload (works in foreground and background)
        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "Data payload: ${remoteMessage.data}")
            handleDataPayload(remoteMessage.data)
        }
        
        Log.d(TAG, "======================================")
    }

    private fun handleDataPayload(data: Map<String, String>) {
        val title = data["title"] ?: return
        val body = data["body"] ?: return
        val type = data["type"]
        val targetUrl = data["targetUrl"]

        // Always prefix with [Admin] to distinguish from customer app
        showNotification("[Admin] $title", body, type, targetUrl)
    }

    private fun showNotification(
        title: String,
        body: String,
        type: String? = null,
        targetUrl: String? = null
    ) {
        Log.d(TAG, "Showing ADMIN notification: $title")
        
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create intent to open admin app when notification is tapped
        val intent = Intent(this, AdminActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            type?.let { putExtra("notification_type", it) }
            targetUrl?.let { putExtra("target_url", it) }
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            System.currentTimeMillis().toInt(),
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Build notification with clear ADMIN branding
        val notification = NotificationCompat.Builder(this, AdminApplication.NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(body)
            .setSubText("PandQ Admin") // Shows app name in notification
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .build()

        // Show notification with unique ID
        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
        Log.d(TAG, "ADMIN notification displayed successfully")
    }
}

