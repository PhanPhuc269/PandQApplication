package com.group1.pandqapplication.util

import android.content.Context
import android.content.res.Configuration
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import java.util.Locale

// Extension for DataStore
val Context.localeDataStore: DataStore<Preferences> by preferencesDataStore(name = "locale_prefs")

object LocaleManager {
    private val LANGUAGE_KEY = stringPreferencesKey("app_language")
    
    // Supported languages
    const val ENGLISH = "en"
    const val VIETNAMESE = "vi"
    
    /**
     * Get the saved language preference as a Flow
     */
    fun getLanguageFlow(context: Context): Flow<String> {
        return context.localeDataStore.data.map { preferences ->
            preferences[LANGUAGE_KEY] ?: ENGLISH // Default to English
        }
    }
    
    /**
     * Get current language synchronously (use sparingly, prefer Flow)
     */
    fun getCurrentLanguage(context: Context): String {
        return runBlocking {
            context.localeDataStore.data.first()[LANGUAGE_KEY] ?: ENGLISH
        }
    }
    
    /**
     * Save language preference
     */
    suspend fun setLanguage(context: Context, languageCode: String) {
        context.localeDataStore.edit { preferences ->
            preferences[LANGUAGE_KEY] = languageCode
        }
    }
    
    /**
     * Apply the saved locale to the context
     * Call this in Application.attachBaseContext() and Activity.attachBaseContext()
     */
    fun applyLocale(context: Context): Context {
        val languageCode = getCurrentLanguage(context)
        return updateResources(context, languageCode)
    }
    
    /**
     * Update the context with new locale
     */
    private fun updateResources(context: Context, languageCode: String): Context {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        
        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        config.setLayoutDirection(locale)
        
        return context.createConfigurationContext(config)
    }
    
    /**
     * Check if current language is Vietnamese
     */
    fun isVietnamese(context: Context): Boolean {
        return getCurrentLanguage(context) == VIETNAMESE
    }
    
    /**
     * Get display name for language code
     */
    fun getLanguageDisplayName(languageCode: String): String {
        return when (languageCode) {
            VIETNAMESE -> "Tiếng Việt"
            else -> "English"
        }
    }
}
