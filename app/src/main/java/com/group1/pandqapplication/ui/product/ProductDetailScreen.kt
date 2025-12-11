package com.group1.pandqapplication.ui.product

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProductDetailScreen(
    onBackClick: () -> Unit,
    onCartClick: () -> Unit
) {
    val primaryColor = Color(0xFFec3713)
    val scrollState = rememberScrollState()

    // Mock Data
    val productImages = listOf(
        "https://lh3.googleusercontent.com/aida-public/AB6AXuD6ekfHt4i-b0oqFNzUuqYt5Us72Q8lJ1BYLLwR_8Tx4D0gd34JqRht6u4PgDi7TW-X4ZeeJG2Od4xBzBT4sCVcrD1iRs-v_MgsqZc4CVkRQjgLCoK5MEpULueDc638ZetstBxLck3fzV09gILj66h3_BWkixMyarXtHiwQdOJwr83R4tBtfk7KnlsFayMDvTpfC8-hbKEsICtO3qmeVeZvSxeypB5symdMx-5MBv1r7YYnFTmHik9ndeDW5S67yI3IM8o-rR9PX9Q",
        "https://lh3.googleusercontent.com/aida-public/AB6AXuBV-zRSiBPL3uYE3FXVe66ALoYgpr8jmL1faAg7W10lGc5rzKa8Ha_pU41qh0bQXtIMg8P6d45TyHIo8gO9LXtFBMp_d_-dhZQKfEZkx8nCQ5jzvSB_C9cBNi3yuEo7u6ZLI3wggEJRoJYDCvE6xDQ8S2TPvt23PIjMO61uw7RkbUk-YTKop2jv_TP6Za9FXKtCHwOkGx8-8YlXtJLL2W1mp0nZjuYkcgx-CO82WaukZZf6rKDFQp0znd1B1otpWQub8ydBf3fWPs4"
    )
    val specs = listOf(
        "Battery" to "30 Hrs",
        "Connect" to "5.2 BT",
        "Weight" to "250g",
        "Color" to "Black"
    )
    val specIcons = listOf(
        Icons.Filled.BatteryFull,
        Icons.Filled.Bluetooth,
        Icons.Filled.MonitorWeight,
        Icons.Filled.Palette
    )

    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Description", "Specifications", "Reviews")
    
    // Bottom Cart Bar State
    var quantity by remember { mutableIntStateOf(1) }

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFFF8F6F6))) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp) // Space for bottom cart bar
                .verticalScroll(scrollState)
        ) {
            // Header Image Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(4f / 5f)
            ) {
                val pagerState = rememberPagerState(pageCount = { productImages.size })
                HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) { page ->
                    AsyncImage(
                        model = productImages[page],
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                
                // Indicators
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    repeat(productImages.size) { iteration ->
                        val color = if (pagerState.currentPage == iteration) primaryColor else Color.White.copy(alpha = 0.5f)
                        Box(
                            modifier = Modifier
                                .padding(2.dp)
                                .clip(CircleShape)
                                .background(color)
                                .size(if (pagerState.currentPage == iteration) 24.dp else 6.dp, 6.dp)
                        )
                    }
                }
                
                // Top Bar Overlay
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Brush.verticalGradient(listOf(Color.Black.copy(alpha = 0.5f), Color.Transparent)))
                        .padding(top = 40.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color.White.copy(alpha = 0.2f), CircleShape)
                    ) {
                        Icon(Icons.Filled.ArrowBackIosNew, contentDescription = "Back", tint = Color.White)
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Cart Button
                        IconButton(
                            onClick = onCartClick,
                            modifier = Modifier
                                .size(40.dp)
                                .background(Color.White.copy(alpha = 0.2f), CircleShape)
                        ) {
                            Icon(Icons.Filled.ShoppingBag, contentDescription = "Cart", tint = Color.White)
                        }
                        IconButton(
                            onClick = {},
                            modifier = Modifier
                                .size(40.dp)
                                .background(Color.White.copy(alpha = 0.2f), CircleShape)
                        ) {
                            Icon(Icons.Filled.Share, contentDescription = "Share", tint = Color.White)
                        }
                        IconButton(
                            onClick = {},
                            modifier = Modifier
                                .size(40.dp)
                                .background(Color.White.copy(alpha = 0.2f), CircleShape)
                        ) {
                            Icon(Icons.Filled.Favorite, contentDescription = "Favorite", tint = primaryColor)
                        }
                    }
                }
            }

            // Products Details Content
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "Sony WH-1000XM5 Wireless Headphones",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF18181B),
                    lineHeight = 32.sp
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "$348.00",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = primaryColor
                    )
                    Surface(
                        color = Color(0xFF22C55E).copy(alpha = 0.1f),
                        shape = RoundedCornerShape(100.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF22C55E).copy(alpha = 0.2f))
                    ) {
                        Text(
                            text = "In Stock",
                            color = Color(0xFF16A34A),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.dp)
                        )
                    }
                }

                // Rating
                Row(
                    modifier = Modifier.padding(top = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Filled.Star, contentDescription = null, tint = Color(0xFFEAB308), modifier = Modifier.size(18.dp))
                    Text(
                        text = "4.8",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                    Text(
                        text = "(1,240 reviews)",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "Read all",
                        color = primaryColor,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .clickable { selectedTab = 2 } // Go to review tab
                    )
                }
                
                Text(
                    text = "Industry-leading noise canceling with two processors controlling 8 microphones for unprecedented noise cancellation. With Auto NC Optimizer, noise canceling is automatically optimized based on your wearing conditions and environment.",
                    color = Color(0xFF52525B),
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    modifier = Modifier.padding(top = 16.dp)
                )

                // Tabs
                Spacer(modifier = Modifier.height(24.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    tabs.forEachIndexed { index, title ->
                        Column(
                            modifier = Modifier
                                .padding(end = 24.dp)
                                .clickable { selectedTab = index }
                        ) {
                            Text(
                                text = title,
                                fontSize = 14.sp,
                                fontWeight = if (selectedTab == index) FontWeight.SemiBold else FontWeight.Medium,
                                color = if (selectedTab == index) primaryColor else Color(0xFF71717A)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            if (selectedTab == index) {
                                Box(
                                    modifier = Modifier
                                        .width(24.dp)
                                        .height(2.dp)
                                        .background(primaryColor)
                                )
                            }
                        }
                    }
                }
                HorizontalDivider(color = Color(0xFFE4E4E7))

                Spacer(modifier = Modifier.height(24.dp))
                
                when (selectedTab) {
                    0 -> Text("Detailed description content goes here...", color = Color(0xFF52525B))
                    1 -> SpecificationsSection(specs, specIcons)
                    2 -> ReviewsSection()
                }
            }
        }
        
        // Sticky Bottom Bar
        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            color = Color.White.copy(alpha = 0.95f),
            shadowElevation = 8.dp,
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF1F5F9).copy(alpha = 0.5f))
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Quantity Selector
                Surface(
                    shape = CircleShape,
                    color = Color(0xFFF1F5F9),
                    modifier = Modifier.height(48.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    ) {
                        IconButton(onClick = { if (quantity > 1) quantity-- }) {
                            Icon(Icons.Filled.Remove, contentDescription = "Decrease", tint = Color.Gray)
                        }
                        Text(
                            text = quantity.toString(),
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.width(20.dp),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        IconButton(onClick = { quantity++ }) {
                            Icon(Icons.Filled.Add, contentDescription = "Increase", tint = Color.Gray)
                        }
                    }
                }
                
                // Add to Cart Button
                Button(
                    onClick = { /* TODO */ },
                    modifier = Modifier.weight(1f).height(48.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = primaryColor)
                ) {
                    Icon(Icons.Filled.ShoppingBag, contentDescription = null, modifier = Modifier.size(20.dp))
                    Text("Add to Cart", modifier = Modifier.padding(start = 8.dp), fontWeight = FontWeight.Bold)
                    Text("| $${348 * quantity}.00", modifier = Modifier.padding(start = 8.dp), color = Color.White.copy(alpha = 0.8f))
                }
            }
        }
    }
}

