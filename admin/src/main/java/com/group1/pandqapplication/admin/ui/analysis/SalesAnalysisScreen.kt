package com.group1.pandqapplication.admin.ui.analysis

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
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.group1.pandqapplication.admin.data.remote.dto.CategorySaleItemDto
import com.group1.pandqapplication.admin.data.remote.dto.DailyRevenueDto
import com.group1.pandqapplication.admin.data.remote.dto.TopProductDto
import com.group1.pandqapplication.shared.ui.theme.AnalysisCardDark
import com.group1.pandqapplication.shared.ui.theme.AnalysisCardLight
import com.group1.pandqapplication.shared.ui.theme.AnalysisNegative
import com.group1.pandqapplication.shared.ui.theme.AnalysisPositive
import com.group1.pandqapplication.shared.ui.theme.AnalysisTextPrimaryDark
import com.group1.pandqapplication.shared.ui.theme.AnalysisTextPrimaryLight
import com.group1.pandqapplication.shared.ui.theme.AnalysisTextSecondaryDark
import com.group1.pandqapplication.shared.ui.theme.AnalysisTextSecondaryLight
import com.group1.pandqapplication.shared.ui.theme.CheckoutBackgroundDark
import com.group1.pandqapplication.shared.ui.theme.CheckoutBackgroundLight
import com.group1.pandqapplication.shared.ui.theme.ProductPrimary
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Locale

