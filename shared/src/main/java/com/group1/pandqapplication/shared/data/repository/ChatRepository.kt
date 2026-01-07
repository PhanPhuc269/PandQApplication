package com.group1.pandqapplication.shared.data.repository

import com.group1.pandqapplication.shared.data.remote.api.ChatApiService
import com.group1.pandqapplication.shared.data.remote.dto.ChatMessageDto
import com.group1.pandqapplication.shared.data.remote.dto.ChatStatus
import com.group1.pandqapplication.shared.data.remote.dto.MessageType
import com.group1.pandqapplication.shared.data.remote.dto.ProductChatDto
import com.group1.pandqapplication.shared.data.remote.dto.SendMessageRequestDto
import com.group1.pandqapplication.shared.data.remote.dto.StartChatRequestDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
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
        messageType: MessageType = MessageType.TEXT
    ): Flow<Result<ChatMessageDto>> = flow {
        try {
            val request = SendMessageRequestDto(message, messageType.name)
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
}
