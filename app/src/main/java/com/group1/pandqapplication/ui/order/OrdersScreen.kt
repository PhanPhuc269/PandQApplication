package com.group1.pandqapplication.ui.order

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.group1.pandqapplication.shared.data.remote.dto.OrderHistoryDto
import java.time.format.DateTimeFormatter

data class Order(
    val id: String,
    val date: String,
    val productName: String,
    val total: Long,
    val status: String,
    val imageUrl: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersScreen(
    onOrderClick: (OrderHistoryDto) -> Unit = {},
    viewModel: OrderHistoryViewModel = hiltViewModel(),
    userId: String = "" // Will be passed from parent
) {
    val backgroundColor = Color(0xFFF8F6F6)
    val primaryColor = Color(0xFFec3713)
    val uiState by viewModel.uiState.collectAsState()

    // Initialize with userId on first composition
    LaunchedEffect(userId) {
        if (userId.isNotBlank()) {
            viewModel.setUserId(userId)
        }
    }

    // Dummy mock orders for preview/no user scenario
    val mockOrders = listOf(
        Order(
            id = "#A1B2-C3D4",
            date = "15/10/2023",
            productName = "iPhone 15 Pro và 2 sản phẩm khác",
            total = 35500000,
            status = "Đã giao",
            imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuDaEFGuCFDuN96fP2nbTbjNE0L30fmimZqD4uXjQ2os6ENCBJgj1n8cYdiqI0qJVpBQHXUkh3mZuQ4kOVgu4oqq0ka4KGBtnaGEWcbYzmQELKokrU4nwH-KSzaSw8nofYNV8rdnChc-Ugpxxv1SUpBvt2fualgsOIc5Xc9CKuBgobDH9e6xS8Vo5hNURUQiuHbxDwQUgp6b42tWxJtIZODXY-zBIk63zIW9sjsG2nYVUofzmKbig2KwtFSypsYTxMiOjgCShH0OYpY"
        ),
        Order(
            id = "#E5F6-G7H8",
            date = "22/10/2023",
            productName = "Macbook Air M2",
            total = 28990000,
            status = "Đang xử lý",
            imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuAvhSvxQ4uV1ao9CCiFMaPCEipzU6rlTdsN2t8Ck7UVQWtEFvBmxV05DrfqxCL2e_pVzBYFveoOmANGvZnB2FilM4J-CD8n4PnOUBcs9CLdlzCCmcltSmIazKJ_7U1IcJpjQzfqK33oWinceYSwJW4idWZAXnElqI5hkRmdIaY5y0GAxAhZ0_J5X9Owgju_-4ykhfVbD4QpnhfMrQsP0BgS7Nfc6BbKPxrBgcgPm0ZNiym6P3j8PspatDvYjfmhGWKU3ysVirzvvQE"
        ),
        Order(
            id = "#I9J0-K1L2",
            date = "05/09/2023",
            productName = "AirPods Pro (2nd Gen)",
            total = 6190000,
            status = "Đã huỷ",
            imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuBGhcDYe_5RqKmuqqk4O5UmE3ogUKFufGMCLyaiYhgeKwhpZJI5G4q1kSWtwem9NOqZ_VqBYzeTz_HnKFNXYVz_YXv-_nkdjl0jO0ssDFn6iXO_PbEGzn0M59mMV-etGCrVKnpi4bAXKmdUh_d1rGDupjn-bVdnoAzrXDCa5ECjiEjZehL46CBrzW-9uSmy432ayPJUQKXN07tmuN-ishfwVjrSYKMsJH-H2kBd9YExVAs2xYlUTFc7czRoyFFUvMjMmY0x4RHl3Us"
        )
    )

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
                   modifier = Modifier.padding(horizontal = 12.dp)
                ) {
                    Icon(Icons.Filled.Search, contentDescription = null, tint = Color.Gray)
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(modifier = Modifier.weight(1f)) {
                        TextField(
                            value = uiState.searchQuery,
                            onValueChange = { viewModel.onSearchQueryChange(it) },
                            placeholder = { 
                                Text(
                                    "Tìm theo mã đơn hàng, tên sản phẩm...", 
                                    color = Color.Gray,
                                    fontSize = 13.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                ) 
                            },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                            ),
                            shape = RoundedCornerShape(8.dp),
                            singleLine = true,
                            modifier = Modifier.fillMaxSize().padding(0.dp),
                            textStyle = TextStyle(fontSize = 14.sp)
                        )
                    }
                    if (uiState.searchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.onClearSearch() }, modifier = Modifier.size(24.dp)) {
                            Icon(Icons.Filled.ArrowBackIosNew, contentDescription = "Clear", tint = Color.Gray, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }

            // Filter Chips with Status Categories
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(OrderStatus.entries) { status ->
                    val isSelected = status == uiState.selectedStatus
                    val bgColor = if (isSelected) primaryColor else Color(0xFFE5E7EB)
                    val textColor = if (isSelected) Color.White else Color(0xFF374151)
                    
                    Surface(
                        color = bgColor,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .height(32.dp)
                            .clickable { viewModel.onStatusFilterChange(status) }
                    ) {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(horizontal = 12.dp)) {
                            Text(status.displayName, color = textColor, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Order List or Empty/Error
            if (uiState.isLoading && uiState.orders.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (uiState.isLoading && uiState.orders.isEmpty()) {
                // Skeleton Loading UI
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(vertical = 12.dp)
                ) {
                    items(5) {
                        OrderHistorySkeletonItem()
                    }
                }
            } else if (uiState.error != null && uiState.orders.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(uiState.error ?: "Lỗi tải dữ liệu", color = Color.Red)
                    }
                }
            } else if (uiState.orders.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Chưa có đơn hàng nào", color = Color.Gray)
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiState.orders) { order ->
                        OrderHistoryItem(order = order, onClick = { onOrderClick(order) })
                    }
                    
                    // Load more button if available
                    if (uiState.orders.size < uiState.totalItems) {
                        item {
                            Button(
                                onClick = { viewModel.loadNextPage() },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 16.dp),
                                enabled = !uiState.isLoading
                            ) {
                                if (uiState.isLoading) {
                                    CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                                } else {
                                    Text("Tải thêm")
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
fun OrderHistoryItem(order: OrderHistoryDto, onClick: () -> Unit) {
    val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val formattedDate = order.createdAt.format(dateFormatter)
    
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
            // Product Image
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                if (order.items.isNotEmpty() && !order.items.first().imageUrl.isNullOrEmpty()) {
                    AsyncImage(
                        model = order.items.first().imageUrl,
                        contentDescription = order.items.first().productName,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(8.dp))
                    )
                } else {
                    Text(
                        order.items.firstOrNull()?.productName?.take(1)?.uppercase() ?: "📦",
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text("Đơn hàng ${order.id.take(8)}", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                Text(order.items.mapNotNull { it.productName }.take(1).joinToString(", "), fontSize = 14.sp, color = Color.Gray, maxLines = 1)
                Text("Tổng: ${formatCurrency(order.finalAmount.toLong())}đ", fontWeight = FontWeight.Medium, fontSize = 14.sp)
                
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val (badgeColor, textColor) = when (order.status) {
                        "DELIVERED" -> Color(0xFFDCFCE7) to Color(0xFF166534)
                        "PENDING", "CONFIRMED" -> Color(0xFFFFEDD5) to Color(0xFF9A3412)
                        "CANCELLED" -> Color(0xFFFEE2E2) to Color(0xFF991B1B)
                        else -> Color.LightGray to Color.Black
                    }
                    
                    Surface(
                        color = badgeColor,
                        shape = RoundedCornerShape(100.dp)
                    ) {
                        Text(
                            order.status,
                            color = textColor,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.dp)
                        )
                    }
                    
                    Text(
                        formattedDate,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }
            Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = Color.Gray)
        }
    }
}

@Composable
fun OrderHistorySkeletonItem() {
    Surface(
        color = Color.White,
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5E7EB)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .shimmerEffect(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Image skeleton
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray)
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Text skeleton
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .height(16.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color.LightGray)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(14.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color.LightGray)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .height(14.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color.LightGray)
                )
            }
            
            Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = Color.Gray)
        }
    }
}

fun formatCurrency(amount: Long): String {
    return String.format("%,d", amount).replace(',', '.')
}

fun Modifier.shimmerEffect(): Modifier {
    return this.background(
        brush = androidx.compose.ui.graphics.Brush.linearGradient(
            colors = listOf(
                Color(0xFFF0F0F0),
                Color(0xFFE0E0E0),
                Color(0xFFF0F0F0)
            ),
            start = androidx.compose.ui.geometry.Offset(0f, 0f),
            end = androidx.compose.ui.geometry.Offset(100f, 100f)
        )
    )
}

@Preview
@Composable
fun OrdersScreenPreview() {
    OrdersScreen()
}
