package com.group1.pandqapplication.ui.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.group1.pandqapplication.shared.data.remote.dto.ProductChat
import com.group1.pandqapplication.ui.chat.viewmodel.ChatViewModel
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Screen showing all chat conversations for the user
 */
@Composable
fun ChatListScreen(
    onBackClick: () -> Unit,
    onChatClick: (productId: String, productName: String, productImage: String, productPrice: String) -> Unit,
    viewModel: ChatViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getAllChats()
    }

    LaunchedEffect(state.chats) {
        if (state.chats.isNotEmpty()) {
            val chat = state.chats.first()
            android.util.Log.d("ChatListScreen", "First chat - ID: ${chat.id}, Name: '${chat.productName}', Image: '${chat.productImage}', Price: '${chat.productPrice}'")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // HEADER
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            color = Color(0xFF7C3AED),
            shadowElevation = 4.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                Text(
                    "Tin nhắn",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // CONTENT
        Box(modifier = Modifier.weight(1f)) {
            when {
                state.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color(0xFF7C3AED))
                    }
                }
                state.chats.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.Chat,
                                contentDescription = null,
                                modifier = Modifier.size(48.dp),
                                tint = Color.LightGray
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "Không có cuộc trò chuyện",
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(state.chats) { chat ->
                            ChatListItem(
                                chat = chat,
                                onClick = { 
                                    // Set product context before navigating
                                    viewModel.setProductContext(
                                        chat.productId ?: "",
                                        chat.productName ?: "Sản phẩm",
                                        chat.productImage ?: "",
                                        chat.productPrice ?: ""
                                    )
                                    onChatClick(
                                        chat.id,
                                        chat.productName ?: "Sản phẩm",
                                        chat.productImage ?: "",
                                        chat.productPrice ?: ""
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }

        // ERROR MESSAGE
        if (state.error != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFFFEBEE))
                    .padding(12.dp)
            ) {
                Text(
                    state.error!!,
                    color = Color(0xFFC62828),
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
private fun ChatListItem(
    chat: ProductChat,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() },
        color = Color.White,
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // PRODUCT IMAGE
            AsyncImage(
                model = chat.productImage,
                contentDescription = "Product",
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            // CHAT INFO
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    chat.productName,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    chat.lastMessagePreview ?: "Không có tin nhắn",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    formatChatTime(chat.lastMessageAt),
                    fontSize = 11.sp,
                    color = Color.LightGray
                )
            }

            // UNREAD BADGE
            if (chat.unreadCount > 0) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF7C3AED)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        if (chat.unreadCount > 99) "99+" else chat.unreadCount.toString(),
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

private fun formatChatTime(lastMessageAt: String?): String {
    if (lastMessageAt == null || lastMessageAt.isEmpty()) return ""
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val date = inputFormat.parse(lastMessageAt)
        if (date != null) {
            val now = System.currentTimeMillis()
            val messageTime = date.time
            val diff = now - messageTime

            when {
                diff < 60000 -> "Vừa xong"  // Less than 1 minute
                diff < 3600000 -> "${diff / 60000} phút trước"  // Less than 1 hour
                diff < 86400000 -> "${diff / 3600000} giờ trước"  // Less than 1 day
                diff < 604800000 -> "${diff / 86400000} ngày trước"  // Less than 1 week
                else -> {
                    val outputFormat = SimpleDateFormat("dd/MM/yy", Locale.getDefault())
                    outputFormat.format(date)
                }
            }
        } else ""
    } catch (e: Exception) {
        ""
    }
}

@Composable
private fun ChatIcon() {
    // Placeholder - Material Design chat icon
    Icon(
        imageVector = Icons.Default.Chat,
        contentDescription = "Chat",
        tint = Color.Black
    )
}
