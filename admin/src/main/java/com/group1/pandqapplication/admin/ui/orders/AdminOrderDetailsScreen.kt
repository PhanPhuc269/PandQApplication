package com.group1.pandqapplication.admin.ui.orders

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.group1.pandqapplication.shared.ui.theme.AdminBackgroundDarkVariant
import com.group1.pandqapplication.shared.ui.theme.AdminBackgroundLight
import com.group1.pandqapplication.shared.ui.theme.AdminCardDarkVariant
import com.group1.pandqapplication.shared.ui.theme.AdminCardLight
import com.group1.pandqapplication.shared.ui.theme.AdminPrimary
import com.group1.pandqapplication.shared.ui.theme.AdminStatusPending
import com.group1.pandqapplication.shared.ui.theme.AdminTextPrimaryDark
import com.group1.pandqapplication.shared.ui.theme.AdminTextPrimaryLight
import com.group1.pandqapplication.shared.ui.theme.AdminTextSecondaryDark
import com.group1.pandqapplication.shared.ui.theme.AdminTextSecondaryLight

@Composable
fun AdminOrderDetailsScreen(
    orderId: String? = null,
    onBackClick: () -> Unit = {},
    viewModel: AdminOrderDetailsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val isDarkTheme = false
    
    var showShipperDialog by remember { mutableStateOf(false) }
    var showStatusDialog by remember { mutableStateOf(false) }
    
    // Load order when screen opens
    LaunchedEffect(orderId) {
        if (orderId != null) {
            viewModel.loadOrder(orderId)
        }
    }
    
    val backgroundColor = if (isDarkTheme) AdminBackgroundDarkVariant else AdminBackgroundLight
    val cardColor = if (isDarkTheme) AdminCardDarkVariant else AdminCardLight
    val textPrimary = if (isDarkTheme) AdminTextPrimaryDark else AdminTextPrimaryLight
    val textSecondary = if (isDarkTheme) AdminTextSecondaryDark else AdminTextSecondaryLight
    val borderColor = if (isDarkTheme) Color(0xFF374151) else Color(0xFFE5E7EB)

    Scaffold(
        containerColor = backgroundColor,
        topBar = {
            Surface(
                color = cardColor, // Preserving cardColor
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
                        IconButton(
                            onClick = onBackClick,
                            modifier = Modifier.align(Alignment.CenterStart)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBackIosNew,
                                contentDescription = "Back",
                                tint = AdminPrimary // Preserving AdminPrimary
                            )
                        }
                        Text(
                            text = "Chi tiết đơn hàng",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = textPrimary, // Preserving textPrimary
                            modifier = Modifier.align(Alignment.Center)
                        )
                        IconButton(
                            onClick = { /* More */ },
                            modifier = Modifier.align(Alignment.CenterEnd)
                        ) {
                            Icon(
                                imageVector = Icons.Default.MoreHoriz,
                                contentDescription = "More",
                                tint = AdminPrimary // Preserving AdminPrimary
                            )
                        }
                    }
                }
            }
        },
        bottomBar = {
            if (uiState.order != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(cardColor.copy(alpha = 0.9f))
                        .padding(16.dp)
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Button(
                            onClick = { showShipperDialog = true },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = AdminPrimary.copy(alpha = 0.2f),
                                contentColor = AdminPrimary
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Giao cho Shipper", fontWeight = FontWeight.SemiBold)
                        }
                        Button(
                            onClick = { showStatusDialog = true },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = AdminPrimary,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Cập nhật trạng thái", fontWeight = FontWeight.SemiBold)
                        }
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
                    CircularProgressIndicator(color = AdminPrimary)
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
                            text = uiState.error ?: "Error loading order",
                            color = Color.Red,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { orderId?.let { viewModel.loadOrder(it) } }) {
                            Text("Retry")
                        }
                    }
                }
            }
            uiState.order == null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No order data", color = textSecondary)
                }
            }
            else -> {
                val order = uiState.order!!
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header Card
            item {
                DetailCard(cardColor = cardColor) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "#${order.id.take(8).uppercase()}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = textPrimary
                        )
                        Box(
                            modifier = Modifier
                                .background(AdminStatusPending.copy(alpha = 0.2f), RoundedCornerShape(100.dp))
                                .padding(horizontal = 12.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = AdminOrderDetailsViewModel.getStatusText(order.status),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = AdminStatusPending
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = AdminOrderDetailsViewModel.formatDate(order.createdAt),
                        fontSize = 14.sp,
                        color = textSecondary
                    )
                }
            }
            
            // Product List Card
            item {
                DetailCard(cardColor = cardColor) {
                    Text(
                        text = "Danh sách sản phẩm",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = textPrimary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    order.items.forEachIndexed { index, item ->
                        if (index > 0) {
                            HorizontalDivider(color = borderColor, modifier = Modifier.padding(vertical = 16.dp))
                        }
                        ProductRow(
                            name = item.productName ?: "Unknown Product",
                            quantity = item.quantity,
                            price = AdminOrderDetailsViewModel.formatPrice(item.price),
                            total = AdminOrderDetailsViewModel.formatPrice(item.totalPrice),
                            imageUrl = item.imageUrl ?: "",
                            textPrimary = textPrimary,
                            textSecondary = textSecondary
                        )
                    }
                }
            }
            
            // Summary Card
            item {
                DetailCard(cardColor = cardColor) {
                    Text("Tóm tắt đơn hàng", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = textPrimary)
                    Spacer(modifier = Modifier.height(16.dp))
                    SummaryRow("Tạm tính", AdminOrderDetailsViewModel.formatPrice(order.totalAmount), textPrimary, textSecondary)
                    SummaryRow("Phí vận chuyển", AdminOrderDetailsViewModel.formatPrice(order.shippingFee), textPrimary, textSecondary)
                    if (order.discountAmount > 0) {
                        SummaryRow("Giảm giá", "- ${AdminOrderDetailsViewModel.formatPrice(order.discountAmount)}", textPrimary, textSecondary)
                    }
                    HorizontalDivider(color = borderColor, modifier = Modifier.padding(vertical = 16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Tổng cộng", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                        Text(AdminOrderDetailsViewModel.formatPrice(order.finalAmount), fontSize = 16.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                    }
                }
            }
            
            // Customer Info Card
            item {
                DetailCard(cardColor = cardColor) {
                    Text("Thông tin khách hàng", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = textPrimary)
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    // Name
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column {
                             Text("Khách hàng", fontSize = 14.sp, color = textSecondary)
                             Text("Khách hàng", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = textPrimary)
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    // Phone
                    Row(
                        modifier = Modifier.fillMaxWidth(), 
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                             Text("Số điện thoại", fontSize = 14.sp, color = textSecondary)
                             Text("N/A", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = textPrimary)
                        }
                        IconButton(
                            onClick = {},
                            modifier = Modifier
                                .size(36.dp)
                                .background(Color.Transparent)
                        ) {
                           Icon(Icons.Default.Call, contentDescription = null, tint = AdminPrimary) 
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    // Address
                    Row(
                        modifier = Modifier.fillMaxWidth(), 
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.fillMaxWidth(0.85f)) {
                             Text("Địa chỉ giao hàng", fontSize = 14.sp, color = textSecondary)
                             Text(order.shippingAddress ?: "N/A", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = textPrimary)
                        }
                        IconButton(onClick = {}) {
                           Icon(Icons.Default.Map, contentDescription = null, tint = AdminPrimary) 
                        }
                    }
                }
            }
            
            // Payment & Delivery
            item {
                 DetailCard(cardColor = cardColor) {
                    Text("Thanh toán & Vận chuyển", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = textPrimary)
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                         Text("Phương thức thanh toán", fontSize = 14.sp, color = textSecondary)
                         Text(AdminOrderDetailsViewModel.getPaymentMethodText(order.paymentMethod), fontSize = 14.sp, fontWeight = FontWeight.Medium, color = textPrimary)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                         Text("Shipper", fontSize = 14.sp, color = textSecondary)
                         Text(
                             text = order.shippingProvider ?: "Chưa gán",
                             fontSize = 14.sp,
                             fontWeight = FontWeight.Medium,
                             color = if (order.shippingProvider.isNullOrBlank()) textSecondary else textPrimary
                         )
                    }
                 }
            }
            
            item {
                Spacer(modifier = Modifier.height(60.dp))
            }
        }
            } // Close else block
        } // Close when block

        // Shipper Selection Dialog
        if (showShipperDialog) {
            ShipperSelectionDialog(
                onDismiss = { showShipperDialog = false },
                onShipperSelected = { shipper ->
                    viewModel.assignCarrier(shipper)
                    showShipperDialog = false
                },
                isDarkTheme = isDarkTheme
            )
        }

        // Status Update Dialog
        if (showStatusDialog) {
            StatusUpdateDialog(
                currentStatus = uiState.order?.status ?: "",
                onDismiss = { showStatusDialog = false },
                onStatusSelected = { status ->
                    viewModel.updateOrderStatus(status)
                    showStatusDialog = false
                },
                isDarkTheme = isDarkTheme
            )
        }
    }
}

