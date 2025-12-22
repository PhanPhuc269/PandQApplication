package com.group1.pandqapplication.service

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.group1.pandqapplication.MainActivity
import com.group1.pandqapplication.PandQApplication
import com.group1.pandqapplication.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PandQFirebaseMessagingService : FirebaseMessagingService() {

    companion object {
        private const val TAG = "FCM"
    }

    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "FCM Token: $token")
        
        // Send token to backend
        sendTokenToServer(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d(TAG, "Message received from: ${remoteMessage.from}")

        // Handle notification payload (when app is in foreground)
        remoteMessage.notification?.let { notification ->
            Log.d(TAG, "Notification Title: ${notification.title}")
            Log.d(TAG, "Notification Body: ${notification.body}")
            showNotification(notification.title ?: "", notification.body ?: "")
        }

        // Handle data payload
        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "Data payload: ${remoteMessage.data}")
            handleDataPayload(remoteMessage.data)
        }
    }

    private fun sendTokenToServer(token: String) {
        serviceScope.launch {
            try {
                // TODO: Send token to backend using repository
                Log.d(TAG, "Token should be sent to server: $token")
            } catch (e: Exception) {
                Log.e(TAG, "Failed to send token to server", e)
            }
        }
    }

    private fun handleDataPayload(data: Map<String, String>) {
        val title = data["title"] ?: return
        val body = data["body"] ?: return
        val type = data["type"]
        val targetUrl = data["targetUrl"]

        showNotification(title, body, type, targetUrl)
    }

    private fun showNotification(
        title: String,
        body: String,
        type: String? = null,
        targetUrl: String? = null
    ) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create intent to open app when notification is tapped
        val intent = Intent(this, MainActivity::class.java).apply {
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

        // Build notification using the channel created in PandQApplication
        val notification = NotificationCompat.Builder(this, PandQApplication.NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .build()

        // Show notification with unique ID
        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }
}

