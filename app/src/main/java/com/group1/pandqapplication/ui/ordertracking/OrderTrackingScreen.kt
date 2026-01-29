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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.group1.pandqapplication.shared.data.remote.dto.OrderDto
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
    onBackClick: () -> Unit = {},
    onSupportClick: () -> Unit = {},
    orderId: String? = null,
    order: OrderDto? = null, // Can pass order directly from navigation
    viewModel: OrderTrackingViewModel = hiltViewModel(),
    onReviewClick: (String, String) -> Unit = { _, _ -> } // productId, productName
) {
    val uiState by viewModel.uiState.collectAsState()
    
    // Load order from API or set from param
    LaunchedEffect(orderId, order) {
        when {
            order != null -> viewModel.setOrder(order)
            orderId != null -> viewModel.loadOrder(orderId)
        }
    }
    
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
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
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
                    text = androidx.compose.ui.res.stringResource(com.group1.pandqapplication.R.string.track_order_title),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = textPrimary,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        },
        bottomBar = {
            val currentOrder = uiState.order
            // Only COMPLETED orders can be reviewed
            val canReview = currentOrder != null && 
                currentOrder.status.uppercase() == "COMPLETED"
            
            Box(
                modifier = Modifier
                    .background(cardColor.copy(alpha = 0.8f))
                    .padding(16.dp)
            ) {
                if (canReview && currentOrder != null) {
                    // Show single review button for the order (reviews first product)
                    val firstItem = currentOrder.items.firstOrNull()
                    if (firstItem != null) {
                        Button(
                            onClick = { 
                                onReviewClick(firstItem.productId, firstItem.productName)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Primary),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = androidx.compose.ui.res.stringResource(com.group1.pandqapplication.R.string.rate_order_btn),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                } else {
                    Button(
                        onClick = onSupportClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Primary),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = androidx.compose.ui.res.stringResource(com.group1.pandqapplication.R.string.contact_support_btn),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Primary)
                }
            }
            uiState.error != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = uiState.error ?: androidx.compose.ui.res.stringResource(com.group1.pandqapplication.R.string.error_generic, ""),
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = { orderId?.let { viewModel.loadOrder(it) } },
                            colors = ButtonDefaults.buttonColors(containerColor = Primary)
                        ) {
                            Text(androidx.compose.ui.res.stringResource(com.group1.pandqapplication.R.string.refresh))
                        }
                    }
                }
            }
            uiState.order != null -> {
                val currentOrder = uiState.order!!
                
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
                                    text = androidx.compose.ui.res.stringResource(com.group1.pandqapplication.R.string.order_number_fmt, currentOrder.id.take(8)),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = textPrimary
                                )
                                Text(
                                    text = OrderTrackingViewModel.formatDate(currentOrder.createdAt),
                                    fontSize = 14.sp,
                                    color = textSecondary
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Row {
                                Text(
                                    text = androidx.compose.ui.res.stringResource(com.group1.pandqapplication.R.string.estimated_delivery_label),
                                    fontSize = 14.sp,
                                    color = textSecondary
                                )
                                Text(
                                    text = OrderTrackingViewModel.calculateEstimatedDelivery(currentOrder.createdAt),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = textPrimary
                                )
                            }
                            
                            // Status Badge for cancelled/failed orders
                            if (currentOrder.status.uppercase() in listOf("CANCELLED", "FAILED", "RETURNED")) {
                                Spacer(modifier = Modifier.height(12.dp))
                                Box(
                                    modifier = Modifier
                                        .background(
                                            color = Color(0xFFFFEBEE),
                                            shape = RoundedCornerShape(4.dp)
                                        )
                                        .padding(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = when (currentOrder.status.uppercase()) {
                                            "CANCELLED" -> androidx.compose.ui.res.stringResource(com.group1.pandqapplication.R.string.status_cancelled_lbl)
                                            "FAILED" -> androidx.compose.ui.res.stringResource(com.group1.pandqapplication.R.string.status_failed_lbl)
                                            "RETURNED" -> androidx.compose.ui.res.stringResource(com.group1.pandqapplication.R.string.status_returned_lbl)
                                            else -> currentOrder.status
                                        },
                                        color = Color(0xFFD32F2F),
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
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
                                .padding(start = 0.dp)
                        ) {
                            uiState.trackingSteps.forEachIndexed { index, step ->
                                TrackingStep(
                                    icon = when (step.iconType) {
                                        TrackingIconType.ORDER_PLACED -> Icons.Default.ReceiptLong
                                        TrackingIconType.CONFIRMED -> Icons.Default.Verified
                                        TrackingIconType.SHIPPING -> Icons.Default.LocalShipping
                                        TrackingIconType.DELIVERED -> Icons.Default.Inventory2
                                    },
                                    title = androidx.compose.ui.res.stringResource(step.titleRes),
                                    subtitle = androidx.compose.ui.res.stringResource(step.subtitleRes),
                                    isActive = step.isActive,
                                    isLast = index == uiState.trackingSteps.lastIndex,
                                    textPrimary = textPrimary,
                                    textSecondary = textSecondary
                                )
                            }
                        }
                    }

                    // Status Updates Section
                    if (uiState.statusUpdates.isNotEmpty()) {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = cardColor),
                            shape = RoundedCornerShape(12.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(20.dp)) {
                                Text(
                                    text = androidx.compose.ui.res.stringResource(com.group1.pandqapplication.R.string.status_update_title),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = textPrimary,
                                    modifier = Modifier.padding(bottom = 16.dp)
                                )
                                
                                uiState.statusUpdates.forEach { update ->
                                    val subtitle = if (update.subtitleRes != null) {
                                        androidx.compose.ui.res.stringResource(update.subtitleRes)
                                    } else {
                                        update.subtitleStr ?: ""
                                    }
                                    
                                    StatusUpdateItem(
                                        time = update.time,
                                        title = androidx.compose.ui.res.stringResource(update.titleRes),
                                        subtitle = subtitle,
                                        isActive = update.isActive,
                                        textPrimary = textPrimary,
                                        textSecondary = textSecondary,
                                        cardColor = cardColor
                                    )
                                }
                            }
                        }
                    }

                    // Order Details Section
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
                                    .clickable { isExpanded = !isExpanded }
                                    .padding(20.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = androidx.compose.ui.res.stringResource(com.group1.pandqapplication.R.string.view_order_detail_btn),
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
                                    
                                    Spacer(modifier = Modifier.height(12.dp))
                                    
                                    // Order ID
                                    Row(
                                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = androidx.compose.ui.res.stringResource(com.group1.pandqapplication.R.string.order_id_lbl),
                                            fontSize = 14.sp,
                                            color = textSecondary
                                        )
                                        Text(
                                            text = "#${currentOrder.id.take(8).uppercase()}",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = textPrimary
                                        )
                                    }
                                    
                                    // Order Date
                                    if (!currentOrder.createdAt.isNullOrBlank()) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                text = androidx.compose.ui.res.stringResource(com.group1.pandqapplication.R.string.order_date_lbl),
                                                fontSize = 14.sp,
                                                color = textSecondary
                                            )
                                            Text(
                                                text = OrderTrackingViewModel.formatDate(currentOrder.createdAt),
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Medium,
                                                color = textPrimary
                                            )
                                        }
                                    }
                                    
                                    HorizontalDivider(
                                        modifier = Modifier.padding(vertical = 12.dp),
                                        color = Color.Gray.copy(alpha = 0.2f)
                                    )
                                    
                                    // Section title: Products
                                    Text(
                                        text = androidx.compose.ui.res.stringResource(com.group1.pandqapplication.R.string.product_section_title),
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = textPrimary,
                                        modifier = Modifier.padding(bottom = 12.dp)
                                    )
                                    
                                    // Display actual order items
                                    if (currentOrder.items.isEmpty()) {
                                        Text(
                                            text = androidx.compose.ui.res.stringResource(com.group1.pandqapplication.R.string.no_product_info_msg),
                                            fontSize = 14.sp,
                                            color = textSecondary,
                                            modifier = Modifier.padding(vertical = 8.dp)
                                        )
                                    } else {
                                        currentOrder.items.forEach { item ->
                                            ProductItem(
                                                imageUrl = item.imageUrl ?: "",
                                                name = item.productName,
                                                quantity = item.quantity,
                                                price = OrderTrackingViewModel.formatPrice(item.totalPrice),
                                                textPrimary = textPrimary,
                                                textSecondary = textSecondary
                                            )
                                        }
                                    }

                                    HorizontalDivider(
                                        modifier = Modifier.padding(top = 16.dp, bottom = 16.dp),
                                        color = Color.Gray.copy(alpha = 0.2f)
                                    )
                                    
                                    // Shipping Address
                                    if (!currentOrder.shippingAddress.isNullOrBlank()) {
                                        Text(
                                            text = androidx.compose.ui.res.stringResource(com.group1.pandqapplication.R.string.shipping_address_title),
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = textPrimary
                                        )
                                        Text(
                                            text = currentOrder.shippingAddress ?: "",
                                            fontSize = 13.sp,
                                            color = textSecondary,
                                            modifier = Modifier.padding(bottom = 12.dp)
                                        )
                                    }
                                    
                                    // Payment Method
                                    if (!currentOrder.paymentMethod.isNullOrBlank()) {
                                        Row(
                                            modifier = Modifier.padding(bottom = 12.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = androidx.compose.ui.res.stringResource(com.group1.pandqapplication.R.string.payment_lbl),
                                                fontSize = 14.sp,
                                                color = textSecondary
                                            )
                                            Text(
                                                text = when (currentOrder.paymentMethod?.uppercase()) {
                                                    "COD" -> androidx.compose.ui.res.stringResource(com.group1.pandqapplication.R.string.payment_cod_lbl)
                                                    "ZALOPAY" -> "ZaloPay"
                                                    "SEPAY" -> "SePay"
                                                    "MOMO" -> "MoMo"
                                                    else -> currentOrder.paymentMethod ?: ""
                                                },
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Medium,
                                                color = textPrimary
                                            )
                                        }
                                    }
                                    
                                    HorizontalDivider(
                                        modifier = Modifier.padding(vertical = 8.dp),
                                        color = Color.Gray.copy(alpha = 0.2f)
                                    )
                                    
                                    // Summary
                                    SummaryRow(androidx.compose.ui.res.stringResource(com.group1.pandqapplication.R.string.summary_subtotal), OrderTrackingViewModel.formatPrice(currentOrder.totalAmount), textSecondary, textPrimary)
                                    SummaryRow(androidx.compose.ui.res.stringResource(com.group1.pandqapplication.R.string.summary_shipping), OrderTrackingViewModel.formatPrice(currentOrder.shippingFee), textSecondary, textPrimary)
                                    if (currentOrder.discountAmount > 0) {
                                        SummaryRow(androidx.compose.ui.res.stringResource(com.group1.pandqapplication.R.string.summary_discount), "-${OrderTrackingViewModel.formatPrice(currentOrder.discountAmount)}", textSecondary, Primary)
                                    }
                                    
                                    Spacer(modifier = Modifier.height(8.dp))
                                    
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = androidx.compose.ui.res.stringResource(com.group1.pandqapplication.R.string.summary_total),
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = textPrimary
                                        )
                                        Text(
                                            text = OrderTrackingViewModel.formatPrice(currentOrder.finalAmount),
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
    }
}

@Composable
fun SummaryRow(
    label: String,
    value: String,
    labelColor: Color,
    valueColor: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 14.sp, color = labelColor)
        Text(text = value, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = valueColor)
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
                        color = if (isActive) Color.LightGray else Color.LightGray,
                        start = Offset(18.dp.toPx(), 40.dp.toPx()),
                        end = Offset(18.dp.toPx(), size.height),
                        strokeWidth = 2.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                    )
                }
            }
    ) {
        Box(
            modifier = Modifier
                .padding(start = 2.dp)
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
                Box(
                    modifier = Modifier
                        .padding(top = 6.dp)
                        .size(12.dp)
                        .background(
                            color = if (isActive) Primary else Color.LightGray,
                            shape = CircleShape
                        )
                        .border(4.dp, cardColor, CircleShape)
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
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.Gray.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            if (imageUrl.isNotBlank()) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = name,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                Text(
                    text = "📦",
                    fontSize = 24.sp
                )
            }
        }
        
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
