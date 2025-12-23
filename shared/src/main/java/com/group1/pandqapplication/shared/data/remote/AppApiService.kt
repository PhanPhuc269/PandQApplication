package com.group1.pandqapplication.shared.data.remote

import com.group1.pandqapplication.shared.data.remote.dto.CategoryDto
import com.group1.pandqapplication.shared.data.remote.dto.CreateReviewDto
import com.group1.pandqapplication.shared.data.remote.dto.InitConfigDto
import com.group1.pandqapplication.shared.data.remote.dto.LocationDto
import com.group1.pandqapplication.shared.data.remote.dto.PaginationResponseDto
import com.group1.pandqapplication.shared.data.remote.dto.ProductDetailDto
import com.group1.pandqapplication.shared.data.remote.dto.ProductDto
import com.group1.pandqapplication.shared.data.remote.dto.ProductSearchDto
import com.group1.pandqapplication.shared.data.remote.dto.SepayCreateQRRequest
import com.group1.pandqapplication.shared.data.remote.dto.SepayCreateQRResponse
import com.group1.pandqapplication.shared.data.remote.dto.SepayStatusResponse
import com.group1.pandqapplication.shared.data.remote.dto.ZaloPayCreateOrderRequest
import com.group1.pandqapplication.shared.data.remote.dto.ZaloPayCreateOrderResponse
import com.group1.pandqapplication.shared.data.remote.dto.ZaloPayStatusResponse
import com.group1.pandqapplication.shared.data.remote.dto.ReviewDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
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

    @GET("api/v1/products/{id}")
    suspend fun getProductById(@Path("id") productId: String): ProductDetailDto

    @GET("api/v1/reviews/product/{productId}")
    suspend fun getReviewsByProductId(
        @Path("productId") productId: String,
        @Query("filterByRating") filterByRating: Int? = null,
        @Query("sortBy") sortBy: String? = "newest"
    ): List<ReviewDto>

    @POST("api/v1/reviews")
    suspend fun createReview(@Body request: CreateReviewDto): ReviewDto


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

    @retrofit2.http.Multipart
    @POST("api/v1/upload")
    suspend fun uploadImage(@retrofit2.http.Part file: okhttp3.MultipartBody.Part): okhttp3.ResponseBody

    // ZaloPay Payment
    @POST("api/v1/payments/zalopay/create-order")
    suspend fun createZaloPayOrder(
        @Body request: ZaloPayCreateOrderRequest
    ): ZaloPayCreateOrderResponse

    @GET("api/v1/payments/zalopay/status/{appTransId}")
    suspend fun getZaloPayStatus(
        @Path("appTransId") appTransId: String
    ): ZaloPayStatusResponse

    // SePay Payment (VietQR)
    @POST("api/v1/payments/sepay/create-qr")
    suspend fun createSepayQR(
        @Body request: SepayCreateQRRequest
    ): SepayCreateQRResponse

    @GET("api/v1/payments/sepay/status/{transactionId}")
    suspend fun getSepayStatus(
        @Path("transactionId") transactionId: String
    ): SepayStatusResponse
}
