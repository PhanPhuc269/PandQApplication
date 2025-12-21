package com.group1.pandqapplication.shared.data.remote

import com.group1.pandqapplication.shared.data.remote.dto.CategoryDto
import com.group1.pandqapplication.shared.data.remote.dto.InitConfigDto
import com.group1.pandqapplication.shared.data.remote.dto.LocationDto
import com.group1.pandqapplication.shared.data.remote.dto.PaginationResponseDto
import com.group1.pandqapplication.shared.data.remote.dto.ProductDto
import retrofit2.http.GET
import retrofit2.http.Query

interface AppApiService {
    @GET("init-config")
    suspend fun getInitConfig(): InitConfigDto

    @GET("master-data/locations")
    suspend fun getLocations(): List<LocationDto>

    @GET("api/v1/categories")
    suspend fun getCategories(): List<CategoryDto>

    @GET("api/v1/products")
    suspend fun getProducts(): List<ProductDto>

    @GET("api/v1/products/search")
    suspend fun searchProducts(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 10,
        @Query("categoryId") categoryId: String? = null,
        @Query("query") query: String? = null
    ): PaginationResponseDto<ProductDto>
}

