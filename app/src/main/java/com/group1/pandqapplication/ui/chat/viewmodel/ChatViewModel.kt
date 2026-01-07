package com.group1.pandqapplication.ui.chat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group1.pandqapplication.shared.data.remote.dto.ChatMessage
import com.group1.pandqapplication.shared.data.remote.dto.ChatMessageDto
import com.group1.pandqapplication.shared.data.remote.dto.MessageType
import com.group1.pandqapplication.shared.data.remote.dto.ProductChat
import com.group1.pandqapplication.shared.data.remote.dto.ProductChatDto
import com.group1.pandqapplication.shared.data.repository.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class ChatUiState {
    object Loading : ChatUiState()
    data class Success<T>(val data: T) : ChatUiState()
    data class Error(val message: String) : ChatUiState()
}

data class ChatScreenState(
    val chat: ProductChat? = null,
    val messages: List<ChatMessage> = emptyList(),
    val unreadCount: Long = 0,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSending: Boolean = false,
    val chatStatus: String = "PENDING"
)

/**
 * ViewModel for managing chat screen state and operations.
 */
@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ChatScreenState())
    val state: StateFlow<ChatScreenState> = _state

    /**
     * Start a chat for a product.
     */
    fun startChat(productId: String, subject: String = "Product Inquiry") {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            chatRepository.startChat(productId, subject).collect { result ->
                result.onSuccess { chat ->
                    _state.value = _state.value.copy(
                        chat = convertDtoToModel(chat),
                        isLoading = false,
                        error = null
                    )
                }.onFailure { error ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = error.message ?: "Failed to start chat"
                    )
                }
            }
        }
    }

    /**
     * Get active chat for a product.
     */
    fun getActiveChat(productId: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            chatRepository.getActiveChat(productId).collect { result ->
                result.onSuccess { chat ->
                    _state.value = _state.value.copy(
                        chat = convertDtoToModel(chat),
                        isLoading = false,
                        error = null
                    )
                    // Load messages
                    loadChatMessages(chat.id)
                }.onFailure { error ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = error.message ?: "Failed to load chat"
                    )
                }
            }
        }
    }

    /**
     * Load chat messages.
     */
    fun loadChatMessages(chatId: String) {
        viewModelScope.launch {
            chatRepository.getChatMessages(chatId).collect { result ->
                result.onSuccess { messages ->
                    _state.value = _state.value.copy(
                        messages = messages.map { convertMessageDtoToModel(it) },
                        error = null
                    )
                    // Mark all as read
                    markAllAsRead(chatId)
                }.onFailure { error ->
                    _state.value = _state.value.copy(
                        error = error.message ?: "Failed to load messages"
                    )
                }
            }
        }
    }

    /**
     * Send a message in the chat.
     */
    fun sendMessage(chatId: String, messageText: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isSending = true)
            chatRepository.sendMessage(chatId, messageText, MessageType.TEXT).collect { result ->
                result.onSuccess { message ->
                    // Add message to list
                    val newMessage = convertMessageDtoToModel(message)
                    _state.value = _state.value.copy(
                        messages = _state.value.messages + newMessage,
                        isSending = false,
                        error = null
                    )
                }.onFailure { error ->
                    _state.value = _state.value.copy(
                        isSending = false,
                        error = error.message ?: "Failed to send message"
                    )
                }
            }
        }
    }

    /**
     * Mark all messages as read.
     */
    fun markAllAsRead(chatId: String) {
        viewModelScope.launch {
            chatRepository.markAllAsRead(chatId).collect { result ->
                result.onSuccess {
                    _state.value = _state.value.copy(unreadCount = 0)
                }.onFailure { error ->
                    // Silently fail
                }
            }
        }
    }

    /**
     * Get unread message count.
     */
    fun getUnreadCount(chatId: String) {
        viewModelScope.launch {
            chatRepository.getUnreadCount(chatId).collect { result ->
                result.onSuccess { count ->
                    _state.value = _state.value.copy(unreadCount = count)
                }.onFailure { error ->
                    // Silently fail
                }
            }
        }
    }

    /**
     * Close the chat.
     */
    fun closeChat(chatId: String) {
        viewModelScope.launch {
            chatRepository.closeChat(chatId).collect { result ->
                result.onSuccess { chat ->
                    _state.value = _state.value.copy(
                        chat = convertDtoToModel(chat),
                        chatStatus = "CLOSED",
                        error = null
                    )
                }.onFailure { error ->
                    _state.value = _state.value.copy(
                        error = error.message ?: "Failed to close chat"
                    )
                }
            }
        }
    }

    /**
     * Reopen the chat.
     */
    fun reopenChat(chatId: String) {
        viewModelScope.launch {
            chatRepository.reopenChat(chatId).collect { result ->
                result.onSuccess { chat ->
                    _state.value = _state.value.copy(
                        chat = convertDtoToModel(chat),
                        chatStatus = "OPEN",
                        error = null
                    )
                }.onFailure { error ->
                    _state.value = _state.value.copy(
                        error = error.message ?: "Failed to reopen chat"
                    )
                }
            }
        }
    }

    /**
     * Get all chats for current customer.
     */
    fun loadMyChats() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            chatRepository.getMyChats().collect { result ->
                result.onSuccess { chats ->
                    // This would be handled by a separate ViewModel for chat list
                }.onFailure { error ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = error.message ?: "Failed to load chats"
                    )
                }
            }
        }
    }

    // Helper functions
    private fun convertDtoToModel(dto: ProductChatDto): ProductChat {
        return ProductChat(
            id = dto.id,
            productId = dto.productId ?: "",
            productName = dto.productName ?: "Unknown Product",
            productImage = dto.productImage,
            customerId = dto.customerId ?: "",
            customerName = dto.customerName ?: "Unknown Customer",
            customerAvatar = dto.customerAvatar,
            adminId = dto.adminId,
            adminName = dto.adminName,
            subject = dto.subject ?: "Product Inquiry",
            status = dto.status ?: "PENDING",
            messageCount = dto.messageCount ?: 0,
            unreadCount = dto.unreadCount?.toLong() ?: 0L,
            createdAt = dto.createdAt ?: "",
            updatedAt = dto.updatedAt ?: "",
            closedAt = dto.closedAt,
            lastMessageAt = dto.lastMessageAt,
            lastMessagePreview = dto.lastMessagePreview
        )
    }

    private fun convertMessageDtoToModel(dto: ChatMessageDto): ChatMessage {
        val messageType = try {
            MessageType.valueOf(dto.messageType ?: "TEXT")
        } catch (e: Exception) {
            MessageType.TEXT
        }
        
        return ChatMessage(
            id = dto.id,
            chatId = dto.productChatId ?: "",
            senderId = dto.senderId ?: "",
            senderName = dto.senderName ?: "Unknown",
            senderRole = dto.senderRole ?: "CUSTOMER",
            senderAvatar = dto.senderAvatar,
            message = dto.message ?: "",
            messageType = messageType,
            isRead = dto.isRead ?: false,
            readAt = dto.readAt,
            createdAt = dto.createdAt ?: ""
        )
    }
}

