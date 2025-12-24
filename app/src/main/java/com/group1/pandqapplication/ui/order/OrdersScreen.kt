package com.group1.pandqapplication.ui.order

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.group1.pandqapplication.shared.data.remote.dto.OrderDto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersScreen(
    viewModel: OrderHistoryViewModel = hiltViewModel(),
    onOrderClick: (String) -> Unit = {}
) {
    val backgroundColor = Color(0xFFF8F6F6)
    val primaryColor = Color(0xFFec3713)

    val uiState by viewModel.uiState.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("Tất cả") }
    var isRefreshing by remember { mutableStateOf(false) }

    // Handle refresh state based on viewModel loading
    LaunchedEffect(uiState.isLoading) {
        if (!uiState.isLoading) {
            isRefreshing = false
        }
    }

    val filters = listOf("Tất cả", "Đang xử lý", "Đang giao", "Đã giao", "Hoàn thành", "Đã huỷ")

    // Map filter to backend status
    val filterToStatus = mapOf(
        "Tất cả" to null,
        "Đang xử lý" to listOf("CONFIRMED"),
        "Đang giao" to listOf("SHIPPING"),
        "Đã giao" to listOf("DELIVERED"),
        "Hoàn thành" to listOf("COMPLETED"),
        "Đã huỷ" to listOf("CANCELLED", "RETURNED", "FAILED")
    )

    val filteredOrders = uiState.orders.filter { order ->
        val matchesSearch = searchQuery.isEmpty() ||
                order.id.contains(searchQuery, ignoreCase = true) ||
                order.items.any { it.productName.contains(searchQuery, ignoreCase = true) }

        val statusFilter = filterToStatus[selectedFilter]
        val matchesFilter = statusFilter == null || statusFilter.contains(order.status.uppercase())

        matchesSearch && matchesFilter
    }

    Scaffold(
        containerColor = backgroundColor,
        topBar = {
            Surface(
                color = backgroundColor,
                shadowElevation = 1.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                        Text(
                            "Lịch sử đơn hàng",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Color(0xFF111827)
                        )
                    }
                    Spacer(modifier = Modifier.size(48.dp))
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Search Bar
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(48.dp),
                color = Color.White,
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp)
                ) {
                    Icon(Icons.Filled.Search, contentDescription = null, tint = Color.Gray)
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (searchQuery.isEmpty()) {
                            Text(
                                "Tìm theo mã đơn hàng, tên sản phẩm...",
                                color = Color.Gray,
                                fontSize = 14.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        androidx.compose.foundation.text.BasicTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            singleLine = true,
                            textStyle = TextStyle(
                                fontSize = 14.sp,
                                color = Color.Black
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            // Filter Chips
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filters) { filter ->
                    val isSelected = filter == selectedFilter
                    val bgColor = if (isSelected) primaryColor else Color(0xFFE5E7EB)
                    val textColor = if (isSelected) Color.White else Color(0xFF374151)

                    Surface(
                        color = bgColor,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .height(32.dp)
                            .clickable { selectedFilter = filter }
                    ) {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(horizontal = 12.dp)) {
                            Text(filter, color = textColor, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Content with pull to refresh
            androidx.compose.material3.pulltorefresh.PullToRefreshBox(
                isRefreshing = isRefreshing,
                onRefresh = {
                    isRefreshing = true
                    viewModel.refresh()
                },
                modifier = Modifier.fillMaxSize()
            ) {
                when {
                    uiState.isLoading && !isRefreshing -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = primaryColor)
                        }
                    }
                    uiState.error != null -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    uiState.error ?: "Đã xảy ra lỗi",
                                    color = Color.Gray,
                                    fontSize = 14.sp
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Button(
                                    onClick = { viewModel.refresh() },
                                    colors = ButtonDefaults.buttonColors(containerColor = primaryColor)
                                ) {
                                    Text("Thử lại")
                                }
                            }
                        }
                    }
                    filteredOrders.isEmpty() -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "Không có đơn hàng nào",
                                color = Color.Gray,
                                fontSize = 14.sp
                            )
                        }
                    }
                    else -> {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(filteredOrders) { order ->
                                OrderItem(
                                    order = order,
                                    onClick = { onOrderClick(order.id) }
                                )
                            }
                            item {
                                Spacer(modifier = Modifier.height(16.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun OrderItem(order: OrderDto, onClick: () -> Unit) {
    val status = OrderHistoryViewModel.mapStatusToVietnamese(order.status)
    val date = OrderHistoryViewModel.formatDate(order.createdAt)
    val firstItem = order.items.firstOrNull()
    val productName = if (order.items.size > 1) {
        "${firstItem?.productName ?: "Sản phẩm"} và ${order.items.size - 1} sản phẩm khác"
    } else {
        firstItem?.productName ?: "Sản phẩm"
    }
    val imageUrl = firstItem?.imageUrl ?: ""

    Surface(
        color = Color.White,
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5E7EB)),
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "Đơn hàng #${order.id.take(8)}",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
                Text("Ngày đặt: $date", fontSize = 14.sp, color = Color.Gray)
                Text(productName, fontSize = 14.sp, color = Color.Gray, maxLines = 1)
                Text(
                    "Tổng cộng: ${formatCurrency(order.finalAmount.toLong())}đ",
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp
                )

                // Status Badge
                val (badgeColor, textColor) = when (order.status.uppercase()) {
                    "DELIVERED", "COMPLETED" -> Color(0xFFDCFCE7) to Color(0xFF166534)
                    "CONFIRMED", "PENDING" -> Color(0xFFFFEDD5) to Color(0xFF9A3412)
                    "SHIPPING" -> Color(0xFFDBEAFE) to Color(0xFF1E40AF)
                    "CANCELLED", "RETURNED", "FAILED" -> Color(0xFFFEE2E2) to Color(0xFF991B1B)
                    else -> Color.LightGray to Color.Black
                }

                Surface(
                    color = badgeColor,
                    shape = RoundedCornerShape(100.dp),
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Text(
                        status,
                        color = textColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.dp)
                    )
                }
            }
            Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = Color.Gray)
        }
    }
}

fun formatCurrency(amount: Long): String {
    return String.format("%,d", amount).replace(',', '.')
}
