package com.group1.pandqapplication.admin.data

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AdminSettingsManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private val _isReturnScreenEnabled = MutableStateFlow(prefs.getBoolean(KEY_RETURN_SCREEN_ENABLED, true))
    val isReturnScreenEnabled: StateFlow<Boolean> = _isReturnScreenEnabled.asStateFlow()

    private val _isBiometricEnabled = MutableStateFlow(prefs.getBoolean(KEY_BIOMETRIC_ENABLED, false))
    val isBiometricEnabled: StateFlow<Boolean> = _isBiometricEnabled.asStateFlow()

    private val _isPushNotificationEnabled = MutableStateFlow(prefs.getBoolean(KEY_PUSH_MAX_ENABLED, true))
    val isPushNotificationEnabled: StateFlow<Boolean> = _isPushNotificationEnabled.asStateFlow()

    fun setReturnScreenEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_RETURN_SCREEN_ENABLED, enabled).apply()
        _isReturnScreenEnabled.value = enabled
    }

    fun setBiometricEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_BIOMETRIC_ENABLED, enabled).apply()
        _isBiometricEnabled.value = enabled
    }

    fun setPushNotificationEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_PUSH_MAX_ENABLED, enabled).apply()
        _isPushNotificationEnabled.value = enabled
    }

    // Synchronous getter for use in FirebaseMessagingService (runs on background thread)
    fun isPushNotificationEnabledSync(): Boolean {
        return prefs.getBoolean(KEY_PUSH_MAX_ENABLED, true)
    }

    companion object {
        private const val PREFS_NAME = "admin_settings_prefs"
        private const val KEY_RETURN_SCREEN_ENABLED = "return_screen_enabled"
        private const val KEY_BIOMETRIC_ENABLED = "biometric_enabled"
        private const val KEY_PUSH_MAX_ENABLED = "push_notification_enabled"
    }
}
