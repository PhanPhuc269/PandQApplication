package com.group1.pandqapplication.admin.ui.dashboard

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Warning

data class OverviewData(
    val title: String,
    val value: String,
    val subtitle: String,
    val icon: ImageVector,
    val trend: String? = null, // e.g. "+5%"
    val trendPositive: Boolean = true,
    val type: OverviewType
)

enum class OverviewType {
    REVENUE, ORDERS, ALERTS
}

data class QuickAction(
    val title: String,
    val icon: ImageVector,
    val color: Color
)

data class RecentActivity(
    val id: String,
    val title: String,
    val subtitle: String,
    val time: String,
    val status: String,
    val statusColor: Color,
    val imageUrl: String? = null,
    val isAlert: Boolean = false
)

// Dummy Data

val dummyOverview = listOf(
    OverviewData(
        title = "Total Revenue",
        value = "$12,450",
        subtitle = "vs yesterday",
        icon = Icons.Default.AttachMoney,
        trend = "+5%",
        type = OverviewType.REVENUE
    ),
    OverviewData(
        title = "New Orders",
        value = "14",
        subtitle = "PENDING",
        icon = Icons.Default.ShoppingBag,
        type = OverviewType.ORDERS
    ),
    OverviewData(
        title = "Low Stock Items",
        value = "3",
        subtitle = "ALERT",
        icon = Icons.Default.Inventory2,
        type = OverviewType.ALERTS
    )
)

val dummyActivities = listOf(
    RecentActivity(
        id = "9921",
        title = "Order #9921",
        subtitle = "MacBook Pro M2 • $1,200",
        time = "2 mins ago",
        status = "Processing",
        statusColor = Color(0xFFF97316), // Orange
        imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuDwWMuEqxIL2JJXN3U7vY4T0Xy3X0Y6eznPXKM0tU59-J8v7Osg9M9OzGKnZxY79oilP1lil0uLE8T15dKZHNiqkthNtLh13ECfFEdUvF7mpQ6xcY7u16Gqi89-4MddXlRFSaDKQaulhtwPGkflMGJeghXrRczNWgS0L-HDM317ihQ5uTH14RLa0Gn2t66jiAFl3n6dosVoD0CP_nqBWPDAC_RKb8XuwtA5icGoCUEUYAXdcPJmeupdo_ksUD2ie0XozifWY7TfEi4"
    ),
    RecentActivity(
        id = "9920",
        title = "Order #9920",
        subtitle = "Sony WH-1000XM5 • $350",
        time = "1 hour ago",
        status = "Shipped",
        statusColor = Color(0xFF22C55E), // Green
        imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuDF52VXRRiCIPjdYZxv1JpeRih0OOQP8EjugZaaTLVV2nDOZHi4326zI9WpaGaE7Ar1897LK4lRAEdwmQ9l1YBoRAa178RIuuUUhwgucBB_bf7o_ebelUjbmRdw9TPHY3tjwOH8Hl6swSoOGjrPZ7p37I6kmUzPyXV_E9h3j_ZPlcqMmTcVdAY5uLzPMjU_qEg3nhLwiczzEWUIQd9Kc114Egz3VPMkPG01EYra0PU01-X5yds_xCN7x55TCX2oB7PC_8vdXmL2m0Q"
    ),
    RecentActivity(
        id = "alert",
        title = "Low Stock Alert",
        subtitle = "iPhone 15 Pro Max (Titanium) is below 5 units",
        time = "3 hours ago",
        status = "Urgent",
        statusColor = Color(0xFFec3713), // Primary
        isAlert = true
    )
)
