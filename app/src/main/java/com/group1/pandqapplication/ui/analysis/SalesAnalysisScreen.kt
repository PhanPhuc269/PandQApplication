package com.group1.pandqapplication.ui.analysis

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Share
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.group1.pandqapplication.ui.theme.AnalysisCardDark
import com.group1.pandqapplication.ui.theme.AnalysisCardLight
import com.group1.pandqapplication.ui.theme.AnalysisGraphBlue
import com.group1.pandqapplication.ui.theme.AnalysisGraphPurple
import com.group1.pandqapplication.ui.theme.AnalysisGraphRed
import com.group1.pandqapplication.ui.theme.AnalysisNegative
import com.group1.pandqapplication.ui.theme.AnalysisPositive
import com.group1.pandqapplication.ui.theme.AnalysisTextPrimaryDark
import com.group1.pandqapplication.ui.theme.AnalysisTextPrimaryLight
import com.group1.pandqapplication.ui.theme.AnalysisTextSecondaryDark
import com.group1.pandqapplication.ui.theme.AnalysisTextSecondaryLight
import com.group1.pandqapplication.ui.theme.CheckoutBackgroundDark
import com.group1.pandqapplication.ui.theme.CheckoutBackgroundLight
import com.group1.pandqapplication.ui.theme.ProductPrimary

