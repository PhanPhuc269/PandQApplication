package com.group1.pandqapplication.ui.ordertracking

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.group1.pandqapplication.shared.ui.theme.BackgroundDark
import com.group1.pandqapplication.shared.ui.theme.BackgroundLight
import com.group1.pandqapplication.shared.ui.theme.CardDark
import com.group1.pandqapplication.shared.ui.theme.CardLight
import com.group1.pandqapplication.shared.ui.theme.Primary
import com.group1.pandqapplication.shared.ui.theme.TextDarkPrimary
import com.group1.pandqapplication.shared.ui.theme.TextDarkSecondary
import com.group1.pandqapplication.shared.ui.theme.TextLightPrimary
import com.group1.pandqapplication.shared.ui.theme.TextLightSecondary

@Composable
fun OrderTrackingScreen(
    onBackClick: () -> Unit = {}
) {
    val isDarkTheme = false // You can hook this up to system theme
    
    val backgroundColor = if (isDarkTheme) BackgroundDark else BackgroundLight
    val cardColor = if (isDarkTheme) CardDark else CardLight
    val textPrimary = if (isDarkTheme) TextDarkPrimary else TextLightPrimary
    val textSecondary = if (isDarkTheme) TextDarkSecondary else TextLightSecondary

    Scaffold(
        containerColor = backgroundColor,
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(cardColor.copy(alpha = 0.8f))
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Primary
                    )
                }
                Text(
                    text = "Theo dõi đơn hàng",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = textPrimary,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .background(cardColor.copy(alpha = 0.8f))
                    .padding(16.dp)
            ) {
                Button(
                    onClick = { /* Contact Support */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Primary),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Liên hệ hỗ trợ",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Order Summary Card
            Card(
                colors = CardDefaults.cardColors(containerColor = cardColor),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Đơn hàng #123456",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = textPrimary
                        )
                        Text(
                            text = "24/08/2024",
                            fontSize = 14.sp,
                            color = textSecondary
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row {
                        Text(
                            text = "Dự kiến giao hàng: ",
                            fontSize = 14.sp,
                            color = textSecondary
                        )
                        Text(
                            text = "28/08/2024",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = textPrimary
                        )
                    }
                }
            }

            // Visual Progress Tracker
            Card(
                colors = CardDefaults.cardColors(containerColor = cardColor),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .padding(start = 0.dp) // Adjust for custom drawing
                ) {
                    TrackingStep(
                        icon = Icons.Default.ReceiptLong,
                        title = "Đã đặt hàng",
                        subtitle = "Đơn hàng của bạn đã được tiếp nhận",
                        isActive = true,
                        isLast = false,
                        textPrimary = textPrimary,
                        textSecondary = textSecondary
                    )
                    TrackingStep(
                        icon = Icons.Default.Verified,
                        title = "Đã xác nhận",
                        subtitle = "Chúng tôi đang chuẩn bị đơn hàng",
                        isActive = true,
                        isLast = false,
                        textPrimary = textPrimary,
                        textSecondary = textSecondary
                    )
                    TrackingStep(
                        icon = Icons.Default.LocalShipping,
                        title = "Đang vận chuyển",
                        subtitle = "Đã giao cho đơn vị vận chuyển",
                        isActive = true,
                        isLast = false,
                        textPrimary = textPrimary,
                        textSecondary = textSecondary
                    )
                    TrackingStep(
                        icon = Icons.Default.Inventory2,
                        title = "Đã giao hàng",
                        subtitle = "Dự kiến giao trong hôm nay",
                        isActive = false,
                        isLast = true,
                        textPrimary = textPrimary,
                        textSecondary = textSecondary
                    )
                }
            }

            // Status Updates Section
            Card(
                colors = CardDefaults.cardColors(containerColor = cardColor),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Cập nhật trạng thái",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = textPrimary,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    
                    StatusUpdateItem(
                        time = "10:30 AM",
                        title = "Tài xế đang giao hàng",
                        subtitle = "Đơn hàng của bạn sẽ sớm được giao",
                        isActive = true,
                        textPrimary = textPrimary,
                        textSecondary = textSecondary,
                        cardColor = cardColor
                    )
                    StatusUpdateItem(
                        time = "08:15 AM",
                        title = "Đơn hàng đã đến kho",
                        subtitle = "Kho phân loại tại TP. Hồ Chí Minh",
                        isActive = false,
                        textPrimary = textPrimary,
                        textSecondary = textSecondary,
                        cardColor = cardColor
                    )
                    StatusUpdateItem(
                        time = "02:00 AM",
                        title = "Đơn hàng rời kho",
                        subtitle = "Kho tổng tại Hà Nội",
                        isActive = false,
                        textPrimary = textPrimary,
                        textSecondary = textSecondary,
                        cardColor = cardColor
                    )
                }
            }

            // Order Details Section
            var isExpanded by remember { mutableStateOf(false) }
            val rotationState by animateFloatAsState(targetValue = if (isExpanded) 180f else 0f,
                label = "rotation"
            )

            Card(
                colors = CardDefaults.cardColors(containerColor = cardColor),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { isExpanded = !isExpanded }
                            .padding(20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Xem chi tiết đơn hàng",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = textPrimary
                        )
                        Icon(
                            imageVector = Icons.Default.ExpandMore,
                            contentDescription = "Expand",
                            tint = textSecondary,
                            modifier = Modifier.rotate(rotationState)
                        )
                    }
                    
                    AnimatedVisibility(visible = isExpanded) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(width = 1.dp, color = Color.Gray.copy(alpha = 0.1f))
                                .padding(horizontal = 20.dp, vertical = 16.dp)
                        ) {
                            HorizontalDivider(color = Color.Gray.copy(alpha = 0.2f))
                            
                            ProductItem(
                                imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuD2QLgAw-PbKaZBCry2SmZ-6Al3osT32sT83U8trPV2KBau97NH24hvyGHemPk4kCyBmTkS0bGQiW-i3oOZais9LHXsjg3f52HQ23ZuKYNOb2y4rJh8fxy3Aw4KHhC9lZNAWkg-nUHYhMdNQZaozH3hk5fdA4oWeQAuIIouc4KQrF1udwchHowVXsESI6jo67RJk1tmsqRlsmjHS331wcEFMZlq9eYzoIL_g9o5zB6aVgEhqnYOhhM1M-vXJclaPY1PG-RzNIw9kNg",
                                name = "Smartphone Model X",
                                quantity = 1,
                                price = "15.000.000₫",
                                textPrimary = textPrimary,
                                textSecondary = textSecondary
                            )
                            
                            ProductItem(
                                imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuDKLRtOQfHBkwMEYYlUfBvnQ_6DhpscO4aDKVefohRTPsLi2ZSHFnzBO2LdYylYEvoso-AtcdKFZbEvdLQd7ywfDosD-nWrZwpWNMM1inUfltWXg0ljMpKCmSA4hTFSpu5rioV3LqNEHp164ViuRJNWEavn4FUcihLUARWEkxuhaVh6ye3gD-isjmMOMw_EpOGV92dp96CheJZV2ywUeXD9GqzMRHtKg86sGRHRDKocokd3BuglNFB6quX95webZBaIvA2ZePadjCQ",
                                name = "Tai nghe Pro",
                                quantity = 1,
                                price = "5.000.000₫",
                                textPrimary = textPrimary,
                                textSecondary = textSecondary
                            )

                            HorizontalDivider(
                                modifier = Modifier.padding(top = 16.dp, bottom = 16.dp),
                                color = Color.Gray.copy(alpha = 0.2f)
                            )
                            
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Tổng cộng",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = textPrimary
                                )
                                Text(
                                    text = "20.000.000₫",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = textPrimary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TrackingStep(
    icon: ImageVector,
    title: String,
    subtitle: String,
    isActive: Boolean,
    isLast: Boolean,
    textPrimary: Color,
    textSecondary: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .drawBehind {
                if (!isLast) {
                    drawLine(
                        color = if (isActive) Color.LightGray else Color.LightGray, // Ideally use theme color
                        start = Offset(18.dp.toPx(), 40.dp.toPx()),
                        end = Offset(18.dp.toPx(), size.height),
                        strokeWidth = 2.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                    )
                }
            }
    ) {
        // Icon
        Box(
            modifier = Modifier
                .padding(start = 2.dp) // Fine tune alignment
                .size(36.dp)
                .background(
                    color = if (isActive) Primary else Color.Transparent,
                    shape = CircleShape
                )
                .border(
                    width = if (isActive) 0.dp else 2.dp,
                    color = if (isActive) Color.Transparent else Color.LightGray,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isActive) Color.White else Color.Gray,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.padding(bottom = 32.dp)) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (isActive) textPrimary else textSecondary
            )
            Text(
                text = subtitle,
                fontSize = 14.sp,
                color = textSecondary,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
fun StatusUpdateItem(
    time: String,
    title: String,
    subtitle: String,
    isActive: Boolean,
    textPrimary: Color,
    textSecondary: Color,
    cardColor: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = time,
            fontSize = 12.sp,
            color = textSecondary,
            modifier = Modifier
                .width(64.dp)
                .padding(top = 2.dp),
            textAlign = TextAlign.End
        )
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column {
            Row(verticalAlignment = Alignment.Top) {
                // Dot
                Box(
                    modifier = Modifier
                        .padding(top = 6.dp)
                        .size(12.dp)
                        .background(
                            color = if (isActive) Primary else Color.LightGray,
                            shape = CircleShape
                        )
                        .border(4.dp, cardColor, CircleShape) // Ring effect
                )
                
                Spacer(modifier = Modifier.width(12.dp))
                
                Column(modifier = Modifier.padding(bottom = 16.dp)) {
                    Text(
                        text = title,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color = textPrimary
                    )
                    Text(
                        text = subtitle,
                        fontSize = 14.sp,
                        color = textSecondary
                    )
                }
            }
        }
    }
}

@Composable
fun ProductItem(
    imageUrl: String,
    name: String,
    quantity: Int,
    price: String,
    textPrimary: Color,
    textSecondary: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = name,
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.Gray.copy(alpha = 0.1f)),
            contentScale = ContentScale.Crop
        )
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = name,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                color = textPrimary
            )
            Text(
                text = "Số lượng: $quantity",
                fontSize = 14.sp,
                color = textSecondary
            )
        }
        
        Text(
            text = price,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            color = textPrimary
        )
    }
}

@Preview
@Composable
fun PreviewOrderTrackingScreen() {
    OrderTrackingScreen()
}
