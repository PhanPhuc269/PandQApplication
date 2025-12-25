package com.group1.pandqapplication.shared.data.remote

import com.group1.pandqapplication.shared.data.remote.dto.AddToCartRequest
import com.group1.pandqapplication.shared.data.remote.dto.FcmTokenByEmailRequest
import com.group1.pandqapplication.shared.data.remote.dto.FcmTokenRequest
import com.group1.pandqapplication.shared.data.remote.dto.NotificationDto
import com.group1.pandqapplication.shared.data.remote.dto.OrderDto
import com.group1.pandqapplication.shared.data.remote.dto.PaymentDetailsDto
import com.group1.pandqapplication.shared.data.remote.dto.UserResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.DELETE
import retrofit2.http.Query

interface ApiService {
    @GET("endpoint")
    suspend fun getMessage(): String

    // Notification endpoints
    @GET("api/v1/notifications/user/{userId}")
    suspend fun getNotifications(@Path("userId") userId: String): Response<List<NotificationDto>>

    @GET("api/v1/notifications/by-email")
    suspend fun getNotificationsByEmail(@Query("email") email: String): Response<List<NotificationDto>>

    @PUT("api/v1/notifications/{id}/read")
    suspend fun markNotificationAsRead(@Path("id") id: String): Response<Unit>

    // FCM token endpoints
    @POST("api/v1/users/fcm-token")
    suspend fun updateFcmToken(@Body request: FcmTokenRequest): Response<Unit>

    @POST("api/v1/users/fcm-token-by-email")
    suspend fun updateFcmTokenByEmail(@Body request: FcmTokenByEmailRequest): Response<Unit>

    // Cart/Order endpoints
    @POST("api/v1/orders/cart/add")
    suspend fun addToCart(@Body request: AddToCartRequest): Response<OrderDto>

    @GET("api/v1/orders/cart/{userId}")
    suspend fun getCart(@Path("userId") userId: String): Response<OrderDto>

    @DELETE("api/v1/orders/cart/{userId}/{productId}")
    suspend fun removeFromCart(@Path("userId") userId: String, @Path("productId") productId: String): Response<OrderDto>

    @POST("api/v1/orders/cart/decrease")
    suspend fun decreaseQuantity(@Body request: AddToCartRequest): Response<OrderDto>

    @POST("api/v1/orders/cart/merge/{userId}")
    suspend fun mergeGuestCart(
        @Path("userId") userId: String,
        @Body guestCartItems: List<AddToCartRequest>
    ): Response<OrderDto>

    // User endpoints
    @GET("api/v1/users/{id}")
    suspend fun getUserById(@Path("id") userId: String): UserResponseDto

    // Payment endpoints
    @GET("api/v1/payments/details/{orderId}")
    suspend fun getPaymentDetails(@Path("orderId") orderId: String): PaymentDetailsDto
}



