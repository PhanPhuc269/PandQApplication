package com.group1.pandqapplication.ui.chat.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.group1.pandqapplication.shared.data.remote.dto.ChatMessage
import com.group1.pandqapplication.shared.data.remote.dto.ChatStatus
import com.group1.pandqapplication.ui.chat.viewmodel.ChatViewModel
import com.group1.pandqapplication.ui.chat.viewmodel.ChatScreenState
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Chat screen composable for real-time chat with admin about a product.
 */
@Composable
fun ChatScreen(
    productId: String,
    onBackClick: () -> Unit,
    userId: String,
    viewModel: ChatViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    var messageText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(productId) {
        viewModel.getActiveChat(productId)
    }

    LaunchedEffect(state.messages.size) {
        if (state.messages.isNotEmpty()) {
            scope.launch {
                listState.animateScrollToItem(state.messages.size - 1)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Header
        ChatHeader(
            productName = state.chat?.productName ?: "Product",
            chatStatus = state.chatStatus,
            adminName = state.chat?.adminName ?: "Support Team",
            onBackClick = onBackClick,
            onCloseChat = { state.chat?.id?.let { viewModel.closeChat(it) } }
        )

        // Messages List
        if (state.isLoading && state.messages.isEmpty()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (state.messages.isEmpty()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text("No messages yet. Start the conversation!")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                state = listState,
                contentPadding = PaddingValues(8.dp)
            ) {
                items(state.messages) { message ->
                    MessageItem(message)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

        // Error Message
        if (state.error != null) {
            Text(
                text = state.error ?: "",
                color = Color.Red,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                fontSize = 12.sp
            )
        }

        // Message Input
        if (state.chatStatus != "CLOSED") {
            MessageInputField(
                messageText = messageText,
                onMessageChange = { messageText = it },
                onSendClick = {
                    if (messageText.isNotBlank() && state.chat != null) {
                        viewModel.sendMessage(state.chat!!.id, messageText)
                        messageText = ""
                    }
                },
                isSending = state.isSending
            )
        } else {
            ClosedChatNotice()
        }
    }
}

@Composable
private fun ChatHeader(
    productName: String,
    chatStatus: String,
    adminName: String,
    onBackClick: () -> Unit,
    onCloseChat: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        color = Color(0xFF6200EE),
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                Column(modifier = Modifier.padding(start = 8.dp)) {
                    Text(
                        text = productName,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Text(
                        text = adminName,
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 12.sp
                    )
                }
            }

            // Status badge
            Surface(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(
                        if (chatStatus == "OPEN") Color(0xFF4CAF50) else Color(0xFFF44336)
                    ),
                color = if (chatStatus == "OPEN") Color(0xFF4CAF50) else Color(0xFFF44336)
            ) {
                Text(
                    text = chatStatus,
                    color = Color.White,
                    fontSize = 10.sp,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun MessageItem(message: ChatMessage) {
    val isFromAdmin = message.senderRole == "ADMIN"
    val defaultAvatarBg = when (message.senderRole) {
        "ADMIN" -> Color(0xFF6200EE)
        else -> Color(0xFF4CAF50)
    }
    val initials = message.senderName.take(1).uppercase()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
        horizontalArrangement = if (isFromAdmin) Arrangement.Start else Arrangement.End
    ) {
        if (isFromAdmin) {
            // Admin message - avatar on left
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(50f))
                    .background(defaultAvatarBg),
                contentAlignment = Alignment.Center
            ) {
                if (!message.senderAvatar.isNullOrEmpty()) {
                    AsyncImage(
                        model = message.senderAvatar,
                        contentDescription = "Admin avatar",
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(50f)),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Text(
                        initials,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }

            Column(
                modifier = Modifier
                    .padding(start = 12.dp)
                    .fillMaxWidth(0.75f)
            ) {
                Text(
                    text = message.senderName,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                    color = Color(0xFF333333)
                )
                Surface(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFE8EAF6)),
                    color = Color(0xFFE8EAF6)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = message.message,
                            fontSize = 14.sp,
                            color = Color.Black
                        )
                        Text(
                            text = formatTime(message.createdAt),
                            fontSize = 11.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }
        } else {
            // Customer message - avatar on right
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.75f),
                horizontalAlignment = Alignment.End
            ) {
                Surface(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF6200EE)),
                    color = Color(0xFF6200EE)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = message.message,
                            color = Color.White,
                            fontSize = 14.sp
                        )
                        Row(
                            modifier = Modifier
                                .padding(top = 4.dp)
                                .align(Alignment.End),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = formatTime(message.createdAt),
                                fontSize = 11.sp,
                                color = Color.White.copy(alpha = 0.8f),
                                modifier = Modifier.padding(end = 4.dp)
                            )
                            // Message status icon
                            Icon(
                                imageVector = if (message.isRead) Icons.Filled.DoneAll else Icons.Filled.Check,
                                contentDescription = if (message.isRead) "Read" else "Sent",
                                modifier = Modifier.size(14.dp),
                                tint = if (message.isRead) Color(0xFF4CAF50) else Color.White.copy(alpha = 0.8f)
                            )
                        }
                    }
                }
            }

            // Customer avatar on right
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(50f))
                    .background(Color(0xFF4CAF50))
                    .padding(start = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                if (!message.senderAvatar.isNullOrEmpty()) {
                    AsyncImage(
                        model = message.senderAvatar,
                        contentDescription = "Customer avatar",
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(50f)),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Text(
                        initials,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

// Helper function to format timestamp
private fun formatTime(createdAt: String?): String {
    if (createdAt == null || createdAt.isEmpty()) return ""
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val date = inputFormat.parse(createdAt)
        if (date != null) outputFormat.format(date) else ""
    } catch (e: Exception) {
        ""
    }
}

@Composable
private fun MessageInputField(
    messageText: String,
    onMessageChange: (String) -> Unit,
    onSendClick: () -> Unit,
    isSending: Boolean
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        color = Color.White,
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = messageText,
                onValueChange = onMessageChange,
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                placeholder = { Text("Type a message...") },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFF5F5F5),
                    unfocusedContainerColor = Color(0xFFF5F5F5)
                ),
                shape = RoundedCornerShape(24.dp)
            )

            IconButton(
                onClick = onSendClick,
                enabled = messageText.isNotBlank() && !isSending
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Send message",
                    tint = if (messageText.isNotBlank()) Color(0xFF6200EE) else Color.Gray
                )
            }
        }
    }
}

@Composable
private fun ClosedChatNotice() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(Color(0xFFF44336)),
        color = Color(0xFFF44336)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "This chat has been closed",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
