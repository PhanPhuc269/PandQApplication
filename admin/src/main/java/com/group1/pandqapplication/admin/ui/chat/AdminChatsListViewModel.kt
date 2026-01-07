package com.group1.pandqapplication.admin.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group1.pandqapplication.shared.data.remote.dto.ProductChat
import com.group1.pandqapplication.shared.data.remote.dto.ProductChatDto
import com.group1.pandqapplication.shared.data.repository.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AdminChatsListState(
    val chats: List<ProductChat> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

/**
 * ViewModel for admin chats list screen.
 */
@HiltViewModel
class AdminChatsListViewModel @Inject constructor(
    private val chatRepository: ChatRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AdminChatsListState())
    val state: StateFlow<AdminChatsListState> = _state

    /**
     * Load all chats.
     */
    fun loadChats() {
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
}
