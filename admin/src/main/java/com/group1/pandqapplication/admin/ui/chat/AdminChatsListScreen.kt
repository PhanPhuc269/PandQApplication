package com.group1.pandqapplication.admin.ui.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.group1.pandqapplication.shared.data.remote.dto.ProductChat

/**
 * Screen showing list of all customer chats for admin.
 */
@Composable
fun AdminChatsListScreen(
    onChatSelected: (chatId: String) -> Unit,
    viewModel: AdminChatsListViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadChats()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFE74C3C))
                .padding(16.dp)
        ) {
            Text(
                text = "Customer Chats",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Loading state
        if (state.isLoading && state.chats.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (state.chats.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No chats yet", fontSize = 16.sp)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                items(state.chats) { chat ->
                    ChatListItem(
                        chat = chat,
                        onChatClick = { onChatSelected(chat.id) }
                    )
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
    }
}

@Composable
private fun ChatListItem(
    chat: ProductChat,
    onChatClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF5F5F5))
            .clickable(onClick = onChatClick)
            .padding(12.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = chat.customerName ?: "Unknown Customer",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    Text(
                        text = chat.productName ?: "Unknown Product",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 4.dp)
                    )

                    Text(
                        text = chat.subject ?: "No subject",
                        fontSize = 11.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 2.dp),
                        maxLines = 1
                    )
                }

                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    // Unread badge
                    if (chat.unreadCount > 0) {
                        Badge(
                            modifier = Modifier.padding(bottom = 4.dp),
                            containerColor = Color(0xFFE74C3C)
                        ) {
                            Text(
                                text = "${chat.unreadCount}",
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Status badge
                    Surface(
                        color = when (chat.status) {
                            "OPEN" -> Color(0xFF4CAF50)
                            "PENDING" -> Color(0xFFFFA500)
                            else -> Color.Gray
                        },
                        shape = RoundedCornerShape(4.dp),
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Text(
                            text = chat.status ?: "UNKNOWN",
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    Text(
                        text = "${chat.messageCount ?: 0} messages",
                        fontSize = 10.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            Text(
                text = "Created: ${chat.createdAt ?: "Unknown"}",
                fontSize = 10.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}
