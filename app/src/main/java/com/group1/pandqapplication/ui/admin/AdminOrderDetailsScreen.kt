package com.group1.pandqapplication.ui.admin

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
    onBackClick: () -> Unit = {}
) {
    val isDarkTheme = false
    
    val backgroundColor = if (isDarkTheme) AdminBackgroundDarkVariant else AdminBackgroundLight
    val cardColor = if (isDarkTheme) AdminCardDarkVariant else AdminCardLight
    val textPrimary = if (isDarkTheme) AdminTextPrimaryDark else AdminTextPrimaryLight
    val textSecondary = if (isDarkTheme) AdminTextSecondaryDark else AdminTextSecondaryLight
    val borderColor = if (isDarkTheme) Color(0xFF374151) else Color(0xFFE5E7EB)

    Scaffold(
        containerColor = backgroundColor,
        topBar = {
            Column {
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
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = "Back",
                            tint = AdminPrimary
                        )
                    }
                    Text(
                        text = "Chi tiết đơn hàng",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = textPrimary,
                        modifier = Modifier.align(Alignment.Center)
                    )
                    IconButton(
                        onClick = { /* More */ },
                        modifier = Modifier.align(Alignment.CenterEnd)
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreHoriz,
                            contentDescription = "More",
                            tint = AdminPrimary
                        )
                    }
                }
                HorizontalDivider(color = borderColor)
            }
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(cardColor.copy(alpha = 0.9f))
                    .padding(16.dp)
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(
                        onClick = { /* Assign Shipper */ },
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
                        onClick = { /* Update Status */ },
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
    ) { paddingValues ->
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
                            text = "#DH00123",
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
                                text = "Chờ xử lý",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = AdminStatusPending
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "15/08/2023, 10:30 AM",
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
                    
                    // Product 1
                    ProductRow(
                        name = "Laptop Pro Model X",
                        quantity = 2,
                        price = "15.000.000₫",
                        total = "30.000.000₫",
                        imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuANe5pq2gG3dqMgB6y6KtybJLL6ndR4LQltHxzczWbDBkVa3NkN1ADisq7OK_cSQNHyWiK2KcslPGakupHIdekGOz8NTKrQOq6KTec06nptVBhiOuJ-0OLUpahrIjaiCxeNcBf_4wyFuLsmXMaIesWgvfSzY1-CfAc4oncWPxd3-DlGLkcplcOvIBcogS5F7ufSht2ul_e-I0wt8gnHIPgVaVuZAxiGkGjZ1aKjSKhK88WEIcT9QQrSvkau6hDHZB68RokCVgxqHpg",
                        textPrimary = textPrimary,
                        textSecondary = textSecondary
                    )
                    
                    HorizontalDivider(color = borderColor, modifier = Modifier.padding(vertical = 16.dp))
                    
                    // Product 2
                    ProductRow(
                        name = "Chuột không dây Z-Series",
                        quantity = 1,
                        price = "850.000₫",
                        total = "850.000₫",
                        imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuCr8AFKx4ftW33DnHQR66yfpQ0BNtaiALF4ITkoXM20Gvp4CaTm0KL8sf_uHsn5l8UD0_ezQSWAz0x_I7qHz6j00jnXiI1q1ACbreifn0wN1Te7dX1uXUR1WK-Fyxh9ALhvlF9apbQoqgQhadcooXDPrgXgl5UCB3rfJ8md6Kv4AGjEXrc57wj106z4uadMekusY_8kmTqqeLdM0Nz0nM9mN5wcu4UZF9DQcOeb2_MLOhmTatRgRb-cWyRXYye0OBunl6aeSBokakE",
                        textPrimary = textPrimary,
                        textSecondary = textSecondary
                    )
                }
            }
            
            // Summary Card
            item {
                DetailCard(cardColor = cardColor) {
                    Text("Tóm tắt đơn hàng", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = textPrimary)
                    Spacer(modifier = Modifier.height(16.dp))
                    SummaryRow("Tạm tính", "30.850.000₫", textPrimary, textSecondary)
                    SummaryRow("Phí vận chuyển", "50.000₫", textPrimary, textSecondary)
                    SummaryRow("Giảm giá", "- 100.000₫", textPrimary, textSecondary)
                    HorizontalDivider(color = borderColor, modifier = Modifier.padding(vertical = 16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Tổng cộng", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                        Text("30.800.000₫", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = textPrimary)
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
                             Text("Nguyễn Văn A", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = textPrimary)
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
                             Text("0987 654 321", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = textPrimary)
                        }
                        IconButton(
                            onClick = {},
                            modifier = Modifier
                                .size(36.dp)
                                .background(Color.Transparent) // HTML has transparent bg but primary icon
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
                             Text("123 Đường ABC, Phường XYZ, Quận 1, TP. Hồ Chí Minh", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = textPrimary)
                        }
                        IconButton(onClick = {}) {
                           Icon(Icons.Default.Map, contentDescription = null, tint = AdminPrimary) 
                        }
                    }
                     Spacer(modifier = Modifier.height(12.dp))
                     
                     // Note
                    Column {
                         Text("Ghi chú", fontSize = 14.sp, color = textSecondary)
                         Text("Giao hàng trong giờ hành chính.", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = textPrimary)
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
                         Text("Thanh toán khi nhận hàng (COD)", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = textPrimary)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                         Text("Shipper", fontSize = 14.sp, color = textSecondary)
                         Text("Chưa gán", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = textSecondary)
                    }
                 }
            }
            
            item {
                Spacer(modifier = Modifier.height(60.dp))
            }
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

@Preview
@Composable
fun PreviewAdminOrderDetailsScreen() {
    AdminOrderDetailsScreen()
}
