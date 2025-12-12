package com.group1.pandqapplication.di

import android.content.Context
import com.group1.pandqapplication.data.local.entity.CategoryEntity
import com.group1.pandqapplication.data.local.entity.LocationEntity
import com.group1.pandqapplication.util.ConnectivityObserver
import com.group1.pandqapplication.util.NetworkConnectivityObserver
import dagger.Module
import dagger.Provides
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideConnectivityObserver(@ApplicationContext context: Context): ConnectivityObserver {
        return NetworkConnectivityObserver(context)
    }

    @Provides
    @Singleton
    fun provideRealm(): Realm {
        val config = RealmConfiguration.Builder(
            schema = setOf(LocationEntity::class, CategoryEntity::class)
        )
        .schemaVersion(1)
        .build()
        return Realm.open(config)
    }
}
