package com.group1.pandqapplication.shared.di

import com.google.firebase.auth.FirebaseAuth
import com.group1.pandqapplication.shared.util.Constants
import com.group1.pandqapplication.shared.data.remote.ApiService
import com.group1.pandqapplication.shared.data.remote.AppApiService
import com.group1.pandqapplication.shared.data.repository.ProductRepository
import com.group1.pandqapplication.shared.data.repository.ProductRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.realm.kotlin.Realm
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideAuthInterceptor(): Interceptor {
        return Interceptor { chain ->
            val originalRequest = chain.request()
            
            // Get Firebase ID token
            val user = FirebaseAuth.getInstance().currentUser
            val token = user?.let {
                try {
                    runBlocking {
                        it.getIdToken(false).await().token
                    }
                } catch (e: Exception) {
                    null
                }
            }
            
            val request = if (token != null) {
                originalRequest.newBuilder()
                    .header("Authorization", "Bearer $token")
                    .build()
            } else {
                originalRequest
            }
            
            chain.proceed(request)
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: Interceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideAppApiService(retrofit: Retrofit): AppApiService {
        return retrofit.create(AppApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideProductRepository(appApiService: AppApiService, realm: Realm): ProductRepository {
        return ProductRepositoryImpl(appApiService, realm)
    }
}
