package com.group1.pandqapplication.ui.notification

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.NotificationImportant
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.group1.pandqapplication.shared.data.remote.dto.NotificationDto
import com.group1.pandqapplication.shared.data.remote.dto.NotificationPreferenceRequest
import com.group1.pandqapplication.shared.data.remote.dto.NotificationPreferenceResponse
import com.group1.pandqapplication.shared.ui.theme.BackgroundLight
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import com.group1.pandqapplication.R
import androidx.compose.ui.res.stringResource

// Date category keys for i18n
enum class DateCategory {
    TODAY, YESTERDAY, LAST_WEEK, OTHER
}

// UI Data Models for display
data class NotificationUiModel(
    val id: String,
    val type: NotificationType,
    val title: String,
    val time: String,
    val description: String,
    val isRead: Boolean,
    val dateCategory: DateCategory, // Use enum for i18n
    val targetUrl: String? = null
)

enum class NotificationType {
    ORDER, PROMO, SYSTEM, FEEDBACK, CHAT
}

// Helper to convert API data to UI model
private fun NotificationDto.toUiModel(): NotificationUiModel {
    val notifType = when (type) {
        "ORDER_UPDATE", "PAYMENT_SUCCESS" -> NotificationType.ORDER
        "PROMOTION" -> NotificationType.PROMO
        "SYSTEM" -> NotificationType.SYSTEM
        "CHAT_MESSAGE" -> NotificationType.CHAT
        else -> NotificationType.FEEDBACK
    }
    
    val (timeStr, dateCategory) = formatDateTime(createdAt)
    
    return NotificationUiModel(
        id = id,
        type = notifType,
        title = title,
        time = timeStr,
        description = body,
        isRead = isRead,
        dateCategory = dateCategory,
        targetUrl = targetUrl
    )
}

