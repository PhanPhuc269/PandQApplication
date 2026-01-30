package com.group1.pandqapplication.ui.chat.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.group1.pandqapplication.shared.data.remote.dto.ProductChat
import com.group1.pandqapplication.ui.chat.viewmodel.ChatListViewModel

/**
 * Chat list screen showing all chats for a customer.
 */
@Composable
fun ChatListScreen(
    onChatClick: (String) -> Unit,
    onNewChatClick: () -> Unit,
    viewModel: ChatListViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadMyChats()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Header
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
                Text(
                    text = "My Chats",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )

                FloatingActionButton(
                    onClick = onNewChatClick,
                    containerColor = Color.White,
                    contentColor = Color(0xFF6200EE)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Start new chat")
                }
            }
        }

        // Loading state
        if (state.isLoading && state.chats.isEmpty()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        // Empty state
        else if (state.chats.isEmpty()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "No chats yet. Start a chat with admin about a product!",
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
        // Chat list
        else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(8.dp)
            ) {
                items(state.chats) { chat ->
                    ChatListItem(
                        chat = chat,
                        onClick = { onChatClick(chat.id) }
                    )
                }
            }
        }

        // Error message
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
            .padding(8.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() },
        color = Color(0xFFFAFAFA),
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Product image
            AsyncImage(
                model = chat.productImage,
                contentDescription = "Product image",
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Gray),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = chat.productName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    maxLines = 1
                )

                Text(
                    text = chat.subject,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    maxLines = 1
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "${chat.messageCount} messages",
                        fontSize = 10.sp,
                        color = Color.Gray
                    )

                    if (chat.unreadCount > 0) {
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50f))
                                .background(Color(0xFF6200EE)),
                            color = Color(0xFF6200EE)
                        ) {
                            Text(
                                text = "${chat.unreadCount}",
                                color = Color.White,
                                fontSize = 10.sp,
                                modifier = Modifier.padding(4.dp),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Status indicator
            Surface(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(
                        when (chat.status) {
                            "OPEN" -> Color(0xFF4CAF50)
                            "PENDING" -> Color(0xFFFFC107)
                            else -> Color(0xFFF44336)
                        }
                    ),
                color = when (chat.status) {
                    "OPEN" -> Color(0xFF4CAF50)
                    "PENDING" -> Color(0xFFFFC107)
                    else -> Color(0xFFF44336)
                }
            ) {
                Text(
                    text = chat.status.substring(0, 1),
                    color = Color.White,
                    fontSize = 10.sp,
                    modifier = Modifier.padding(4.dp),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
