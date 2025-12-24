package com.group1.pandqapplication.ui.ordertracking

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
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
import java.time.format.DateTimeFormatter

@Composable
fun OrderTrackingScreen(
    orderId: String? = null,
    order: OrderHistoryDto? = null,
    viewModel: OrderTrackingViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    onDetailClick: () -> Unit = {},
    onSupportClick: () -> Unit = {}
) {
    val isDarkTheme = false
    val backgroundColor = if (isDarkTheme) BackgroundDark else BackgroundLight
    val cardColor = if (isDarkTheme) CardDark else CardLight
    val textPrimary = if (isDarkTheme) TextDarkPrimary else TextLightPrimary
    val textSecondary = if (isDarkTheme) TextDarkSecondary else TextLightSecondary

    val uiState by viewModel.uiState.collectAsState()

    // Load order tracking - either from parameter or by API
    LaunchedEffect(orderId, order) {
        when {
            order != null -> viewModel.setOrder(order)
            orderId != null -> viewModel.loadOrderTracking(orderId)
        }
    }

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
                        text = "Đang tải thông tin theo dõi...",
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
                        text = uiState.error ?: "Có lỗi xảy ra",
                        color = textSecondary,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                    Button(
                        onClick = { orderId?.let { viewModel.loadOrderTracking(it) } },
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
            val currentOrder: OrderHistoryDto = uiState.order!!
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Order Header
                Card(
                    colors = CardDefaults.cardColors(containerColor = cardColor),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                        val orderDate = currentOrder.createdAt.format(dateFormatter)
                        val estimatedDate = currentOrder.createdAt.plusDays(4).format(dateFormatter)
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Đơn hàng #${currentOrder.id.take(6).uppercase()}",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = textPrimary
                            )
                            Text(
                                text = orderDate,
                                fontSize = 14.sp,
                                color = textSecondary
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Dự kiến giao hàng: $estimatedDate",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = textPrimary
                        )
                    }
                }

                // Tracking Steps
                Card(
                    colors = CardDefaults.cardColors(containerColor = cardColor),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .padding(20.dp)
                            .padding(start = 0.dp)
                    ) {
                        uiState.trackingSteps.forEachIndexed { index, step ->
                            TrackingStepItem(
                                step = step,
                                isLast = index == uiState.trackingSteps.size - 1,
                                textPrimary = textPrimary,
                                textSecondary = textSecondary
                            )
                        }
                    }
                }

                // Status Updates
                if (uiState.trackingUpdates.isNotEmpty()) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = cardColor),
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text(
                                text = "Cập nhật trạng thái",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = textPrimary,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )

                            uiState.trackingUpdates.forEachIndexed { index, update ->
                                StatusUpdateItem(
                                    update = update,
                                    textPrimary = textPrimary,
                                    textSecondary = textSecondary,
                                    cardColor = cardColor
                                )
                                if (index < uiState.trackingUpdates.size - 1) {
                                    Spacer(modifier = Modifier.height(16.dp))
                                }
                            }
                        }
                    }
                }

                // Products Section
                if (currentOrder.items.isNotEmpty()) {
                    var isExpanded by remember { mutableStateOf(false) }
                    val rotationState by animateFloatAsState(
                        targetValue = if (isExpanded) 180f else 0f,
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
                                    .padding(20.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Sản phẩm (${currentOrder.items.size})",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = textPrimary
                                )
                                Icon(
                                    imageVector = Icons.Default.ExpandMore,
                                    contentDescription = null,
                                    modifier = Modifier.rotate(rotationState),
                                    tint = textSecondary
                                )
                            }

                            AnimatedVisibility(visible = isExpanded) {
                                Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)) {
                                    currentOrder.items.forEachIndexed { index, item ->
                                        ProductItem(
                                            imageUrl = item.imageUrl ?: "",
                                            name = item.productName,
                                            quantity = item.quantity,
                                            price = "${item.price}₫",
                                            textPrimary = textPrimary,
                                            textSecondary = textSecondary
                                        )
                                        if (index < currentOrder.items.size - 1) {
                                            HorizontalDivider(
                                                modifier = Modifier.padding(vertical = 12.dp),
                                                color = Color.Gray.copy(alpha = 0.2f)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Support Button
                Button(
                    onClick = onSupportClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Primary),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Liên hệ hỗ trợ",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
private fun TrackingStepItem(
    step: TrackingStep,
    isLast: Boolean,
    textPrimary: Color,
    textSecondary: Color
) {
    val isActive = step.isCompleted || step.isCurrent
    val stepIcon = when (step.status) {
        "PENDING" -> "📋"
        "CONFIRMED" -> "✅"
        "SHIPPING" -> "🚚"
        "DELIVERED" -> "📦"
        else -> "•"
    }
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = if (isLast) 0.dp else 16.dp)
            .drawBehind {
                if (!isLast) {
                    drawLine(
                        color = if (step.isCompleted) Primary else Color.LightGray,
                        start = Offset(20.dp.toPx(), 44.dp.toPx()),
                        end = Offset(20.dp.toPx(), size.height + 8.dp.toPx()),
                        strokeWidth = 2.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 8f), 0f)
                    )
                }
            }
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(
                    color = if (isActive) Primary else Color.LightGray.copy(alpha = 0.3f),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stepIcon,
                fontSize = 18.sp
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.padding(top = 2.dp)) {
            Text(
                text = step.label,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (isActive) textPrimary else textSecondary
            )
            if (step.description.isNotEmpty()) {
                Text(
                    text = step.description,
                    fontSize = 13.sp,
                    color = textSecondary
                )
            }
        }
    }
}

@Composable
private fun StatusUpdateItem(
    update: TrackingUpdate,
    textPrimary: Color,
    textSecondary: Color,
    cardColor: Color
) {
    val timeFormatter = DateTimeFormatter.ofPattern("hh:mm\na", java.util.Locale("vi", "VN"))
    val formattedTime = update.timestamp.format(timeFormatter).replace("\n", "\n")

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Column(
            modifier = Modifier.width(48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val timeParts = formattedTime.split("\n")
            Text(
                text = timeParts.getOrNull(0) ?: "",
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = textSecondary
            )
            Text(
                text = timeParts.getOrNull(1)?.uppercase() ?: "AM",
                fontSize = 11.sp,
                color = textSecondary
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Timeline dot
        Box(
            modifier = Modifier
                .padding(top = 6.dp)
                .size(10.dp)
                .background(
                    color = if (update.isCompleted) Primary else Color.LightGray,
                    shape = CircleShape
                )
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = update.title,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = textPrimary
            )
            Text(
                text = update.description,
                fontSize = 13.sp,
                color = textSecondary
            )
        }
    }
}

@Composable
private fun ProductItem(
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
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.Gray.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            if (imageUrl.isNotEmpty()) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(8.dp))
                )
            } else {
                Text("📦", fontSize = 28.sp)
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = name,
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp,
                color = textPrimary,
                maxLines = 2
            )
            Text(
                text = "Số lượng: $quantity",
                fontSize = 13.sp,
                color = textSecondary
            )
        }

        Text(
            text = price,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            color = textPrimary
        )
    }
}
