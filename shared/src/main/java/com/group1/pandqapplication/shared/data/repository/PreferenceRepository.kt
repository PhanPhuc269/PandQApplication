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
}

@Singleton
class PreferenceRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : PreferenceRepository {

    private object PreferencesKeys {
        val IS_FIRST_LAUNCH = booleanPreferencesKey("is_first_launch")
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
}
