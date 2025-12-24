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
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.group1.pandqapplication.shared.data.remote.dto.OrderItemDto
import java.text.NumberFormat
import java.util.Locale

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
    onBackClick: () -> Unit,
    onCheckoutClick: (orderId: String) -> Unit,
    userId: String = "",
    viewModel: CartViewModel = hiltViewModel()
) {
    val primaryColor = Color(0xFFec3713)
    val backgroundColor = Color(0xFFF8F6F6)
    val uiState by viewModel.uiState.collectAsState()

    // Load cart when screen appears
    LaunchedEffect(userId) {
        if (userId.isNotEmpty()) {
            viewModel.loadCart(userId)
        }
    }

    val totalPrice = uiState.cart?.items?.sumOf { it.price * it.quantity } ?: 0.0

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
            if (!uiState.isLoading && (uiState.cart?.items?.isNotEmpty() == true)) {
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
                            onClick = { 
                                uiState.cart?.id?.let { orderId ->
                                    onCheckoutClick(orderId)
                                }
                            },
                            enabled = uiState.cart?.id != null,
                            modifier = Modifier.fillMaxWidth().height(50.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = primaryColor)
                        ) {
                            Text("Tiến hành Thanh toán", fontWeight = FontWeight.Bold, fontSize = 16.sp)
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
                    CircularProgressIndicator(color = primaryColor)
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
                        Text("Lỗi: ${uiState.error}", color = Color.Red)
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { if (userId.isNotEmpty()) viewModel.loadCart(userId) }) {
                            Text("Thử lại")
                        }
                    }
                }
            }
            uiState.cart?.items?.isEmpty() == true -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Giỏ hàng trống", fontSize = 16.sp, color = Color.Gray)
                }
            }
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentPadding = PaddingValues(top = 8.dp)
                ) {
                    items(uiState.cart?.items ?: emptyList()) { item ->
                        CartItemRow(
                            item = item,
                            primaryColor = primaryColor,
                            onIncrease = { 
                                viewModel.addToCart(userId, item.productId, 1)
                            },
                            onDecrease = {
                                viewModel.decreaseQuantity(userId, item.productId)
                            },
                            onDelete = {
                                viewModel.removeFromCart(userId, item.productId)
                            }
                        )
                        HorizontalDivider(color = Color(0xFFE5E7EB), modifier = Modifier.padding(horizontal = 16.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun CartItemRow(
    item: OrderItemDto,
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
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Product image
            if (!item.imageUrl.isNullOrEmpty()) {
                AsyncImage(
                    model = item.imageUrl,
                    contentDescription = item.productName,
                    modifier = Modifier
                        .size(70.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(70.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.LightGray),
                    contentAlignment = Alignment.Center
                ) {
                    Text("IMG", color = Color.Gray, fontWeight = FontWeight.Bold)
                }
            }
            
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(item.productName, fontWeight = FontWeight.Medium, fontSize = 16.sp)
                Text("${formatCurrency(item.price)}đ", color = Color(0xFFec3713), fontSize = 14.sp, fontWeight = FontWeight.Bold)
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

fun formatCurrency(amount: Double): String {
    val longAmount = amount.toLong()
    return String.format("%,d", longAmount).replace(',', '.')
}

@Preview
@Composable
fun CartScreenPreview() {
    CartScreen({},{})
}
