package com.group1.pandqapplication.ui.comparison

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
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarHalf
import androidx.compose.material.icons.filled.StarOutline
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.foundation.Canvas
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.group1.pandqapplication.shared.ui.theme.ComparisonBackgroundDark
import com.group1.pandqapplication.shared.ui.theme.ComparisonBackgroundLight
import com.group1.pandqapplication.shared.ui.theme.ComparisonBorder
import com.group1.pandqapplication.shared.ui.theme.ComparisonPlaceholderText
import com.group1.pandqapplication.shared.ui.theme.ComparisonPrimary
import com.group1.pandqapplication.shared.ui.theme.ComparisonTextWhite

@Composable
fun ComparisonScreen(
    onBackClick: () -> Unit = {}
) {
    val isDarkTheme = true // Specific to this screen design which seems dark-themed by default based on HTML
    
    val backgroundColor = if (isDarkTheme) ComparisonBackgroundDark else ComparisonBackgroundLight
    val textPrimary = ComparisonTextWhite
    val textSecondary = ComparisonPlaceholderText
    val borderColor = ComparisonBorder

    Scaffold(
        containerColor = backgroundColor,
        topBar = {
             Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(backgroundColor.copy(alpha = 0.8f))
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
             ) {
                 IconButton(onClick = onBackClick) {
                     Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back", tint = textPrimary)
                 }
                 Text(
                     "So sánh sản phẩm",
                     color = textPrimary,
                     fontSize = 18.sp,
                     fontWeight = FontWeight.Bold,
                     modifier = Modifier.weight(1f),
                     textAlign = TextAlign.Center
                 )
                 Spacer(modifier = Modifier.size(48.dp)) // Balance icon button
             }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Sticky-like Product Header (Not actually sticky in this simplified LazyColumn, but first item)
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Left column placeholder for labels row below (empty or part of scrolling) - Design has products in grid from start?
                    // Re-reading loop: "grid grid-cols-[repeat(3,minmax(150px,1fr))]" - Actually design is a horizontal list of 3 items.
                    // But main content has "grid-cols-[1fr_repeat(2,minmax(150px,1fr))]".
                    // The first column is "Labels" e.g. Price.
                    // Let's implement this as a Row with weights.
                    // Col 1: Label (Weight 1.0)
                    // Col 2: Product A (Weight 1.25)
                    // Col 3: Product B (Weight 1.25)
                    
                    // Actually usually header row only has Products. The "Label" column is empty in header row?
                    // HTML header has 3 div items: Product 1, Product 2, Add Product.
                    // HTML Table has: Label, P1 Value, P2 Value.
                    // So visually:
                    // Header: [ ... Product 1 ... ] [ ... Product 2 ... ] [ ... Add New ... ] 
                    // This creates misalignment if not careful.
                    // Let's force a layout:
                    // Row 1: Header. Item 1 is Product 1. Item 2 is Product 2. Item 3 is Add Product placeholder.
                    // Row 2+: Table rows. Item 1 Label, Item 2 P1 Value, Item 3 P2 Value.
                    // This means Add Product column is "extra" if we only compare 2. Or we compare 3.
                    // Let's align columns.
                    // Col 1: Label (or empty in header)
                    // Col 2: Product 1
                    // Col 3: Product 2
                    
                    // HTML structure: Header has grid-cols-3. Table has grid-cols-[1fr_2cols].
                    // This implies the header scrolls with content? Yes sticky.
                    // Let's make the header align with the content columns.
                    // Col 0: Labels (Width ~100dp)
                    // Col 1: Product 1 (Weight 1)
                    // Col 2: Product 2 (Weight 1)
                    
                    // Simplified implementation:
                    // We will put "Labels" column as width 100dp.
                    // Products share remaining width.
                    
                    Column(modifier = Modifier.weight(1f)) {
                         ProductHeaderItem(
                             "iPhone 15 Pro",
                             "https://lh3.googleusercontent.com/aida-public/AB6AXuDOvi9X2uN7zIH4ez8AaHBSXxqXmaSEGYPHMuV7NLWgLS3t5x9dABs5y-fa8BADBQXlDwhK2l6xK_EKLwcO1oxKVUzva7NZ99eA5T2CF-rGwA7SxcIe2QC3H2383pDk1JFHiTNSjWIaV1NwVLSplQkHin86q1suikxb5rgqOwRknpFpK9055ok7pNTav7dImnhYIpX3wwfucrTSTM_xF4mB5JeLRnM2tC7q1HvCxB5S6GMIK0LeViJzHo9NQvYPHMYlllJFIhQM0AM"
                         )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                         ProductHeaderItem(
                             "Galaxy S24 Ultra",
                             "https://lh3.googleusercontent.com/aida-public/AB6AXuBxTsYFwOkeCllHmoPGwf9NEOltY3x_G8VmeDps4AMk02tpka79nr5bWLdWsIgCrZw9sTjKRbcF8zcno-iSyQS4pjEHkJ3otZndxUgpVgQmqiNvv9ahgD9_MS49Bo9ggwajYi_wwXVwBEEmeGldjAI04vlzuLzurFycmSDIrUUUhZ5HOQYvBqCHM72W9WDnmw-V_-TaXJgjWF48CqnOjBEHFCoF2tTN4kWRQukUFJz3q6KKALsv0VR9Z-ons3cwXd0I9A2MKcKSAvQ"
                         )
                    }
                    // Optional Add Product Button could be here or floating. HTML has it in grid.
                }
            }
            
            // Overview Section
            item { SectionHeader("Tổng quan", textPrimary) }
            item { ComparisonRow("Giá", "28.990.000₫", "25.490.000₫", borderColor, textSecondary, textPrimary) }
            item { RatingRow("Đánh giá", 4.5, 4.0, "(1,234)", "(987)", borderColor, textSecondary) }
            
            // Specs Section
            item { SectionHeader("Thông số kỹ thuật", textPrimary) }
            item { ComparisonRow("Màn hình", "6.1\" Super Retina XDR", "6.8\" Dynamic AMOLED 2X", borderColor, textSecondary, textPrimary) }
            item { ComparisonRow("Vi xử lý", "Apple A17 Pro", "Snapdragon 8 Gen 3", borderColor, textSecondary, textPrimary) }
            item { ComparisonRow("RAM", "8 GB", "12 GB", borderColor, textSecondary, textPrimary) }
            item { ComparisonRow("Bộ nhớ", "256 GB", "256 GB", borderColor, textSecondary, textPrimary) }
            item { ComparisonRow("Camera", "Chính 48MP & Phụ 12MP, 12MP", "Chính 200MP & Phụ 12MP, 10MP, 10MP", borderColor, textSecondary, textPrimary) }
            
            // Features Section
            item { SectionHeader("Tính năng", textPrimary) }
            item { ComparisonRow("Kết nối", "5G, Wi-Fi 6E", "5G, Wi-Fi 7", borderColor, textSecondary, textPrimary) }
            item { ComparisonRow("Kháng nước", "IP68", "IP68", borderColor, textSecondary, textPrimary) }
            
            item { Spacer(modifier = Modifier.height(32.dp)) }
        }
    }
}

