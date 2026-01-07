package com.group1.pandqapplication.shared.data.remote.api

import com.group1.pandqapplication.shared.data.remote.dto.ChatMessageDto
import com.group1.pandqapplication.shared.data.remote.dto.ProductChatDto
import com.group1.pandqapplication.shared.data.remote.dto.SendMessageRequestDto
import com.group1.pandqapplication.shared.data.remote.dto.StartChatRequestDto
import retrofit2.http.*
import java.util.UUID

/**
 * Retrofit API service for chat operations.
 */
interface ChatApiService {

    // ========== Chat Management ==========

    /**
     * Start a new chat for a product.
     */
    @POST("/api/v1/chats/products/{productId}/start")
    suspend fun startChat(
        @Path("productId") productId: String,
        @Body request: StartChatRequestDto
    ): ProductChatDto

    /**
     * Get active chat for a product.
     */
    @GET("/api/v1/chats/products/{productId}")
    suspend fun getActiveChat(
        @Path("productId") productId: String
    ): ProductChatDto

    /**
     * Get specific chat details.
     */
    @GET("/api/v1/chats/{chatId}")
    suspend fun getChatDetails(
        @Path("chatId") chatId: String
    ): ProductChatDto

    /**
     * Get all chats for current customer.
     */
    @GET("/api/v1/chats/my-chats")
    suspend fun getMyChats(): List<ProductChatDto>

    /**
     * Get all chats (for admin).
     */
    @GET("/api/v1/chats")
    suspend fun getAllChats(): List<ProductChatDto>

    /**
     * Close a chat.
     */
    @POST("/api/v1/chats/{chatId}/close")
    suspend fun closeChat(
        @Path("chatId") chatId: String
    ): ProductChatDto

    /**
     * Reopen a chat.
     */
    @POST("/api/v1/chats/{chatId}/reopen")
    suspend fun reopenChat(
        @Path("chatId") chatId: String
    ): ProductChatDto

    /**
     * Delete a chat.
     */
    @DELETE("/api/v1/chats/{chatId}")
    suspend fun deleteChat(
        @Path("chatId") chatId: String
    )

    // ========== Messages ==========

    /**
     * Send a message in a chat.
     */
    @POST("/api/v1/chats/{chatId}/messages")
    suspend fun sendMessage(
        @Path("chatId") chatId: String,
        @Body request: SendMessageRequestDto
    ): ChatMessageDto

    /**
     * Get all messages in a chat.
     */
    @GET("/api/v1/chats/{chatId}/messages")
    suspend fun getChatMessages(
        @Path("chatId") chatId: String
    ): List<ChatMessageDto>

    /**
     * Mark message as read.
     */
    @PUT("/api/v1/chats/messages/{messageId}/read")
    suspend fun markMessageAsRead(
        @Path("messageId") messageId: String
    ): ChatMessageDto

    /**
     * Mark all messages as read in a chat.
     */
    @PUT("/api/v1/chats/{chatId}/read")
    suspend fun markAllAsRead(
        @Path("chatId") chatId: String
    )

    /**
     * Get unread message count.
     */
    @GET("/api/v1/chats/{chatId}/unread-count")
    suspend fun getUnreadCount(
        @Path("chatId") chatId: String
    ): Long
}
