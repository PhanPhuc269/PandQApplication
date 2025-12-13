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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.group1.pandqapplication.shared.ui.theme.PandQApplicationTheme

@Composable
fun AdminDashboardScreen(
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
    onMenuClick: () -> Unit = {}
) {
    val overviewData = remember { dummyOverview }
    val recentActivities = remember { dummyActivities }
    
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

    Scaffold(
        topBar = { DashboardAppbar(onAvatarClick = onNavigateToProfile, onMenuClick = onMenuClick) },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Overview Section
            item {
                OverviewSection(overviewData)
            }

            // Quick Actions Section
            item {
                QuickActionsSection(
                    actions = quickActions,
                    onActionClick = { actionTitle ->
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

            // Recent Activity Section
            item {
                RecentActivitySection(recentActivities)
            }
        }
    }
}

@Composable
fun DashboardAppbar(onAvatarClick: () -> Unit = {}, onMenuClick: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onMenuClick) {
                Icon(Icons.Default.Menu, contentDescription = "Menu")
            }
            Spacer(modifier = Modifier.width(8.dp))
            // Avatar
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clickable(onClick = onAvatarClick)
            ) {
                 AsyncImage(
                    model = "https://lh3.googleusercontent.com/aida-public/AB6AXuB-MDECu3xTWbvaQAjHgy1A0suAdFwQiXMXT3epXFofjzEr9WRfxAiftf07diywxjiODpNgrHuNrY-l7I4bJ2RMbtye_fdG8IcfRyXTn67blLu8GAYFPnw_oM3oeo30bUEcN3ATo-atTvSOrF4vvwVXCUp-QGViHuAbxDkdgXiEP4dcAUmf5dndvzJuVs7iPRTfiiEVvQZoYkfsTegZ3rmrGi5blOp-Bsn2jZRfC6M-FRdi0sNCu8TERL7A9RyEMCsmdE_HSqUMizI",
                    contentDescription = "Profile",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .border(2.dp, Color(0xFFec3713).copy(alpha = 0.2f), CircleShape),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(12.dp)
                        .background(Color.Green, CircleShape)
                        .border(2.dp, MaterialTheme.colorScheme.background, CircleShape)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "Welcome back,",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
                Text(
                    text = "Admin Dashboard",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
        
        Box {
            IconButton(
                onClick = { /* Check notifs */ },
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface, CircleShape)
                    .size(40.dp)
            ) {
                Icon(Icons.Default.Notifications, contentDescription = "Notifications")
            }
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .size(8.dp)
                    .background(Color(0xFFec3713), CircleShape)
            )
        }
    }
}

@Composable
fun OverviewSection(data: List<OverviewData>) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Overview",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground
            )
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.surface,
                border = androidx.compose.foundation.BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f))
            ) {
                Text(
                    text = "Today",
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    color = Color.Gray
                )
            }
        }
        
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
                modifier = Modifier.fillMaxWidth(),
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
            .height(160.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(Color(0xFFec3713), Color(0xFFb92b0f))
                )
            )
            .padding(20.dp)
    ) {
        Column(
            modifier = Modifier.align(Alignment.TopStart),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = data.title,
                color = Color.White.copy(alpha = 0.8f),
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
            )
            Text(
                text = data.value,
                color = Color.White,
                style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold, fontSize = 28.sp)
            )
        }
        
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(40.dp)
                .background(Color.White.copy(alpha = 0.2f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(data.icon, contentDescription = null, tint = Color.White)
        }
        
        Row(
            modifier = Modifier.align(Alignment.BottomStart),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
             data.trend?.let {
                 Surface(
                     color = Color.White.copy(alpha = 0.2f),
                     shape = RoundedCornerShape(50)
                 ) {
                     Row(
                         modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                         verticalAlignment = Alignment.CenterVertically
                     ) {
                         Icon(
                             Icons.Default.TrendingUp, 
                             contentDescription = null, 
                             tint = Color.White,
                             modifier = Modifier.size(14.dp)
                         )
                         Text(
                             text = it,
                             color = Color.White,
                             style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                         )
                     }
                 }
             }
            Text(
                text = data.subtitle,
                color = Color.White.copy(alpha = 0.7f),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun OverviewCard(data: OverviewData) {
    val iconColor = if (data.type == OverviewType.ALERTS) Color(0xFFec3713) else Color(0xFF3B82F6)
    val iconBg = iconColor.copy(alpha = 0.1f)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp))
            .border(1.dp, Color.LightGray.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(iconBg, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(data.icon, contentDescription = null, tint = iconColor)
            }
            Text(
                text = data.subtitle,
                color = if(data.type == OverviewType.ALERTS) iconColor else Color.Gray,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
            )
        }
        
        Column {
            Text(
                text = data.value,
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = if (data.type == OverviewType.ALERTS) iconColor else MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = data.title,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun QuickActionsSection(actions: List<QuickAction>, onActionClick: (String) -> Unit) {
    Column {
        Text(
            text = "Quick Actions",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        
        // Grid of 2 columns
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            val rows = actions.chunked(2)
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
                }
            }
        }
    }
}

@Composable
fun QuickActionCard(action: QuickAction, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(12.dp))
            .border(1.dp, Color.LightGray.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(action.color.copy(alpha = 0.1f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(action.icon, contentDescription = null, tint = action.color, modifier = Modifier.size(28.dp))
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = action.title,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun RecentActivitySection(activities: List<RecentActivity>) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Recent Activity",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "View All",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                color = Color(0xFFec3713)
            )
        }
        
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp))
                .border(1.dp, Color.LightGray.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
                .clip(RoundedCornerShape(16.dp))
        ) {
            activities.forEachIndexed { index, activity ->
                RecentActivityItem(activity)
                if (index < activities.size - 1) {
                    Divider(color = Color.LightGray.copy(alpha = 0.2f))
                }
            }
        }
    }
}

@Composable
fun RecentActivityItem(activity: RecentActivity) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { /* Detail */ },
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (activity.imageUrl != null) {
            AsyncImage(
                model = activity.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray),
                contentScale = ContentScale.Crop
            )
        } else {
             Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(activity.statusColor.copy(alpha = 0.1f), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Warning, contentDescription = null, tint = activity.statusColor)
            }
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = activity.title,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = if(activity.isAlert) activity.statusColor else MaterialTheme.colorScheme.onSurface
                )
                Surface(
                    color = activity.statusColor.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(50)
                ) {
                    Text(
                        text = activity.status,
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                        color = activity.statusColor,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Text(
                text = activity.subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = activity.time,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                color = Color.LightGray,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Preview
@Composable
fun AdminDashboardScreenPreview() {
    PandQApplicationTheme {
        AdminDashboardScreen()
    }
}