private fun formatDateTime(isoString: String): Pair<String, DateCategory> {
    return try {
        // Handle timezone if needed, simple parsing for now
        // Assuming ISO format like "2023-10-27T10:00:00"
        val dateTime = LocalDateTime.parse(isoString.substringBefore("+").substringBefore("Z"))
        val now = LocalDateTime.now()
        val daysBetween = ChronoUnit.DAYS.between(dateTime.toLocalDate(), now.toLocalDate())
        
        val timeStr = dateTime.format(DateTimeFormatter.ofPattern("HH:mm"))
        val dateCategory = when {
            daysBetween == 0L -> DateCategory.TODAY
            daysBetween == 1L -> DateCategory.YESTERDAY
            daysBetween <= 7L -> DateCategory.LAST_WEEK
            else -> DateCategory.OTHER
        }
        Pair(timeStr, dateCategory)
    } catch (e: Exception) {
        Pair("--:--", DateCategory.OTHER)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(
    viewModel: NotificationViewModel = hiltViewModel(),
    onNotificationClick: (String?) -> Unit = {} // targetUrl for navigation
) {
    val uiState by viewModel.uiState.collectAsState()
    val primaryColor = Color(0xFFec3713)
    val backgroundColor = BackgroundLight
    
    var showSettingsDialog by remember { mutableStateOf(false) }

    // Convert API notifications to UI models
    val allNotifications = remember(uiState.notifications) {
        uiState.notifications.map { it.toUiModel() }
    }

    // Filter Logic matching ViewModel
    val filteredNotifications = when (uiState.selectedFilter) {
        "Orders" -> allNotifications.filter { it.type == NotificationType.ORDER }
        "Promos" -> allNotifications.filter { it.type == NotificationType.PROMO }
        "Chats" -> allNotifications.filter { it.type == NotificationType.CHAT }
        "System" -> allNotifications.filter { it.type == NotificationType.SYSTEM }
        else -> allNotifications
    }

    // Grouping Logic - we use dateCategory enum for grouping
    val groupedNotifications = filteredNotifications.groupBy { it.dateCategory }

    Scaffold(
        containerColor = backgroundColor,
        topBar = {
            Surface(
                color = backgroundColor.copy(alpha = 0.95f),
                shadowElevation = 0.dp,
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5E7EB))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(backgroundColor)
                        .statusBarsPadding()
                        .height(56.dp)
                        .padding(horizontal = 16.dp)
                ) {
                    Text(
                        stringResource(R.string.notifications),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color(0xFF111827),
                        modifier = Modifier.align(Alignment.Center)
                    )
                    
                    IconButton(
                        onClick = { showSettingsDialog = true },
                        modifier = Modifier.align(Alignment.CenterEnd)
                    ) {
                        Icon(Icons.Filled.Settings, contentDescription = "Cài đặt", tint = Color(0xFF4B5563))
                    }
                }
            }
        }
    ) { paddingValues ->
        val pullRefreshState = rememberPullToRefreshState()
        
        PullToRefreshBox(
            isRefreshing = uiState.isLoading,
            onRefresh = { viewModel.refresh() },
            state = pullRefreshState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
            // Filter Segmented Control
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFE5E7EB))
                    .padding(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val filters = listOf(
                    "All" to R.string.filter_all, 
                    "Orders" to R.string.filter_orders, 
                    "Promos" to R.string.filter_promos, 
                    "Chats" to R.string.filter_messages
                )
                filters.forEach { (key, labelRes) ->
                    val isSelected = uiState.selectedFilter == key
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) Color.White else Color.Transparent)
                            .clickable { viewModel.setFilter(key) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(labelRes),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (isSelected) primaryColor else Color(0xFF6B7280)
                        )
                    }
                }
            }

            // Loading State
            if (uiState.isLoading && uiState.notifications.isEmpty()) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    item { ShimmerDateHeader() }
                    items(5) { ShimmerNotificationItem() }
                }
            }
            // Error State
            else if (uiState.errorMessage != null) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(uiState.errorMessage ?: "Đã có lỗi xảy ra", color = Color(0xFF6B7280), fontSize = 14.sp)
                    Button(onClick = { viewModel.refresh() }, colors = ButtonDefaults.buttonColors(containerColor = primaryColor)) {
                        Text("Thử lại")
                    }
                }
            }
            // Empty State
            else if (filteredNotifications.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(96.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFF3F4F6)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Filled.NotificationsOff,
                            contentDescription = null,
                            tint = Color(0xFF9CA3AF),
                            modifier = Modifier.size(40.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        stringResource(R.string.no_notifications),
                        color = Color(0xFF6B7280),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            // Notification List
            else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    groupedNotifications.forEach { (dateCategory, items) ->
                        item {
                            val dateLabel = when (dateCategory) {
                                DateCategory.TODAY -> stringResource(R.string.date_today)
                                DateCategory.YESTERDAY -> stringResource(R.string.date_yesterday)
                                DateCategory.LAST_WEEK -> stringResource(R.string.date_last_week)
                                DateCategory.OTHER -> stringResource(R.string.date_other)
                            }
                            Text(
                                text = dateLabel,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF111827)
                            )
                        }
                        items(items, key = { it.id }) { notification ->
                            SwipeToDismissNotificationItem(
                                notification = notification,
                                onDismiss = { viewModel.deleteNotification(notification.id) },
                                onClick = {
                                    viewModel.markAsRead(notification.id)
                                    if (!notification.targetUrl.isNullOrEmpty()) {
                                        onNotificationClick(notification.targetUrl)
                                    }
                                }
                            )
                        }
                    }
                }
            }
            }
        }
    }
    
    if (showSettingsDialog) {
        NotificationSettingsDialog(
            preferences = uiState.preferences,
            onDismiss = { showSettingsDialog = false },
            onUpdate = { request -> viewModel.updatePreference(request) }
        )
    }
}

// ... Keep existing SwipToDismissNotificationItem, NotificationItem, Shimmer helpers ...

// ... Keep existing SwipToDismissNotificationItem, NotificationItem, Shimmer helpers ...
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeToDismissNotificationItem(
    notification: NotificationUiModel,
    onDismiss: () -> Unit,
    onClick: () -> Unit = {}
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            if (it == SwipeToDismissBoxValue.EndToStart) {
                onDismiss()
                true
            } else {
                false
            }
        }
    )

    SwipeToDismissBox(
        state = dismissState,
        backgroundContent = {
            val color by animateColorAsState(
                if (dismissState.targetValue == SwipeToDismissBoxValue.EndToStart) Color(0xFFDC2626) else Color.Transparent,
                label = "DismissColor"
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color)
                    .padding(end = 16.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    Icons.Filled.Delete,
                    contentDescription = "Delete",
                    tint = Color.White
                )
            }
        },
        enableDismissFromStartToEnd = false
    ) {
        NotificationItem(notification, onClick)
    }
}