@Composable
fun SalesAnalysisScreen(
    initialDateRange: String? = null,
    onBackClick: () -> Unit = {},
    viewModel: SalesAnalysisViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    // Sync initial date range from navigation
    androidx.compose.runtime.LaunchedEffect(initialDateRange) {
        initialDateRange?.let { viewModel.setDateRange(it) }
    }
    
    // Colors - sync with AdminAnalyticsScreen
    val backgroundColor = Color(0xFFF8F9FA)
    val primaryColor = Color(0xFF137fec)
    val positiveColor = Color(0xFF10B981)
    val negativeColor = Color(0xFFEF4444)
    val textPrimary = Color(0xFF1F2937)
    val textSecondary = Color.Gray

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
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                            tint = textPrimary
                        )
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
                        Text(
                            text = uiState.error ?: "Unknown error",
                            color = textSecondary,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        TextButton(onClick = { viewModel.retry() }) {
                            Icon(Icons.Default.Refresh, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Thử lại")
                        }
                    }
                }
            }
            else -> {
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
                                DateChip("7 ngày qua", uiState.selectedRange == "7d", primaryColor, textPrimary) {
                                    viewModel.setDateRange("7d")
                                }
                            }
                            item {
                                DateChip("30 ngày qua", uiState.selectedRange == "30d", primaryColor, textPrimary) {
                                    viewModel.setDateRange("30d")
                                }
                            }
                            item {
                                DateChip("90 ngày qua", uiState.selectedRange == "90d", primaryColor, textPrimary) {
                                    viewModel.setDateRange("90d")
                                }
                            }
                            item {
                                DateChip("Tùy chỉnh", false, primaryColor, textPrimary, hasIcon = true) {}
                            }
                        }
                    }

                    // KPI Stats Grid
                    item {
                        val overview = uiState.overview
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                KPIItem(
                                    modifier = Modifier.weight(1f),
                                    title = "Tổng doanh thu",
                                    value = formatCurrency(overview?.totalRevenue),
                                    change = formatPercent(overview?.revenueChangePercent),
                                    isPositive = (overview?.revenueChangePercent ?: 0.0) >= 0,
                                    cardColor = androidx.compose.material3.MaterialTheme.colorScheme.surface,
                                    textPrimary = textPrimary,
                                    textSecondary = textSecondary
                                )
                                KPIItem(
                                    modifier = Modifier.weight(1f),
                                    title = "Tỷ lệ chuyển đổi",
                                    value = "${overview?.conversionRate ?: 0.0}%",
                                    change = formatPercent(overview?.conversionChangePercent),
                                    isPositive = (overview?.conversionChangePercent ?: 0.0) >= 0,
                                    cardColor = androidx.compose.material3.MaterialTheme.colorScheme.surface,
                                    textPrimary = textPrimary,
                                    textSecondary = textSecondary
                                )
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                KPIItem(
                                    modifier = Modifier.weight(1f),
                                    title = "Giá trị trung bình",
                                    value = formatCurrency(overview?.averageOrderValue),
                                    change = formatPercent(overview?.averageOrderChangePercent),
                                    isPositive = (overview?.averageOrderChangePercent ?: 0.0) >= 0,
                                    cardColor = androidx.compose.material3.MaterialTheme.colorScheme.surface,
                                    textPrimary = textPrimary,
                                    textSecondary = textSecondary
                                )
                                KPIItem(
                                    modifier = Modifier.weight(1f),
                                    title = "Tổng đơn hàng",
                                    value = "${overview?.totalOrders ?: 0}",
                                    change = formatPercent(overview?.ordersChangePercent),
                                    isPositive = (overview?.ordersChangePercent ?: 0.0) >= 0,
                                    cardColor = androidx.compose.material3.MaterialTheme.colorScheme.surface,
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

                    // Revenue Line Chart
                    item {
                        val chartData = uiState.revenueChart
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(androidx.compose.material3.MaterialTheme.colorScheme.surface)
                                .padding(16.dp)
                        ) {
                            Column {
                                val dailyRevenues = chartData?.dailyRevenues ?: emptyList()
                                
                                Text("Doanh thu theo ngày", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = textSecondary)
                                Text(formatCurrency(chartData?.totalRevenue), fontSize = 28.sp, fontWeight = FontWeight.Bold, color = textPrimary, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(chartData?.dateRangeLabel ?: "", fontSize = 14.sp, color = textSecondary)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    val positiveColor = Color(0xFF10B981)
                                    val negativeColor = Color(0xFFEF4444)
                                    Text(formatPercent(chartData?.changePercent), fontSize = 14.sp, fontWeight = FontWeight.Medium, 
                                        color = if ((chartData?.changePercent ?: 0.0) >= 0) positiveColor else negativeColor)
                                }
                                
                                Spacer(modifier = Modifier.height(24.dp))
                                
                                // Line Chart Area
                                Box(modifier = Modifier.fillMaxWidth().height(192.dp)) {
                                    if (dailyRevenues.isNotEmpty()) {
                                        Canvas(modifier = Modifier.fillMaxSize()) {
                                            val width = size.width
                                            val height = size.height
                                            val maxRevenue = dailyRevenues.maxOfOrNull { it.revenue ?: java.math.BigDecimal.ZERO } ?: java.math.BigDecimal.ONE
                                            
                                            // Calculate points
                                            val points = dailyRevenues.mapIndexed { index, daily ->
                                                val x = width * (index.toFloat() / (dailyRevenues.size - 1).coerceAtLeast(1))
                                                val yPercent = if (maxRevenue > java.math.BigDecimal.ZERO) {
                                                    1 - ((daily.revenue ?: java.math.BigDecimal.ZERO).toFloat() / maxRevenue.toFloat())
                                                } else 0.5f
                                                Pair(x, height * yPercent.coerceIn(0.05f, 0.95f))
                                            }
                                            
                                            if (points.size > 1) {
                                                val path = androidx.compose.ui.graphics.Path().apply {
                                                    moveTo(points[0].first, points[0].second)
                                                    for (i in 1 until points.size) {
                                                        val prev = points[i - 1]
                                                        val curr = points[i]
                                                        val controlX1 = prev.first + (curr.first - prev.first) / 2
                                                        val controlX2 = prev.first + (curr.first - prev.first) / 2
                                                        cubicTo(controlX1, prev.second, controlX2, curr.second, curr.first, curr.second)
                                                    }
                                                }

                                                // Fill Gradient
                                                val fillPath = androidx.compose.ui.graphics.Path().apply {
                                                    addPath(path)
                                                    lineTo(width, height)
                                                    lineTo(0f, height)
                                                    close()
                                                }
                                                
                                                drawPath(
                                                    path = fillPath,
                                                    brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                                                        colors = listOf(Color(0xFF137fec).copy(alpha = 0.4f), Color(0xFF137fec).copy(alpha = 0f))
                                                    )
                                                )

                                                // Stroke
                                                drawPath(
                                                    path = path,
                                                    color = Color(0xFF137fec),
                                                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                                                )

                                                // Points
                                                points.forEach { (x, y) ->
                                                    drawCircle(
                                                        color = Color(0xFF137fec),
                                                        radius = 4.dp.toPx(),
                                                        center = androidx.compose.ui.geometry.Offset(x, y),
                                                        style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2.dp.toPx())
                                                    )
                                                    drawCircle(
                                                        color = Color.White,
                                                        radius = 2.dp.toPx(),
                                                        center = androidx.compose.ui.geometry.Offset(x, y)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                                
                                // X-axis labels
                                if (dailyRevenues.isNotEmpty()) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                                    ) {
                                        dailyRevenues.forEach { daily ->
                                            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                                                Text(
                                                    daily.dayLabel ?: "", 
                                                    style = androidx.compose.material3.MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp), 
                                                    color = Color.Gray,
                                                    maxLines = 1,
                                                    textAlign = TextAlign.Center,
                                                    overflow = TextOverflow.Visible,
                                                    softWrap = false
                                                )
                                            }
                                        }
                                    }
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
                        val products = uiState.topProducts?.products ?: emptyList()
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(androidx.compose.material3.MaterialTheme.colorScheme.surface)
                        ) {
                            products.forEachIndexed { index, product ->
                                RankedItem(
                                    product = product,
                                    textPrimary = textPrimary,
                                    textSecondary = textSecondary
                                )
                                if (index < products.size - 1) {
                                    HorizontalDivider(color = Color(0xFFE5E5EA))
                                }
                            }
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
                        val categoryData = uiState.categorySales
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(androidx.compose.material3.MaterialTheme.colorScheme.surface)
                                .padding(16.dp)
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Box(contentAlignment = Alignment.Center, modifier = Modifier.size(200.dp)) {
                                    DonutChart(categories = categoryData?.categories ?: emptyList())
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text("Tổng", fontSize = 12.sp, color = textSecondary)
                                        Text(formatCurrency(categoryData?.totalRevenue), fontSize = 24.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                                    }
                                }
                                
                                Spacer(modifier = Modifier.height(24.dp))
                                
                                Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                    categoryData?.categories?.forEach { category ->
                                        LegendItem(
                                            label = category.categoryName ?: "",
                                            value = "${String.format("%.1f", category.percentage)}%",
                                            color = parseColor(category.colorHex),
                                            textPrimary = textPrimary
                                        )
                                    }
                                }
                            }
                        }
                    }
                    
                    // Bottom spacing
                    item {
                        Spacer(modifier = Modifier.height(32.dp))
                    }
                }
            }
        }
    }
}

