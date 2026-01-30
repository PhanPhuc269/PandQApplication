package com.group1.pandqapplication.admin.ui.shipping

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Pending
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.group1.pandqapplication.admin.data.remote.dto.ShippingOrderDto
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
    onBackClick: () -> Unit = {},
    onOrderClick: (String) -> Unit = {},
    viewModel: ShippingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val isDarkTheme = false
    
    val backgroundColor = if (isDarkTheme) ShippingBackgroundDark else ShippingBackgroundLight
    val cardColor = if (isDarkTheme) ShippingCardDark else CardLight
    val textPrimary = if (isDarkTheme) Color.White else ShippingTextLightPrimary
    val textSecondary = if (isDarkTheme) ShippingTextDarkSecondary else ShippingTextLightSecondary
    val borderColor = if (isDarkTheme) Color(0xFF3A5169) else Color(0xFFE6E6E6)

    // Assign Carrier Dialog
    if (uiState.showAssignCarrierDialog) {
        AssignCarrierDialog(
            onDismiss = { viewModel.dismissAssignCarrierDialog() },
            onConfirm = { provider, tracking -> viewModel.assignCarrier(provider, tracking) },
            isLoading = uiState.isProcessing
        )
    }

    // Update Status Dialog
    if (uiState.showUpdateStatusDialog) {
        UpdateStatusDialog(
            currentStatus = uiState.selectedOrder?.status ?: "",
            onDismiss = { viewModel.dismissUpdateStatusDialog() },
            onConfirm = { newStatus -> viewModel.updateStatus(newStatus) },
            isLoading = uiState.isProcessing
        )
    }

    Scaffold(
        containerColor = backgroundColor,
        topBar = {
            Column(
                modifier = Modifier.statusBarsPadding()
            ) {
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
                        onClick = { viewModel.loadOrders() },
                        modifier = Modifier.align(Alignment.CenterEnd)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh",
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
                       .height(52.dp)
                       .background(cardColor, RoundedCornerShape(24.dp)),
                   verticalAlignment = Alignment.CenterVertically
               ) {
                   Box(
                       modifier = Modifier
                           .width(48.dp)
                           .height(52.dp),
                       contentAlignment = Alignment.Center
                   ) {
                       Icon(
                           imageVector = Icons.Default.Search,
                           contentDescription = "Search",
                           tint = textSecondary
                       )
                   }
                   TextField(
                       value = uiState.searchQuery,
                       onValueChange = { viewModel.setSearchQuery(it) },
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
                       singleLine = true,
                       trailingIcon = {
                           if (uiState.searchQuery.isNotEmpty()) {
                               IconButton(onClick = { viewModel.setSearchQuery("") }) {
                                   Icon(Icons.Default.Close, contentDescription = "Clear", tint = textSecondary)
                               }
                           }
                       }
                   )
               }
            }

            // Tabs
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(backgroundColor)
                    .padding(horizontal = 8.dp)
            ) {
                ShippingTabItem(
                    text = "Chờ xử lý",
                    count = uiState.pendingCount,
                    isSelected = uiState.selectedTab == ShippingTab.PENDING,
                    textPrimary = textPrimary,
                    textSecondary = textSecondary,
                    primaryColor = ShippingPrimary,
                    onClick = { viewModel.selectTab(ShippingTab.PENDING) },
                    modifier = Modifier.weight(1f)
                )
                ShippingTabItem(
                    text = "Đang giao",
                    count = uiState.shippingCount,
                    isSelected = uiState.selectedTab == ShippingTab.SHIPPING,
                    textPrimary = textPrimary,
                    textSecondary = textSecondary,
                    primaryColor = ShippingPrimary,
                    onClick = { viewModel.selectTab(ShippingTab.SHIPPING) },
                    modifier = Modifier.weight(1f)
                )
                ShippingTabItem(
                    text = "Hoàn thành",
                    count = uiState.completedCount,
                    isSelected = uiState.selectedTab == ShippingTab.COMPLETED,
                    textPrimary = textPrimary,
                    textSecondary = textSecondary,
                    primaryColor = ShippingPrimary,
                    onClick = { viewModel.selectTab(ShippingTab.COMPLETED) },
                    modifier = Modifier.weight(1f)
                )
                ShippingTabItem(
                    text = "Đã huỷ",
                    count = uiState.cancelledCount,
                    isSelected = uiState.selectedTab == ShippingTab.CANCELLED,
                    textPrimary = textPrimary,
                    textSecondary = textSecondary,
                    primaryColor = ShippingPrimary,
                    onClick = { viewModel.selectTab(ShippingTab.CANCELLED) },
                    modifier = Modifier.weight(1f)
                )
            }
            HorizontalDivider(color = borderColor)

            // Content
            when {
                uiState.isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = ShippingPrimary)
                    }
                }
                uiState.error != null -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "Lỗi: ${uiState.error}", color = Color.Red)
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(onClick = { viewModel.loadOrders() }) {
                                Text("Thử lại")
                            }
                        }
                    }
                }
                uiState.filteredOrders.isEmpty() -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = "Không có đơn hàng nào", color = textSecondary)
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(uiState.filteredOrders) { order ->
                            ShippingOrderCard(
                                order = order,
                                cardColor = cardColor,
                                textPrimary = textPrimary,
                                textSecondary = textSecondary,
                                borderColor = borderColor,
                                onCardClick = { onOrderClick(order.id) },
                                onAssignCarrier = { viewModel.showAssignCarrierDialog(order) },
                                onUpdateStatus = { viewModel.showUpdateStatusDialog(order) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ShippingTabItem(
    text: String,
    count: Int,
    isSelected: Boolean,
    textPrimary: Color,
    textSecondary: Color,
    primaryColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "$text ($count)",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = if (isSelected) primaryColor else textSecondary,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .height(3.dp)
                .fillMaxWidth()
                .padding(horizontal = 4.dp)
                .background(if (isSelected) primaryColor else Color.Transparent)
        )
    }
}

@Composable
fun ShippingOrderCard(
    order: ShippingOrderDto,
    cardColor: Color,
    textPrimary: Color,
    textSecondary: Color,
    borderColor: Color,
    onCardClick: () -> Unit = {},
    onAssignCarrier: () -> Unit,
    onUpdateStatus: () -> Unit
) {
    val status = order.status ?: "PENDING"
    
    // Màu sắc riêng cho từng status
    val statusPending = Color(0xFFFF9800)      // Cam - Chờ xác nhận
    val statusConfirmed = Color(0xFF2196F3)     // Xanh dương - Đã xác nhận
    val statusShipping = Color(0xFF9C27B0)      // Tím - Đang giao
    val statusDelivered = Color(0xFF4CAF50)     // Xanh lá - Đã giao
    val statusCompleted = Color(0xFF1B5E20)     // Xanh đậm - Hoàn thành
    val statusCancelled = Color(0xFFF44336)     // Đỏ - Đã huỷ
    
    val statusColor = when (status) {
        "PENDING" -> statusPending
        "CONFIRMED" -> statusConfirmed
        "SHIPPING" -> statusShipping
        "DELIVERED" -> statusDelivered
        "COMPLETED" -> statusCompleted
        "CANCELLED" -> statusCancelled
        else -> statusPending
    }
    val statusText = when (status) {
        "PENDING" -> "Chờ xác nhận"
        "CONFIRMED" -> "Đã xác nhận"
        "SHIPPING" -> "Đang giao"
        "DELIVERED" -> "Đã giao"
        "COMPLETED" -> "Hoàn thành"
        "CANCELLED" -> "Đã huỷ"
        else -> status
    }
    val statusIcon = when (status) {
        "PENDING" -> Icons.Default.Pending             // Pending icon
        "CONFIRMED" -> Icons.Default.CheckCircle       // Checkmark
        "SHIPPING" -> Icons.Default.LocalShipping      // Truck icon
        "DELIVERED" -> Icons.Default.CheckCircle       // Checkmark
        "COMPLETED" -> Icons.Default.CheckCircle       // Checkmark
        else -> Icons.Default.Pending
    }

    // Parse customer info - ưu tiên dùng customerName từ API, fallback đến shippingAddress parsing
    val customerName = order.customerName?.takeIf { it.isNotBlank() } 
        ?: parseField(order.shippingAddress, "fullName") 
        ?: parseField(order.shippingAddress, "name") 
        ?: "Khách hàng"
    
    val customerPhone = order.customerPhone?.takeIf { it.isNotBlank() }
        ?: parseField(order.shippingAddress, "phone")
    
    // Parse địa chỉ - thử hiển thị trực tiếp nếu không phải JSON, fallback đến parse JSON
    val address = getDisplayAddress(order.shippingAddress) ?: "Địa chỉ không xác định"

    // Determine button text and action
    val hasCarrier = !order.shippingProvider.isNullOrBlank()
    val buttonText = when {
        status == "PENDING" && !hasCarrier -> "Gán đơn vị"
        status == "PENDING" || status == "CONFIRMED" -> "Cập nhật trạng thái"
        status == "SHIPPING" -> "Xem theo dõi"
        else -> null
    }
    val isSecondaryButton = status == "SHIPPING"

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(cardColor)
            .clickable { onCardClick() }
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column {
                Text(
                    text = "Đơn hàng #${order.id.take(8).uppercase()}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = textPrimary
                )
                Text(
                    text = order.createdAt?.take(10) ?: "",
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
        
        // Customer Info
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = customerName,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = textPrimary
            )
            Text(
                text = address,
                fontSize = 14.sp,
                color = textSecondary,
                maxLines = 2
            )
            Text(
                text = order.shippingProvider ?: "Chưa gán đơn vị vận chuyển",
                fontSize = 14.sp,
                color = if (order.shippingProvider.isNullOrBlank()) ShippingStatusPending else textSecondary,
                fontWeight = if (order.shippingProvider.isNullOrBlank()) FontWeight.Medium else FontWeight.Normal
            )
        }
        
        // Action Buttons - Hiển thị 2 button cho tab Chờ xử lý
        if (status == "PENDING" || status == "CONFIRMED") {
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Button Gán đơn vị (trái) - luôn enabled
                Button(
                    onClick = { onAssignCarrier() },
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (hasCarrier) Color.LightGray else ShippingPrimary,
                        contentColor = if (hasCarrier) Color.DarkGray else Color.White
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocalShipping,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (hasCarrier) "Đổi ĐVVC" else "Gán đơn vị",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                // Button Cập nhật trạng thái (phải) - disabled khi chưa gán ĐVVC
                Button(
                    onClick = { onUpdateStatus() },
                    enabled = hasCarrier,
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (hasCarrier) ShippingPrimary else Color.LightGray.copy(alpha = 0.5f),
                        contentColor = if (hasCarrier) Color.White else Color.Gray,
                        disabledContainerColor = Color.LightGray.copy(alpha = 0.3f),
                        disabledContentColor = Color.Gray.copy(alpha = 0.5f)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Cập nhật",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        } else if (buttonText != null) {
            // Các tab khác - hiển thị 1 button như cũ
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = { onUpdateStatus() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSecondaryButton) Color.Transparent else ShippingPrimary,
                    contentColor = if (isSecondaryButton) ShippingPrimary else Color.White
                ),
                border = if (isSecondaryButton) androidx.compose.foundation.BorderStroke(1.dp, ShippingPrimary) else null,
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = buttonText,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

fun parseField(json: String?, field: String): String? {
    if (json.isNullOrBlank()) return null
    return try {
        val regex = """"$field"\s*:\s*"([^"]+)"""".toRegex()
        regex.find(json)?.groupValues?.get(1)
    } catch (e: Exception) {
        null
    }
}

/**
 * Build địa chỉ đầy đủ từ các fields trong shippingAddress JSON
 * Format: address, ward, district, city
 */
fun buildFullAddress(json: String?): String? {
    if (json.isNullOrBlank()) return null
    
    val parts = mutableListOf<String>()
    
    // Thử lấy địa chỉ chi tiết
    val address = parseField(json, "address") 
        ?: parseField(json, "street") 
        ?: parseField(json, "detailAddress")
        ?: parseField(json, "fullAddress")
    if (!address.isNullOrBlank()) parts.add(address)
    
    // Thêm phường/xã
    val ward = parseField(json, "ward")
    if (!ward.isNullOrBlank()) parts.add(ward)
    
    // Thêm quận/huyện
    val district = parseField(json, "district")
    if (!district.isNullOrBlank()) parts.add(district)
    
    // Thêm thành phố/tỉnh
    val city = parseField(json, "city") ?: parseField(json, "province")
    if (!city.isNullOrBlank()) parts.add(city)
    
    return if (parts.isNotEmpty()) parts.joinToString(", ") else null
}

/**
 * Get display address from shippingAddress
 * - Nếu là plain text (không phải JSON) -> hiển thị trực tiếp
 * - Nếu là JSON -> parse và build full address
 */
fun getDisplayAddress(shippingAddress: String?): String? {
    if (shippingAddress.isNullOrBlank()) return null
    
    val trimmed = shippingAddress.trim()
    
    // Kiểm tra nếu là JSON (bắt đầu bằng { )
    if (trimmed.startsWith("{")) {
        // Parse JSON và build full address
        return buildFullAddress(trimmed)
    }
    
    // Không phải JSON -> hiển thị trực tiếp
    return trimmed
}

@Composable
fun AssignCarrierDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String?) -> Unit,
    isLoading: Boolean
) {
    var selectedProvider by remember { mutableStateOf("") }
    var trackingNumber by remember { mutableStateOf("") }
    
    val carriers = listOf(
        "Giao Hàng Nhanh",
        "Viettel Post",
        "J&T Express",
        "GHTK",
        "Ninja Van"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Gán đơn vị vận chuyển", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Chọn đơn vị vận chuyển:", fontSize = 14.sp)
                carriers.forEach { carrier ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                if (selectedProvider == carrier) ShippingPrimary.copy(alpha = 0.1f)
                                else Color.Transparent
                            )
                            .border(
                                1.dp,
                                if (selectedProvider == carrier) ShippingPrimary else Color.LightGray,
                                RoundedCornerShape(8.dp)
                            )
                            .clickable { selectedProvider = carrier }
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = carrier,
                            fontSize = 14.sp,
                            color = if (selectedProvider == carrier) ShippingPrimary else Color.Gray
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedTextField(
                    value = trackingNumber,
                    onValueChange = { trackingNumber = it },
                    label = { Text("Mã theo dõi (tùy chọn)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(selectedProvider, trackingNumber.ifBlank { null }) },
                enabled = selectedProvider.isNotBlank() && !isLoading,
                colors = ButtonDefaults.buttonColors(containerColor = ShippingPrimary)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White, strokeWidth = 2.dp)
                } else {
                    Text("Xác nhận")
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Huỷ")
            }
        }
    )
}

@Composable
fun UpdateStatusDialog(
    currentStatus: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
    isLoading: Boolean
) {
    var selectedStatus by remember { mutableStateOf("") }
    
    val statusOptions = when (currentStatus) {
        "PENDING" -> listOf("CONFIRMED" to "Xác nhận đơn", "CANCELLED" to "Huỷ đơn")
        "CONFIRMED" -> listOf("SHIPPING" to "Bắt đầu giao", "CANCELLED" to "Huỷ đơn")
        "SHIPPING" -> listOf("DELIVERED" to "Đã giao hàng", "CANCELLED" to "Huỷ đơn")
        "DELIVERED" -> listOf("COMPLETED" to "Hoàn thành")
        else -> emptyList()
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Cập nhật trạng thái", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Chọn trạng thái mới:", fontSize = 14.sp)
                statusOptions.forEach { (status, label) ->
                    val isCancelled = status == "CANCELLED"
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                when {
                                    selectedStatus == status && isCancelled -> ShippingStatusCancelled.copy(alpha = 0.1f)
                                    selectedStatus == status -> ShippingPrimary.copy(alpha = 0.1f)
                                    else -> Color.Transparent
                                }
                            )
                            .border(
                                1.dp,
                                when {
                                    selectedStatus == status && isCancelled -> ShippingStatusCancelled
                                    selectedStatus == status -> ShippingPrimary
                                    else -> Color.LightGray
                                },
                                RoundedCornerShape(8.dp)
                            )
                            .clickable { selectedStatus = status }
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = label,
                            fontSize = 14.sp,
                            color = when {
                                selectedStatus == status && isCancelled -> ShippingStatusCancelled
                                selectedStatus == status -> ShippingPrimary
                                else -> Color.Gray
                            }
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(selectedStatus) },
                enabled = selectedStatus.isNotBlank() && !isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedStatus == "CANCELLED") ShippingStatusCancelled else ShippingPrimary
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White, strokeWidth = 2.dp)
                } else {
                    Text("Cập nhật")
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Huỷ")
            }
        }
    )
}
