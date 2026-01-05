package com.group1.pandqapplication.admin.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddBox
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.group1.pandqapplication.admin.data.remote.dto.DashboardSummaryResponse
import com.group1.pandqapplication.shared.ui.theme.PandQApplicationTheme
import java.math.BigDecimal

@Composable
fun AdminDashboardScreen(
    viewModel: AdminDashboardViewModel = hiltViewModel(),
    onNavigateToOrders: () -> Unit = {},
    onNavigateToPromotions: () -> Unit = {},
    onNavigateToAddProduct: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    onNavigateToInventory: () -> Unit = {},
    onNavigateToCategory: () -> Unit = {},
    onNavigateToBranch: () -> Unit = {},
    onNavigateToSupplier: () -> Unit = {},
    onNavigateToRoles: () -> Unit = {},
    onNavigateToAnalytics: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {},
    onNavigateToCustomer: () -> Unit = {},
    onNavigateToShipping: () -> Unit = {},
    onMenuClick: () -> Unit = {},
    userName: String = "Admin",
    avatarUrl: String? = null
) {
    val uiState by viewModel.uiState.collectAsState()
    
    // Convert API data to UI model or use dummy if loading/error
    val overviewData = remember(uiState.summary) {
        uiState.summary?.let { summary ->
            listOf(
                OverviewData(
                    title = "Total Revenue",
                    value = formatCurrency(summary.totalRevenue ?: BigDecimal.ZERO),
                    subtitle = summary.revenueSubtitle ?: "vs last month",
                    icon = Icons.Default.TrendingUp,
                    type = OverviewType.REVENUE,
                    trend = summary.revenueTrend
                ),
                OverviewData(
                    title = "Total Orders",
                    value = (summary.totalOrders ?: 0).toString(),
                    subtitle = "${summary.pendingOrders ?: 0} pending",
                    icon = Icons.Default.LocalShipping,
                    type = OverviewType.ORDERS
                ),
                OverviewData(
                    title = "Low Stock Alerts",
                    value = (summary.lowStockAlerts ?: 0).toString(),
                    subtitle = "Needs attention",
                    icon = Icons.Default.Warning,
                    type = OverviewType.ALERTS
                )
            )
        } ?: dummyOverview
    }
    
    val recentActivities = remember(uiState.summary) {
        uiState.summary?.recentActivities?.map { activity ->
            RecentActivity(
                id = activity.id ?: "",
                title = activity.title ?: "",
                subtitle = activity.subtitle ?: "",
                status = activity.status ?: "",
                statusColor = parseStatusColor(activity.statusColor),
                time = activity.time ?: "",
                imageUrl = activity.imageUrl,
                isAlert = activity.isAlert ?: false
            )
        } ?: dummyActivities
    }
    
    val quickActions = listOf(
        QuickAction("Add Product", Icons.Default.AddBox, Color(0xFFA855F7)), // Purple
        QuickAction("Manage Orders", Icons.Default.LocalShipping, Color(0xFF3B82F6)), // Blue
        QuickAction("Analytics", Icons.Default.BarChart, Color(0xFF10B981)), // Emerald
        QuickAction("Promotions", Icons.Default.Campaign, Color(0xFFF97316)), // Orange
        QuickAction("Inventory", Icons.Default.Inventory, Color(0xFFEAB308)), // Yellow
        QuickAction("Category", Icons.Default.Widgets, Color(0xFFEC4899)), // Pink
        QuickAction("Branch", Icons.Default.Storefront, Color(0xFF6366F1)), // Indigo
        QuickAction("Supplier", Icons.Default.Inventory, Color(0xFF14B8A6)), // Teal
        QuickAction("Roles", Icons.Default.ManageAccounts, Color(0xFF64748B)), // Slate
        QuickAction("Settings", Icons.Default.Settings, Color(0xFF607D8B)), // Blue Grey
        QuickAction("Customer", Icons.Default.ManageAccounts, Color(0xFFE91E63)), // Pink
        QuickAction("Shipping", Icons.Default.LocalShipping, Color(0xFF00BCD4)) // Cyan
    )

    // State for staggered animation
    var isVisible by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        isVisible = true
    }

    Scaffold(
        topBar = { 
            DashboardAppbar(
                onAvatarClick = onNavigateToProfile, 
                onMenuClick = onMenuClick,
                userName = userName,
                avatarUrl = avatarUrl
            ) 
        },
        containerColor = Color.Transparent // Transparent to show gradient
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFFF8F9FA), Color(0xFFE2E8F0))
                    )
                )
                .padding(paddingValues)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                // Overview Section
                item {
                    androidx.compose.animation.AnimatedVisibility(
                        visible = isVisible,
                        enter = androidx.compose.animation.slideInVertically(initialOffsetY = { 50 }) + androidx.compose.animation.fadeIn()
                    ) {
                        OverviewSection(overviewData)
                    }
                }

                // Quick Actions Section
                item {
                     androidx.compose.animation.AnimatedVisibility(
                        visible = isVisible,
                        enter = androidx.compose.animation.slideInVertically(initialOffsetY = { 50 }, animationSpec = androidx.compose.animation.core.tween(delayMillis = 100)) + androidx.compose.animation.fadeIn(animationSpec = androidx.compose.animation.core.tween(delayMillis = 100))
                    ) {
                        QuickActionsSection(
                            actions = quickActions,
                            onActionClick = { actionTitle ->
                                // Navigation logic keeps same...
                                when(actionTitle) {
                                    "Manage Orders" -> onNavigateToOrders()
                                    "Promotions" -> onNavigateToPromotions()
                                    "Add Product" -> onNavigateToAddProduct()
                                    "Analytics" -> onNavigateToAnalytics()
                                    "Inventory" -> onNavigateToInventory()
                                    "Category" -> onNavigateToCategory()
                                    "Branch" -> onNavigateToBranch()
                                    "Supplier" -> onNavigateToSupplier()
                                    "Roles" -> onNavigateToRoles()
                                    "Settings" -> onNavigateToSettings()
                                    "Customer" -> onNavigateToCustomer()
                                    "Shipping" -> onNavigateToShipping()
                                }
                            }
                        )
                    }
                }

                // Recent Activity Section
                item {
                     androidx.compose.animation.AnimatedVisibility(
                        visible = isVisible,
                        enter = androidx.compose.animation.slideInVertically(initialOffsetY = { 50 }, animationSpec = androidx.compose.animation.core.tween(delayMillis = 200)) + androidx.compose.animation.fadeIn(animationSpec = androidx.compose.animation.core.tween(delayMillis = 200))
                    ) {
                        RecentActivitySection(recentActivities)
                    }
                }
            }
        }
    }
}