// ==================== Helper Functions ====================

private fun formatCurrency(value: BigDecimal?): String {
    if (value == null) return "₫0"
    val formatted = NumberFormat.getNumberInstance(Locale("vi", "VN")).format(value)
    return "₫$formatted"
}

private fun formatPercent(value: Double?): String {
    if (value == null) return "0%"
    val sign = if (value >= 0) "+" else ""
    return "$sign${String.format("%.1f", value)}%"
}

private fun parseColor(hex: String?): Color {
    if (hex == null) return Color.Gray
    return try {
        Color(android.graphics.Color.parseColor(hex))
    } catch (e: Exception) {
        Color.Gray
    }
}

// ==================== Composable Components ====================

@Composable
fun DateChip(
    text: String,
    isSelected: Boolean,
    primaryColor: Color,
    textColor: Color,
    hasIcon: Boolean = false,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .height(36.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) primaryColor else Color(0xFFE4E4E7))
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = if (isSelected) Color.White else Color(0xFF18181B)
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
    val positiveColor = Color(0xFF10B981)
    val negativeColor = Color(0xFFEF4444)
    
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(cardColor)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = title, 
            fontSize = 14.sp, 
            fontWeight = FontWeight.Medium, 
            color = textSecondary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = value, 
            fontSize = 24.sp, 
            fontWeight = FontWeight.Bold, 
            color = textPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = change, 
            fontSize = 14.sp, 
            fontWeight = FontWeight.Medium, 
            color = if (isPositive) positiveColor else negativeColor,
            maxLines = 1
        )
    }
}


@Composable
fun RankedItem(
    product: TopProductDto,
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
            text = "${product.rank ?: 0}",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = textSecondary,
            modifier = Modifier.width(24.dp),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.width(16.dp))
        AsyncImage(
            model = product.imageUrl ?: "",
            contentDescription = null,
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.Gray.copy(alpha = 0.1f)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                product.productName ?: "", 
                fontWeight = FontWeight.SemiBold, 
                fontSize = 16.sp, 
                color = textPrimary, 
                maxLines = 1, 
                overflow = TextOverflow.Ellipsis
            )
            Text(formatCurrency(product.price), fontSize = 14.sp, color = textSecondary)
        }
        Text("${product.quantitySold ?: 0} sp", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = textPrimary)
    }
}

@Composable
fun DonutChart(categories: List<CategorySaleItemDto>) {
    Canvas(modifier = Modifier.size(160.dp)) {
        val strokeWidth = 16.dp.toPx()
        var startAngle = -90f
        
        categories.forEach { category ->
            val sweepAngle = ((category.percentage ?: 0.0) / 100 * 360).toFloat()
            drawArc(
                color = parseColor(category.colorHex),
                startAngle = startAngle,
                sweepAngle = sweepAngle - 5f, // Gap between segments
                useCenter = false,
                style = androidx.compose.ui.graphics.drawscope.Stroke(strokeWidth, cap = StrokeCap.Round)
            )
            startAngle += sweepAngle
        }
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
