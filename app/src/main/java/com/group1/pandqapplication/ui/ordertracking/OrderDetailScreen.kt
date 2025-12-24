package com.group1.pandqapplication.ui.ordertracking

import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
    orderId: String? = null,
    order: OrderHistoryDto? = null,
    viewModel: OrderDetailViewModel? = null,
    onBackClick: () -> Unit = {}
) {
    val isDarkTheme = false
    val backgroundColor = if (isDarkTheme) BackgroundDark else BackgroundLight
    val cardColor = if (isDarkTheme) CardDark else CardLight
    val textPrimary = if (isDarkTheme) TextDarkPrimary else TextLightPrimary
    val textSecondary = if (isDarkTheme) TextDarkSecondary else TextLightSecondary

    val snackbarHostState = remember { SnackbarHostState() }
    
    // Collect UI state from ViewModel if available
    val uiState = viewModel?.uiState?.collectAsState()?.value
        ?: OrderDetailUiState(order = order)

    // Load order detail if orderId is provided
    LaunchedEffect(orderId) {
        if (orderId != null && viewModel != null) {
            viewModel.loadOrderDetail(orderId)
        }
    }

    // Show snackbar for success or error messages
    LaunchedEffect(uiState.error) {
        if (uiState.error != null) {
            snackbarHostState.showSnackbar(
                message = uiState.error,
                duration = SnackbarDuration.Long
            )
            viewModel?.clearMessage()
        }
    }

    LaunchedEffect(uiState.actionSuccess) {
        if (uiState.actionSuccess != null) {
            snackbarHostState.showSnackbar(
                message = uiState.actionSuccess,
                duration = SnackbarDuration.Short
            )
            viewModel?.clearMessage()
        }
    }

    Scaffold(
        containerColor = backgroundColor,
        snackbarHost = { SnackbarHost(snackbarHostState) },
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
        // Loading State
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(backgroundColor),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CircularProgressIndicator(
                        color = Primary,
                        modifier = Modifier.size(50.dp)
                    )
                    Text(
                        text = "Đang tải thông tin đơn hàng...",
                        color = textSecondary,
                        fontSize = 14.sp
                    )
                }
            }
        }
        // Error State
        else if (uiState.error != null && uiState.order == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(backgroundColor),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "❌ Không thể tải dữ liệu",
                        color = Color.Red,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = uiState.error,
                        color = textSecondary,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                    Button(
                        onClick = { orderId?.let { viewModel?.loadOrderDetail(it) } },
                        colors = ButtonDefaults.buttonColors(containerColor = Primary),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                    ) {
                        Text("Thử lại", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
        // Content State
        else if (uiState.order != null) {
            val currentOrder = uiState.order
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
                                text = "Đơn hàng ${currentOrder.id.take(8)}",
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
                                    text = currentOrder.status.replace("_", " "),
                                    color = Color(0xFFFEF3C7),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = DateFormatter.formatDateTime(currentOrder.createdAt),
                            fontSize = 14.sp,
                            color = textSecondary
                        )
                    }
                }

                // Order Status Timeline
                OrderStatusTimeline(
                    currentStatus = currentOrder.status,
                    cardColor = cardColor,
                    textPrimary = textPrimary,
                    textSecondary = textSecondary
                )

                // Products Section
                if (currentOrder.items.isNotEmpty()) {
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

                            currentOrder.items.forEach { item ->
                                OrderDetailProductItem(
                                    imageUrl = item.imageUrl ?: "",
                                    name = item.productName,
                                    quantity = item.quantity,
                                    unitPrice = "${item.price}₫",
                                    totalPrice = "${item.price.multiply(java.math.BigDecimal.valueOf(item.quantity.toLong()))}₫",
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

                        BillingRow("Tạm tính", "${currentOrder.totalAmount}₫", textPrimary, textSecondary)
                        BillingRow("Phí vận chuyển", "${currentOrder.shippingFee}₫", textPrimary, textSecondary)
                        BillingRow("Giảm giá", "-${currentOrder.discountAmount}₫", Color.Red, textSecondary)
                        
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 12.dp),
                            color = Color.Gray.copy(alpha = 0.2f)
                        )
                        
                        BillingRow(
                            "Tổng cộng",
                            "${currentOrder.finalAmount}₫",
                            textPrimary,
                            textSecondary,
                            isTotal = true
                        )
                    }
                }

                // Shipping & Payment Information
                Card(
                    colors = CardDefaults.cardColors(containerColor = cardColor),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = "Địa chỉ giao hàng",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = textPrimary,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = currentOrder.shippingAddress ?: "Chưa có thông tin",
                            fontSize = 14.sp,
                            color = textSecondary,
                            modifier = Modifier.padding(bottom = 20.dp)
                        )

                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 12.dp),
                            color = Color.Gray.copy(alpha = 0.2f)
                        )

                        Text(
                            text = "Phương thức thanh toán",
                            fontSize = 14.sp,
                            color = textSecondary,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = currentOrder.paymentMethod.replace("_", " "),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = textPrimary,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }

                // Customer Support Section
                Card(
                    colors = CardDefaults.cardColors(containerColor = cardColor),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = "Hỗ trợ khách hàng",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = textPrimary,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(Primary.copy(alpha = 0.1f))
                                    .padding(12.dp)
                            ) {
                                Text("☎", fontSize = 20.sp)
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Gọi cho chúng tôi",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = textPrimary
                                )
                                Text(
                                    text = "1800-2024",
                                    fontSize = 12.sp,
                                    color = textSecondary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(Primary.copy(alpha = 0.1f))
                                    .padding(12.dp)
                            ) {
                                Text("💬", fontSize = 20.sp)
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Chat với chúng tôi",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = textPrimary
                                )
                                Text(
                                    text = "Hỗ trợ 24/7",
                                    fontSize = 12.sp,
                                    color = textSecondary
                                )
                            }
                        }
                    }
                }

                // Action Buttons (based on order status)
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    when (currentOrder.status.uppercase()) {
                        "DELIVERED" -> {
                            Button(
                                onClick = { viewModel?.submitReview(5, "") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Primary),
                                shape = RoundedCornerShape(8.dp),
                                enabled = !uiState.isLoading
                            ) {
                                Text("Đánh giá sản phẩm", fontWeight = FontWeight.Bold)
                            }

                            Button(
                                onClick = { viewModel?.reorder() },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Primary.copy(alpha = 0.8f)
                                ),
                                shape = RoundedCornerShape(8.dp),
                                enabled = !uiState.isLoading
                            ) {
                                Text("Mua lại", fontWeight = FontWeight.Bold)
                            }

                            Button(
                                onClick = { viewModel?.requestReturn() },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFEF4444).copy(alpha = 0.8f)
                                ),
                                shape = RoundedCornerShape(8.dp),
                                enabled = !uiState.isLoading
                            ) {
                                Text("Yêu cầu trả hàng", fontWeight = FontWeight.Bold)
                            }
                        }
                        "PENDING", "CONFIRMED" -> {
                            Button(
                                onClick = { viewModel?.cancelOrder() },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFEF4444)
                                ),
                                shape = RoundedCornerShape(8.dp),
                                enabled = !uiState.isLoading
                            ) {
                                Text("Hủy đơn hàng", fontWeight = FontWeight.Bold)
                            }

                            Button(
                                onClick = { /* Contact support */ },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Primary.copy(alpha = 0.8f)
                                ),
                                shape = RoundedCornerShape(8.dp),
                                enabled = !uiState.isLoading
                            ) {
                                Text("Liên hệ hỗ trợ", fontWeight = FontWeight.Bold)
                            }
                        }
                        "SHIPPING" -> {
                            Button(
                                onClick = { /* Track shipment */ },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Primary),
                                shape = RoundedCornerShape(8.dp),
                                enabled = !uiState.isLoading
                            ) {
                                Text("Theo dõi vận chuyển", fontWeight = FontWeight.Bold)
                            }

                            Button(
                                onClick = { /* Contact support */ },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Primary.copy(alpha = 0.8f)
                                ),
                                shape = RoundedCornerShape(8.dp),
                                enabled = !uiState.isLoading
                            ) {
                                Text("Liên hệ hỗ trợ", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
private fun OrderStatusTimeline(
    currentStatus: String,
    cardColor: Color,
    textPrimary: Color,
    textSecondary: Color
) {
    val statuses = listOf("PENDING", "CONFIRMED", "SHIPPING", "DELIVERED")
    val statusLabels = listOf("Đã đặt", "Đã xác nhận", "Đang giao", "Đã giao")
    val currentIndex = statuses.indexOf(currentStatus.uppercase())

    Card(
        colors = CardDefaults.cardColors(containerColor = cardColor),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Tiến độ đơn hàng",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = textPrimary,
                modifier = Modifier.padding(bottom = 20.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                statuses.forEachIndexed { index, status ->
                    val isCompleted = index <= currentIndex
                    val isCurrent = index == currentIndex

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        // Status Circle
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .background(
                                    color = if (isCompleted) Primary else Color.Gray.copy(alpha = 0.3f),
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isCompleted) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            } else {
                                Text(
                                    text = "${index + 1}",
                                    color = Color.Gray,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        // Status Label
                        Text(
                            text = statusLabels[index],
                            fontSize = 11.sp,
                            color = if (isCurrent) Primary else textSecondary,
                            fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }

                    // Connector Line
                    if (index < statuses.size - 1) {
                        Box(
                            modifier = Modifier
                                .weight(0.3f)
                                .height(2.dp)
                                .background(
                                    color = if (index < currentIndex) Primary else Color.Gray.copy(alpha = 0.3f)
                                )
                        )
                    }
                }
            }
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
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("📦", fontSize = 28.sp)
                }
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
