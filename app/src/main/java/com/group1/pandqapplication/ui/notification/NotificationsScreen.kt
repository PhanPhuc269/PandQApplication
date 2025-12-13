package com.group1.pandqapplication.ui.notification

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.NotificationImportant
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.group1.pandqapplication.shared.ui.theme.BackgroundLight

// Mock Data Models
data class Notification(
    val id: String,
    val type: NotificationType,
    val title: String,
    val time: String,
    val description: String,
    val isRead: Boolean,
    val date: String // "Hôm nay", "Hôm qua", "Tuần trước"
)

enum class NotificationType {
    ORDER, PROMO, SYSTEM, FEEDBACK
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen() {
    val primaryColor = Color(0xFFec3713)
    val backgroundColor = BackgroundLight

    // Filter State
    var selectedFilter by remember { mutableStateOf("All") } // All, Orders, Promos

    // Mock Data State
    var notifications by remember {
        mutableStateOf(
            listOf(
                Notification("1", NotificationType.ORDER, "Giao hàng thành công", "10:30 AM", "Đơn hàng #29381 của bạn đã được giao thành công. Vui lòng xác nhận và đánh giá sản phẩm để nhận 200 xu.", false, "Hôm nay"),
                Notification("2", NotificationType.PROMO, "Siêu Sale 11.11 \uD83D\uDD25", "10:00 AM", "Giảm giá lên đến 50% cho các sản phẩm điện tử. Số lượng có hạn, mua ngay kẻo lỡ!", false, "Hôm nay"),
                Notification("3", NotificationType.ORDER, "Đơn hàng đang vận chuyển", "16:45", "Đơn hàng #29381 đã được bàn giao cho đơn vị vận chuyển GHN.", true, "Hôm qua"),
                Notification("4", NotificationType.SYSTEM, "Bảo trì hệ thống", "09:00", "Hệ thống sẽ bảo trì định kỳ vào lúc 02:00 AM ngày 15/11. Dịch vụ sẽ gián đoạn trong 30 phút.", true, "Hôm qua"),
                Notification("5", NotificationType.PROMO, "Mã giảm giá dành riêng cho bạn", "02 Th11", "Tặng bạn mã GIAM20K cho đơn hàng từ 100K. Hạn sử dụng đến hết tháng này.", true, "Tuần trước"),
                Notification("6", NotificationType.FEEDBACK, "Shop đã trả lời đánh giá", "30 Th10", "\"Cảm ơn bạn đã tin tưởng ủng hộ shop ạ. Hy vọng bạn sẽ hài lòng với sản phẩm!\"", true, "Tuần trước")
            )
        )
    }

    // Filter Logic
    val filteredNotifications = when (selectedFilter) {
        "Orders" -> notifications.filter { it.type == NotificationType.ORDER }
        "Promos" -> notifications.filter { it.type == NotificationType.PROMO }
        else -> notifications
    }

    // Grouping Logic
    val groupedNotifications = filteredNotifications.groupBy { it.date }

    Scaffold(
        containerColor = backgroundColor,
        topBar = {
            Surface(
                color = backgroundColor.copy(alpha = 0.95f),
                shadowElevation = 0.dp, // No shadow in design, just border usually but flat here
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5E7EB)) // Close to gray-200
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {}) {
                        Icon(Icons.Filled.ArrowBackIosNew, contentDescription = "Back", tint = Color(0xFF111827))
                    }
                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                        Text(
                            "Thông báo",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Color(0xFF111827)
                        )
                    }
                    TextButton(onClick = {
                        notifications = notifications.map { it.copy(isRead = true) }
                    }) {
                        Text("Đọc tất cả", color = primaryColor, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Filter Segmented Control
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp) // px-4 py-4
                    .height(48.dp) // h-12 in design roughly? No h-10 in design
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFE5E7EB)) // gray-200
                    .padding(4.dp), // p-1
                verticalAlignment = Alignment.CenterVertically
            ) {
                val filters = listOf("All" to "Tất cả", "Orders" to "Đơn hàng", "Promos" to "Khuyến mãi")
                filters.forEach { (key, label) ->
                    val isSelected = selectedFilter == key
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) Color.White else Color.Transparent)
                            .clickable { selectedFilter = key },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = label,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (isSelected) primaryColor else Color(0xFF6B7280) // text-primary vs text-gray-500
                        )
                    }
                }
            }

            // Notification List
            if (filteredNotifications.isEmpty()) {
                // Empty State
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
                        "Bạn đã xem hết thông báo",
                        color = Color(0xFF6B7280),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    groupedNotifications.forEach { (date, items) ->
                        item {
                            Text(
                                text = date,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF111827)
                            )
                        }
                        items(items, key = { it.id }) { notification ->
                            SwipeToDismissNotificationItem(
                                notification = notification,
                                onDismiss = {
                                    notifications = notifications.filter { it.id != notification.id }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeToDismissNotificationItem(
    notification: Notification,
    onDismiss: () -> Unit
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
        NotificationItem(notification)
    }
}

@Composable
fun NotificationItem(notification: Notification) {
    val primaryColor = Color(0xFFec3713)

    Surface(
        color = if (notification.isRead) Color.White.copy(alpha = 0.75f) else Color.White,
        modifier = Modifier.fillMaxWidth().clickable { /* TODO: Open detail */ },
        border = androidx.compose.foundation.BorderStroke(0.5.dp, Color(0xFFF3F4F6)) // Light divider
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
                 Spacer(modifier = Modifier.width(14.dp)) // Maintain alignment if no dot
            }

            // Icon
            val (icon, bgColor, iconColor) = when (notification.type) {
                NotificationType.ORDER -> Triple(Icons.Filled.LocalShipping, Color(0xFFDBEAFE), Color(0xFF2563EB)) // Blue-100, Blue-600
                NotificationType.PROMO -> Triple(Icons.Filled.Percent, Color(0xFFFFE4E6), primaryColor) // slightly diff red for bg
                NotificationType.SYSTEM -> Triple(Icons.Filled.NotificationImportant, Color(0xFFF3F4F6), Color(0xFF4B5563)) // Gray-100, Gray-600
                NotificationType.FEEDBACK -> Triple(Icons.Filled.Chat, Color(0xFFDCFCE7), Color(0xFF166534)) // Green-100, Green-800
            }
            // Logic for specific icons like package for "processing" not strictly mapped here for brevity, simplified logic

            // Special case for "Order Update" (Shipping/Package icon) from mock data logic correction
            val actualIcon = if (notification.type == NotificationType.ORDER && notification.title.contains("vận chuyển")) Icons.Filled.Inventory2 
                             else if (notification.type == NotificationType.PROMO && notification.title.contains("Mã giảm giá")) Icons.Filled.ConfirmationNumber
                             else icon

            val actualBgColor = if (notification.type == NotificationType.ORDER && notification.title.contains("vận chuyển")) Color(0xFFFFEDD5) // Orange-100
                                else if (notification.type == NotificationType.PROMO && notification.title.contains("Mã giảm giá")) Color(0xFFF3E8FF) // Purple-100
                                else bgColor

            val actualIconColor = if (notification.type == NotificationType.ORDER && notification.title.contains("vận chuyển")) Color(0xFFEA580C) // Orange-600
                                  else if (notification.type == NotificationType.PROMO && notification.title.contains("Mã giảm giá")) Color(0xFF9333EA) // Purple-600
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

@Preview(showBackground = true)
@Composable
fun NotificationsScreenPreview() {
    NotificationsScreen()
}