@Composable
fun DashboardAppbar(
    onAvatarClick: () -> Unit = {}, 
    onMenuClick: () -> Unit = {},
    userName: String = "Admin",
    avatarUrl: String? = null
) {
    // COMPACT HEADER: Reduced padding, specific height, smaller elements
    Surface(
        color = Color.White.copy(alpha = 0.8f),
        shadowElevation = 4.dp,
        modifier = Modifier.fillMaxWidth().height(64.dp) // Fixed compact height
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp), // Reduced side padding
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onMenuClick, modifier = Modifier.size(36.dp)) { // Smaller touch target visually
                    Icon(Icons.Default.Menu, contentDescription = "Menu", modifier = Modifier.size(20.dp))
                }
                Spacer(modifier = Modifier.width(8.dp))
                
                // Avatar - Compact Size
                Box(
                    modifier = Modifier
                        .size(32.dp) // Reduced from 40dp
                        .clip(CircleShape)
                        .clickable(onClick = onAvatarClick)
                ) {
                     AsyncImage(
                        model = avatarUrl ?: "https://ui-avatars.com/api/?name=${userName.replace(" ", "+")}&size=128&background=ec3713&color=fff",
                        contentDescription = "Profile",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                
                // Text - Compact
                Column {
                    Text(
                        text = "Hello, $userName",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
            
            // Notifications - Compact
            IconButton(
                onClick = { /* Check notifs */ },
                modifier = Modifier
                    .size(36.dp)
                    .background(Color.Gray.copy(alpha = 0.1f), CircleShape)
            ) {
                Box {
                    Icon(Icons.Default.Notifications, contentDescription = "Notifications", modifier = Modifier.size(20.dp), tint = Color(0xFF64748B))
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .size(8.dp)
                            .background(Color(0xFFec3713), CircleShape)
                            .border(1.dp, Color.White, CircleShape)
                    )
                }
            }
        }
    }
}

// ... (Rest of components: OverviewSection, RevenueCard, etc. keep their refined look but reusing existing functions if not changing logic)
// Since the previous tool call updated the cards, we can assume they are good. 
// BUT this tool replaces from line 80 downwards, so I need to re-include the updated card definitions or ensures they are preserved.
// The user instruction says "Replace content from DashboardAppbar onwards...". 
// To avoid deleting the updated cards from previous step, I should target specifically the Scaffold block and Appbar.

// RE-STRATEGY: Target specific blocks to avoid overwriting the complex cards I just wrote. 

// 1. Replace Scaffold block in AdminDashboardScreen
// 2. Replace DashboardAppbar function



@Composable
fun OverviewSection(data: List<OverviewData>) {
    Column {
                
        // Custom Grid Layout for Overview
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            // Revenue Card (Full width logic handled by passing data carefully or hardcoding structure as per design)
            // The HTML has Revenue separate, then a grid of 2 for others.
            val revenue = data.find { it.type == OverviewType.REVENUE }
            val others = data.filter { it.type != OverviewType.REVENUE }

            revenue?.let { item ->
                RevenueCard(item)
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                others.forEach { item ->
                    Box(modifier = Modifier.weight(1f)) {
                        OverviewCard(item)
                    }
                }
            }
        }
    }
}

