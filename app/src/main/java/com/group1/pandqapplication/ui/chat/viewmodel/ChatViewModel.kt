package com.group1.pandqapplication.ui.chat.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group1.pandqapplication.shared.data.remote.dto.ChatMessage
import com.group1.pandqapplication.shared.data.remote.dto.ChatMessageDto
import com.group1.pandqapplication.shared.data.remote.dto.MessageType
import com.group1.pandqapplication.shared.data.remote.dto.ProductChat
import com.group1.pandqapplication.shared.data.remote.dto.ProductChatDto
import com.group1.pandqapplication.shared.data.repository.ChatRepository
import com.group1.pandqapplication.shared.util.FileUtil
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
    val chatStatus: String = "PENDING",
    val chats: List<ProductChat> = emptyList(),
    val productId: String = "",
    val productName: String = "",
    val productImage: String? = null,
    val productPrice: String = ""
)

/**
 * ViewModel for managing chat screen state and operations.
 */
@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val application: Application,
    private val productContextHolder: com.group1.pandqapplication.ui.chat.ProductContextHolder
) : ViewModel() {

    private val _state = MutableStateFlow(ChatScreenState())
    val state: StateFlow<ChatScreenState> = _state

    init {
        // Load product context from holder if available
        android.util.Log.d("ChatViewModel", "Init - ProductContextHolder: id=${productContextHolder.productId}, name=${productContextHolder.productName}, image=${productContextHolder.productImage}, price=${productContextHolder.productPrice}")
        if (productContextHolder.productId.isNotEmpty()) {
            _state.value = _state.value.copy(
                productId = productContextHolder.productId,
                productName = productContextHolder.productName,
                productImage = productContextHolder.productImage,
                productPrice = productContextHolder.productPrice
            )
            android.util.Log.d("ChatViewModel", "State updated with product context")
        }
    }

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
     * Get general chat with admin (continuous thread across all products).
     */
    fun getGeneralChat() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            chatRepository.getGeneralChat().collect { result ->
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
     * Set product context for displaying in divider and sending with messages
     */
    fun setProductContext(productId: String, productName: String, productImage: String, productPrice: String) {
        productContextHolder.setContext(productId, productName, productImage, productPrice)
        _state.value = _state.value.copy(
            productId = productId,
            productName = productName,
            productImage = productImage,
            productPrice = productPrice
        )
    }

    /**
     * Load product context from holder (called by ChatScreen on launch)
     */
    fun loadProductContextFromHolder() {
        android.util.Log.d("ChatViewModel", "loadProductContextFromHolder - holder hashCode=${productContextHolder.hashCode()}")
        android.util.Log.d("ChatViewModel", "loadProductContextFromHolder - holder: id=${productContextHolder.productId}, name=${productContextHolder.productName}, image=${productContextHolder.productImage}, price=${productContextHolder.productPrice}")
        if (productContextHolder.productId.isNotEmpty()) {
            _state.value = _state.value.copy(
                productId = productContextHolder.productId,
                productName = productContextHolder.productName,
                productImage = productContextHolder.productImage,
                productPrice = productContextHolder.productPrice
            )
            android.util.Log.d("ChatViewModel", "State updated from holder")
        }
    }

    /**
     * Get all chats for the user.
     */
    fun getAllChats() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            chatRepository.getAllChats().collect { result ->
                result.onSuccess { chats ->
                    _state.value = _state.value.copy(
                        chats = chats.map { convertDtoToModel(it) },
                        isLoading = false,
                        error = null
                    )
                }.onFailure { error ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = error.message ?: "Failed to load chats"
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
     * Refresh messages - called periodically to fetch new messages from admin.
     */
    fun refreshMessages(chatId: String) {
        viewModelScope.launch {
            chatRepository.getChatMessages(chatId).collect { result ->
                result.onSuccess { messages ->
                    _state.value = _state.value.copy(
                        messages = messages.map { convertMessageDtoToModel(it) },
                        error = null
                    )
                }.onFailure {
                    // Silently fail on refresh
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
            
            // Get product context from current state
            val productContextId = _state.value.productId.takeIf { it.isNotEmpty() }
            val productContextName = _state.value.productName.takeIf { it.isNotEmpty() }
            val productContextImage = _state.value.productImage.takeIf { !it.isNullOrEmpty() }
            val productContextPrice = _state.value.productPrice.takeIf { it.isNotEmpty() }
            
            android.util.Log.d("ChatViewModel", "sendMessage - productContextId=$productContextId, name=$productContextName, image=$productContextImage, price=$productContextPrice")
            android.util.Log.d("ChatViewModel", "sendMessage - state.productId=${_state.value.productId}, state.productName=${_state.value.productName}")
            
            chatRepository.sendMessage(
                chatId = chatId, 
                message = messageText, 
                messageType = MessageType.TEXT,
                productContextId = productContextId,
                productContextName = productContextName,
                productContextImage = productContextImage,
                productContextPrice = productContextPrice
            ).collect { result ->
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
     * Upload and send an image in the chat.
     * This uploads to server first, which then saves to database.
     */
    fun sendImage(chatId: String, imageUri: String) {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(isSending = true)
                // Convert URI to actual file path
                val filePath = FileUtil.getFilePathFromUri(application, android.net.Uri.parse(imageUri))
                chatRepository.uploadChatImage(chatId, filePath).collect { result ->
                    result.onSuccess { message ->
                        val newMessage = convertMessageDtoToModel(message)
                        _state.value = _state.value.copy(
                            messages = _state.value.messages + newMessage,
                            isSending = false,
                            error = null
                        )
                    }.onFailure { error ->
                        _state.value = _state.value.copy(
                            isSending = false,
                            error = error.message ?: "Failed to upload image"
                        )
                    }
                }
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isSending = false,
                    error = e.message ?: "Error processing image"
                )
            }
        }
    }

    /**
     * Send an image message with URL (for Cloudinary client-side upload).
     * 
     * Flow:
     * 1. Client uploads image to Cloudinary directly from Android
     * 2. Cloudinary returns the public URL
     * 3. This method sends the URL to server
     * 4. Server creates a chat message with the Cloudinary URL and saves to database
     */
    fun sendImageWithCloudinaryUrl(chatId: String, imageUrl: String, fileName: String = "Image") {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(isSending = true)
                chatRepository.sendImageMessage(chatId, imageUrl, fileName).collect { result ->
                    result.onSuccess { message ->
                        val newMessage = convertMessageDtoToModel(message)
                        _state.value = _state.value.copy(
                            messages = _state.value.messages + newMessage,
                            isSending = false,
                            error = null
                        )
                    }.onFailure { error ->
                        _state.value = _state.value.copy(
                            isSending = false,
                            error = error.message ?: "Failed to send image message"
                        )
                    }
                }
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isSending = false,
                    error = e.message ?: "Error sending image message"
                )
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
            productPrice = dto.productPrice ?: "",
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
            createdAt = dto.createdAt ?: "",
            imageUrl = dto.imageUrl,
            productContextId = dto.productContextId,
            productContextName = dto.productContextName,
            productContextImage = dto.productContextImage,
            productContextPrice = dto.productContextPrice
        )
    }
}