package com.group1.pandqapplication.shared.data.remote

import com.group1.pandqapplication.shared.data.remote.dto.CategoryDto
import com.group1.pandqapplication.shared.data.remote.dto.InitConfigDto
import com.group1.pandqapplication.shared.data.remote.dto.LocationDto
import retrofit2.http.GET

interface AppApiService {
    @GET("init-config")
    suspend fun getInitConfig(): InitConfigDto

    @GET("master-data/locations")
    suspend fun getLocations(): List<LocationDto>

    @GET("master-data/categories")
    suspend fun getCategories(): List<CategoryDto>
}