@Composable
fun RevenueCard(data: OverviewData) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(170.dp)
            .shadow(15.dp, RoundedCornerShape(24.dp), spotColor = Color(0xFFec3713).copy(alpha = 0.5f))
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(Color(0xFFec3713), Color(0xFFF1664C))
                )
            )
    ) {
        // Decorative circles
        Box(
            modifier = Modifier
                .offset(x = 220.dp, y = (-20).dp)
                .size(150.dp)
                .background(Color.White.copy(alpha = 0.1f), CircleShape)
        )
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(x = (-20).dp, y = 20.dp)
                .size(100.dp)
                .background(Color.White.copy(alpha = 0.1f), CircleShape)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(Color.White.copy(alpha = 0.2f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(data.icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = data.title,
                        color = Color.White.copy(alpha = 0.9f),
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
                    )
                }
                
                // Trend badge
                 data.trend?.let {
                     Surface(
                         color = Color.White,
                         shape = RoundedCornerShape(50)
                     ) {
                         Row(
                             modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                             verticalAlignment = Alignment.CenterVertically
                         ) {
                             Icon(
                                 Icons.Default.TrendingUp, 
                                 contentDescription = null, 
                                 tint = Color(0xFFec3713),
                                 modifier = Modifier.size(14.dp)
                             )
                             Spacer(modifier = Modifier.width(4.dp))
                             Text(
                                 text = it,
                                 color = Color(0xFFec3713),
                                 style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                             )
                         }
                     }
                 }
            }

            Column {
                Text(
                    text = data.value,
                    color = Color.White,
                    style = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.Bold, fontSize = 36.sp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = data.subtitle,
                    color = Color.White.copy(alpha = 0.7f),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun OverviewCard(data: OverviewData) {
    val iconColor = if (data.type == OverviewType.ALERTS) Color(0xFFEF4444) else Color(0xFF3B82F6) // Red or Blue
    val gradientColors = if (data.type == OverviewType.ALERTS) 
        listOf(Color(0xFFFEF2F2), Color(0xFFFEE2E2)) 
    else 
        listOf(Color(0xFFEFF6FF), Color(0xFFDBEAFE))

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .shadow(4.dp, RoundedCornerShape(20.dp), spotColor = Color.LightGray.copy(alpha = 0.4f))
            .clip(RoundedCornerShape(20.dp))
            .background(Brush.verticalGradient(gradientColors))
            .border(1.dp, Color.White.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.White, CircleShape)
                    .shadow(2.dp, CircleShape, spotColor = Color.Black.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(data.icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(20.dp))
            }
            
            if (data.type == OverviewType.ALERTS) {
                 Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(Color.Red, CircleShape)
                )
            }
        }
        
        Column {
            Text(
                text = data.value,
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Black),
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = data.title,
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun QuickActionsSection(actions: List<QuickAction>, onActionClick: (String) -> Unit) {
    Column {
        Text(
            text = "Quick Actions",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        // Grid of 3 columns
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            val rows = actions.chunked(3)
            rows.forEach { rowItems ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    rowItems.forEach { action ->
                        Box(modifier = Modifier.weight(1f)) {
                            QuickActionCard(action) { onActionClick(action.title) }
                        }
                    }
                    repeat(3 - rowItems.size) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
fun QuickActionCard(action: QuickAction, onClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            modifier = Modifier.size(64.dp),
            shape = RoundedCornerShape(18.dp),
            color = action.color.copy(alpha = 0.1f),
            onClick = onClick
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    action.icon, 
                    contentDescription = null, 
                    tint = action.color, 
                    modifier = Modifier.size(28.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = action.title,
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium),
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}

@Composable
fun RecentActivitySection(activities: List<RecentActivity>) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Recent Activity",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
                color = MaterialTheme.colorScheme.onBackground
            )
            TextButton(onClick = { /* View All */ }) {
                Text(
                    text = "View All",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = Color(0xFFec3713)
                )
            }
        }
        
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            activities.forEach { activity ->
                RecentActivityItem(activity)
            }
        }
    }
}

@Composable
fun RecentActivityItem(activity: RecentActivity) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 2.dp,
        modifier = Modifier.fillMaxWidth().clickable { /* Detail */ }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (activity.imageUrl != null) {
                AsyncImage(
                    model = activity.imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.LightGray),
                    contentScale = ContentScale.Crop
                )
            } else {
                 Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(activity.statusColor.copy(alpha = 0.1f), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        if (activity.isAlert) Icons.Default.Warning else Icons.Default.Notifications, 
                        contentDescription = null, 
                        tint = activity.statusColor
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = activity.title,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = activity.subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            
            Column(horizontalAlignment = Alignment.End) {
                Surface(
                    color = activity.statusColor.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = activity.status,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = activity.statusColor,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = activity.time,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray
                )
            }
        }
    }
}

// Helper functions
private fun formatCurrency(amount: BigDecimal): String {
    return String.format("%,.0f VND", amount)
}

private fun parseStatusColor(colorString: String?): Color {
    return when (colorString?.lowercase()) {
        "green" -> Color(0xFF10B981)
        "orange" -> Color(0xFFF97316)
        "red" -> Color(0xFFEF4444)
        "blue" -> Color(0xFF3B82F6)
        "purple" -> Color(0xFFA855F7)
        else -> Color.Gray
    }
}

@Preview
@Composable
fun AdminDashboardScreenPreview() {
    PandQApplicationTheme {
        AdminDashboardScreen()
    }
}