@Composable
fun SalesAnalysisScreen(
    onBackClick: () -> Unit = {}
) {
    val isDarkTheme = false
    
    val backgroundColor = if (isDarkTheme) CheckoutBackgroundDark else CheckoutBackgroundLight
    val cardColor = if (isDarkTheme) AnalysisCardDark else AnalysisCardLight
    val textPrimary = if (isDarkTheme) AnalysisTextPrimaryDark else AnalysisTextPrimaryLight
    val textSecondary = if (isDarkTheme) AnalysisTextSecondaryDark else AnalysisTextSecondaryLight

    Scaffold(
        containerColor = backgroundColor,
        topBar = {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(backgroundColor.copy(alpha = 0.8f))
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
                        text = "Phân tích bán hàng",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = textPrimary,
                        modifier = Modifier.align(Alignment.Center)
                    )
                    IconButton(
                        onClick = { /* Share */ },
                        modifier = Modifier.align(Alignment.CenterEnd)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share, // iOS share icon usually, typical share is close
                            contentDescription = "Share",
                            tint = textPrimary
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Date Range Selector
            item {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp)
                ) {
                    item {
                        DateChip("30 ngày qua", true, ProductPrimary, textPrimary)
                    }
                    item {
                        DateChip("Tháng này", false, ProductPrimary, textPrimary)
                    }
                    item {
                        DateChip("Năm nay", false, ProductPrimary, textPrimary)
                    }
                    item {
                        DateChip("Tùy chỉnh", false, ProductPrimary, textPrimary, hasIcon = true)
                    }
                }
            }

            // KPI Stats Grid
            item {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        KPIItem(
                            modifier = Modifier.weight(1f),
                            title = "Tổng doanh thu",
                            value = "₫1.2B",
                            change = "+5.2%",
                            isPositive = true,
                            cardColor = cardColor,
                            textPrimary = textPrimary,
                            textSecondary = textSecondary
                        )
                        KPIItem(
                            modifier = Modifier.weight(1f),
                            title = "Tỷ lệ chuyển đổi",
                            value = "3.5%",
                            change = "-1.8%",
                            isPositive = false,
                            cardColor = cardColor,
                            textPrimary = textPrimary,
                            textSecondary = textSecondary
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        KPIItem(
                            modifier = Modifier.weight(1f),
                            title = "Giá trị trung bình",
                            value = "₫2.5M",
                            change = "+2.1%",
                            isPositive = true,
                            cardColor = cardColor,
                            textPrimary = textPrimary,
                            textSecondary = textSecondary
                        )
                        KPIItem(
                            modifier = Modifier.weight(1f),
                            title = "Tổng đơn hàng",
                            value = "480",
                            change = "+7.0%",
                            isPositive = true,
                            cardColor = cardColor,
                            textPrimary = textPrimary,
                            textSecondary = textSecondary
                        )
                    }
                }
            }

            // Revenue Chart Header
            item {
                Text(
                    text = "Xu hướng doanh thu",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = textPrimary,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

            // Bar Chart
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(cardColor)
                        .padding(16.dp)
                ) {
                    Column {
                        Text("Doanh thu theo ngày", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = textSecondary)
                        Text("₫85.4M", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("01 Th05 - 07 Th05", fontSize = 14.sp, color = textSecondary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("+15%", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = AnalysisPositive)
                        }
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        // Mock Chart Area
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            // Data: T2 90%, T3 100%, T4 80%, T5 30% (Selected), T6 30%, T7 80%, CN 90%
                            BarItem("T2", 0.9f, false, ProductPrimary, textSecondary)
                            BarItem("T3", 1.0f, false, ProductPrimary, textSecondary)
                            BarItem("T4", 0.8f, false, ProductPrimary, textSecondary)
                            BarItem("T5", 0.3f, true, ProductPrimary, ProductPrimary) 
                            BarItem("T6", 0.3f, false, ProductPrimary, textSecondary)
                            BarItem("T7", 0.8f, false, ProductPrimary, textSecondary)
                            BarItem("CN", 0.9f, false, ProductPrimary, textSecondary)
                        }
                    }
                }
            }

            // Best Sellers Header
            item {
                Text(
                    text = "Sản phẩm bán chạy nhất",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = textPrimary,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

            // Ranked List
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(cardColor)
                ) {
                    RankedItem(1, "Tai nghe Pro Max", "₫2,490,000", "120 sp", "https://lh3.googleusercontent.com/aida-public/AB6AXuB2po5X-W-JmG5H8Wf5gGA0od4HZezHWFB46I7H8QuESP04KoOpr9Z-swM-Y4pjl0VVx1uzNvepNtZJ9VkN0qghG00xKHuX7vk_XPAxqcFkUY1Ewpy0_UxWZndEGGcnBgqlUiakCSQ62_u9hIbF47S-VQh63NpWa8iWlKm-DWhnah_Ymp8-zvEZWAPU544beuM2q4auGT3905OfYuxNLiouPXJIyoVtQzSDGLW84zOnH6YFGLKKkLH9o-GDJfiHRn-wnJIhpFJbel0", textPrimary, textSecondary)
                    HorizontalDivider(color = Color(0xFFE5E5EA))
                    RankedItem(2, "Điện thoại Galaxy S23", "₫18,990,000", "95 sp", "https://lh3.googleusercontent.com/aida-public/AB6AXuAf1bLvJ9dxGxkhA6QkUtYczr-4rtjhmGf1suKXLI8rtY3WbRKLgI96qHnIMa2aoX2lXz7JgV4jGj8_dtZAeCoaxog7zQuV0brN1UdndHgeHqIpzVM01fyIdS9WKwpis-XIC68QuoQrNNZ92L7ROGnsuUGmCIpepr07SxV3AxzUSpCkBTr7d9CE54sbOGuto5jqdyZmIbGwLLQ6K7yj4ku0GAST_u2RQnpxgbW710zAH33HbHCLFWEpakDKBeG6n5zYCaF8EODfwvw", textPrimary, textSecondary)
                    HorizontalDivider(color = Color(0xFFE5E5EA))
                    RankedItem(3, "Đồng hồ thông minh Fit 5", "₫4,150,000", "88 sp", "https://lh3.googleusercontent.com/aida-public/AB6AXuA21d6gmlVbgiy17Pu-EBQn_30XbBq09vlP_DAehPMl_nTlz3eHq1OzuSdqlZsRB31tB_bMn4frPoZLkd2_XoBsYU-er13rrXtVsB9FT3J2fpGbEq-s-7PHhQdn7ZIwJdwq9q_235bxla5U8OJPZquwR4tR2505TtglhdMfJ-Thf32xMGih-7M8-YC0xzA67wykmJi-OiPk_xCTjydyZqOy7u1nLUmHCRZpoKlfcI2sJLfitqET1n8eFCnLze6_Zb3n-zSgkSJBN5s", textPrimary, textSecondary)
                    RankedItem(4, "Laptop Air M2", "₫27,500,000", "72 sp", "https://lh3.googleusercontent.com/aida-public/AB6AXuAFbCn83J3wUM9SPujNn3ZkyOAjzADqmKdnSv1olY80Q-iQoXaqWVIn_h4lF-ArCLX5OBJkg9jtOO6p17XQ5Nm5tpItuGGt862ZX_hcgF-tKUcXjvJtBoIvR7Pda9-zcViuYjGcJW7YIFrJYLFVuw2xbFpqu8-Bl21TxzjRVLXCzn7t2Xu43b1j9dNJ-6gLosK9QEobpZ2PQRkLXbZ0bZn7jeymFLGtRE3mFLXhLsj1wdTBmHdtQR_saDGxawCLpszMD8DZjjwn0n4", textPrimary, textSecondary)
                }
            }

            // Category Analysis Header
            item {
                Text(
                    text = "Phân tích Theo danh mục",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = textPrimary,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

            // Donut Chart
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(cardColor)
                        .padding(16.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(200.dp)) {
                            Canvas(modifier = Modifier.size(160.dp)) {
                                val strokeWidth = 16.dp.toPx()
                                // Blue: 60% -> 216 deg
                                drawArc(
                                    color = AnalysisGraphBlue,
                                    startAngle = -90f,
                                    sweepAngle = 216f,
                                    useCenter = false,
                                    style = androidx.compose.ui.graphics.drawscope.Stroke(strokeWidth, cap = StrokeCap.Round)
                                )
                                // Purple: 25% -> 90 deg. Start at -90 + 216 + gap?
                                // HTML uses dashed stroke-dasharray. Here simplified usage.
                                drawArc(
                                    color = AnalysisGraphPurple,
                                    startAngle = -90f + 216f + 10f, // manual gap
                                    sweepAngle = 90f - 10f,
                                    useCenter = false,
                                    style = androidx.compose.ui.graphics.drawscope.Stroke(strokeWidth, cap = StrokeCap.Round)
                                )
                                // Red: 15% -> 54 deg
                                drawArc(
                                    color = AnalysisGraphRed,
                                    startAngle = -90f + 216f + 90f + 10f,
                                    sweepAngle = 54f - 10f,
                                    useCenter = false,
                                    style = androidx.compose.ui.graphics.drawscope.Stroke(strokeWidth, cap = StrokeCap.Round)
                                )
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Tổng", fontSize = 12.sp, color = textSecondary)
                                Text("₫1.2B", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            LegendItem("Điện thoại", "60%", AnalysisGraphBlue, textPrimary)
                            LegendItem("Laptop", "25%", AnalysisGraphPurple, textPrimary)
                            LegendItem("Phụ kiện", "15%", AnalysisGraphRed, textPrimary)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DateChip(
    text: String,
    isSelected: Boolean,
    primaryColor: Color,
    textColor: Color,
    hasIcon: Boolean = false
) {
    Row(
        modifier = Modifier
            .height(36.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) primaryColor else Color(0xFFE4E4E7)) // zinc-200
            .padding(horizontal = 12.dp)
            .clickable { },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = if (isSelected) Color.White else Color(0xFF18181B) // zinc-900 hardcoded for light bg
        )
        if (hasIcon) {
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                imageVector = Icons.Default.CalendarMonth,
                contentDescription = null,
                tint = if (isSelected) Color.White else Color(0xFF18181B),
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
fun KPIItem(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    change: String,
    isPositive: Boolean,
    cardColor: Color,
    textPrimary: Color,
    textSecondary: Color
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(cardColor)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(title, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = textSecondary)
        Text(value, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = textPrimary)
        Text(change, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = if (isPositive) AnalysisPositive else AnalysisNegative)
    }
}

@Composable
fun androidx.compose.foundation.layout.RowScope.BarItem(
    label: String,
    heightRatio: Float,
    isActive: Boolean,
    primaryColor: Color,
    labelColor: Color
) {
    Column(
        modifier = Modifier.weight(1f).fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.8f) // Spacing
                .fillMaxHeight(heightRatio)
                .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                .background(if (isActive) primaryColor else primaryColor.copy(alpha = 0.2f))
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = if (isActive) FontWeight.Bold else FontWeight.Medium,
            color = if (isActive) primaryColor else labelColor
        )
    }
}

@Composable
fun RankedItem(
    rank: Int,
    name: String,
    price: String,
    count: String,
    imageUrl: String,
    textPrimary: Color,
    textSecondary: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = rank.toString(),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = textSecondary,
            modifier = Modifier.width(24.dp),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.width(16.dp))
        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.Gray.copy(alpha = 0.1f)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(name, fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = textPrimary, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(price, fontSize = 14.sp, color = textSecondary)
        }
        Text(count, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = textPrimary)
    }
}

@Composable
fun LegendItem(
    label: String,
    value: String,
    color: Color,
    textPrimary: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(12.dp).background(color, CircleShape))
            Spacer(modifier = Modifier.width(8.dp))
            Text(label, fontSize = 16.sp, color = textPrimary)
        }
        Text(value, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = textPrimary)
    }
}

@Preview
@Composable
fun PreviewSalesAnalysisScreen() {
    SalesAnalysisScreen()
}
