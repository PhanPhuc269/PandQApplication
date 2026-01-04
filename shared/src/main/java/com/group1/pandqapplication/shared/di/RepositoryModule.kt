package com.group1.pandqapplication.shared.di

import com.group1.pandqapplication.shared.data.repository.MyRepository
import com.group1.pandqapplication.shared.data.repository.MyRepositoryImpl
import com.group1.pandqapplication.shared.data.repository.AuthRepository
import com.group1.pandqapplication.shared.data.repository.AuthRepositoryImpl
import com.group1.pandqapplication.shared.data.repository.AppRepository
import com.group1.pandqapplication.shared.data.repository.AppRepositoryImpl
import com.group1.pandqapplication.shared.data.repository.OrderRepository
import com.group1.pandqapplication.shared.data.repository.OrderRepositoryImpl
import com.group1.pandqapplication.shared.data.repository.CategoryRepository
import com.group1.pandqapplication.shared.data.repository.CategoryRepositoryImpl
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
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindAppRepository(
        appRepositoryImpl: AppRepositoryImpl
    ): AppRepository

    @Binds
    @Singleton
    abstract fun bindOrderRepository(
        orderRepositoryImpl: OrderRepositoryImpl
    ): OrderRepository

    @Binds
    @Singleton
    abstract fun bindCategoryRepository(
        categoryRepositoryImpl: CategoryRepositoryImpl
    ): CategoryRepository
}
