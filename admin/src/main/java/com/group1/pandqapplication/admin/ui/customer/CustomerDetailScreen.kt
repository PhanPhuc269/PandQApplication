package com.group1.pandqapplication.admin.ui.customer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocalPhone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.group1.pandqapplication.admin.data.remote.dto.AccountStatus
import com.group1.pandqapplication.admin.data.remote.dto.CustomerTier
import com.group1.pandqapplication.shared.ui.theme.*
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerDetailScreen(
    onBackClick: () -> Unit,
    viewModel: CustomerDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val isDarkTheme = false // Get from theme settings if available

    val backgroundColor = if (isDarkTheme) CheckoutBackgroundDark else CheckoutBackgroundLight
    val textPrimary = if (isDarkTheme) CustomerTextPrimaryDark else CustomerTextPrimaryLight
    val textSecondary = if (isDarkTheme) CustomerTextSecondaryDark else CustomerTextSecondaryLight
    val borderColor = if (isDarkTheme) Color(0xFF374151) else Color(0xFFE5E7EB)
    val cardBg = if (isDarkTheme) Color(0xFF1F2937) else Color.White

    Scaffold(
        containerColor = backgroundColor,
        topBar = {
            TopAppBar(
                title = { Text("Chi tiết khách hàng", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = backgroundColor,
                    titleContentColor = textPrimary,
                    navigationIconContentColor = textSecondary
                )
            )
        }
    ) { paddingValues ->
        if (uiState.isLoading) {
            Box(Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (uiState.error != null) {
            Box(Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                Text("Lỗi: ${uiState.error}", color = Color.Red)
            }
        } else {
            val customer = uiState.customer
            if (customer != null) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Profile Header Card (with contact info merged)
                    Card(
                        colors = CardDefaults.cardColors(containerColor = cardBg),
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Avatar
                            if (customer.avatarUrl != null) {
                                AsyncImage(
                                    model = customer.avatarUrl,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(80.dp)
                                        .clip(CircleShape)
                                        .background(Color.Gray.copy(alpha = 0.1f)),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                val initials = customer.fullName?.split(" ")?.mapNotNull { it.firstOrNull() }?.take(2)?.joinToString("") ?: "?"
                                Box(
                                    modifier = Modifier
                                        .size(80.dp)
                                        .background(ProductPrimary, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(initials, fontSize = 28.sp, color = Color.White, fontWeight = FontWeight.Bold)
                                }
                            }
                            
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(customer.fullName ?: "N/A", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = textPrimary)

                            Spacer(modifier = Modifier.height(8.dp))
                            
                            // Contact info (merged into header)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Email, contentDescription = null, tint = textSecondary, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(customer.email, fontSize = 14.sp, color = textSecondary)
                            }
                            if (customer.phone != null) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.LocalPhone, contentDescription = null, tint = textSecondary, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(customer.phone, fontSize = 14.sp, color = textSecondary)
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))
                            
                            // Tier & Status Badges
                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                // Tier Badge
                                val (tierText, tierBgColor, tierTextColor) = when (customer.customerTier) {
                                    CustomerTier.BRONZE -> Triple("Đồng", Color(0xFFCD7F32).copy(alpha = 0.15f), Color(0xFF8B4513))
                                    CustomerTier.SILVER -> Triple("Bạc", Color(0xFFC0C0C0).copy(alpha = 0.25f), Color(0xFF6B6B6B))
                                    CustomerTier.GOLD -> Triple("Vàng", Color(0xFFFFD700).copy(alpha = 0.2f), Color(0xFFB8860B))
                                    CustomerTier.PLATINUM -> Triple("Bạch Kim", Color(0xFF9B59B6).copy(alpha = 0.15f), Color(0xFF8E44AD))
                                    null -> Triple("Đồng", Color(0xFFCD7F32).copy(alpha = 0.15f), Color(0xFF8B4513))
                                }
                                Box(
                                    modifier = Modifier
                                        .background(tierBgColor, RoundedCornerShape(6.dp))
                                        .padding(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Text(tierText, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = tierTextColor)
                                }

                                // Status Badge
                                val (statusText, statusBg) = when (customer.accountStatus) {
                                    AccountStatus.ACTIVE -> "Hoạt động" to CustomerStatusActive
                                    AccountStatus.INACTIVE -> "Không hoạt động" to CustomerStatusPending
                                    AccountStatus.BANNED -> "Đã khóa" to CustomerStatusLocked
                                    null -> "Hoạt động" to CustomerStatusActive
                                }
                                Box(
                                    modifier = Modifier
                                        .background(statusBg.copy(alpha = 0.15f), RoundedCornerShape(6.dp))
                                        .padding(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Text(statusText, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = statusBg)
                                }
                            }
                        }
                    }
                    
                    // Stats Card
                    Card(
                        colors = CardDefaults.cardColors(containerColor = cardBg),
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, borderColor)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Thống kê", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = textPrimary)
                            Spacer(modifier = Modifier.height(16.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                StatItem("Đơn hàng", customer.orderCount?.toString() ?: "0", textSecondary, textPrimary)
                                StatItem("Tổng chi tiêu", NumberFormat.getCurrencyInstance(Locale("vi", "VN")).format(customer.totalSpent ?: 0.0), textSecondary, textPrimary)
                            }
                        }
                    }

                    // Order History Section
                    if (!customer.recentOrders.isNullOrEmpty()) {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = cardBg),
                            shape = RoundedCornerShape(12.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, borderColor)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    "Lịch sử đơn hàng",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = textPrimary
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                
                                customer.recentOrders.forEachIndexed { index, order ->
                                    OrderHistoryItem(
                                        order = order,
                                        textPrimary = textPrimary,
                                        textSecondary = textSecondary,
                                        onClick = {
                                            // TODO: Navigate to order detail
                                        }
                                    )
                                    if (index < customer.recentOrders.size - 1) {
                                        HorizontalDivider(
                                            modifier = Modifier.padding(vertical = 8.dp),
                                            color = borderColor
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Lock/Unlock Button (at the bottom)
                    Button(
                        onClick = { 
                            val newStatus = if (customer.accountStatus == AccountStatus.BANNED) AccountStatus.ACTIVE else AccountStatus.BANNED
                            viewModel.updateStatus(newStatus)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (customer.accountStatus == AccountStatus.BANNED) CustomerStatusActive else CustomerStatusLocked
                        ),
                        enabled = !uiState.isStatusUpdating,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            if (customer.accountStatus == AccountStatus.BANNED) "Mở khóa tài khoản" else "Khóa tài khoản",
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StatItem(label: String, value: String, labelColor: Color, valueColor: Color) {
    Column {
        Text(label, fontSize = 12.sp, color = labelColor)
        Text(value, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = valueColor)
    }
}

@Composable
fun OrderHistoryItem(
    order: com.group1.pandqapplication.admin.data.remote.dto.OrderSummaryDto,
    textPrimary: Color,
    textSecondary: Color,
    onClick: () -> Unit
) {
    val currencyFormat = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
    
    // Parse date for display
    val displayDate = try {
        val inputFormat = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val outputFormat = java.text.SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val date = inputFormat.parse(order.orderDate)
        date?.let { outputFormat.format(it) } ?: order.orderDate
    } catch (e: Exception) {
        order.orderDate.take(10)
    }
    
    // Status display
    val (statusText, statusColor) = when (order.status) {
        "PENDING" -> "Chờ xử lý" to Color(0xFFF59E0B)
        "CONFIRMED" -> "Đã xác nhận" to Color(0xFF3B82F6)
        "PREPARING" -> "Đang chuẩn bị" to Color(0xFF8B5CF6)
        "READY" -> "Sẵn sàng" to Color(0xFF10B981)
        "DELIVERED" -> "Đã giao" to Color(0xFF059669)
        "COMPLETED" -> "Hoàn thành" to Color(0xFF22C55E)
        "CANCELLED" -> "Đã hủy" to Color(0xFFEF4444)
        else -> order.status to Color.Gray
    }
    
    
    Row(
        modifier = androidx.compose.ui.Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = androidx.compose.ui.Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "Đơn #${order.orderId.take(8)}...",
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    color = textPrimary
                )
                Spacer(modifier = androidx.compose.ui.Modifier.width(8.dp))
                Text(
                    statusText,
                    fontSize = 11.sp,
                    color = statusColor,
                    fontWeight = FontWeight.Medium
                )
            }
            Spacer(modifier = androidx.compose.ui.Modifier.height(4.dp))
            Row {
                Text(
                    displayDate,
                    fontSize = 12.sp,
                    color = textSecondary
                )
                Text(
                    " • ${order.itemCount} sản phẩm",
                    fontSize = 12.sp,
                    color = textSecondary
                )
            }
        }
        Text(
            currencyFormat.format(order.totalAmount),
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = textPrimary
        )
    }
}
