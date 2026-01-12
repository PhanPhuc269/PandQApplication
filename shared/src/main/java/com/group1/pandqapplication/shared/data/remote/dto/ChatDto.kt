package com.group1.pandqapplication.shared.data.remote.dto

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime
import java.util.UUID

// ========== Request DTOs ==========

data class StartChatRequestDto(
    val subject: String = "Product Inquiry"
)

data class SendMessageRequestDto(
    val message: String,
    val messageType: String = "TEXT",  // TEXT, IMAGE, FILE, SYSTEM
    // Product context - for displaying "Đang hỏi về [Product]" divider
    val productContextId: String? = null,
    val productContextName: String? = null,
    val productContextImage: String? = null,
    val productContextPrice: String? = null
)

// ========== Response DTOs ==========

data class ProductChatDto(
    val id: String,  // UUID
    @SerializedName("productId")
    val productId: String? = null,
    @SerializedName("productName")
    val productName: String? = null,
    @SerializedName("productImage")
    val productImage: String? = null,
    @SerializedName("productPrice")
    val productPrice: String? = null,
    @SerializedName("customerId")
    val customerId: String? = null,
    @SerializedName("customerName")
    val customerName: String? = null,
    @SerializedName("customerAvatar")
    val customerAvatar: String? = null,
    @SerializedName("adminId")
    val adminId: String? = null,
    @SerializedName("adminName")
    val adminName: String? = null,
    @SerializedName("subject")
    val subject: String? = null,
    @SerializedName("status")
    val status: String? = null,  // OPEN, CLOSED, PENDING
    @SerializedName("messageCount")
    val messageCount: Int? = null,
    @SerializedName("unreadCount")
    val unreadCount: Int? = null,
    @SerializedName("createdAt")
    val createdAt: String? = null,
    @SerializedName("updatedAt")
    val updatedAt: String? = null,
    @SerializedName("closedAt")
    val closedAt: String? = null,
    @SerializedName("lastMessageAt")
    val lastMessageAt: String? = null,
    @SerializedName("lastMessagePreview")
    val lastMessagePreview: String? = null,
    @SerializedName("lastMessageSenderRole")
    val lastMessageSenderRole: String? = null,  // ADMIN or CUSTOMER
    @SerializedName("messages")
    val messages: List<ChatMessageDto>? = null
)

data class ChatMessageDto(
    val id: String,
    @SerializedName("chatId")
    val productChatId: String? = null,
    @SerializedName("senderId")
    val senderId: String? = null,
    @SerializedName("senderName")
    val senderName: String? = null,
    @SerializedName("senderRole")
    val senderRole: String? = null,
    @SerializedName("senderAvatar")
    val senderAvatar: String? = null,
    @SerializedName("message")
    val message: String? = null,
    @SerializedName("messageType")
    val messageType: String? = null,
    @SerializedName("isRead")
    val isRead: Boolean? = null,
    @SerializedName("readAt")
    val readAt: String? = null,
    @SerializedName("createdAt")
    val createdAt: String? = null,
    @SerializedName("imageUrl")
    val imageUrl: String? = null,
    // Product context - for displaying "Đang hỏi về [Product]" divider
    @SerializedName("productContextId")
    val productContextId: String? = null,
    @SerializedName("productContextName")
    val productContextName: String? = null,
    @SerializedName("productContextImage")
    val productContextImage: String? = null,
    @SerializedName("productContextPrice")
    val productContextPrice: String? = null
)

// ========== Domain Models ==========

data class ProductChat(
    val id: String,
    val productId: String = "",
    val productName: String = "",
    val productImage: String? = null,
    val productPrice: String = "",
    val customerId: String = "",
    val customerName: String = "",
    val customerAvatar: String? = null,
    val adminId: String? = null,
    val adminName: String? = null,
    val subject: String = "",
    val status: String = "PENDING",  // OPEN, CLOSED, PENDING
    val messageCount: Int = 0,
    val unreadCount: Long = 0L,
    val createdAt: String = "",
    val updatedAt: String = "",
    val closedAt: String? = null,
    val lastMessageAt: String? = null,
    val lastMessagePreview: String? = null,
    val lastMessageSenderRole: String? = null,  // ADMIN or CUSTOMER
    val messages: List<ChatMessage> = emptyList()
)

data class ChatMessage(
    val id: String,
    val chatId: String = "",
    val senderId: String = "",
    val senderName: String = "Unknown",
    val senderRole: String = "CUSTOMER",  // ADMIN, CUSTOMER
    val senderAvatar: String? = null,
    val message: String = "",
    val messageType: MessageType = MessageType.TEXT,
    val isRead: Boolean = false,
    val readAt: String? = null,
    val createdAt: String = "",
    val imageUrl: String? = null,
    // Product context - for displaying "Đang hỏi về [Product]" divider
    val productContextId: String? = null,
    val productContextName: String? = null,
    val productContextImage: String? = null,
    val productContextPrice: String? = null
)

enum class ChatStatus {
    OPEN,
    CLOSED,
    PENDING
}

enum class MessageType {
    TEXT,
    IMAGE,
    FILE,
    SYSTEM
}
