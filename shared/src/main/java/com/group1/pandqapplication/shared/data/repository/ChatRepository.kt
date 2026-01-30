package com.group1.pandqapplication.shared.data.repository

import com.group1.pandqapplication.shared.data.remote.api.ChatApiService
import com.group1.pandqapplication.shared.data.remote.dto.ChatMessageDto
import com.group1.pandqapplication.shared.data.remote.dto.ChatStatus
import com.group1.pandqapplication.shared.data.remote.dto.MessageType
import com.group1.pandqapplication.shared.data.remote.dto.ProductChatDto
import com.group1.pandqapplication.shared.data.remote.dto.SendMessageRequestDto
import com.group1.pandqapplication.shared.data.remote.dto.SendImageMessageRequestDto
import com.group1.pandqapplication.shared.data.remote.dto.StartChatRequestDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

/**
 * Repository for handling chat operations.
 */
class ChatRepository @Inject constructor(
    private val chatApiService: ChatApiService
) {

    /**
     * Start a new chat for a product.
     */
    fun startChat(
        productId: String,
        subject: String = "Product Inquiry"
    ): Flow<Result<ProductChatDto>> = flow {
        try {
            val request = StartChatRequestDto(subject)
            val response = chatApiService.startChat(productId, request)
            emit(Result.success(response))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    /**
     * Get active chat for a product.
     */
    fun getActiveChat(productId: String): Flow<Result<ProductChatDto>> = flow {
        try {
            val response = chatApiService.getActiveChat(productId)
            emit(Result.success(response))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    /**
     * Get general chat with admin (not tied to specific product).
     * This returns one continuous chat thread with the admin.
     */
    fun getGeneralChat(): Flow<Result<ProductChatDto>> = flow {
        try {
            // Call endpoint that returns the general chat (without productId)
            val response = chatApiService.getGeneralChat()
            emit(Result.success(response))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    /**
     * Get specific chat details.
     */
    fun getChatDetails(chatId: String): Flow<Result<ProductChatDto>> = flow {
        try {
            val response = chatApiService.getChatDetails(chatId)
            emit(Result.success(response))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    /**
     * Get all chats for current customer.
     */
    fun getMyChats(): Flow<Result<List<ProductChatDto>>> = flow {
        try {
            val response = chatApiService.getMyChats()
            emit(Result.success(response))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    /**
     * Send a message in a chat.
     */
    fun sendMessage(
        chatId: String,
        message: String,
        messageType: MessageType = MessageType.TEXT,
        productContextId: String? = null,
        productContextName: String? = null,
        productContextImage: String? = null,
        productContextPrice: String? = null
    ): Flow<Result<ChatMessageDto>> = flow {
        try {
            val request = SendMessageRequestDto(
                message = message,
                messageType = messageType.name,
                productContextId = productContextId,
                productContextName = productContextName,
                productContextImage = productContextImage,
                productContextPrice = productContextPrice
            )
            val response = chatApiService.sendMessage(chatId, request)
            emit(Result.success(response))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    /**
     * Get all messages in a chat.
     */
    fun getChatMessages(chatId: String): Flow<Result<List<ChatMessageDto>>> = flow {
        try {
            val response = chatApiService.getChatMessages(chatId)
            emit(Result.success(response))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    /**
     * Mark message as read.
     */
    fun markMessageAsRead(messageId: String): Flow<Result<ChatMessageDto>> = flow {
        try {
            val response = chatApiService.markMessageAsRead(messageId)
            emit(Result.success(response))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    /**
     * Mark all messages as read in a chat.
     */
    fun markAllAsRead(chatId: String): Flow<Result<Unit>> = flow {
        try {
            chatApiService.markAllAsRead(chatId)
            emit(Result.success(Unit))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    /**
     * Get unread message count.
     */
    fun getUnreadCount(chatId: String): Flow<Result<Long>> = flow {
        try {
            val response = chatApiService.getUnreadCount(chatId)
            emit(Result.success(response))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    /**
     * Close a chat.
     */
    fun closeChat(chatId: String): Flow<Result<ProductChatDto>> = flow {
        try {
            val response = chatApiService.closeChat(chatId)
            emit(Result.success(response))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    /**
     * Get all chats (for admin).
     */
    fun getAllChats(): Flow<Result<List<ProductChatDto>>> = flow {
        try {
            val response = chatApiService.getAllChats()
            emit(Result.success(response))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    /**
     * Reopen a chat.
     */
    fun reopenChat(chatId: String): Flow<Result<ProductChatDto>> = flow {
        try {
            val response = chatApiService.reopenChat(chatId)
            emit(Result.success(response))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    /**
     * Delete a chat.
     */
    fun deleteChat(chatId: String): Flow<Result<Unit>> = flow {
        try {
            chatApiService.deleteChat(chatId)
            emit(Result.success(Unit))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    /**
     * Upload image to chat.
     */
    fun uploadChatImage(chatId: String, imageUriPath: String): Flow<Result<ChatMessageDto>> = flow {
        try {
            val file = File(imageUriPath)
            val mediaType = "image/jpeg".toMediaType()
            val requestBody = file.asRequestBody(mediaType)
            val multipartBody = MultipartBody.Part.createFormData("file", file.name, requestBody)
            val response = chatApiService.uploadChatImage(chatId, multipartBody)
            emit(Result.success(response))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    /**
     * Send an image message with URL (for Cloudinary client-side upload).
     * 
     * Flow:
     * 1. Client uploads image to Cloudinary
     * 2. Cloudinary returns the public URL
     * 3. Client sends this URL to server via this method
     * 4. Server creates a chat message with the URL
     */
    fun sendImageMessage(
        chatId: String,
        imageUrl: String,
        message: String = "Image"
    ): Flow<Result<ChatMessageDto>> = flow {
        try {
            val sendImageMessageRequest = SendImageMessageRequestDto(
                message = message,
                imageUrl = imageUrl,
                messageType = "IMAGE"
            )
            val response = chatApiService.sendImageMessage(chatId, sendImageMessageRequest)
            emit(Result.success(response))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}