@Composable
fun DetailCard(
    cardColor: Color,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(cardColor)
            .padding(16.dp)
    ) {
        content()
    }
}

@Composable
fun ProductRow(
    name: String,
    quantity: Int,
    price: String,
    total: String,
    imageUrl: String,
    textPrimary: Color,
    textSecondary: Color
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.Gray.copy(alpha = 0.1f)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(name, fontWeight = FontWeight.Medium, fontSize = 16.sp, color = textPrimary, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text("SL: $quantity x $price", fontSize = 14.sp, color = textSecondary)
        }
        Text(total, fontWeight = FontWeight.Medium, fontSize = 16.sp, color = textPrimary)
    }
}

@Composable
fun SummaryRow(
    label: String, 
    value: String, 
    textPrimary: Color, 
    textSecondary: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontSize = 14.sp, color = textSecondary)
        Text(value, fontSize = 14.sp, color = textPrimary)
    }
}

@Composable
fun ShipperSelectionDialog(
    onDismiss: () -> Unit,
    onShipperSelected: (String) -> Unit,
    isDarkTheme: Boolean
) {
    val shippers = listOf("Giao Hàng Nhanh", "Viettel Post", "SPX Express", "GrabExpress", "AhaMove")
    val cardColor = if (isDarkTheme) AdminCardDarkVariant else AdminCardLight
    val textPrimary = if (isDarkTheme) AdminTextPrimaryDark else AdminTextPrimaryLight

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            color = cardColor
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "Chọn đơn vị vận chuyển",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = textPrimary,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                shippers.forEach { shipper ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onShipperSelected(shipper) }
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Map,
                            contentDescription = null,
                            tint = AdminPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(shipper, fontSize = 16.sp, color = textPrimary)
                    }
                    HorizontalDivider(color = Color.LightGray.copy(alpha = 0.3f))
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = AdminPrimary)
                ) {
                    Text("Hủy")
                }
            }
        }
    }
}

