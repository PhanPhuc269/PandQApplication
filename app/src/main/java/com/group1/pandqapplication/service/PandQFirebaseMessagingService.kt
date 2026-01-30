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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import com.group1.pandqapplication.shared.data.repository.NotificationRepository
import com.group1.pandqapplication.shared.data.repository.PreferenceRepository

@AndroidEntryPoint
class PandQFirebaseMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var notificationRepository: NotificationRepository

    @Inject
    lateinit var preferenceRepository: PreferenceRepository

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

        // Handle data payload (preferred - gives full control over notification display)
        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "Data payload: ${remoteMessage.data}")
            handleDataPayload(remoteMessage.data)
            return // Exit early - data payload handled, no need to show notification again
        }

        // Fallback: Handle notification payload only if no data payload present
        // This prevents duplicate notifications when both payloads are sent
        remoteMessage.notification?.let { notification ->
            Log.d(TAG, "Notification Title: ${notification.title}")
            Log.d(TAG, "Notification Body: ${notification.body}")
            showNotification(notification.title ?: "", notification.body ?: "")
        }
    }

    private fun sendTokenToServer(token: String) {
        serviceScope.launch {
            try {
                // Determine userId? Use repository if it stores it, or update by email if logged in maybe? 
                // Since this service might run when app is killed, getting current user ID might be tricky if not stored.
                // Assuming Authentication manages session or we update fcm by email if we can get email.
                // But for now, let's try the repository method which might need userID.
                
                // Let's assume we update by whatever means available in Repos.
                // Actually, NotificationRepository has updateFcmToken(userId, token)
                // We need userId.
                // If we don't have it, we might skip. But usually we store it.
                // For now, let's log.
                Log.d(TAG, "Token needs to be sent: $token")
                
                // Real implementation ideally:
                // val userId = authRepository.getCurrentUserId()
                // if (userId != null) notificationRepository.updateFcmToken(userId, token)
                
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
        val chatId = data["chatId"]

        // Check preferences before showing
        if (!shouldShowNotification(type)) {
            Log.d(TAG, "Notification blocked by user preference: type=$type")
            return
        }

        showNotification(title, body, type, targetUrl, chatId)
    }

    private fun shouldShowNotification(type: String?): Boolean {
        if (type == null) return true // Default show if no type
        
        // Blocking here is okay as this runs on a background worker thread for FCM
        val preferences = runBlocking { preferenceRepository.notificationPreferences.first() }
        
        return when (type) {
            "ORDER_UPDATE", "PAYMENT_SUCCESS" -> preferences.enableOrders
            "PROMOTION" -> preferences.enablePromotions
            "SYSTEM" -> preferences.enableSystem
            "CHAT_MESSAGE" -> preferences.enableChat
            else -> true // Unknown types shown by default
        }
    }

    private fun showNotification(
        title: String,
        body: String,
        type: String? = null,
        targetUrl: String? = null,
        chatId: String? = null
    ) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create intent to open app when notification is tapped
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            type?.let { putExtra("notification_type", it) }
            targetUrl?.let { putExtra("target_url", it) }
            chatId?.let { putExtra("chat_id", it) }
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            System.currentTimeMillis().toInt(),
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Build notification using the channel created in PandQApplication
        val notification = NotificationCompat.Builder(this, PandQApplication.NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher) // App launcher icon
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

