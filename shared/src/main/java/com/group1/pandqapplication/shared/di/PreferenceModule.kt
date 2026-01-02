package com.group1.pandqapplication.shared.di

import com.group1.pandqapplication.shared.data.repository.PreferenceRepository
import com.group1.pandqapplication.shared.data.repository.PreferenceRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PreferenceModule {

    @Binds
    @Singleton
    abstract fun bindPreferenceRepository(
        preferenceRepositoryImpl: PreferenceRepositoryImpl
    ): PreferenceRepository
}
