package com.group1.pandqapplication.ui.ordertracking

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.group1.pandqapplication.shared.data.remote.dto.OrderHistoryDto
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
fun OrderDetailScreen(
    order: OrderHistoryDto? = null,
    onBackClick: () -> Unit = {}
) {
    val isDarkTheme = false
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
                    text = "Chi tiết đơn hàng",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = textPrimary,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Order Header Card
            Card(
                colors = CardDefaults.cardColors(containerColor = cardColor),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Đơn hàng ${order?.id?.take(8) ?: "#DH00123"}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = textPrimary
                        )
                        Box(
                            modifier = Modifier
                                .background(
                                    color = Color(0xFF92400E),
                                    shape = RoundedCornerShape(50.dp)
                                )
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = order?.status?.replace("_", " ") ?: "Chờ xử lý",
                                color = Color(0xFFFEF3C7),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "15/08/2023, 10:30 AM",
                        fontSize = 14.sp,
                        color = textSecondary
                    )
                }
            }

            // Products Section
            Card(
                colors = CardDefaults.cardColors(containerColor = cardColor),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Sản phẩm",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = textPrimary
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    order?.items?.forEach { item ->
                        OrderDetailProductItem(
                            imageUrl = "",
                            name = item.productName,
                            quantity = item.quantity,
                            unitPrice = "${item.price}₫",
                            totalPrice = "${(item.price * item.quantity)}₫",
                            textPrimary = textPrimary,
                            textSecondary = textSecondary
                        )
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 12.dp),
                            color = Color.Gray.copy(alpha = 0.2f)
                        )
                    }
                }
            }

            // Billing Summary
            Card(
                colors = CardDefaults.cardColors(containerColor = cardColor),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Tóm tắt đơn hàng",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = textPrimary
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    BillingRow("Tạm tính", "15.850.000₫", textPrimary, textSecondary)
                    BillingRow("Phí vận chuyển", "50.000₫", textPrimary, textSecondary)
                    BillingRow("Giảm giá", "-1.000.000₫", Color.Red, textSecondary)
                    
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 12.dp),
                        color = Color.Gray.copy(alpha = 0.2f)
                    )
                    
                    BillingRow(
                        "Tổng cộng",
                        "${order?.finalAmount ?: "14.900.000"}₫",
                        textPrimary,
                        textSecondary,
                        isTotal = true
                    )
                }
            }

            // Customer Information
            Card(
                colors = CardDefaults.cardColors(containerColor = cardColor),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Thông tin khách hàng",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = textPrimary
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    InfoRow("Tên:", "Nguyễn Văn A", textPrimary, textSecondary)
                    InfoRow("Số điện thoại:", "0987654321", textPrimary, textSecondary)
                    InfoRow("Địa chỉ giao hàng:", "123 Đường Lê Lợi, Quận 1, TP.HCM", textPrimary, textSecondary)
                    InfoRow("Ghi chú:", "Giao hàng nhanh, liên lạc trước khi tới", textPrimary, textSecondary)
                }
            }

            // Payment & Shipping
            Card(
                colors = CardDefaults.cardColors(containerColor = cardColor),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Thanh toán & Vận chuyển",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = textPrimary
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    InfoRow("Phương thức thanh toán:", "Thanh toán khi nhận hàng (COD)", textPrimary, textSecondary)
                    InfoRow("Shipper:", "Chưa được giao", textPrimary, Color(0xFFFB7185))
                    InfoRow("Trạng thái thanh toán:", "Chưa thanh toán", textPrimary, Color(0xFFFB7185))
                }
            }

            // Action Buttons
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = { /* Assign to Shipper */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Primary),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Giao cho Shipper", fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = { /* Update Status */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Primary.copy(alpha = 0.8f)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Cập nhật trạng thái", fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun OrderDetailProductItem(
    imageUrl: String,
    name: String,
    quantity: Int,
    unitPrice: String,
    totalPrice: String,
    textPrimary: Color,
    textSecondary: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Product Image
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(Color.Gray.copy(alpha = 0.2f))
                .height(80.dp)
                .fillMaxWidth(0.2f)
        ) {
            if (imageUrl.isNotEmpty()) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                )
            }
        }

        // Product Details
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = name,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = textPrimary,
                maxLines = 2
            )
            Text(
                text = "$quantity x $unitPrice",
                fontSize = 12.sp,
                color = textSecondary
            )
            Text(
                text = totalPrice,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = textPrimary
            )
        }
    }
}

@Composable
private fun BillingRow(
    label: String,
    value: String,
    labelColor: Color,
    valueColor: Color,
    isTotal: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = if (isTotal) 16.sp else 14.sp,
            fontWeight = if (isTotal) FontWeight.Bold else FontWeight.Normal,
            color = labelColor
        )
        Text(
            text = value,
            fontSize = if (isTotal) 16.sp else 14.sp,
            fontWeight = if (isTotal) FontWeight.Bold else FontWeight.Normal,
            color = valueColor
        )
    }
}

@Composable
private fun InfoRow(
    label: String,
    value: String,
    labelColor: Color,
    valueColor: Color
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color.Gray,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            fontSize = 14.sp,
            color = valueColor
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}
