package com.group1.pandqapplication.admin.di

import com.google.firebase.auth.FirebaseAuth
import com.group1.pandqapplication.admin.data.AdminUserManager
import com.group1.pandqapplication.admin.data.remote.AdminApiService
import com.group1.pandqapplication.admin.data.repository.AdminAuthRepository
import com.group1.pandqapplication.admin.data.repository.AdminAuthRepositoryImpl
import com.group1.pandqapplication.shared.data.remote.AppApiService
import com.group1.pandqapplication.shared.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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

    @Provides
    @Singleton
    fun provideAdminUserManager(
        apiService: AppApiService
    ): AdminUserManager {
        return AdminUserManager(apiService)
    }

    @Provides
    @Singleton
    fun provideAdminApiService(okHttpClient: OkHttpClient): AdminApiService {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AdminApiService::class.java)
    }
}