@Composable
fun SpecificationsSection(specs: List<Pair<String, String>>, icons: List<ImageVector>) {
    Column {
        val rows = specs.chunked(2)
        rows.forEach { rowSpecs ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                rowSpecs.forEachIndexed { index, pair ->
                    val flatIndex = specs.indexOf(pair)
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White)
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = icons.getOrElse(flatIndex) { Icons.Filled.Info },
                            contentDescription = null,
                            tint = Color(0xFF94A3B8), // slate-400
                            modifier = Modifier.size(24.dp)
                        )
                        Column(modifier = Modifier.padding(start = 12.dp)) {
                            Text(pair.first.uppercase(), fontSize = 10.sp, color = Color(0xFF64748B))
                            Text(pair.second, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF0F172A))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ReviewsSection() {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Reviews & Ratings", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text("Write a review", color = Color(0xFFec3713), fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
        }
        
        // Summary Card
        Surface(
            color = Color.White,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
        ) {
            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("4.8", fontSize = 32.sp, fontWeight = FontWeight.Bold)
                    Row {
                        repeat(4) { Icon(Icons.Filled.Star, null, tint = Color(0xFFEAB308), modifier = Modifier.size(16.dp)) }
                        Icon(Icons.Filled.StarHalf, null, tint = Color(0xFFEAB308), modifier = Modifier.size(16.dp))
                    }
                    Text("1,240 ratings", fontSize = 12.sp, color = Color.Gray)
                }
                Spacer(modifier = Modifier.width(24.dp))
                // Progress bars (Simplified)
                Column(modifier = Modifier.weight(1f)) {
                    RatingBar(5, 0.85f)
                    RatingBar(4, 0.10f)
                    RatingBar(3, 0.03f)
                    RatingBar(2, 0.01f)
                    RatingBar(1, 0.01f)
                }
            }
        }
        
        ReviewItem(
            name = "John D.",
            time = "2 days ago",
            content = "Best headphones I've owned. The noise cancellation is unreal for travel.",
            imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuDoqgopaoAIPe_E9U965naw3fpVD7GVtwVNDhiJTxHSOgEyMM-X_tEfmvXiMLxysMqp-J8R1XPuAgch4USucpTYUbLHNJOfSfvRNnGrX5vifRYebDiotlZeEBY6JHrAXwG1fmfePNBo_XGx3s8znHw5dNevLq1VTEL3OOP9l70Iuibbrt2n6kmb5L4GZ2NsC71zSLp8JhuuZEg1BY2mDFTMGrHrkmJcAIbEoeMd1IpKj14lYK7trw3v1pLQYLsCEo20TheJucTx9Ic"
        )
        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = Color(0xFFF1F5F9))
        ReviewItem(
            name = "Sarah M.", 
            time = "1 week ago",
            content = "Great sound quality, but the clamping force is a bit tight on my head after 3 hours.",
            imageUrl = null,
            initials = "SM"
        )
        
        OutlinedButton(
            onClick = {},
            modifier = Modifier.fillMaxWidth().padding(top = 24.dp),
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
        ) {
            Text("See all 1,240 reviews", color = Color(0xFF0F172A))
        }
    }
}

