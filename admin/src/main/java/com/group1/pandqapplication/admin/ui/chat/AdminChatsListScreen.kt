package com.group1.pandqapplication.admin.ui.chat

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.group1.pandqapplication.shared.data.remote.dto.ProductChat
import java.text.SimpleDateFormat
import java.util.*

// Light theme colors matching the design
private val DarkBackground = Color(0xFFF5F5F5)
private val DarkSurface = Color(0xFFFFFFFF)
private val DarkCard = Color(0xFFFFFFFF)
private val AccentOrange = Color(0xFFEC6B45)
private val AccentGreen = Color(0xFF4CAF50)
private val TextPrimary = Color(0xFF1C1C1E)
private val TextSecondary = Color(0xFF64748B)

/**
 * Screen showing list of all customer chats for admin - Support Center style.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminChatsListScreen(
    onChatSelected: (chatId: String) -> Unit,
    onBackClick: () -> Unit = {},
    viewModel: AdminChatsListViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var isOnline by remember { mutableStateOf(true) }
    var selectedChatId by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        viewModel.loadChats()
    }

    // Auto-refresh chat list every 5 seconds to update unread counts
    LaunchedEffect(Unit) {
        while (true) {
            kotlinx.coroutines.delay(5000)
            viewModel.refreshChats()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        // Header
        SupportCenterHeader(
            isOnline = isOnline,
            onToggleOnline = { isOnline = it },
            onBackClick = onBackClick
        )

        // Search Bar
        SearchBarSection(
            query = searchQuery,
            onQueryChange = { searchQuery = it }
        )

        // Chat List
        Box(modifier = Modifier.weight(1f)) {
            if (state.isLoading && state.chats.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = AccentOrange)
                }
            } else if (state.chats.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.Chat,
                            contentDescription = null,
                            tint = TextSecondary,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "No chats yet",
                            color = TextSecondary,
                            fontSize = 16.sp
                        )
                    }
                }
            } else {
                val filteredChats = state.chats.filter { chat ->
                    searchQuery.isEmpty() ||
                            chat.customerName?.contains(searchQuery, ignoreCase = true) == true ||
                            chat.lastMessagePreview?.contains(searchQuery, ignoreCase = true) == true
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(filteredChats) { chat ->
                        ChatListItem(
                            chat = chat,
                            isSelected = chat.id == selectedChatId,
                            onChatClick = {
                                selectedChatId = chat.id
                                onChatSelected(chat.id)
                            }
                        )
                    }
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
private fun SupportCenterHeader(
    isOnline: Boolean,
    onToggleOnline: (Boolean) -> Unit,
    onBackClick: () -> Unit = {}
) {
    Surface(
        color = Color.White,
        shadowElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            Spacer(modifier = Modifier.windowInsetsTopHeight(WindowInsets.statusBars))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 8.dp)
            ) {
                // Left side - Back button and title
                Row(
                    modifier = Modifier.align(Alignment.CenterStart),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBackClick
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.Black,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(AccentOrange, RoundedCornerShape(6.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Dashboard,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        "Support Center",
                        color = Color.Black,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }

                // Right side - Online toggle
                Row(
                    modifier = Modifier.align(Alignment.CenterEnd),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "ONLINE",
                        color = TextSecondary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Switch(
                        checked = isOnline,
                        onCheckedChange = onToggleOnline,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = AccentGreen,
                            uncheckedThumbColor = Color.Gray,
                            uncheckedTrackColor = Color.DarkGray
                        ),
                        modifier = Modifier.height(24.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchBarSection(
    query: String,
    onQueryChange: (String) -> Unit
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = {
            Text(
                "Search customers or tickets...",
                color = TextSecondary
            )
        },
        leadingIcon = {
            Icon(
                Icons.Default.Search,
                contentDescription = null,
                tint = TextSecondary
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = DarkSurface,
            unfocusedContainerColor = DarkSurface,
            focusedBorderColor = AccentOrange,
            unfocusedBorderColor = Color.Transparent,
            focusedTextColor = TextPrimary,
            unfocusedTextColor = TextPrimary,
            cursorColor = AccentOrange
        ),
        shape = RoundedCornerShape(12.dp),
        singleLine = true
    )
}

@Composable
private fun FilterChipsSection(
    selectedFilter: String,
    onFilterSelected: (String) -> Unit,
    unassignedCount: Int
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            FilterChipItem(
                text = "Active",
                isSelected = selectedFilter == "Active",
                onClick = { onFilterSelected("Active") },
                hasDropdown = true
            )
        }
        item {
            FilterChipItem(
                text = "Unassigned",
                isSelected = selectedFilter == "Unassigned",
                onClick = { onFilterSelected("Unassigned") },
                badgeCount = unassignedCount
            )
        }
        item {
            FilterChipItem(
                text = "Technical",
                isSelected = selectedFilter == "Technical",
                onClick = { onFilterSelected("Technical") },
                hasIcon = true
            )
        }
    }
}

@Composable
private fun FilterChipItem(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    hasDropdown: Boolean = false,
    hasIcon: Boolean = false,
    badgeCount: Int = 0
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        color = if (isSelected) AccentOrange else DarkSurface,
        border = if (!isSelected) BorderStroke(1.dp, Color(0xFFE0E0E0)) else null
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = text,
                color = TextPrimary,
                fontSize = 14.sp,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
            )
            if (badgeCount > 0) {
                Box(
                    modifier = Modifier
                        .background(if (isSelected) Color.White.copy(alpha = 0.3f) else AccentOrange, CircleShape)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "$badgeCount",
                        color = if (isSelected) TextPrimary else Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            if (hasDropdown) {
                Icon(
                    Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = TextPrimary,
                    modifier = Modifier.size(16.dp)
                )
            }
            if (hasIcon) {
                Icon(
                    Icons.Default.Bolt,
                    contentDescription = null,
                    tint = if (isSelected) Color.White else Color(0xFFFFD700),
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}

@Composable
private fun ChatListItem(
    chat: ProductChat,
    isSelected: Boolean,
    onChatClick: () -> Unit
) {
    val isTyping = false
    val isOnline = true
    val hasUrgent = chat.status == "PENDING" && (chat.unreadCount ?: 0) > 0
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (isSelected) {
                    Modifier.border(2.dp, AccentOrange, RoundedCornerShape(16.dp))
                } else {
                    Modifier
                }
            )
            .clip(RoundedCornerShape(16.dp))
            .background(if (isSelected) DarkCard else Color.Transparent)
            .clickable(onClick = onChatClick)
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar with online indicator
            Box {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .border(
                            width = if (isSelected) 3.dp else 2.dp,
                            color = if (isSelected) AccentOrange else Color(0xFFE0E0E0),
                            shape = CircleShape
                        )
                        .padding(3.dp)
                        .clip(CircleShape)
                        .background(DarkSurface),
                    contentAlignment = Alignment.Center
                ) {
                    if (chat.customerAvatar != null) {
                        AsyncImage(
                            model = chat.customerAvatar,
                            contentDescription = "Avatar",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Text(
                            text = (chat.customerName?.firstOrNull() ?: 'U').uppercase(),
                            color = AccentOrange,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                if (isOnline) {
                    Box(
                        modifier = Modifier
                            .size(14.dp)
                            .align(Alignment.BottomEnd)
                            .offset(x = (-2).dp, y = (-2).dp)
                            .border(2.dp, DarkBackground, CircleShape)
                            .background(AccentGreen, CircleShape)
                    )
                }
            }

            // Chat info
            val hasUnread = (chat.unreadCount ?: 0) > 0
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = chat.customerName ?: "Unknown Customer",
                    color = TextPrimary,
                    fontSize = 16.sp,
                    fontWeight = if (hasUnread) FontWeight.Bold else FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                // Show "Bạn: " prefix if last message was sent by admin
                val messagePreview = when {
                    isTyping -> "Typing..."
                    chat.lastMessageSenderRole == "ADMIN" -> "Bạn: ${chat.lastMessagePreview ?: ""}"
                    else -> chat.lastMessagePreview ?: chat.subject ?: "No messages yet"
                }
                Text(
                    text = messagePreview,
                    color = if (isTyping) AccentOrange else if (hasUnread) TextPrimary else TextSecondary,
                    fontSize = 13.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = if (isTyping) FontWeight.Medium else if (hasUnread) FontWeight.SemiBold else FontWeight.Normal
                )
            }

            // Right side - time, badge, status
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = formatChatTime(chat.lastMessageAt ?: chat.updatedAt ?: chat.createdAt),
                    color = if (hasUnread) AccentOrange else TextSecondary,
                    fontSize = 12.sp,
                    fontWeight = if (hasUnread) FontWeight.SemiBold else FontWeight.Normal
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (hasUnread) {
                        Box(
                            modifier = Modifier
                                .size(22.dp)
                                .background(AccentOrange, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${chat.unreadCount}",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    } else {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = AccentGreen,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    if (hasUrgent) {
                        Box(
                            modifier = Modifier
                                .size(22.dp)
                                .background(Color(0xFFCC3300), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "!",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            if (!isSelected) {
                Icon(
                    Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = TextSecondary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
private fun AdminChatTools() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            "ADMIN CHAT TOOLS",
            color = TextSecondary,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ChatToolButton(
                icon = Icons.Default.AutoAwesome,
                label = "Replies",
                color = AccentOrange
            )
            ChatToolButton(
                icon = Icons.Default.Receipt,
                label = "Orders",
                color = Color(0xFF5B9BD5)
            )
            ChatToolButton(
                icon = Icons.Default.Inventory,
                label = "Product",
                color = Color(0xFFE57373)
            )
            ChatToolButton(
                icon = Icons.Default.PersonAdd,
                label = "Transfer",
                color = Color(0xFF9575CD)
            )
        }
    }
}

@Composable
private fun ChatToolButton(
    icon: ImageVector,
    label: String,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .background(DarkSurface, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                icon,
                contentDescription = label,
                tint = color,
                modifier = Modifier.size(28.dp)
            )
        }
        Text(
            text = label,
            color = TextSecondary,
            fontSize = 12.sp
        )
    }
}

private fun formatChatTime(dateString: String?): String {
    if (dateString.isNullOrEmpty()) return ""
    
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val date = inputFormat.parse(dateString) ?: return ""
        val now = Calendar.getInstance()
        val messageDate = Calendar.getInstance().apply { time = date }
        
        val diffMillis = now.timeInMillis - messageDate.timeInMillis
        val diffMinutes = diffMillis / (1000 * 60)
        val diffHours = diffMillis / (1000 * 60 * 60)
        val diffDays = diffMillis / (1000 * 60 * 60 * 24)
        
        when {
            diffMinutes < 1 -> "Just now"
            diffMinutes < 60 -> "${diffMinutes}m ago"
            diffHours < 24 && now.get(Calendar.DAY_OF_YEAR) == messageDate.get(Calendar.DAY_OF_YEAR) -> {
                SimpleDateFormat("h:mm a", Locale.getDefault()).format(date)
            }
            diffDays < 2 -> "Yesterday"
            diffDays < 7 -> SimpleDateFormat("EEEE", Locale.getDefault()).format(date)
            else -> SimpleDateFormat("MMM d", Locale.getDefault()).format(date)
        }
    } catch (e: Exception) {
        ""
    }
}
