package com.group1.pandqapplication.ui.shipping

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Pending
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.group1.pandqapplication.shared.ui.theme.CardLight
import com.group1.pandqapplication.shared.ui.theme.ShippingBackgroundDark
import com.group1.pandqapplication.shared.ui.theme.ShippingBackgroundLight
import com.group1.pandqapplication.shared.ui.theme.ShippingCardDark
import com.group1.pandqapplication.shared.ui.theme.ShippingPrimary
import com.group1.pandqapplication.shared.ui.theme.ShippingStatusCancelled
import com.group1.pandqapplication.shared.ui.theme.ShippingStatusInProgress
import com.group1.pandqapplication.shared.ui.theme.ShippingStatusPending
import com.group1.pandqapplication.shared.ui.theme.ShippingStatusSuccess
import com.group1.pandqapplication.shared.ui.theme.ShippingTextDarkSecondary
import com.group1.pandqapplication.shared.ui.theme.ShippingTextLightPrimary
import com.group1.pandqapplication.shared.ui.theme.ShippingTextLightSecondary

@Composable
fun ShippingManagementScreen(
    onBackClick: () -> Unit = {}
) {
    val isDarkTheme = false
    
    val backgroundColor = if (isDarkTheme) ShippingBackgroundDark else ShippingBackgroundLight
    val cardColor = if (isDarkTheme) ShippingCardDark else CardLight
    val textPrimary = if (isDarkTheme) Color.White else ShippingTextLightPrimary
    val textSecondary = if (isDarkTheme) ShippingTextDarkSecondary else ShippingTextLightSecondary
    val borderColor = if (isDarkTheme) Color(0xFF3A5169) else Color(0xFFE6E6E6)

    Scaffold(
        containerColor = backgroundColor,
        topBar = {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(backgroundColor)
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier.align(Alignment.CenterStart)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = "Back",
                            tint = textPrimary
                        )
                    }
                    Text(
                        text = "Quản lý vận chuyển",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = textPrimary,
                        modifier = Modifier.align(Alignment.Center)
                    )
                    IconButton(
                        onClick = { /* Filter/Tune */ },
                        modifier = Modifier.align(Alignment.CenterEnd)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Tune,
                            contentDescription = "Tune",
                            tint = textPrimary
                        )
                    }
                }
                HorizontalDivider(color = borderColor)
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Search Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
               Row(
                   modifier = Modifier
                       .fillMaxWidth()
                       .height(48.dp)
                       .background(cardColor, RoundedCornerShape(24.dp)), // pill shape
                   verticalAlignment = Alignment.CenterVertically
               ) {
                   Box(
                       modifier = Modifier
                           .width(48.dp)
                           .fillMaxSize(),
                       contentAlignment = Alignment.Center
                   ) {
                       Icon(
                           imageVector = Icons.Default.Search,
                           contentDescription = "Search",
                           tint = textSecondary
                       )
                   }
                   TextField(
                       value = "",
                       onValueChange = {},
                       placeholder = { Text("Tìm theo mã đơn, tên khách hàng...", color = textSecondary) },
                       modifier = Modifier.weight(1f),
                       colors = TextFieldDefaults.colors(
                           focusedContainerColor = Color.Transparent,
                           unfocusedContainerColor = Color.Transparent,
                           focusedIndicatorColor = Color.Transparent,
                           unfocusedIndicatorColor = Color.Transparent,
                           focusedTextColor = textPrimary,
                           unfocusedTextColor = textPrimary
                       ),
                       singleLine = true
                   )
               }
            }

            // Tabs
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(backgroundColor)
                    .padding(horizontal = 16.dp)
            ) {
                ShippingTab("Chờ xử lý (12)", true, textPrimary, textSecondary, ShippingPrimary)
                ShippingTab("Đang giao (5)", false, textPrimary, textSecondary, ShippingPrimary)
                ShippingTab("Hoàn thành (30)", false, textPrimary, textSecondary, ShippingPrimary)
                // Leaving out "Cancelled" to fit design or scrollable if needed. 
                // HTML shows 4 tabs, flex-1.
                ShippingTab("Đã huỷ (2)", false, textPrimary, textSecondary, ShippingPrimary)
            }
            HorizontalDivider(color = borderColor)

            // Order List
            val orders = listOf(
                ShippingOrder(
                    id = "ELC-12095",
                    date = "08/10/2023",
                    status = ShippingStatus.PENDING,
                    customerName = "Nguyễn Văn An",
                    address = "123 Đường ABC, Quận 1, TPHCM",
                    shippingProvider = "Giao Hàng Nhanh",
                    buttonText = "Cập nhật trạng thái"
                ),
                ShippingOrder(
                    id = "ELC-12094",
                    date = "08/10/2023",
                    status = ShippingStatus.PENDING,
                    customerName = "Trần Thị Bích",
                    address = "456 Đường XYZ, Quận 3, TPHCM",
                    shippingProvider = "Chưa gán đơn vị vận chuyển",
                    buttonText = "Gán đơn vị",
                    isProviderPending = true
                ),
                ShippingOrder(
                    id = "ELC-12090",
                    date = "07/10/2023",
                    status = ShippingStatus.IN_PROGRESS,
                    customerName = "Lê Minh Tuấn",
                    address = "789 Đường LMN, Quận Gò Vấp, TPHCM",
                    shippingProvider = "Viettel Post",
                    buttonText = "Xem theo dõi",
                    isSecondaryButton = true
                )
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(orders) { order ->
                    ShippingOrderItem(
                        order = order,
                        cardColor = cardColor,
                        textPrimary = textPrimary,
                        textSecondary = textSecondary,
                        borderColor = borderColor
                    )
                }
            }
        }
    }
}

