package com.group1.pandqapplication.admin.di

import com.google.firebase.auth.FirebaseAuth
import com.group1.pandqapplication.admin.data.repository.AdminAuthRepository
import com.group1.pandqapplication.admin.data.repository.AdminAuthRepositoryImpl
import com.group1.pandqapplication.shared.data.remote.AppApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module providing admin-specific dependencies.
 */
@Module
@InstallIn(SingletonComponent::class)
object AdminModule {

    @Provides
    @Singleton
    fun provideAdminAuthRepository(
        firebaseAuth: FirebaseAuth,
        apiService: AppApiService
    ): AdminAuthRepository {
        return AdminAuthRepositoryImpl(firebaseAuth, apiService)
    }
}
