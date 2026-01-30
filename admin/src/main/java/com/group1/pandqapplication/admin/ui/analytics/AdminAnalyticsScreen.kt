package com.group1.pandqapplication.admin.ui.analytics

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.group1.pandqapplication.admin.util.AnalyticsPdfData
import com.group1.pandqapplication.admin.util.PdfUtils
import androidx.compose.ui.platform.LocalContext
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.input.pointer.pointerInput
import com.group1.pandqapplication.admin.data.remote.dto.CategorySaleItemDto
import com.group1.pandqapplication.admin.data.remote.dto.DailyRevenueDto
import com.group1.pandqapplication.admin.data.remote.dto.TopProductDto
import com.group1.pandqapplication.shared.ui.theme.PandQApplicationTheme
import java.math.BigDecimal
import java.text.NumberFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminAnalyticsScreen(
    onBackClick: () -> Unit = {},
    onNavigateToDetail: (String, String) -> Unit = { _, _ -> },
    onNavigateToDailyDetail: (String) -> Unit = {},
    viewModel: AdminAnalyticsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val filters = listOf(
        "7 ngày qua" to "7d", 
        "30 ngày qua" to "30d", 
        "90 ngày qua" to "90d", 
        "Tùy chọn" to "custom"
    )
    var reportType by remember { mutableStateOf("Top Sản phẩm (Doanh thu)") }
    LaunchedEffect(reportType) {
        viewModel.setReportType(reportType)
    }

    var showDateRangePicker by remember { mutableStateOf(false) }

    val context = LocalContext.current
    
    val comparisonLabel = when (uiState.selectedRange) {
        "7d" -> "so với tuần trước"
        "30d" -> "so với tháng trước"
        "90d" -> "so với quý trước"
        else -> "so với kỳ trước"
    }

    val createDocumentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/pdf")
    ) { uri ->
        uri?.let {
            val isCategory = reportType.contains("Danh mục", ignoreCase = true)
            val pdfData = AnalyticsPdfData(
                totalRevenue = uiState.totalRevenue,
                totalOrders = uiState.totalOrders,
                reportType = reportType,
                topProducts = uiState.topProducts,
                categories = uiState.categorySales,
                isCategoryReport = isCategory
            )
            PdfUtils.createAnalyticsPdf(context, it, pdfData)
        }
    }


    
    if (showDateRangePicker) {
        val dateRangePickerState = rememberDateRangePickerState()
        val confirmEnabled = dateRangePickerState.selectedStartDateMillis != null && dateRangePickerState.selectedEndDateMillis != null
        
        DatePickerDialog(
            onDismissRequest = { showDateRangePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        val startDate = dateRangePickerState.selectedStartDateMillis?.let { 
                            java.time.Instant.ofEpochMilli(it).atZone(java.time.ZoneId.of("UTC")).toLocalDate() 
                        }
                        val endDate = dateRangePickerState.selectedEndDateMillis?.let { 
                            java.time.Instant.ofEpochMilli(it).atZone(java.time.ZoneId.of("UTC")).toLocalDate() 
                        }
                        
                        if (startDate != null && endDate != null) {
                            val customRange = "custom:$startDate,$endDate"
                            viewModel.setDateRange(customRange)
                        }
                        showDateRangePicker = false
                    },
                    enabled = confirmEnabled
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDateRangePicker = false }) {
                    Text("Hủy")
                }
            }
        ) {
            DateRangePicker(state = dateRangePickerState)
        }
    }

    val backgroundColor = Color(0xFFF8F9FA)
    val textPrimary = Color(0xFF1F2937)
    val textSecondary = Color(0xFF6B7280)
    val borderColor = Color(0xFFE5E7EB)

    Scaffold(
        topBar = {
            Surface(
                color = Color.White,
                shadowElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Spacer(modifier = Modifier.windowInsetsTopHeight(WindowInsets.statusBars))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .padding(horizontal = 8.dp)
                    ) {
                        IconButton(
                            onClick = onBackClick,
                            modifier = Modifier.align(Alignment.CenterStart)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.Black,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Text(
                            text = "Tổng quan doanh thu",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = Color.Black,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            }
        },
        containerColor = backgroundColor
    ) { paddingValues ->
        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFF137fec))
            }
        } else if (uiState.error != null) {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Outlined.Error, contentDescription = null, tint = Color.Red, modifier = Modifier.size(48.dp))
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(uiState.error ?: "Đã có lỗi xảy ra", color = Color.Red)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { viewModel.retry() }) {
                        Text("Thử lại")
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Date Range Chips (on top)
                item {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(filters) { (label, range) ->
                            val isSelected = if (range == "custom") uiState.selectedRange.startsWith("custom") 
                                             else range == uiState.selectedRange
                            val bgColor = if (isSelected) Color(0xFF137fec) else MaterialTheme.colorScheme.surface
                            val contentColor = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                            
                            val isCustom = range == "custom"

                            Surface(
                                shape = RoundedCornerShape(50),
                                color = bgColor,
                                contentColor = contentColor,
                                onClick = { 
                                    if (isCustom) {
                                        showDateRangePicker = true
                                    } else {
                                        viewModel.setDateRange(range)
                                    }
                                },
                                modifier = Modifier.height(36.dp),
                                shadowElevation = if(isSelected) 4.dp else 0.dp
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                ) {
                                    if (isCustom) {
                                        Icon(
                                            Icons.Outlined.CalendarMonth, 
                                            contentDescription = null,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                    }
                                    
                                    val displayText = if (isCustom && isSelected && uiState.selectedRange.startsWith("custom:")) {
                                        try {
                                            val parts = uiState.selectedRange.substring(7).split(",")
                                            if (parts.size == 2) {
                                                val start = java.time.LocalDate.parse(parts[0])
                                                val end = java.time.LocalDate.parse(parts[1])
                                                "${start.dayOfMonth}/${start.monthValue} - ${end.dayOfMonth}/${end.monthValue}"
                                            } else {
                                                label
                                            }
                                        } catch (e: Exception) {
                                            label
                                        }
                                    } else {
                                        label
                                    }
                                    
                                    Text(displayText, style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
                                }
                            }
                        }
                    }
                }    

                    // Key Metrics Grid (4 cards: Revenue, Orders, Products, New Customers)
                    item {
                        KeyMetricsGrid(
                            totalRevenue = uiState.totalRevenue,
                            revenueChangePercent = uiState.revenueChangePercent,
                            totalOrders = uiState.totalOrders,
                            ordersChangePercent = uiState.ordersChangePercent,
                            totalProductsSold = uiState.totalProductsSold,
                            productsChangePercent = uiState.productsChangePercent,
                            newCustomers = uiState.newCustomers,
                            customersChangePercent = uiState.customersChangePercent,
                            comparisonLabel = comparisonLabel
                        )
                    }

                    // Main Chart Card (with interactive popup)
                    item {
                        AnalyticsChartCard(
                            totalRevenue = uiState.totalRevenue,
                            changePercent = uiState.revenueChangePercent,
                            dailyRevenues = uiState.dailyRevenues,
                            onDayClick = onNavigateToDailyDetail
                        )
                    }

                    // Report Type Dropdown (moved here, before Top Products)
                    item {
                        var dropdownExpanded by remember { mutableStateOf(false) }
                        val reportTypes = listOf(
                            "Top Sản phẩm (Doanh thu)",
                            "Top Danh mục (Doanh thu)",
                            "Top Sản phẩm (Số lượng)",
                            "Top Danh mục (Số lượng)"
                        )
                        
                        Column {
                            Box {
                                OutlinedCard(
                                    onClick = { dropdownExpanded = true },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.surface)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(reportType, fontWeight = FontWeight.Medium)
                                        Icon(Icons.Outlined.ExpandMore, contentDescription = null, tint = Color.Gray)
                                    }
                                }
                                DropdownMenu(
                                    expanded = dropdownExpanded,
                                    onDismissRequest = { dropdownExpanded = false }
                                ) {
                                    reportTypes.forEach { type ->
                                        DropdownMenuItem(
                                            text = { Text(type) },
                                            onClick = { 
                                                reportType = type
                                                dropdownExpanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Detailed List (Top Products/Categories based on reportType)
                    item {
                        TopProductsSection(
                            products = uiState.topProducts, 
                            categories = uiState.categorySales,
                            reportType = reportType,
                            isLoadingReportData = uiState.isLoadingReportData,
                            onSeeAllClick = { onNavigateToDetail(reportType, uiState.selectedRange) },
                            onExportPdf = {
                                val fileName = "BaoCao_Pandora_${reportType.replace(" ", "_")}.pdf"
                                createDocumentLauncher.launch(fileName)
                            }
                        )
                    }
                }
            }
        }
    }

@Composable
fun AnalyticsChartCard(
    totalRevenue: BigDecimal,
    changePercent: Double,
    dailyRevenues: List<DailyRevenueDto>,
    onDayClick: (String) -> Unit = {}
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp),
        modifier = Modifier
            .border(1.dp, Color.LightGray.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text("Tổng doanh thu", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
                    Text(
                        formatCurrency(totalRevenue), 
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                    )
                }
                Surface(
                    color = if (changePercent >= 0) Color(0xFF10B981).copy(alpha = 0.1f) 
                           else Color(0xFFEF4444).copy(alpha = 0.1f),
                    shape = RoundedCornerShape(50)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            if (changePercent >= 0) Icons.Outlined.TrendingUp else Icons.Outlined.TrendingDown, 
                            contentDescription = null, 
                            tint = if (changePercent >= 0) Color(0xFF10B981) else Color(0xFFEF4444), 
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            "${if (changePercent >= 0) "+" else ""}${String.format("%.0f", changePercent)}%", 
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), 
                            color = if (changePercent >= 0) Color(0xFF10B981) else Color(0xFFEF4444)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            
            // Chart Area with clickable dots
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(192.dp)) {
                if (dailyRevenues.isNotEmpty()) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val width = size.width
                        val height = size.height
                        val maxRevenue = dailyRevenues.maxOfOrNull { it.revenue ?: BigDecimal.ZERO } ?: BigDecimal.ONE
                        
                        // Calculate points from real data
                        val points = dailyRevenues.mapIndexed { index, daily ->
                            val x = width * (index.toFloat() / (dailyRevenues.size - 1).coerceAtLeast(1))
                            val yPercent = if (maxRevenue > BigDecimal.ZERO) {
                                1 - ((daily.revenue ?: BigDecimal.ZERO).toFloat() / maxRevenue.toFloat())
                            } else 0.5f
                            Pair(x, height * yPercent.coerceIn(0.05f, 0.95f))
                        }
                        
                        if (points.size > 1) {
                            val path = Path().apply {
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
                            val fillPath = Path().apply {
                                addPath(path)
                                lineTo(width, height)
                                lineTo(0f, height)
                                close()
                            }
                            
                            drawPath(
                                path = fillPath,
                                brush = Brush.verticalGradient(
                                    colors = listOf(Color(0xFF137fec).copy(alpha = 0.4f), Color(0xFF137fec).copy(alpha = 0f))
                                )
                            )

                            // Stroke
                            drawPath(
                                path = path,
                                color = Color(0xFF137fec),
                                style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                            )

                            // Points (will be overlaid with clickable boxes)
                            points.forEach { (x, y) ->
                                drawCircle(
                                    color = Color(0xFF137fec),
                                    radius = 4.dp.toPx(),
                                    center = androidx.compose.ui.geometry.Offset(x, y),
                                    style = Stroke(width = 2.dp.toPx())
                                )
                                drawCircle(
                                    color = Color.White,
                                    radius = 2.dp.toPx(),
                                    center = androidx.compose.ui.geometry.Offset(x, y)
                                )
                            }
                        }
                    }
                    
                    // Clickable overlay using pointerInput for better hit detection
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .pointerInput(dailyRevenues) {
                                detectTapGestures { offset ->
                                    val width = size.width.toFloat()
                                    val height = size.height.toFloat()
                                    val maxRevenue = dailyRevenues.maxOfOrNull { it.revenue ?: BigDecimal.ZERO } ?: BigDecimal.ONE
                                    
                                    // Calculate all points
                                    val points = dailyRevenues.mapIndexed { index, daily ->
                                        val x = width * (index.toFloat() / (dailyRevenues.size - 1).coerceAtLeast(1))
                                        val yPercent = if (maxRevenue > BigDecimal.ZERO) {
                                            1 - ((daily.revenue ?: BigDecimal.ZERO).toFloat() / maxRevenue.toFloat())
                                        } else 0.5f
                                        Triple(x, height * yPercent.coerceIn(0.05f, 0.95f), daily.date)
                                    }
                                    
                                    // Find closest point to tap
                                    val clickRadius = 30f // 30px hit area
                                    points.forEach { (x, y, date) ->
                                        val distance = kotlin.math.sqrt(
                                            (offset.x - x) * (offset.x - x) + 
                                            (offset.y - y) * (offset.y - y)
                                        )
                                        if (distance <= clickRadius && date != null) {
                                            onDayClick(date)
                                        }
                                    }
                                }
                            }
                    )
                } else {
                    // Fallback static chart
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val width = size.width
                        val height = size.height
                        
                        val path = Path().apply {
                            moveTo(0f, height * 0.8f)
                            cubicTo(width * 0.1f, height * 0.75f, width * 0.16f, height * 0.4f, width * 0.26f, height * 0.45f)
                            cubicTo(width * 0.36f, height * 0.5f, width * 0.43f, height * 0.2f, width * 0.53f, height * 0.25f)
                            cubicTo(width * 0.63f, height * 0.3f, width * 0.7f, height * 0.6f, width * 0.8f, height * 0.5f)
                            cubicTo(width * 0.9f, height * 0.4f, width * 0.96f, height * 0.1f, width, height * 0.05f)
                        }
                        
                        val fillPath = Path().apply {
                            addPath(path)
                            lineTo(width, height)
                            lineTo(0f, height)
                            close()
                        }
                        
                        drawPath(
                            path = fillPath,
                            brush = Brush.verticalGradient(
                                colors = listOf(Color(0xFF137fec).copy(alpha = 0.4f), Color(0xFF137fec).copy(alpha = 0f))
                            )
                        )
                        drawPath(
                            path = path,
                            color = Color(0xFF137fec),
                            style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                        )
                    }
                }
            }
            
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            ) {
                if (dailyRevenues.isNotEmpty()) {
                    dailyRevenues.forEach { daily ->
                        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                            Text(
                                daily.dayLabel ?: "", 
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp), 
                                color = Color.Gray,
                                maxLines = 1,
                                textAlign = TextAlign.Center,
                                overflow = androidx.compose.ui.text.style.TextOverflow.Visible,
                                softWrap = false
                            )
                        }
                    }
                } else {
                    listOf("T2", "T3", "T4", "T5", "T6", "T7", "CN").forEach { day ->
                        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                            Text(day, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun KeyMetricsGrid(
    totalRevenue: BigDecimal,
    revenueChangePercent: Double,
    totalOrders: Long,
    ordersChangePercent: Double,
    totalProductsSold: Long,
    productsChangePercent: Double,
    newCustomers: Long,
    customersChangePercent: Double,
    comparisonLabel: String
) {
    // Optimal order: Revenue (most important) → Orders → Products → New Customers
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            MetricCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Outlined.AttachMoney,
                iconColor = Color(0xFF10B981),
                iconBgColor = Color(0xFFD1FAE5),
                label = "Tổng doanh thu",
                value = formatCurrency(totalRevenue),
                changePercent = revenueChangePercent,
                comparisonLabel = comparisonLabel
            )
            MetricCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Outlined.ShoppingCart,
                iconColor = Color(0xFF137fec),
                iconBgColor = Color(0xFFEBF4FF),
                label = "Số đơn hàng",
                value = totalOrders.toString(),
                changePercent = ordersChangePercent,
                comparisonLabel = comparisonLabel
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            MetricCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Outlined.Inventory2,
                iconColor = Color(0xFFF97316),
                iconBgColor = Color(0xFFFFEDD5),
                label = "Số sản phẩm",
                value = totalProductsSold.toString(),
                changePercent = productsChangePercent,
                comparisonLabel = comparisonLabel
            )
            MetricCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Outlined.PersonAdd,
                iconColor = Color(0xFF8B5CF6),
                iconBgColor = Color(0xFFEDE9FE),
                label = "Khách mới",
                value = newCustomers.toString(),
                changePercent = customersChangePercent,
                comparisonLabel = comparisonLabel
            )
        }
    }
}

