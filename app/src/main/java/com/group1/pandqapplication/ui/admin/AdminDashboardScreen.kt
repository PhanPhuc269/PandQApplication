package com.group1.pandqapplication.ui.admin

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.group1.pandqapplication.shared.ui.theme.AdminBackgroundDark
import com.group1.pandqapplication.shared.ui.theme.AdminBackgroundLight
import com.group1.pandqapplication.shared.ui.theme.AdminCardDark
import com.group1.pandqapplication.shared.ui.theme.AdminCardLight
import com.group1.pandqapplication.shared.ui.theme.AdminError
import com.group1.pandqapplication.shared.ui.theme.AdminPrimary
import com.group1.pandqapplication.shared.ui.theme.AdminSuccess
import com.group1.pandqapplication.shared.ui.theme.AdminTextPrimaryDark
import com.group1.pandqapplication.shared.ui.theme.AdminTextPrimaryLight
import com.group1.pandqapplication.shared.ui.theme.AdminTextSecondaryDark
import com.group1.pandqapplication.shared.ui.theme.AdminTextSecondaryLight

@Composable
fun AdminDashboardScreen() {
    val isDarkTheme = false
    
    val backgroundColor = if (isDarkTheme) AdminBackgroundDark else AdminBackgroundLight
    val cardColor = if (isDarkTheme) AdminCardDark else AdminCardLight
    val textPrimary = if (isDarkTheme) AdminTextPrimaryDark else AdminTextPrimaryLight
    val textSecondary = if (isDarkTheme) AdminTextSecondaryDark else AdminTextSecondaryLight
    val controlBg = if (isDarkTheme) Color(0xFF1F2937) else Color(0xFFE5E7EB) // gray-800 : gray-200
    val borderColor = if(isDarkTheme) Color(0xFF1F2937) else Color(0xFFE5E7EB)

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
                        onClick = { /* Menu */ },
                        modifier = Modifier.align(Alignment.CenterStart)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu",
                            tint = textSecondary
                        )
                    }
                    Text(
                        text = "Dashboard",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = textPrimary,
                        modifier = Modifier.align(Alignment.Center)
                    )
                    IconButton(
                        onClick = { /* Notifications */ },
                        modifier = Modifier.align(Alignment.CenterEnd)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notifications",
                            tint = textSecondary
                        )
                    }
                }
                HorizontalDivider(color = borderColor)
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Segmented Buttons
            item {
                var selectedIndex by remember { mutableStateOf(1) } // Weekly default
                val options = listOf("Daily", "Weekly", "Monthly", "Yearly")
                
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(controlBg, RoundedCornerShape(8.dp))
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    options.forEachIndexed { index, text ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(32.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (selectedIndex == index) cardColor else Color.Transparent)
                                .clickable { selectedIndex = index },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = text,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = if (selectedIndex == index) textPrimary else textSecondary
                            )
                        }
                    }
                }
            }

            // Stats Cards Grid
            item {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    // Mobile stacking or simple column
                   
                    AdminStatCard(
                        title = "Total Revenue",
                        value = "$12,450",
                        change = "+15%",
                        isPositive = true,
                        cardColor = cardColor,
                        textPrimary = textPrimary,
                        textSecondary = textSecondary
                    )
                    
                    AdminStatCard(
                        title = "Gross Profit",
                        value = "$4,820",
                        change = "+8.2%",
                        isPositive = true,
                        cardColor = cardColor,
                        textPrimary = textPrimary,
                        textSecondary = textSecondary
                    )
                    
                    AdminStatCard(
                        title = "New Orders",
                        value = "152",
                        change = "-3.1%",
                        isPositive = false,
                        cardColor = cardColor,
                        textPrimary = textPrimary,
                        textSecondary = textSecondary
                    )
                }
            }

            // Line Chart Card
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(cardColor)
                        .padding(16.dp)
                ) {
                    Column {
                        Text("Revenue Trend", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = textPrimary)
                        Text("$12,450", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                        
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Last 7 Days", fontSize = 14.sp, color = textSecondary)
                            Spacer(modifier = Modifier.width(8.dp))
                             Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.ArrowUpward, 
                                    contentDescription = null, 
                                    tint = AdminSuccess,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text("+15%", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = AdminSuccess)
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        // Line Chart
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp)
                        ) {
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                val width = size.width
                                val height = size.height
                                
                                val path = Path().apply {
                                    moveTo(0f, height * 0.7f)
                                    cubicTo(
                                        width * 0.2f, height * 0.7f,
                                        width * 0.2f, height * 0.2f,
                                        width * 0.4f, height * 0.2f
                                    )
                                    cubicTo(
                                        width * 0.6f, height * 0.2f,
                                        width * 0.6f, height * 0.6f,
                                        width * 0.8f, height * 0.4f
                                    )
                                    lineTo(width, height * 0.5f)
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
                                        colors = listOf(AdminPrimary.copy(alpha = 0.3f), AdminPrimary.copy(alpha = 0f))
                                    )
                                )
                                
                                drawPath(
                                    path = path,
                                    color = AdminPrimary,
                                    style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                                )
                            }
                        }
                        
                        // X-Axis Labels
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun").forEach { day ->
                                Text(day, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = textSecondary.copy(alpha = 0.7f))
                            }
                        }
                    }
                }
            }

            // Bar Chart Card
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(cardColor)
                        .padding(16.dp)
                ) {
                    Column {
                        Text("Gross Profit", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = textPrimary)
                        Text("$4,820", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                        
                         Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("vs. Previous Week", fontSize = 14.sp, color = textSecondary)
                            Spacer(modifier = Modifier.width(8.dp))
                             Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.ArrowUpward, 
                                    contentDescription = null, 
                                    tint = AdminSuccess,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text("+8.2%", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = AdminSuccess)
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp),
                            horizontalArrangement = Arrangement.Center, // Centered columns
                            verticalAlignment = Alignment.Bottom
                        ) {
                           Column(
                               horizontalAlignment = Alignment.CenterHorizontally,
                               modifier = Modifier.padding(horizontal = 24.dp)
                           ) {
                               Box(
                                   modifier = Modifier
                                       .width(40.dp)
                                       .height(100.dp) // 60% approx visual
                                       .clip(RoundedCornerShape(6.dp))
                                       .background(controlBg)
                               )
                               Spacer(modifier = Modifier.height(8.dp))
                               Text("Last Week", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = textSecondary)
                           }
                           
                           Column(
                               horizontalAlignment = Alignment.CenterHorizontally,
                               modifier = Modifier.padding(horizontal = 24.dp)
                           ) {
                               Box(
                                   modifier = Modifier
                                       .width(40.dp)
                                       .height(140.dp) // 80-90% approx visual
                                       .clip(RoundedCornerShape(6.dp))
                                       .background(AdminPrimary)
                               )
                               Spacer(modifier = Modifier.height(8.dp))
                               Text("This Week", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = textSecondary)
                           }
                        }
                    }
                }
            }
            
            // Additional Stats
             item {
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    VerticalStatCard(
                        modifier = Modifier.weight(1f),
                        title = "Growth Rate",
                        value = "12.5%",
                        change = "+1.2%",
                        isPositive = true,
                        cardColor = cardColor,
                        textPrimary = textPrimary,
                        textSecondary = textSecondary
                    )
                     VerticalStatCard(
                        modifier = Modifier.weight(1f),
                        title = "Avg. Value",
                        value = "$81.90",
                        change = "+5.7%",
                        isPositive = true,
                        cardColor = cardColor,
                        textPrimary = textPrimary,
                        textSecondary = textSecondary
                    )
                }
            }
        }
    }
}

