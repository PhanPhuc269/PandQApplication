package com.group1.pandqapplication.ui.cart

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

data class CartItem(
    val id: String,
    val name: String,
    val price: Long,
    val imageUrl: String,
    val attributes: String,
    var quantity: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    onBackClick: () -> Unit
) {
    val primaryColor = Color(0xFFec3713)
    val backgroundColor = Color(0xFFF8F6F6)

    // Mock Data
    val initialItems = listOf(
        CartItem(
            id = "1",
            name = "iPhone 15 Pro Max",
            price = 28990000,
            imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuDdODobtQd_W5spuyIGTgg_cqsIU72MtAASv72x6Mopjd9AznJ1IEQAOeLiUIV8h-y-RBI7tZCKXrZTKoOO3RT1UyKk_U10eott4lXGX8BNnEEWXPbhWamoYLDhScmST0BA1MXWO_PMFuYux5Ya5nacwU8kZqlWYGItZgvxpGypJa65k8o8kF9h5svoIYSOzIM5YXLwiGbIRBIVSApJFsAe7Z5tar16dwVR1PXDSuceKbRVwiiDA166KdQEVhh4-HFSjwisIclvdVY",
            attributes = "Màu: Titan Tự nhiên, Dung lượng: 256GB",
            quantity = 1
        ),
        CartItem(
            id = "2",
            name = "MacBook Pro 14 inch M3",
            price = 45490000,
            imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuAcDejJUzHOU86Zp2P-V-MWK-67Ft3fu9kMGSqUzSvUJrk6NzDy02JVMzNYEMWhYTqL2qpWkc3pGR4amaTQ630O0OxLZZf5j9EYMaf8pHbueu7Hq3lZyPRN1WWsajCcbuPxS3MCag_2wLO-HXfSUMd5gHcIO6cfyUHp5CzcESaFi3pzYQSEWBfKBLlimDfg4v7_DS7tKBYrrkMdJbAoziQbyPZVNH2PudZu3JV2As36MbL6ZS2DNlryf7NXXAJEJMmFyRWSZAb1KJs",
            attributes = "Màu: Bạc, RAM: 16GB",
            quantity = 1
        )
    )

    var cartItems by remember { mutableStateOf(initialItems) }
    
    val totalPrice = cartItems.sumOf { it.price * it.quantity }

    Scaffold(
        containerColor = backgroundColor,
        topBar = {
            TopAppBar(
                title = { 
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                         Text("Giỏ hàng", fontWeight = FontWeight.Bold, fontSize = 18.sp) 
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Filled.ArrowBackIosNew, contentDescription = "Back")
                    }
                },
                actions = {
                    // Spacer to balance the title centering
                    Spacer(modifier = Modifier.width(48.dp))
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = backgroundColor)
            )
        },
        bottomBar = {
            Surface(
                color = backgroundColor,
                shadowElevation = 16.dp,
                modifier = Modifier.shadow(16.dp)
            ) {
               Column(
                   modifier = Modifier
                       .fillMaxWidth()
                       .background(backgroundColor)
                       .padding(16.dp)
               ) {
                   Row(
                       modifier = Modifier.fillMaxWidth(),
                       horizontalArrangement = Arrangement.SpaceBetween
                   ) {
                       Text("Tạm tính", color = Color.Gray, fontSize = 14.sp)
                       Text("${formatCurrency(totalPrice)}đ", fontWeight = FontWeight.Medium, fontSize = 14.sp)
                   }
                   Text(
                       "Phí vận chuyển sẽ được tính ở bước tiếp theo.", 
                       color = Color.Gray, 
                       fontSize = 12.sp,
                       modifier = Modifier.padding(top = 4.dp)
                   )
                   Spacer(modifier = Modifier.height(8.dp))
                   Row(
                       modifier = Modifier.fillMaxWidth(),
                       horizontalArrangement = Arrangement.SpaceBetween
                   ) {
                       Text("Thành tiền", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                       Text("${formatCurrency(totalPrice)}đ", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                   }
                   Spacer(modifier = Modifier.height(16.dp))
                   Button(
                       onClick = { /* TODO: Checkout */ },
                       modifier = Modifier.fillMaxWidth().height(50.dp),
                       shape = RoundedCornerShape(8.dp),
                       colors = ButtonDefaults.buttonColors(containerColor = primaryColor)
                   ) {
                       Text("Tiến hành Thanh toán", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                   }
               } 
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(top = 8.dp)
        ) {
            items(cartItems) { item ->
                CartItemRow(
                    item = item,
                    primaryColor = primaryColor,
                    onIncrease = { 
                        cartItems = cartItems.map { if (it.id == item.id) it.copy(quantity = it.quantity + 1) else it }
                    },
                    onDecrease = {
                        if (item.quantity > 1) {
                            cartItems = cartItems.map { if (it.id == item.id) it.copy(quantity = it.quantity - 1) else it }
                        }
                    },
                    onDelete = {
                        cartItems = cartItems.filter { it.id != item.id }
                    }
                )
                HorizontalDivider(color = Color(0xFFE5E7EB), modifier = Modifier.padding(horizontal = 16.dp))
            }
        }
    }
}

@Composable
fun CartItemRow(
    item: CartItem,
    primaryColor: Color,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Product Info
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AsyncImage(
                model = item.imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray)
            )
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(item.name, fontWeight = FontWeight.Medium, fontSize = 16.sp)
                Text("${formatCurrency(item.price)}đ", color = Color(0xFFc99b92), fontSize = 14.sp) // Color approx from image
                Text(
                    item.attributes, 
                    color = Color.Gray, 
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )
            }
        }
        
        // Actions
        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(start = 8.dp)
        ) {
            // Quantity Control
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(primaryColor.copy(alpha = 0.1f))
                        .clickable(onClick = onDecrease),
                    contentAlignment = Alignment.Center
                ) {
                    Text("-", color = primaryColor, fontWeight = FontWeight.Bold)
                }
                Text(item.quantity.toString(), fontWeight = FontWeight.Medium, modifier = Modifier.width(16.dp), textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                Box(
                     modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(primaryColor.copy(alpha = 0.1f))
                        .clickable(onClick = onIncrease),
                    contentAlignment = Alignment.Center
                ) {
                    Text("+", color = primaryColor, fontWeight = FontWeight.Bold)
                }
            }
            
            // Delete
            IconButton(onClick = onDelete, modifier = Modifier.size(24.dp)) {
                Icon(Icons.Filled.Delete, contentDescription = "Delete", tint = Color.Red, modifier = Modifier.size(20.dp))
            }
        }
    }
}

fun formatCurrency(amount: Long): String {
    return String.format("%,d", amount).replace(',', '.')
}

@Preview
@Composable
fun CartScreenPreview() {
    CartScreen(onBackClick = {})
}
