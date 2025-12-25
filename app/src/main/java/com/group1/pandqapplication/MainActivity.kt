package com.group1.pandqapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.group1.pandqapplication.ui.common.NetworkErrorScreen
import com.group1.pandqapplication.util.RequestNotificationPermission

import com.group1.pandqapplication.shared.ui.theme.PandQApplicationTheme
import com.group1.pandqapplication.shared.util.ConnectivityObserver
import com.group1.pandqapplication.ui.navigation.PandQNavGraph
import dagger.hilt.android.AndroidEntryPoint
import vn.zalopay.sdk.ZaloPaySDK

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()
    
    // Deep link from push notification
    private var pendingDeepLink by mutableStateOf<String?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // Handle deep link from notification if app was launched from notification
        handleNotificationDeepLink(intent)
        
        setContent {
            val networkStatus by mainViewModel.networkStatus.collectAsState()
            
            // Request notification permission on Android 13+
            RequestNotificationPermission { isGranted ->
                Log.d("FCM", "Notification permission granted: $isGranted")
            }
            
            PandQApplicationTheme {
                Box(modifier = Modifier.fillMaxSize()) {
                    val startDestination = mainViewModel.getStartDestination()
                    val destinationAfterSplash = mainViewModel.getNextDestination()
                    
                    PandQNavGraph(
                        startDestination = startDestination,
                        destinationAfterSplash = destinationAfterSplash,
                        pendingDeepLink = pendingDeepLink,
                        onDeepLinkHandled = { pendingDeepLink = null }
                    )

                    if (networkStatus == ConnectivityObserver.Status.Unavailable || 
                        networkStatus == ConnectivityObserver.Status.Lost) {
                       NetworkErrorScreen()
                    }
                }
            }
        }
    }

    /**
     * Handle result from ZaloPay app after payment
     */
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        ZaloPaySDK.getInstance().onResult(intent)
        
        // Also handle deep link if app is already open and notification is clicked
        handleNotificationDeepLink(intent)
    }
    
    /**
     * Parse notification intent and extract deep link URL
     * Format: pandq://orders/{orderId}
     */
    private fun handleNotificationDeepLink(intent: Intent?) {
        intent?.getStringExtra("target_url")?.let { targetUrl ->
            Log.d("DeepLink", "Received deep link from notification: $targetUrl")
            pendingDeepLink = targetUrl
        }
    }
}