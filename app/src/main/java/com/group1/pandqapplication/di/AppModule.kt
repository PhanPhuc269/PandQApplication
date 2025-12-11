package com.group1.pandqapplication.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    // Other app-wide dependencies can go here (e.g. Database, SharedPreferences)
}