@Composable
fun NotificationItem(notification: NotificationUiModel, onClick: () -> Unit = {}) {
    val primaryColor = Color(0xFFec3713)

    Surface(
        color = if (notification.isRead) Color.White.copy(alpha = 0.75f) else Color.White,
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        border = androidx.compose.foundation.BorderStroke(0.5.dp, Color(0xFFF3F4F6))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Unread Indicator
            if (!notification.isRead) {
                Box(
                    modifier = Modifier
                        .padding(top = 8.dp, end = 8.dp)
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(primaryColor)
                )
            } else {
                 Spacer(modifier = Modifier.width(14.dp))
            }

            // Icon
            val (icon, bgColor, iconColor) = when (notification.type) {
                NotificationType.ORDER -> Triple(Icons.Filled.LocalShipping, Color(0xFFDBEAFE), Color(0xFF2563EB))
                NotificationType.PROMO -> Triple(Icons.Filled.Percent, Color(0xFFFFE4E6), primaryColor)
                NotificationType.SYSTEM -> Triple(Icons.Filled.NotificationImportant, Color(0xFFF3F4F6), Color(0xFF4B5563))
                NotificationType.FEEDBACK -> Triple(Icons.Filled.Chat, Color(0xFFDCFCE7), Color(0xFF166534))
                NotificationType.CHAT -> Triple(Icons.Filled.Chat, Color(0xFFE0F2FE), Color(0xFF0284C7))
            }

            val actualIcon = if (notification.type == NotificationType.ORDER && notification.title.contains("vận chuyển")) Icons.Filled.Inventory2 
                             else if (notification.type == NotificationType.PROMO && notification.title.contains("Mã giảm giá")) Icons.Filled.ConfirmationNumber
                             else icon

            val actualBgColor = if (notification.type == NotificationType.ORDER && notification.title.contains("vận chuyển")) Color(0xFFFFEDD5)
                                else if (notification.type == NotificationType.PROMO && notification.title.contains("Mã giảm giá")) Color(0xFFF3E8FF)
                                else bgColor

            val actualIconColor = if (notification.type == NotificationType.ORDER && notification.title.contains("vận chuyển")) Color(0xFFEA580C)
                                  else if (notification.type == NotificationType.PROMO && notification.title.contains("Mã giảm giá")) Color(0xFF9333EA)
                                  else iconColor


            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(actualBgColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = actualIcon,
                    contentDescription = null,
                    tint = actualIconColor,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = notification.title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF111827),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f).padding(end = 8.dp)
                    )
                    Text(
                        text = notification.time,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF9CA3AF)
                    )
                }
                Text(
                    text = notification.description,
                    fontSize = 14.sp,
                    color = Color(0xFF4B5563),
                    lineHeight = 20.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

// Shimmer Effect
@Composable
fun shimmerBrush(): Brush {
    val shimmerColors = listOf(
        Color(0xFFE5E7EB),
        Color(0xFFF3F4F6),
        Color(0xFFE5E7EB)
    )

    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer"
    )

    return Brush.linearGradient(
        colors = shimmerColors,
        start = Offset.Zero,
        end = Offset(x = translateAnim.value, y = translateAnim.value)
    )
}

@Composable
fun ShimmerDateHeader() {
    Box(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .width(80.dp)
            .height(20.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(shimmerBrush())
    )
}

@Composable
fun ShimmerNotificationItem() {
    Surface(
        color = Color.White,
        modifier = Modifier.fillMaxWidth(),
        border = androidx.compose.foundation.BorderStroke(0.5.dp, Color(0xFFF3F4F6))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Spacer(modifier = Modifier.width(14.dp))
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(shimmerBrush())
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(modifier = Modifier.weight(1f).height(16.dp).padding(end = 8.dp).clip(RoundedCornerShape(4.dp)).background(shimmerBrush()))
                    Box(modifier = Modifier.width(40.dp).height(14.dp).clip(RoundedCornerShape(4.dp)).background(shimmerBrush()))
                }
                Spacer(modifier = Modifier.height(8.dp))
                Box(modifier = Modifier.fillMaxWidth().height(14.dp).clip(RoundedCornerShape(4.dp)).background(shimmerBrush()))
                Spacer(modifier = Modifier.height(4.dp))
                Box(modifier = Modifier.fillMaxWidth(0.7f).height(14.dp).clip(RoundedCornerShape(4.dp)).background(shimmerBrush()))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NotificationsScreenPreview() {
    NotificationsScreen()
}
