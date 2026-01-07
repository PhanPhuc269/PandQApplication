package com.group1.pandqapplication.admin.ui.chat

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

sealed class AdminChatUiState {
    object Loading : AdminChatUiState()
    data class Success<T>(val data: T) : AdminChatUiState()
    data class Error(val message: String) : AdminChatUiState()
}

data class AdminChatScreenState(
    val chat: ProductChat? = null,
    val messages: List<ChatMessage> = emptyList(),
    val customerName: String = "Customer",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSending: Boolean = false
)

/**
 * ViewModel for admin chat management.
 */
@HiltViewModel
class AdminChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AdminChatScreenState())
    val state: StateFlow<AdminChatScreenState> = _state

    /**
     * Load chat details and messages.
     */
    fun loadChat(chatId: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            chatRepository.getChatDetails(chatId).collect { result ->
                result.onSuccess { chat ->
                    _state.value = _state.value.copy(
                        chat = convertDtoToModel(chat),
                        customerName = chat.customerName ?: "Customer",
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
                }.onFailure { error ->
                    _state.value = _state.value.copy(
                        error = error.message ?: "Failed to load messages"
                    )
                }
            }
        }
    }

    /**
     * Send message from admin.
     */
    fun sendMessage(chatId: String, messageText: String) {
        if (messageText.isBlank()) return

        viewModelScope.launch {
            _state.value = _state.value.copy(isSending = true)
            chatRepository.sendMessage(chatId, messageText, MessageType.TEXT).collect { result ->
                result.onSuccess { message ->
                    // Add message to list
                    val updatedMessages = _state.value.messages + convertMessageDtoToModel(message)
                    _state.value = _state.value.copy(
                        messages = updatedMessages,
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
     * Refresh messages - called periodically to fetch new messages.
     */
    fun refreshMessages(chatId: String) {
        viewModelScope.launch {
            chatRepository.getChatMessages(chatId).collect { result ->
                result.onSuccess { messages ->
                    _state.value = _state.value.copy(
                        messages = messages.map { convertMessageDtoToModel(it) },
                        error = null
                    )
                }.onFailure { error ->
                    // Silently fail on refresh
                }
            }
        }
    }

    private fun convertDtoToModel(dto: ProductChatDto): ProductChat {
        return ProductChat(
            id = dto.id,
            productId = dto.productId ?: "",
            productName = dto.productName ?: "",
            productImage = dto.productImage,
            customerId = dto.customerId ?: "",
            customerName = dto.customerName ?: "",
            customerAvatar = dto.customerAvatar,
            adminId = dto.adminId,
            adminName = dto.adminName,
            subject = dto.subject ?: "",
            status = dto.status ?: "PENDING",
            messageCount = dto.messageCount ?: 0,
            unreadCount = dto.unreadCount?.toLong() ?: 0L,
            createdAt = dto.createdAt ?: "",
            updatedAt = dto.updatedAt ?: "",
            closedAt = dto.closedAt,
            lastMessageAt = dto.lastMessageAt,
            lastMessagePreview = dto.lastMessagePreview,
            messages = emptyList()
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
