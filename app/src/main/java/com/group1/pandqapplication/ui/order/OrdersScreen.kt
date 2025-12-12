package com.group1.pandqapplication.ui.order

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import coil.compose.AsyncImage

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
    onOrderClick: () -> Unit = {}
) {
    val backgroundColor = Color(0xFFF8F6F6)
    val primaryColor = Color(0xFFec3713)

    // Mock Data
    val allOrders = listOf(
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

    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("Tất cả") }
    
    val filters = listOf("Tất cả", "Đã giao", "Đang xử lý", "Đã huỷ")

    val filteredOrders = allOrders.filter { order ->
        val matchesSearch = order.id.contains(searchQuery, ignoreCase = true) || order.productName.contains(searchQuery, ignoreCase = true)
        val matchesFilter = selectedFilter == "Tất cả" || order.status == selectedFilter
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
                    Spacer(modifier = Modifier.size(48.dp)) // Balance the Back button size
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
//                        if (searchQuery.isEmpty()) {
//                            Text("Tìm theo mã đơn hàng, tên sản phẩm...", color = Color.Gray, modifier = Modifier.fillMaxSize())
//                        }
                        TextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
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
                            // Remove default padding to prevent text clipping in small height
                            shape = RoundedCornerShape(8.dp),
                            singleLine = true,
                            modifier = Modifier.fillMaxSize().padding(0.dp),
                            textStyle = TextStyle(fontSize = 14.sp)
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

            // Order List
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                filteredOrders.forEach { order ->
                    OrderItem(order = order, onClick = onOrderClick)
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun OrderItem(order: Order, onClick: () -> Unit) {
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
                model = order.imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text("Đơn hàng ${order.id}", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                Text("Ngày đặt: ${order.date}", fontSize = 14.sp, color = Color.Gray)
                Text(order.productName, fontSize = 14.sp, color = Color.Gray, maxLines = 1)
                Text("Tổng cộng: ${formatCurrency(order.total)}đ", fontWeight = FontWeight.Medium, fontSize = 14.sp)
                
                // Status Badge
                val (badgeColor, textColor) = when (order.status) {
                    "Đã giao" -> Color(0xFFDCFCE7) to Color(0xFF166534) // Green-100 to Green-800
                    "Đang xử lý" -> Color(0xFFFFEDD5) to Color(0xFF9A3412) // Orange-100 to Orange-800
                    "Đã huỷ" -> Color(0xFFFEE2E2) to Color(0xFF991B1B) // Red-100 to Red-800
                    else -> Color.LightGray to Color.Black
                }
                
                Surface(
                    color = badgeColor,
                    shape = RoundedCornerShape(100.dp),
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Text(
                        order.status,
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

@Preview
@Composable
fun OrdersScreenPreview() {
    OrdersScreen()
}