@Composable
fun RatingBar(stars: Int, progress: Float) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 2.dp)) {
        Text(stars.toString(), fontSize = 10.sp, color = Color.Gray, modifier = Modifier.width(12.dp))
        Box(modifier = Modifier.weight(1f).height(6.dp).clip(CircleShape).background(Color(0xFFF1F5F9))) {
            Box(modifier = Modifier.fillMaxWidth(progress).fillMaxHeight().background(Color(0xFFec3713)))
        }
    }
}

@Composable
fun ReviewItem(name: String, time: String, content: String, imageUrl: String? = null, initials: String? = null) {
    Column {
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (imageUrl != null && initials == null) {
                    // Profile Image placeholder if we had one, logic here assumes imageUrl is attached photo usually, 
                    // but for avatar lets use a colored box
                     Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(Color.LightGray))
                } else {
                     Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(Color(0xFFE9D5FF)), contentAlignment = Alignment.Center) {
                         Text(initials ?: "U", color = Color(0xFF7E22CE), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                     }
                }
                
                Column(modifier = Modifier.padding(start = 12.dp)) {
                    Text(name, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                    Row {
                        repeat(5) { Icon(Icons.Filled.Star, null, tint = Color(0xFFEAB308), modifier = Modifier.size(12.dp)) }
                    }
                }
            }
            Text(time, fontSize = 12.sp, color = Color.Gray)
        }
        Text(content, fontSize = 14.sp, color = Color(0xFF475569), modifier = Modifier.padding(top = 8.dp))
        if (imageUrl != null) {
            AsyncImage(
                model = imageUrl, contentDescription = null, 
                modifier = Modifier.padding(top = 8.dp).size(100.dp, 80.dp).clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
        }
    }
}
