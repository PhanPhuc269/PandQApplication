package com.group1.pandqapplication.shared.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.group1.pandqapplication.shared.data.local.entity.CategoryEntity
import com.group1.pandqapplication.shared.data.local.entity.LocationEntity
import com.group1.pandqapplication.shared.data.remote.AppApiService
import com.group1.pandqapplication.shared.util.Result
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_preferences")

class AppRepositoryImpl @Inject constructor(
    private val apiService: AppApiService,
    private val realm: Realm,
    @ApplicationContext private val context: Context
) : AppRepository {

    companion object {
        val LOCATION_VERSION_KEY = intPreferencesKey("location_version")
        val CATEGORY_VERSION_KEY = intPreferencesKey("category_version")
    }

    override suspend fun initializeApp(): Flow<Result<Boolean>> = flow {
        emit(Result.Loading)
        try {
            // 1. Get Init Config
            val config = apiService.getInitConfig()
            
            // 2. Read Local Versions
            val preferences = context.dataStore.data.first()
            val localLocationVersion = preferences[LOCATION_VERSION_KEY] ?: 0
            val localCategoryVersion = preferences[CATEGORY_VERSION_KEY] ?: 0

            // 3. Compare and Sync Locations
            if (config.locationVersion != localLocationVersion) {
                val locations = apiService.getLocations()
                realm.write {
                    locations.forEach { dto ->
                        copyToRealm(LocationEntity().apply {
                            id = dto.id
                            name = dto.name
                            address = dto.address
                            latitude = dto.latitude
                            longitude = dto.longitude
                        }, updatePolicy = UpdatePolicy.ALL)
                    }
                }
                context.dataStore.edit { prefs ->
                    prefs[LOCATION_VERSION_KEY] = config.locationVersion
                }
            }

            // 4. Compare and Sync Categories
            if (config.categoryVersion != localCategoryVersion) {
                val categories = apiService.getCategories()
                realm.write {
                     categories.forEach { dto ->
                        copyToRealm(CategoryEntity().apply {
                            id = dto.id
                            name = dto.name
                            iconUrl = dto.iconUrl
                        }, updatePolicy = UpdatePolicy.ALL)
                    }
                }
                context.dataStore.edit { prefs ->
                    prefs[CATEGORY_VERSION_KEY] = config.categoryVersion
                }
            }

            emit(Result.Success(true))

        } catch (e: Exception) {
            e.printStackTrace()
            // In a real app, perhaps proceed with stale data if available, but for now blocking error
            emit(Result.Error(e.message ?: "Initialization failed"))
        }
    }
}
