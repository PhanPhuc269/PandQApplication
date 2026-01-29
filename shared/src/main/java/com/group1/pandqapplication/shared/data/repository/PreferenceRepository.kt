package com.group1.pandqapplication.shared.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

interface PreferenceRepository {
    val isFirstLaunch: Flow<Boolean>
    suspend fun setFirstLaunchCompleted()

    val notificationPreferences: Flow<com.group1.pandqapplication.shared.data.remote.dto.NotificationPreferenceResponse>
    suspend fun saveNotificationPreferences(prefs: com.group1.pandqapplication.shared.data.remote.dto.NotificationPreferenceResponse)
}

@Singleton
class PreferenceRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : PreferenceRepository {

    private object PreferencesKeys {
        val IS_FIRST_LAUNCH = booleanPreferencesKey("is_first_launch")
        
        val ENABLE_PROMOTIONS = booleanPreferencesKey("enable_promotions")
        val ENABLE_ORDERS = booleanPreferencesKey("enable_orders")
        val ENABLE_SYSTEM = booleanPreferencesKey("enable_system")
        val ENABLE_CHAT = booleanPreferencesKey("enable_chat")
    }

    override val isFirstLaunch: Flow<Boolean> = context.settingsDataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.IS_FIRST_LAUNCH] ?: true
        }

    override suspend fun setFirstLaunchCompleted() {
        context.settingsDataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_FIRST_LAUNCH] = false
        }
    }

    override val notificationPreferences: Flow<com.group1.pandqapplication.shared.data.remote.dto.NotificationPreferenceResponse> = context.settingsDataStore.data
        .map { preferences ->
            com.group1.pandqapplication.shared.data.remote.dto.NotificationPreferenceResponse(
                enablePromotions = preferences[PreferencesKeys.ENABLE_PROMOTIONS] ?: true,
                enableOrders = preferences[PreferencesKeys.ENABLE_ORDERS] ?: true,
                enableSystem = preferences[PreferencesKeys.ENABLE_SYSTEM] ?: true,
                enableChat = preferences[PreferencesKeys.ENABLE_CHAT] ?: true
            )
        }

    override suspend fun saveNotificationPreferences(prefs: com.group1.pandqapplication.shared.data.remote.dto.NotificationPreferenceResponse) {
        context.settingsDataStore.edit { preferences ->
            preferences[PreferencesKeys.ENABLE_PROMOTIONS] = prefs.enablePromotions
            preferences[PreferencesKeys.ENABLE_ORDERS] = prefs.enableOrders
            preferences[PreferencesKeys.ENABLE_SYSTEM] = prefs.enableSystem
            preferences[PreferencesKeys.ENABLE_CHAT] = prefs.enableChat
        }
    }
}