@Composable
fun MetricCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    iconColor: Color,
    iconBgColor: Color,
    label: String,
    value: String,
    changePercent: Double,
    comparisonLabel: String = "so với tuần trước"
) {
    val isPositiveChange = changePercent >= 0
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.3f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 8.dp)) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(iconBgColor, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(18.dp))
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(label, style = MaterialTheme.typography.labelMedium, color = Color.Gray)
            }
            Text(value, style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 4.dp)) {
                Text(
                    "${if (isPositiveChange) "+" else ""}${String.format("%.1f", kotlin.math.abs(changePercent))}% $comparisonLabel",
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                    color = if (isPositiveChange) Color(0xFF10B981) else Color(0xFFEF4444)
                )
            }
        }
    }
}

@Composable
fun TopProductsSection(
    products: List<TopProductDto>, 
    categories: List<CategorySaleItemDto>,
    reportType: String = "Doanh thu theo sản phẩm",
    isLoadingReportData: Boolean = false,
    onSeeAllClick: () -> Unit = {},
    onExportPdf: () -> Unit = {}
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Xem tất cả", 
                style = MaterialTheme.typography.labelMedium, 
                color = Color(0xFF137fec),
                modifier = Modifier.clickable { onSeeAllClick() }
            )

            // PDF Export Button
            TextButton(
                onClick = onExportPdf,
                colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFF137fec))
            ) {
                Icon(Icons.Outlined.PictureAsPdf, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Xuất PDF", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
            }
        }
        
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.3f))
        ) {
            val isCategory = reportType.contains("Danh mục", ignoreCase = true)
            val isEmpty = if (isCategory) categories.isEmpty() else products.isEmpty()

            if (isEmpty) {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (isLoadingReportData) {
                        CircularProgressIndicator(
                            color = Color(0xFF137fec),
                            modifier = Modifier.size(32.dp)
                        )
                    } else {
                        Text("Chưa có dữ liệu", color = Color.Gray)
                    }
                }
            } else {
                if (isCategory) {
                    categories.forEachIndexed { index, category ->
                        CategoryItem(category, reportType)
                        if (index < categories.size - 1) {
                            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.2f))
                        }
                    }
                } else {
                    products.forEachIndexed { index, product ->
                        TopProductItem(product, reportType)
                        if (index < products.size - 1) {
                            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.2f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TopProductItem(product: TopProductDto, reportType: String) {
    val showRevenue = reportType.contains("Doanh thu")
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = product.imageUrl ?: "",
            contentDescription = null,
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.LightGray.copy(alpha = 0.5f)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                product.productName ?: "Unknown", 
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold), 
                maxLines = 1
            )
            Text(
                "Đã bán: ${product.quantitySold ?: 0}", 
                style = MaterialTheme.typography.labelSmall, 
                color = Color.Gray, 
                maxLines = 1
            )
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(
                if (showRevenue) formatCurrency(product.totalRevenue) else "${product.quantitySold}", 
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
}

@Composable
fun CategoryItem(category: CategorySaleItemDto, reportType: String) {
    val showRevenue = reportType.contains("Doanh thu")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (!category.imageUrl.isNullOrEmpty()) {
            AsyncImage(
                model = category.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray.copy(alpha = 0.5f)),
                contentScale = ContentScale.Crop
            )
        } else {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(try { Color(android.graphics.Color.parseColor(category.colorHex)) } catch(e:Exception){ Color.Gray }),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = category.categoryName?.take(1) ?: "?",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                category.categoryName ?: "Unknown", 
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold), 
                maxLines = 1
            )
            Text(
                if (showRevenue) "${String.format("%.1f", category.percentage)}% tổng doanh thu" else "Đã bán: ${category.quantitySold}", 
                style = MaterialTheme.typography.labelSmall, 
                color = Color.Gray, 
                maxLines = 1
            )
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(
                if (showRevenue) formatCurrency(category.revenue) else "${category.quantitySold}", 
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
            )
            // Removed redundant sold count here
        }
    }
}





private fun formatCurrency(value: BigDecimal?): String {
    if (value == null) return "0 VNĐ"
    val billions = value.divide(BigDecimal(1_000_000_000), 1, java.math.RoundingMode.HALF_UP)
    return if (billions >= BigDecimal.ONE) {
        "${billions}B VNĐ"
    } else {
        val millions = value.divide(BigDecimal(1_000_000), 1, java.math.RoundingMode.HALF_UP)
        if (millions >= BigDecimal.ONE) {
            "${millions}M VNĐ"
        } else {
            "${NumberFormat.getNumberInstance(Locale("vi", "VN")).format(value)} VNĐ"
        }
    }
}

@Preview
@Composable
fun AdminAnalyticsScreenPreview() {
    PandQApplicationTheme {
        // Preview with mock data - ViewModel won't be available in preview
        Text("Preview not available - requires ViewModel")
    }
}
