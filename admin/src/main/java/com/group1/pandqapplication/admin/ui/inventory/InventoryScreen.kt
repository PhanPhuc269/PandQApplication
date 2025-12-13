package com.group1.pandqapplication.admin.ui.inventory

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import com.group1.pandqapplication.shared.ui.theme.InventoryAlert
import com.group1.pandqapplication.shared.ui.theme.InventoryBackgroundDark
import com.group1.pandqapplication.shared.ui.theme.InventoryBackgroundLight
import com.group1.pandqapplication.shared.ui.theme.InventoryCardDark
import com.group1.pandqapplication.shared.ui.theme.InventoryCardLight
import com.group1.pandqapplication.shared.ui.theme.InventoryPrimary
import com.group1.pandqapplication.shared.ui.theme.InventoryTextPrimaryDark
import com.group1.pandqapplication.shared.ui.theme.InventoryTextPrimaryLight
import com.group1.pandqapplication.shared.ui.theme.InventoryTextSecondaryDark
import com.group1.pandqapplication.shared.ui.theme.InventoryTextSecondaryLight

@Composable
fun InventoryScreen(
    onBackClick: () -> Unit = {},
    onNavigateToAddProduct: () -> Unit = {}
) {
    val isDarkTheme = false
    
    val backgroundColor = if (isDarkTheme) InventoryBackgroundDark else InventoryBackgroundLight
    val cardColor = if (isDarkTheme) InventoryCardDark else InventoryCardLight
    val textPrimary = if (isDarkTheme) InventoryTextPrimaryDark else InventoryTextPrimaryLight
    val textSecondary = if (isDarkTheme) InventoryTextSecondaryDark else InventoryTextSecondaryLight
    val searchBg = if (isDarkTheme) Color(0xFF1E293B) else Color(0xFFE2E8F0) // slate-800 : slate-200
    val dividerColor = if (isDarkTheme) Color(0xFF334155) else Color(0xFFE2E8F0) // slate-700 : slate-200

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
                        text = "Thống kê tồn kho",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = textPrimary,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToAddProduct,
                containerColor = InventoryPrimary,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.size(56.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(bottom = 80.dp) // Space for FAB
        ) {
            // Search Bar
            item {
                Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .background(searchBg, RoundedCornerShape(8.dp)),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .width(48.dp)
                                .fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = textSecondary
                            )
                        }
                        TextField(
                            value = "",
                            onValueChange = {},
                            placeholder = { Text("Tìm kiếm theo ID, tên, danh mục", color = textSecondary) },
                            modifier = Modifier.weight(1f),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                focusedTextColor = textPrimary,
                                unfocusedTextColor = textPrimary
                            ),
                            singleLine = true
                        )
                    }
                }
            }

            // Stats Carousel
            item {
                LazyRow(
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    item {
                        StatsCard("Giá trị tồn kho", "1,2 tỷ ₫", cardColor, textPrimary, textSecondary)
                    }
                    item {
                        StatsCard("Sản phẩm trong kho", "8,450", cardColor, textPrimary, textSecondary)
                    }
                    item {
                        StatsCard("Sắp hết hàng", "12", cardColor, InventoryAlert, textSecondary)
                    }
                }
            }

            // Low Stock Section
            item {
                Text(
                    text = "Cảnh báo tồn kho thấp",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = textPrimary,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

            items(listOf(
                LowStockItem("iPhone 15 Pro Max 256GB", "Hiện có: 2", "https://lh3.googleusercontent.com/aida-public/AB6AXuA8HbiX10FfCVyqBoFSlNmN3X19C5pevbqkC-DZBBcjGK1KcL-zGmCgMiZAvwte_eDP4H_XMT2s258p8cPJbLKVu4njcY2n5c0LGJCIG47fr8IOIdK2pCYeksBlvOOsbH-pG2OaVwulfz5CzJ8wNk8oNd8wQMg3KBiLeO7Lp8Fa2S83qpD3nMEvKy2yaJpywsYIyv5KSFdezctPZHtrw0_ldnGbcMtGZ8EshGQ3_vQhRzisCLp_0rUhh054V5M04kfjqfH4g-PcMmE"),
                LowStockItem("Samsung Galaxy S24 Ultra", "Hiện có: 5", "https://lh3.googleusercontent.com/aida-public/AB6AXuB5Cwa9Lm4Jw1HNu172zDXynsA1qWAIUhWBzZh4CbKvXnmXTfy_wBLaaQ-ZW42ptBaEMNLeuu2Ah0ZZXWSkTG_JL5RNIKY9-TavyqFTB65m7fMpoOX4r4xPOWiy6oQ51-C4peWpH0hobO1CWszMDklsWosbzw7DbMWmbwzUt9vO_RFtnFxye3cp3or5a5Xl-qxiaJ9I2r_h2LMQHRegEPjwPY4Zpfnux-1AD7m1HOZHnfN-U1M_6cubBo6IedkNpch5ln3SJ1SQZgU")
            )) { item ->
                LowStockRow(item, cardColor, textPrimary, InventoryAlert)
                Spacer(modifier = Modifier.height(12.dp))
            }

            // All Products Header
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Tất cả sản phẩm",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = textPrimary
                    )
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(100.dp))
                            .background(InventoryPrimary.copy(alpha = 0.2f))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                            .clickable { },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = "Filter",
                            tint = InventoryPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Lọc",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = InventoryPrimary
                        )
                    }
                }
            }

            // Inventory List
            val inventoryItems = listOf(
                InventoryItem("Macbook Pro 16\" M3", "SKU: MBP16M3-512", "150", "12", "900tr", "https://lh3.googleusercontent.com/aida-public/AB6AXuCMn_iX4RsnNtwjc01F3qwYlhzbDx6LdUSyK8Rlib9F0hRjyxZxY1ciazmy_qraRUZNuXcRH732IHO2xXxAMu2a-c9diQt0tMJo9Odo2sHyryRDSGOxcqWBOAASr3GzZoUujKnvgL7ZkgX8Z1dXVgy6m7M9akcZoC0MTmHUYyr_bf-N7N9upLUDZ8mNyC4yiTSS7l5igTF16pG8leuUo8y32SO8ZMk8ivfTUUrjzH9oGDBLkpLm85ne2iTKHWYD0lbLXe_TlNT97yw"),
                InventoryItem("Sony WH-1000XM5", "SKU: SNY-XM5-BLK", "78", "4", "624tr", "https://lh3.googleusercontent.com/aida-public/AB6AXuB51kK-Kp1NXWdHEzbYwVS057-Z8OfC_viFkMql6Z3w6nIceKKJQXjOzHcoLeDJmQ0acfOvJRinN2MW6cLcoGE_G28sGKSGx3E7gw0ys7tVHNISmCM4_-tQ7o_cCqq3WdXlADcy2yghha3g5327II1OatLR7SAEMjsRlKnyAy-GeO6MNKwnj6MUqZYlyzjKpq5svY4I1AjxuLPIulxeEYl4Wp-fO8XqQVZa3Uu0fI2MeAfXnfs9zsELcmHErYhqOD7L5MU9HLzypIE"),
                InventoryItem("Apple Watch Series 9", "SKU: APW-S9-45M", "210", "31", "2.1 tỷ", "https://lh3.googleusercontent.com/aida-public/AB6AXuDAsLJGX2SfCdGiZfEGi_iqLjBKxZ3vEdGkayXhB5FgZLTXTkD47awmBZ788tEzPpw9dvRRF99kHnBvTyEsHPPWzienQy9AjfOn5pIuAMD29H7C3vnflIXna1cV45Ij2tvGjcDuq-SgSxULUeGEkSXb42QbIFVwbXPASBIFkCILqxFsaNfs0IYDxWgbEEwHR6vwUHImrnizLpUM8dnPM4LJN689Cp7tOPx8mXWwBiWpt3h0m2lt5cT6_7KpgNqgSYNmB-S5w0lJ35U")
            )

            items(inventoryItems) { item ->
                InventoryItemRow(item, cardColor, dividerColor, textPrimary, textSecondary)
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun StatsCard(
    title: String,
    value: String,
    backgroundColor: Color,
    valueColor: Color,
    titleColor: Color
) {
    Column(
        modifier = Modifier
            .width(160.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = titleColor
        )
        Text(
            text = value,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = valueColor
        )
    }
}

@Composable
fun LowStockRow(
    item: LowStockItem,
    backgroundColor: Color,
    textPrimary: Color,
    alertColor: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .clickable { }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = item.imageUrl,
            contentDescription = null,
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.Gray.copy(alpha = 0.1f)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.name,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                color = textPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = item.status,
                fontSize = 14.sp,
                color = alertColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = Color.Gray,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun InventoryItemRow(
    item: InventoryItem,
    backgroundColor: Color,
    dividerColor: Color,
    textPrimary: Color,
    textSecondary: Color
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .padding(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = item.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Gray.copy(alpha = 0.1f)),
                contentScale = ContentScale.Crop
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column {
                Text(
                    text = item.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = textPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = item.sku,
                    fontSize = 14.sp,
                    color = textSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        
        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = dividerColor)
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            InventoryStatColumn("Hiện có", item.available, textSecondary, textPrimary)
            InventoryStatColumn("Đang giữ", item.held, textSecondary, textPrimary)
            InventoryStatColumn("Giá trị", item.value, textSecondary, textPrimary)
        }
    }
}

@Composable
fun InventoryStatColumn(
    label: String,
    value: String,
    labelColor: Color,
    valueColor: Color
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = labelColor
        )
        Text(
            text = value,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = valueColor
        )
    }
}

data class LowStockItem(
    val name: String,
    val status: String,
    val imageUrl: String
)

data class InventoryItem(
    val name: String,
    val sku: String,
    val available: String,
    val held: String,
    val value: String,
    val imageUrl: String
)

@Preview
@Composable
fun PreviewInventoryScreen() {
    InventoryScreen()
}
