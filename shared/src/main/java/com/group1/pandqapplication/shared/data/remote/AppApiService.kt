package com.group1.pandqapplication.shared.data.remote

import com.group1.pandqapplication.shared.data.remote.dto.CategoryDto
import com.group1.pandqapplication.shared.data.remote.dto.InitConfigDto
import com.group1.pandqapplication.shared.data.remote.dto.LocationDto
import com.group1.pandqapplication.shared.data.remote.dto.OrderHistoryDto
import com.group1.pandqapplication.shared.data.remote.dto.PaginationResponseDto
import com.group1.pandqapplication.shared.data.remote.dto.ProductDto
import com.group1.pandqapplication.shared.data.remote.dto.ProductSearchDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AppApiService {
    @GET("api/v1/init-config")
    suspend fun getInitConfig(): InitConfigDto

    @GET("api/v1/master-data/locations")
    suspend fun getLocations(): List<LocationDto>

    @GET("api/v1/master-data/categories")
    suspend fun getCategories(): List<CategoryDto>

    @GET("api/v1/products")
    suspend fun getProducts(): List<ProductDto>

    // For HomeScreen - returns ProductDto
    @GET("api/v1/products/search")
    suspend fun searchProductsHome(
        @Query("query") query: String? = null,
        @Query("categoryId") categoryId: String? = null,
        @Query("page") page: Int? = 0,
        @Query("size") size: Int? = 10
    ): PaginationResponseDto<ProductDto>

    // For SearchScreen - returns ProductSearchDto with full filter options
    @GET("api/v1/products/search")
    suspend fun searchProducts(
        @Query("query") query: String? = null,
        @Query("categoryId") categoryId: String? = null,
        @Query("minPrice") minPrice: Double? = null,
        @Query("maxPrice") maxPrice: Double? = null,
        @Query("minRating") minRating: Double? = null,
        @Query("inStockOnly") inStockOnly: Boolean? = false,
        @Query("sortBy") sortBy: String? = "newest",
        @Query("page") page: Int? = 0,
        @Query("size") size: Int? = 20
    ): PaginationResponseDto<ProductSearchDto>

    // Order history with filters and search
    @GET("api/v1/orders/user/{userId}/search")
    suspend fun searchOrderHistory(
        @Path("userId") userId: String,
        @Query("status") status: String? = "all",
        @Query("q") query: String? = null,
        @Query("page") page: Int? = 0,
        @Query("size") size: Int? = 20
    ): PaginationResponseDto<OrderHistoryDto>
}

