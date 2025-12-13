package com.group1.pandqapplication.ui.checkout

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.group1.pandqapplication.shared.ui.theme.CheckoutBackgroundDark
import com.group1.pandqapplication.shared.ui.theme.CheckoutBackgroundLight
import com.group1.pandqapplication.shared.ui.theme.CheckoutBorderDark
import com.group1.pandqapplication.shared.ui.theme.CheckoutBorderLight
import com.group1.pandqapplication.shared.ui.theme.CheckoutPrimary
import com.group1.pandqapplication.shared.ui.theme.CheckoutSurfaceDark
import com.group1.pandqapplication.shared.ui.theme.CheckoutSurfaceLight
import com.group1.pandqapplication.shared.ui.theme.CheckoutTextDark
import com.group1.pandqapplication.shared.ui.theme.CheckoutTextLight
import com.group1.pandqapplication.shared.ui.theme.CheckoutTextSecondaryDark
import com.group1.pandqapplication.shared.ui.theme.CheckoutTextSecondaryLight

@Composable
fun CheckoutScreen(
    onBackClick: () -> Unit = {},
    onEditAddressClick: () -> Unit = {}
) {
    val isDarkTheme = false // Toggle for testing
    
    val backgroundColor = if (isDarkTheme) CheckoutBackgroundDark else CheckoutBackgroundLight
    val surfaceColor = if (isDarkTheme) CheckoutSurfaceDark else CheckoutSurfaceLight
    val textPrimary = if (isDarkTheme) CheckoutTextDark else CheckoutTextLight
    val textSecondary = if (isDarkTheme) CheckoutTextSecondaryDark else CheckoutTextSecondaryLight
    val borderColor = if (isDarkTheme) CheckoutBorderDark else CheckoutBorderLight
    val dotsInactive = if (isDarkTheme) Color(0xFF673B32) else Color(0xFFD6D3D1)

    Scaffold(
        containerColor = backgroundColor,
        topBar = {
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
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = textPrimary
                    )
                }
                Text(
                    text = "Thanh toán",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = textPrimary,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .background(surfaceColor.copy(alpha = 0.8f))
                    .padding(16.dp)
            ) {
                Button(
                    onClick = { /* Confirm Order */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = CheckoutPrimary),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Xác nhận - 2.480.000₫",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Page Indicators
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier
                    .size(width = 32.dp, height = 8.dp)
                    .background(dotsInactive, CircleShape))
                Spacer(modifier = Modifier.width(12.dp))
                Box(modifier = Modifier
                    .size(width = 32.dp, height = 8.dp)
                    .background(CheckoutPrimary, CircleShape))
                Spacer(modifier = Modifier.width(12.dp))
                Box(modifier = Modifier
                    .size(width = 32.dp, height = 8.dp)
                    .background(dotsInactive, CircleShape))
            }
            
            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Delivery Address Section
                Column {
                    Text(
                        text = "Địa chỉ Giao hàng",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = textPrimary,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(surfaceColor, RoundedCornerShape(12.dp))
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(CheckoutPrimary.copy(alpha = 0.2f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = CheckoutPrimary
                            )
                        }
                        
                        Spacer(modifier = Modifier.width(16.dp))
                        
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "John Doe",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                color = textPrimary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "(+84) 987 654 321, 123 Đường ABC, Phường X, Quận Y, TP. Z",
                                fontSize = 14.sp,
                                color = textSecondary,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        
                        Spacer(modifier = Modifier.width(8.dp))
                        
                        Text(
                            text = "Thay đổi",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = CheckoutPrimary,
                            modifier = Modifier.clickable { onEditAddressClick() }
                        )
                    }
                }

                // Payment Method Section
                Column {
                    Text(
                        text = "Phương thức Thanh toán",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = textPrimary,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        PaymentOption(
                            text = "Thanh toán bằng ZaloPay",
                            isSelected = true,
                            surfaceColor = surfaceColor,
                            borderColor = CheckoutPrimary, // Active border
                            textPrimary = textPrimary
                        )
                        PaymentOption(
                            text = "Thanh toán bằng SoPay",
                            isSelected = false,
                            surfaceColor = surfaceColor,
                            borderColor = borderColor,
                            textPrimary = textPrimary
                        )
                    }
                }

                // Discount Code Section
                Column {
                    Text(
                        text = "Mã giảm giá",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = textPrimary,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                       TextField(
                           value = "",
                           onValueChange = {},
                           placeholder = { Text("Nhập mã giảm giá", color = Color.Gray) },
                           modifier = Modifier
                               .weight(1f)
                               .border(1.dp, borderColor, RoundedCornerShape(12.dp))
                               .clip(RoundedCornerShape(12.dp)),
                           colors = TextFieldDefaults.colors(
                               focusedContainerColor = surfaceColor,
                               unfocusedContainerColor = surfaceColor,
                               focusedIndicatorColor = Color.Transparent,
                               unfocusedIndicatorColor = Color.Transparent,
                               disabledIndicatorColor = Color.Transparent // Hide underline
                           )
                       )
                       
                       Spacer(modifier = Modifier.width(8.dp))
                       
                       Box(
                           modifier = Modifier
                               .background(CheckoutPrimary.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                               .padding(horizontal = 20.dp, vertical = 16.dp)
                       ) {
                           Text(
                               text = "Áp dụng",
                               fontSize = 14.sp,
                               fontWeight = FontWeight.SemiBold,
                               color = CheckoutPrimary
                           )
                       }
                    }
                }

                // Order Summary Section
                Column(modifier = Modifier.padding(bottom = 100.dp)) {
                    Text(
                        text = "Tóm tắt Đơn hàng",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = textPrimary,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(surfaceColor, RoundedCornerShape(12.dp))
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        SummaryRow("Tạm tính", "2.500.000₫", textSecondary, textPrimary)
                        SummaryRow("Phí vận chuyển", "30.000₫", textSecondary, textPrimary)
                        SummaryRow("Giảm giá", "-50.000₫", textSecondary, CheckoutPrimary)
                        
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 0.dp),
                            thickness = 1.dp,
                            color = borderColor
                        )
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Tổng cộng",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = textPrimary
                            )
                            Text(
                                text = "2.480.000₫",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = textPrimary
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PaymentOption(
    text: String,
    isSelected: Boolean,
    surfaceColor: Color,
    borderColor: Color,
    textPrimary: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = if (isSelected) 1.dp else 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(12.dp)
            )
            .background(surfaceColor, RoundedCornerShape(12.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Compose RadioButton doesn't support easy custom "dot" drawing identical to HTML without custom Canvas, 
        // but default RadioButton is close enough.
        RadioButton(
            selected = isSelected,
            onClick = { },
            colors = RadioButtonDefaults.colors(
                selectedColor = CheckoutPrimary,
                unselectedColor = Color.Gray
            ),
            modifier = Modifier.size(20.dp)
        )
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = textPrimary
        )
    }
}

@Composable
fun SummaryRow(label: String, value: String, labelColor: Color, valueColor: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = labelColor
        )
        Text(
            text = value,
            fontSize = 14.sp,
            color = valueColor,
            fontWeight = if (valueColor == CheckoutPrimary) FontWeight.Normal else FontWeight.Normal
        )
    }
}

@Preview
@Composable
fun PreviewCheckoutScreen() {
    CheckoutScreen()
}