@Composable
fun AdminStatCard(
    title: String,
    value: String,
    change: String,
    isPositive: Boolean,
    cardColor: Color,
    textPrimary: Color,
    textSecondary: Color
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(cardColor)
            .padding(16.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(title, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = textSecondary)
            Text(value, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = textPrimary)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = if(isPositive) Icons.Default.ArrowUpward else Icons.Default.ArrowDownward,
                    contentDescription = null,
                    tint = if(isPositive) AdminSuccess else AdminError,
                     modifier = Modifier.size(14.dp)
                )
                Text(
                    text = change,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = if(isPositive) AdminSuccess else AdminError
                )
            }
        }
    }
}

@Composable
fun VerticalStatCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    change: String,
    isPositive: Boolean,
    cardColor: Color,
    textPrimary: Color,
    textSecondary: Color
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(cardColor)
            .padding(16.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(title, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = textSecondary, maxLines = 1)
            Text(value, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = textPrimary)
             Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = if(isPositive) Icons.Default.ArrowUpward else Icons.Default.ArrowDownward,
                    contentDescription = null,
                    tint = if(isPositive) AdminSuccess else AdminError,
                     modifier = Modifier.size(14.dp)
                )
                Text(
                    text = change,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = if(isPositive) AdminSuccess else AdminError
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewAdminDashboardScreen() {
    AdminDashboardScreen()
}
