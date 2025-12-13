package com.group1.pandqapplication.admin.ui.analytics

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.group1.pandqapplication.shared.ui.theme.PandQApplicationTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminAnalyticsScreen(
    onNavigateToSalesAnalysis: () -> Unit = {}
) {
    val filters = listOf("7 ngày qua", "Tháng này", "Quý này", "Tùy chọn")
    var selectedFilter by remember { mutableStateOf("7 ngày qua") }
    var reportType by remember { mutableStateOf("Doanh thu theo sản phẩm") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Báo Cáo & Phân Tích", 
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    ) 
                },
                actions = {
                    IconButton(onClick = {}) { Icon(Icons.Outlined.Search, contentDescription = "Search") }
                    IconButton(onClick = {}) { Icon(Icons.Outlined.FilterList, contentDescription = "Filter") }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background.copy(alpha = 0.9f)
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 24.dp)
        ) {
            // Filters Section
            item {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    // Dropdown simulation
                    Column {
                        Text(
                            "Loại báo cáo",
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.Gray,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                        OutlinedCard(
                            onClick = { /* Open Dropdown */ },
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
                    }

                    // Chips
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(filters) { filter ->
                            val isSelected = filter == selectedFilter
                            val bgColor = if (isSelected) Color(0xFF137fec) else MaterialTheme.colorScheme.surface
                            val contentColor = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                            val border = if(isSelected) null else androidx.compose.foundation.BorderStroke(0.dp, Color.Transparent)

                            Surface(
                                shape = RoundedCornerShape(50),
                                color = bgColor,
                                contentColor = contentColor,
                                onClick = { selectedFilter = filter },
                                modifier = Modifier.height(36.dp),
                                shadowElevation = if(isSelected) 4.dp else 0.dp
                                // Note: Border handling logic simplified here
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                ) {
                                    if (filter == "Tùy chọn") {
                                        Icon(
                                            Icons.Outlined.CalendarMonth, 
                                            contentDescription = null, 
                                            modifier = Modifier.size(18.dp).padding(end = 4.dp)
                                        )
                                    }
                                    Text(filter, style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium))
                                }
                            }
                        }
                    }
                }
            }

            // Main Chart Card
            item {
                AnalyticsChartCard(onClick = onNavigateToSalesAnalysis)
            }

            // Key Metrics Grid
            item {
                KeyMetricsGrid()
            }

            // Detailed List
            item {
                TopProductsSection()
            }
            
            // Export Actions
            item {
                ExportActions()
            }
        }
    }
}

@Composable
fun AnalyticsChartCard(onClick: () -> Unit = {}) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp),
        modifier = Modifier
            .border(1.dp, Color.LightGray.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text("Tổng doanh thu", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
                    Text("4.5B VNĐ", style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold))
                }
                Surface(
                    color = Color(0xFF10B981).copy(alpha = 0.1f),
                    shape = RoundedCornerShape(50)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Outlined.TrendingUp, contentDescription = null, tint = Color(0xFF10B981), modifier = Modifier.size(16.dp))
                        Text("+12%", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = Color(0xFF10B981))
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            
            // Chart Area simulating SVG
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(192.dp)) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val width = size.width
                    val height = size.height
                    
                    val path = Path().apply {
                        moveTo(0f, height * 0.8f) // Start
                        cubicTo(
                            width * 0.1f, height * 0.75f,
                            width * 0.16f, height * 0.4f,
                            width * 0.26f, height * 0.45f
                        )
                         cubicTo(
                            width * 0.36f, height * 0.5f,
                            width * 0.43f, height * 0.2f,
                            width * 0.53f, height * 0.25f
                        )
                        cubicTo(
                            width * 0.63f, height * 0.3f,
                            width * 0.7f, height * 0.6f,
                            width * 0.8f, height * 0.5f
                        )
                        cubicTo(
                            width * 0.9f, height * 0.4f,
                            width * 0.96f, height * 0.1f,
                            width, height * 0.05f
                        )
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

                    // Points (Simulated at approximate curves)
                    val points = listOf(
                        Pair(0.26f, 0.45f),
                        Pair(0.53f, 0.25f),
                        Pair(0.8f, 0.5f)
                    )
                    
                    points.forEach { (xPercent, yPercent) ->
                        drawCircle(
                            color = Color(0xFF137fec),
                            radius = 4.dp.toPx(),
                            center = androidx.compose.ui.geometry.Offset(width * xPercent, height * yPercent),
                            style = Stroke(width = 2.dp.toPx())
                        )
                         drawCircle(
                            color = Color.White,
                            radius = 2.dp.toPx(),
                            center = androidx.compose.ui.geometry.Offset(width * xPercent, height * yPercent)
                        )
                    }
                }
            }
            
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                listOf("T2", "T3", "T4", "T5", "T6", "T7", "CN").forEach { day ->
                    Text(day, style = MaterialTheme.typography.labelSmall, color = Color.LightGray)
                }
            }
        }
    }
}

@Composable
fun KeyMetricsGrid() {
    val metrics = remember { dummyMetrics }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        metrics.forEach { metric ->
            MetricCard(metric, Modifier.weight(1f))
        }
    }
}

@Composable
fun MetricCard(metric: AnalysisMetric, modifier: Modifier = Modifier) {
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
                        .background(metric.iconBgColor, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(metric.icon, contentDescription = null, tint = metric.iconTint, modifier = Modifier.size(18.dp))
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(metric.title, style = MaterialTheme.typography.labelMedium, color = Color.Gray)
            }
            Text(metric.value, style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 4.dp)) {
                 Icon(
                     if (metric.isPositiveChange) Icons.Outlined.ArrowUpward else Icons.Outlined.ArrowDownward,
                     contentDescription = null,
                     tint = if (metric.isPositiveChange) Color(0xFF10B981) else Color(0xFFEF4444),
                     modifier = Modifier.size(14.dp)
                 )
                Text(
                    metric.changeText,
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                    color = if (metric.isPositiveChange) Color(0xFF10B981) else Color(0xFFEF4444)
                )
            }
        }
    }
}

@Composable
fun TopProductsSection() {
    val products = remember { dummyTopProducts }
    Column {
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Top sản phẩm bán chạy", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
            Text("Xem tất cả", style = MaterialTheme.typography.labelMedium, color = Color(0xFF137fec))
        }
        
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.3f))
        ) {
            products.forEachIndexed { index, product ->
                 TopProductItem(product)
                 if (index < products.size - 1) {
                     Divider(color = Color.LightGray.copy(alpha = 0.2f))
                 }
            }
        }
    }
}

@Composable
fun TopProductItem(product: TopProduct) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = product.imageUrl,
            contentDescription = null,
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.LightGray.copy(alpha = 0.5f)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(product.name, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold), maxLines = 1)
            Text(product.variant, style = MaterialTheme.typography.labelSmall, color = Color.Gray, maxLines = 1)
        }
        Column(horizontalAlignment = Alignment.End) {
             Text(product.price, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
             Text(product.soldCount, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
        }
    }
}

@Composable
fun ExportActions() {
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        OutlinedButton(
            onClick = {},
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color.LightGray)
        ) {
            Icon(Icons.Outlined.FileDownload, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Xuất CSV", color = MaterialTheme.colorScheme.onSurface)
        }
        
        Button(
            onClick = {},
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF137fec)),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
        ) {
            Icon(Icons.Outlined.PictureAsPdf, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Tải PDF")
        }
    }
}

@Preview
@Composable
fun AdminAnalyticsScreenPreview() {
    PandQApplicationTheme {
        AdminAnalyticsScreen()
    }
}