@Composable
fun androidx.compose.foundation.layout.RowScope.ShippingTab(
    text: String,
    isSelected: Boolean,
    textPrimary: Color,
    textSecondary: Color,
    primaryColor: Color
) {
    Column(
        modifier = Modifier
            .weight(1f)
            .clickable { }
            .padding(vertical = 13.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = text,
            fontSize = 12.sp, // Adjusted for space
            fontWeight = FontWeight.Bold,
            color = if (isSelected) primaryColor else textSecondary,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
        Spacer(modifier = Modifier.height(13.dp))
        Box(
            modifier = Modifier
                .height(3.dp)
                .fillMaxWidth()
                .background(if (isSelected) primaryColor else Color.Transparent)
        )
    }
}

@Composable
fun ShippingOrderItem(
    order: ShippingOrder,
    cardColor: Color,
    textPrimary: Color,
    textSecondary: Color,
    borderColor: Color
) {
    val statusColor = when(order.status) {
        ShippingStatus.PENDING -> ShippingStatusPending
        ShippingStatus.IN_PROGRESS -> ShippingStatusInProgress
        ShippingStatus.SUCCESS -> ShippingStatusSuccess
        ShippingStatus.CANCELLED -> ShippingStatusCancelled
    }
    val statusText = when(order.status) {
        ShippingStatus.PENDING -> "Chờ xử lý"
        ShippingStatus.IN_PROGRESS -> "Đang giao"
        ShippingStatus.SUCCESS -> "Hoàn thành"
        ShippingStatus.CANCELLED -> "Đã huỷ"
    }
    val statusIcon = when(order.status) {
        ShippingStatus.PENDING -> Icons.Default.Pending
        ShippingStatus.IN_PROGRESS -> Icons.Default.LocalShipping
        else -> Icons.Default.Pending
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(cardColor)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column {
                Text(
                    text = "Đơn hàng #${order.id}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = textPrimary
                )
                Text(
                    text = order.date,
                    fontSize = 12.sp,
                    color = textSecondary
                )
            }
            
            Row(
                modifier = Modifier
                    .background(statusColor.copy(alpha = 0.1f), RoundedCornerShape(100.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = statusIcon,
                    contentDescription = null,
                    tint = statusColor,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = statusText,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = statusColor
                )
            }
        }
        
        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = borderColor)
        
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = order.customerName,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = textPrimary
            )
            Text(
                text = order.address,
                fontSize = 14.sp,
                color = textSecondary
            )
            Text(
                text = order.shippingProvider,
                fontSize = 14.sp,
                color = if (order.isProviderPending) ShippingStatusPending else textSecondary,
                fontWeight = if (order.isProviderPending) FontWeight.Medium else FontWeight.Normal
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(
            onClick = { },
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (order.isSecondaryButton) Color.Transparent else ShippingPrimary,
                contentColor = if (order.isSecondaryButton) ShippingPrimary else Color.White
            ),
            border = if (order.isSecondaryButton) androidx.compose.foundation.BorderStroke(1.dp, ShippingPrimary) else null,
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = order.buttonText,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

data class ShippingOrder(
    val id: String,
    val date: String,
    val status: ShippingStatus,
    val customerName: String,
    val address: String,
    val shippingProvider: String,
    val buttonText: String,
    val isSecondaryButton: Boolean = false,
    val isProviderPending: Boolean = false
)

enum class ShippingStatus {
    PENDING, IN_PROGRESS, SUCCESS, CANCELLED
}

@Preview
@Composable
fun PreviewShippingManagementScreen() {
    ShippingManagementScreen()
}