@Composable
fun ProductHeaderItem(name: String, imageUrl: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Box {
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Gray),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(4.dp)
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.2f))
                    .clickable { },
                contentAlignment = Alignment.Center
            ) {
                 Icon(Icons.Default.Close, null, tint = Color.White, modifier = Modifier.size(16.dp))
            }
        }
        Text(name, fontWeight = FontWeight.Medium, fontSize = 14.sp, color = ComparisonTextWhite, maxLines = 1, overflow = TextOverflow.Ellipsis)
        Button(
            onClick = {},
            modifier = Modifier.fillMaxWidth().height(36.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = ComparisonPrimary, contentColor = Color.White),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
        ) {
            Text("Thêm vào giỏ", fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun SectionHeader(title: String, textColor: Color) {
    Text(
        text = title,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = textColor,
        modifier = Modifier.padding(16.dp)
    )
}

@Composable
fun ComparisonRow(
    label: String,
    val1: String,
    val2: String,
    borderColor: Color,
    labelColor: Color,
    valueColor: Color
) {
    Column {
        HorizontalDivider(color = borderColor)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
             Text(label, fontSize = 14.sp, color = labelColor, modifier = Modifier.width(100.dp))
             Text(val1, fontSize = 14.sp, color = valueColor, textAlign = TextAlign.Center, modifier = Modifier.weight(1f))
             Text(val2, fontSize = 14.sp, color = valueColor, textAlign = TextAlign.Center, modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun RatingRow(
    label: String,
    rating1: Double,
    rating2: Double,
    count1: String,
    count2: String,
    borderColor: Color,
    labelColor: Color
) {
    Column {
        HorizontalDivider(color = borderColor)
        Row(
             modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
             Text(label, fontSize = 14.sp, color = labelColor, modifier = Modifier.width(100.dp))
             
             Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                 FiveStarRow(rating1)
                 Text(count1, fontSize = 12.sp, color = labelColor)
             }
             
             Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                 FiveStarRow(rating2)
                 Text(count2, fontSize = 12.sp, color = labelColor)
             }
        }
    }
}

@Composable
fun FiveStarRow(rating: Double) {
    Row {
        repeat(5) { index ->
            val icon = when {
                index < rating.toInt() -> Icons.Default.Star
                index == rating.toInt() && rating % 1 != 0.0 -> Icons.Default.StarHalf
                else -> Icons.Default.StarOutline // Or just Star with darker tint
            }
            Icon(icon, null, tint = ComparisonPrimary, modifier = Modifier.size(16.dp))
        }
    }
}

@Preview
@Composable
fun PreviewComparisonScreen() {
    ComparisonScreen()
}
