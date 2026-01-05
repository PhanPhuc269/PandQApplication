package com.group1.pandqapplication.admin.ui.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group1.pandqapplication.admin.data.AdminSettingsManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val isReturnScreenEnabled: Boolean = true,
    val isBiometricEnabled: Boolean = false,
    val isPushNotificationEnabled: Boolean = true
)

@HiltViewModel
class AdminSettingsViewModel @Inject constructor(
    private val settingsManager: AdminSettingsManager
) : ViewModel() {

    val uiState: StateFlow<SettingsUiState> = kotlinx.coroutines.flow.combine(
        settingsManager.isReturnScreenEnabled,
        settingsManager.isBiometricEnabled,
        settingsManager.isPushNotificationEnabled
    ) { returnScreen, biometric, push ->
        SettingsUiState(
            isReturnScreenEnabled = returnScreen,
            isBiometricEnabled = biometric,
            isPushNotificationEnabled = push
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SettingsUiState()
    )

    fun toggleReturnScreen(enabled: Boolean) {
        viewModelScope.launch {
            settingsManager.setReturnScreenEnabled(enabled)
        }
    }

    fun toggleBiometric(enabled: Boolean) {
        viewModelScope.launch {
            settingsManager.setBiometricEnabled(enabled)
        }
    }

    fun togglePushNotification(enabled: Boolean) {
        viewModelScope.launch {
            settingsManager.setPushNotificationEnabled(enabled)
            
            // Subscribe or unsubscribe from FCM topics
            val messaging = com.google.firebase.messaging.FirebaseMessaging.getInstance()
            if (enabled) {
                messaging.subscribeToTopic("admin_notifications")
                    .addOnSuccessListener {
                        android.util.Log.d("AdminSettings", "Subscribed to admin_notifications topic")
                    }
                    .addOnFailureListener { e ->
                        android.util.Log.e("AdminSettings", "Failed to subscribe: ${e.message}")
                    }
            } else {
                messaging.unsubscribeFromTopic("admin_notifications")
                    .addOnSuccessListener {
                        android.util.Log.d("AdminSettings", "Unsubscribed from admin_notifications topic")
                    }
                    .addOnFailureListener { e ->
                        android.util.Log.e("AdminSettings", "Failed to unsubscribe: ${e.message}")
                    }
            }
        }
    }
}
