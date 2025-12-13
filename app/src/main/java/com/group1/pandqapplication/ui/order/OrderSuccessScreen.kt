package com.group1.pandqapplication.ui.order

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.group1.pandqapplication.shared.ui.theme.CheckoutBackgroundDark
import com.group1.pandqapplication.shared.ui.theme.CheckoutBackgroundLight
import com.group1.pandqapplication.shared.ui.theme.ProductPrimary
import com.group1.pandqapplication.shared.ui.theme.SuccessGreen
import com.group1.pandqapplication.shared.ui.theme.SuccessTextPrimaryDark
import com.group1.pandqapplication.shared.ui.theme.SuccessTextPrimaryLight
import com.group1.pandqapplication.shared.ui.theme.SuccessTextSecondaryDark
import com.group1.pandqapplication.shared.ui.theme.SuccessTextSecondaryLight

@Composable
fun OrderSuccessScreen(
    onCloseClick: () -> Unit = {},
    onTrackOrderClick: () -> Unit = {},
    onContinueShoppingClick: () -> Unit = {}
) {
    val isDarkTheme = false
    
    val backgroundColor = if (isDarkTheme) CheckoutBackgroundDark else CheckoutBackgroundLight
    val textPrimary = if (isDarkTheme) SuccessTextPrimaryDark else SuccessTextPrimaryLight
    val textSecondary = if (isDarkTheme) SuccessTextSecondaryDark else SuccessTextSecondaryLight
    val surfaceColor = if (isDarkTheme) Color.White.copy(alpha = 0.05f) else Color.Black.copy(alpha = 0.05f) // approximated for detail box

    Scaffold(
        containerColor = backgroundColor,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = "Close",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = textSecondary,
                    modifier = Modifier.clickable { onCloseClick() }
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Spacer(modifier = Modifier.height(32.dp))
                
                // Success Icon
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(SuccessGreen.copy(alpha = 0.2f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = SuccessGreen,
                        modifier = Modifier.size(48.dp)
                    )
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Text(
                    text = "Đặt hàng thành công!",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = textPrimary,
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "Cảm ơn bạn đã mua sắm tại ElectroStore.",
                    fontSize = 16.sp,
                    color = textSecondary,
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Image
                AsyncImage(
                    model = "https://lh3.googleusercontent.com/aida-public/AB6AXuCJCtkSVDYlIK0roebqFK_dCMzcctgNbnCbfwvSMB2hIwYj3hyFzshngQwMjMhk0fBMTTXZaUzggEsyndBDjLi3N7RNDmLouODyiT_8CZAq-dpa0sCBdzbz9njIq1k_GSPdUDAdLDGcZc6aXDZjeKFHhgOQvoMDrBmfb03MLsSuVhfwYJn57NDqQEjxi64_QUC4MLSq_K09yl83CWqcNrGOCUiM0Q7IrIcBN7Oo5J7Lz-ctMNKxo6VuTSBwaUb8x4nPkyYk5YQdaoo",
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .aspectRatio(4f / 3f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.Gray.copy(alpha = 0.1f)),
                    contentScale = ContentScale.Crop
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Details Box
                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(surfaceColor)
                        .padding(16.dp)
                ) {
                   Row(
                       modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
                       horizontalArrangement = Arrangement.SpaceBetween
                   ) {
                       Text("Mã đơn hàng", fontSize = 14.sp, color = textSecondary)
                       Text("#ABC12345XYZ", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = textPrimary)
                   }
                   HorizontalDivider(color = Color.White.copy(alpha = 0.1f)) // Using white alpha as per dark mode HTML hint, serves both reasonably
                   Row(
                       modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
                       horizontalArrangement = Arrangement.SpaceBetween
                   ) {
                       Text("Giao hàng dự kiến", fontSize = 14.sp, color = textSecondary)
                       Text("Thứ Ba, 28/11/2023", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = textPrimary)
                   }
                   HorizontalDivider(color = Color.White.copy(alpha = 0.1f))
                   Row(
                       modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
                       horizontalArrangement = Arrangement.SpaceBetween
                   ) {
                       Text("Tổng cộng", fontSize = 14.sp, color = textSecondary)
                       Text("15.990.000₫", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = ProductPrimary)
                   }
                }
            }
            
            // Buttons
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onTrackOrderClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ProductPrimary,
                        contentColor = Color.White
                    )
                ) {
                    Text("Theo dõi đơn hàng", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
                
                Button(
                    onClick = onContinueShoppingClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = ProductPrimary
                    )
                ) {
                    Text("Tiếp tục mua sắm", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewOrderSuccessScreen() {
    OrderSuccessScreen()
}
