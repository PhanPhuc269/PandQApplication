package com.group1.pandqapplication.shared.data.remote

import com.group1.pandqapplication.shared.data.remote.dto.AddressDto
import com.group1.pandqapplication.shared.data.remote.dto.CategoryDto
import com.group1.pandqapplication.shared.data.remote.dto.CreateReviewDto
import com.group1.pandqapplication.shared.data.remote.dto.CreateAddressRequest
import com.group1.pandqapplication.shared.data.remote.dto.InitConfigDto
import com.group1.pandqapplication.shared.data.remote.dto.InventoryStatsDto
import com.group1.pandqapplication.shared.data.remote.dto.LocationDto
import com.group1.pandqapplication.shared.data.remote.dto.PaymentDetailsDto
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
import com.group1.pandqapplication.shared.data.remote.dto.OrderDto
import com.group1.pandqapplication.shared.data.remote.dto.CreateProductRequest
import com.group1.pandqapplication.shared.data.remote.dto.UpdateProductRequest

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.PUT
import com.group1.pandqapplication.shared.data.remote.dto.UpdateAddressRequest
import com.group1.pandqapplication.shared.data.remote.dto.UpdateUserRequest
import com.group1.pandqapplication.shared.data.remote.dto.UserDto
import retrofit2.http.DELETE
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

    @POST("api/v1/products")
    suspend fun createProduct(@Body request: CreateProductRequest): ProductDetailDto

    @PUT("api/v1/products/{id}")
    suspend fun updateProduct(@Path("id") id: String, @Body request: UpdateProductRequest): ProductDetailDto

    @DELETE("api/v1/products/{id}")
    suspend fun deleteProduct(@Path("id") id: String)

    @GET("api/v1/inventory/stats")
    suspend fun getInventoryStats(): InventoryStatsDto

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

    @GET("api/v1/products/trending-searches")
    suspend fun getTrendingSearches(): List<String>


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

    // COD (Cash on Delivery) Payment
    @PUT("api/v1/orders/{orderId}/confirm-cod")
    suspend fun confirmCODOrder(
        @Path("orderId") orderId: String
    ): OrderDto

    // Payment Details - Get order information for checkout
    @GET("api/v1/payments/details/{orderId}")
    suspend fun getPaymentDetails(
        @Path("orderId") orderId: String
    ): PaymentDetailsDto

    // User endpoints
    @GET("api/v1/users/{id}")
    suspend fun getUserById(@retrofit2.http.Path("id") id: String): UserDto

    @GET("api/v1/users/email/{email}")
    suspend fun getUserByEmail(@retrofit2.http.Path("email") email: String): UserDto

    @retrofit2.http.PUT("api/v1/users/{id}")
    suspend fun updateUser(
        @retrofit2.http.Path("id") id: String,
        @retrofit2.http.Body request: UpdateUserRequest
    ): UserDto

    // Address endpoints
    @GET("api/v1/addresses/user/{userId}")
    suspend fun getAddressesByUserId(@retrofit2.http.Path("userId") userId: String): List<AddressDto>

    @GET("api/v1/addresses/{id}")
    suspend fun getAddressById(@retrofit2.http.Path("id") id: String): AddressDto

    @POST("api/v1/addresses")
    suspend fun createAddress(@retrofit2.http.Body request: CreateAddressRequest): AddressDto

    @retrofit2.http.PUT("api/v1/addresses/{id}")
    suspend fun updateAddress(
        @retrofit2.http.Path("id") id: String,
        @retrofit2.http.Body request: UpdateAddressRequest
    ): AddressDto

    @DELETE("api/v1/addresses/{id}")
    suspend fun deleteAddress(@retrofit2.http.Path("id") id: String)

    @PUT("api/v1/addresses/{id}/default")
    suspend fun setAddressDefault(@retrofit2.http.Path("id") id: String)

    // Order endpoints
    @GET("api/v1/orders/user/{userId}")
    suspend fun getOrdersByUserId(@Path("userId") userId: String): List<OrderDto>

    @GET("api/v1/orders/{id}")
    suspend fun getOrderById(@Path("id") orderId: String): OrderDto

    // Apply promotion to order before payment
    @PUT("api/v1/orders/{id}/apply-promotion")
    suspend fun applyPromotion(
        @Path("id") orderId: String,
        @Body request: com.group1.pandqapplication.shared.data.remote.dto.ApplyPromotionRequest
    ): OrderDto

    // Promotion validation
    @POST("api/v1/promotions/validate")
    suspend fun validatePromotion(
        @Body request: com.group1.pandqapplication.shared.data.remote.dto.ValidatePromotionRequest
    ): com.group1.pandqapplication.shared.data.remote.dto.ValidatePromotionResponse

    // Voucher endpoints
    @GET("api/v1/vouchers/available")
    suspend fun getAvailableVouchers(
        @Query("userId") userId: String? = null
    ): com.group1.pandqapplication.shared.data.remote.dto.VoucherListResponseDto

    @GET("api/v1/vouchers/my-wallet")
    suspend fun getMyVouchers(
        @Query("userId") userId: String
    ): com.group1.pandqapplication.shared.data.remote.dto.VoucherListResponseDto

    @POST("api/v1/vouchers/claim")
    suspend fun claimVoucher(
        @Query("userId") userId: String,
        @Body request: com.group1.pandqapplication.shared.data.remote.dto.ClaimVoucherRequest
    ): com.group1.pandqapplication.shared.data.remote.dto.ClaimVoucherResponse

    // Get all promotions for voucher selection
    @GET("api/v1/promotions")
    suspend fun getAllPromotions(): List<com.group1.pandqapplication.shared.data.remote.dto.PromotionDto>

    // Admin Auth endpoints
    @GET("api/v1/auth/verify-admin")
    suspend fun verifyAdmin(): VerifyAdminResponse

    @GET("api/v1/auth/me")
    suspend fun getCurrentAuthUser(): AdminUserInfo
    @GET("api/v1/notifications/user/{userId}")
    suspend fun getNotificationsByUserId(
        @Path("userId") userId: String,
        @Query("type") type: String? = null
    ): List<com.group1.pandqapplication.shared.data.remote.dto.NotificationDto>

    @GET("api/v1/notifications/preferences/{userId}")
    suspend fun getNotificationPreferences(
        @Path("userId") userId: String
    ): com.group1.pandqapplication.shared.data.remote.dto.NotificationPreferenceResponse

    @PUT("api/v1/notifications/preferences/{userId}")
    suspend fun updateNotificationPreferences(
        @Path("userId") userId: String,
        @Body request: com.group1.pandqapplication.shared.data.remote.dto.NotificationPreferenceRequest
    ): retrofit2.Response<Void>

    @PUT("api/v1/notifications/{id}/read")
    suspend fun markNotificationAsRead(@Path("id") id: String)
}

/**
 * Response from admin verification endpoint.
 */
data class VerifyAdminResponse(
    @com.google.gson.annotations.SerializedName("isAdmin")
    val isAdmin: Boolean = false,
    @com.google.gson.annotations.SerializedName("message")
    val message: String = "",
    @com.google.gson.annotations.SerializedName("user")
    val user: AdminUserInfo? = null
)

/**
 * Admin user info from backend.
 */
data class AdminUserInfo(
    @com.google.gson.annotations.SerializedName("id")
    val id: String = "",
    @com.google.gson.annotations.SerializedName("email")
    val email: String = "",
    @com.google.gson.annotations.SerializedName("fullName")
    val fullName: String? = null,
    @com.google.gson.annotations.SerializedName("avatarUrl")
    val avatarUrl: String? = null,
    @com.google.gson.annotations.SerializedName("role")
    val role: String = ""
)
