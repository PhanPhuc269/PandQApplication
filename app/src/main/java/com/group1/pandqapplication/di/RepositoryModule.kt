package com.group1.pandqapplication.di

import com.group1.pandqapplication.data.repository.MyRepository
import com.group1.pandqapplication.data.repository.MyRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindMyRepository(
        myRepositoryImpl: MyRepositoryImpl
    ): MyRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: com.group1.pandqapplication.data.repository.AuthRepositoryImpl
    ): com.group1.pandqapplication.data.repository.AuthRepository

    @Binds
    @Singleton
    abstract fun bindAppRepository(
        appRepositoryImpl: com.group1.pandqapplication.data.repository.AppRepositoryImpl
    ): com.group1.pandqapplication.data.repository.AppRepository
}
