package com.group1.pandqapplication.shared.di

import com.google.firebase.auth.FirebaseAuth
import com.group1.pandqapplication.shared.util.Constants
import com.group1.pandqapplication.shared.data.remote.ApiService
import com.group1.pandqapplication.shared.data.remote.AppApiService
import com.group1.pandqapplication.shared.data.remote.LocationIQService
import com.group1.pandqapplication.shared.data.remote.api.ChatApiService
import com.group1.pandqapplication.shared.data.repository.AddressRepository
import com.group1.pandqapplication.shared.data.repository.AddressRepositoryImpl
import com.group1.pandqapplication.shared.data.repository.ChatRepository
import com.group1.pandqapplication.shared.data.remote.AuthInterceptor
import com.group1.pandqapplication.shared.data.repository.ProductRepository
import com.group1.pandqapplication.shared.data.repository.ProductRepositoryImpl
import com.group1.pandqapplication.shared.data.repository.UserRepository
import com.group1.pandqapplication.shared.data.repository.UserRepositoryImpl
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
import com.google.gson.Gson
import com.google.gson.GsonBuilder
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
            .connectTimeout(120, java.util.concurrent.TimeUnit.SECONDS)
            .readTimeout(120, java.util.concurrent.TimeUnit.SECONDS)
            .writeTimeout(120, java.util.concurrent.TimeUnit.SECONDS)
            .addInterceptor(authInterceptor)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.HEADERS
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

    @Provides
    @Singleton
    fun provideAddressRepository(appApiService: AppApiService): AddressRepository {
        return AddressRepositoryImpl(appApiService)
    }

    @Provides
    @Singleton
    fun provideUserRepository(appApiService: AppApiService): UserRepository {
        return UserRepositoryImpl(appApiService)
    }

    @Provides
    @Singleton
    fun provideChatApiService(retrofit: Retrofit): ChatApiService {
        return retrofit.create(ChatApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideChatRepository(chatApiService: ChatApiService): ChatRepository {
        return ChatRepository(chatApiService)
    }

    @Provides
    @Singleton
    fun provideLocationIQService(): LocationIQService {
        // Simple OkHttpClient without auth for external LocationIQ API
        val simpleClient = OkHttpClient.Builder()
            .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
            .readTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
        
        return Retrofit.Builder()
            .baseUrl(com.group1.pandqapplication.shared.util.LocationIQConstants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(simpleClient)
            .build()
            .create(LocationIQService::class.java)
    }
}