@Composable
fun StatusUpdateDialog(
    currentStatus: String,
    onDismiss: () -> Unit,
    onStatusSelected: (String) -> Unit,
    isDarkTheme: Boolean
) {
    val statuses = listOf(
        "PENDING" to "Chờ xử lý",
        "CONFIRMED" to "Đã xác nhận",
        "SHIPPING" to "Đang giao",
        "DELIVERED" to "Đã giao",
        "COMPLETED" to "Hoàn thành",
        "CANCELLED" to "Đã hủy"
    )
    val cardColor = if (isDarkTheme) AdminCardDarkVariant else AdminCardLight
    val textPrimary = if (isDarkTheme) AdminTextPrimaryDark else AdminTextPrimaryLight

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            color = cardColor
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "Cập nhật trạng thái",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = textPrimary,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                statuses.forEach { (status, label) ->
                    val isCurrent = status.uppercase() == currentStatus.uppercase()
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onStatusSelected(status) }
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .clip(CircleShape)
                                .background(if (isCurrent) AdminPrimary else Color.Gray.copy(alpha = 0.3f))
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            label, 
                            fontSize = 16.sp, 
                            color = textPrimary,
                            fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                    HorizontalDivider(color = Color.LightGray.copy(alpha = 0.3f))
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = AdminPrimary)
                ) {
                    Text("Hủy")
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewAdminOrderDetailsScreen() {
    AdminOrderDetailsScreen()
}
