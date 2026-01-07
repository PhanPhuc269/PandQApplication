package com.group1.pandqapplication.admin.ui.chat

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
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Admin chat screen for viewing and responding to customer inquiries.
 */
@Composable
fun AdminChatScreen(
    chatId: String,
    onBackClick: () -> Unit,
    viewModel: AdminChatViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    var messageText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(chatId) {
        viewModel.loadChat(chatId)
    }

    // Auto-refresh messages every 3 seconds
    LaunchedEffect(chatId) {
        while (true) {
            kotlinx.coroutines.delay(3000)
            viewModel.refreshMessages(chatId)
        }
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
        AdminChatHeader(
            customerName = state.customerName,
            productName = state.chat?.productName ?: "Product",
            onBackClick = onBackClick
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
                Text("No messages yet.", fontSize = 16.sp)
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
                    AdminMessageItem(message)
                }
            }
        }

        // Error message
        if (state.error != null) {
            Text(
                text = state.error!!,
                color = Color.Red,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                fontSize = 12.sp
            )
        }

        // Message Input
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            TextField(
                value = messageText,
                onValueChange = { messageText = it },
                modifier = Modifier
                    .weight(1f)
                    .heightIn(min = 40.dp, max = 100.dp),
                placeholder = { Text("Type reply...") },
                enabled = !state.isSending,
                singleLine = false
            )

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(
                onClick = {
                    state.chat?.id?.let {
                        viewModel.sendMessage(it, messageText)
                        messageText = ""
                    }
                },
                enabled = messageText.isNotBlank() && !state.isSending
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Send",
                    tint = if (messageText.isNotBlank() && !state.isSending)
                        Color(0xFFE74C3C) else Color.Gray
                )
            }
        }
    }
}

@Composable
private fun AdminChatHeader(
    customerName: String,
    productName: String,
    onBackClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp),
        color = Color(0xFFE74C3C),
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(onClick = onBackClick, modifier = Modifier.size(40.dp)) {
                Icon(Icons.Default.Close, contentDescription = "Back", tint = Color.White, modifier = Modifier.size(24.dp))
            }

            Column(modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)) {
                Text(
                    text = "Chat with $customerName",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Product: $productName",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 13.sp
                )
            }
        }
    }
}

@Composable
private fun AdminMessageItem(message: ChatMessage) {
    val isAdmin = message.senderRole == "ADMIN"
    val defaultAvatarBg = when (message.senderRole) {
        "ADMIN" -> Color(0xFFE74C3C)
        else -> Color(0xFF2196F3)
    }
    val initials = message.senderName.take(1).uppercase()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 12.dp),
        horizontalArrangement = if (isAdmin) Arrangement.End else Arrangement.Start
    ) {
        if (isAdmin) {
            // Admin message on right
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.75f),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = message.senderName,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Gray,
                    modifier = Modifier.padding(end = 12.dp)
                )
                
                Surface(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFE74C3C)),
                    color = Color(0xFFE74C3C)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = message.message,
                            color = Color.White,
                            fontSize = 14.sp
                        )
                        Row(
                            modifier = Modifier.padding(top = 4.dp),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = formatAdminTime(message.createdAt),
                                fontSize = 11.sp,
                                color = Color.White.copy(alpha = 0.8f),
                                modifier = Modifier.padding(end = 4.dp)
                            )
                            Icon(
                                imageVector = if (message.isRead) Icons.Filled.DoneAll else Icons.Filled.Check,
                                contentDescription = if (message.isRead) "Read" else "Sent",
                                modifier = Modifier.size(14.dp),
                                tint = Color.White.copy(alpha = 0.8f)
                            )
                        }
                    }
                }
            }

            // Admin avatar on right
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(50f))
                    .background(defaultAvatarBg)
                    .padding(start = 12.dp),
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
        } else {
            // Customer message on left
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

            Column(
                modifier = Modifier
                    .padding(start = 12.dp)
                    .fillMaxWidth(0.75f)
            ) {
                Text(
                    text = message.senderName,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Gray
                )
                
                Surface(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFF0F0F0)),
                    color = Color(0xFFF0F0F0)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = message.message,
                            color = Color.Black,
                            fontSize = 14.sp
                        )
                        Text(
                            text = formatAdminTime(message.createdAt),
                            fontSize = 11.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

private fun formatAdminTime(createdAt: String?): String {
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
