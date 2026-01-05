package com.group1.pandqapplication.admin.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.group1.pandqapplication.admin.ui.navigation.AdminScreen

@Composable
fun AdminDrawerContent(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    onLogout: () -> Unit,
    userName: String = "Admin",
    userRole: String = "Administrator",
    avatarUrl: String? = null
) {
    val scrollState = rememberScrollState()
    val primaryColor = Color(0xFFec3713) // From HTML config

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(320.dp)
            .background(MaterialTheme.colorScheme.surface)
            .verticalScroll(scrollState)
    ) {
        // --- Header Section ---
        Column(
            modifier = Modifier
                .padding(top = 16.dp, bottom = 8.dp, start = 16.dp, end = 16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onNavigate(AdminScreen.Profile.route) }
                    .padding(4.dp)
            ) {
                Box {
                    AsyncImage(
                        model = avatarUrl ?: "https://ui-avatars.com/api/?name=${userName.replace(" ", "+")}&size=128&background=ec3713&color=fff",
                        contentDescription = "Profile",
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .border(2.dp, primaryColor, CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(12.dp)
                            .background(Color.Green, CircleShape)
                            .border(2.dp, MaterialTheme.colorScheme.surface, CircleShape)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = userName,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Surface(
                        color = Color.LightGray.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(100.dp),
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Text(
                            text = userRole,
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                            color = Color.Gray
                        )
                    }
                }
            }
        }

        HorizontalDivider(color = Color.LightGray.copy(alpha = 0.2f))

        // --- Navigation Items ---
        Column(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 16.dp)
                .weight(1f) // Push footer down
                .verticalScroll(rememberScrollState())
        ) {
            
            // Primary Modules
            DrawerSectionHeader("Main")
            DrawerItem(
                label = "Dashboard",
                icon = Icons.Default.Dashboard,
                isSelected = currentRoute == AdminScreen.Dashboard.route,
                onClick = { onNavigate(AdminScreen.Dashboard.route) },
                primaryColor = primaryColor
            )
            DrawerItem(
                label = "Product Management",
                icon = Icons.Default.Inventory, // inventory_2
                isSelected = currentRoute == AdminScreen.Inventory.route,
                onClick = { onNavigate(AdminScreen.Inventory.route) },
                primaryColor = primaryColor
            )
            DrawerItem(
                label = "Order Management",
                icon = Icons.Default.ShoppingCart, // shopping_bag
                isSelected = currentRoute == AdminScreen.Orders.route,
                onClick = { onNavigate(AdminScreen.Orders.route) },
                primaryColor = primaryColor,
                badgeCount = 3
            )

            // Operations Modules
            Spacer(modifier = Modifier.height(16.dp))
            DrawerSectionHeader("Operations")
            DrawerItem(
                label = "Category Management",
                icon = Icons.Default.Widgets,
                isSelected = currentRoute == AdminScreen.CategoryManagement.route,
                onClick = { onNavigate(AdminScreen.CategoryManagement.route) },
                primaryColor = primaryColor
            )
            DrawerItem(
                label = "Branch Management",
                icon = Icons.Default.Storefront,
                isSelected = currentRoute == AdminScreen.BranchManagement.route,
                onClick = { onNavigate(AdminScreen.BranchManagement.route) },
                primaryColor = primaryColor
            )
            DrawerItem(
                label = "Supplier Management",
                icon = Icons.Default.Inventory,
                isSelected = currentRoute == AdminScreen.SupplierManagement.route,
                onClick = { onNavigate(AdminScreen.SupplierManagement.route) },
                 primaryColor = primaryColor
            )
            DrawerItem(
                label = "Auditor/Roles",
                icon = Icons.Default.ManageAccounts,
                isSelected = currentRoute == AdminScreen.RoleManagement.route,
                onClick = { onNavigate(AdminScreen.RoleManagement.route) },
                primaryColor = primaryColor
            )
            DrawerItem(
                label = "Customer Management",
                icon = Icons.Default.People, // group (replaced with People earlier)
                isSelected = currentRoute == AdminScreen.CustomerList.route,
                onClick = { onNavigate(AdminScreen.CustomerList.route) },
                primaryColor = primaryColor
            )
            DrawerItem(
                label = "Shipping Management",
                icon = Icons.Default.LocalShipping,
                isSelected = currentRoute == AdminScreen.ShippingManagement.route,
                onClick = { onNavigate(AdminScreen.ShippingManagement.route) },
                primaryColor = primaryColor
            )

            // Marketing
            Spacer(modifier = Modifier.height(16.dp))
            DrawerSectionHeader("Marketing")
            DrawerItem(
                label = "Promotions",
                icon = Icons.Default.LocalOffer,
                isSelected = currentRoute == AdminScreen.Promotions.route,
                onClick = { onNavigate(AdminScreen.Promotions.route) },
                primaryColor = primaryColor
            )
            DrawerItem(
                label = "Notifications",
                icon = Icons.Default.Notifications,
                isSelected = currentRoute == AdminScreen.NotificationTemplates.route,
                onClick = { onNavigate(AdminScreen.NotificationTemplates.route) },
                primaryColor = primaryColor
            )

            // Analytics
            Spacer(modifier = Modifier.height(16.dp))
            DrawerSectionHeader("Insights")
            DrawerItem(
                label = "Reports & Analytics",
                icon = Icons.Default.BarChart,
                isSelected = currentRoute == AdminScreen.Analytics.route,
                onClick = { onNavigate(AdminScreen.Analytics.route) },
                primaryColor = primaryColor
            )
        }

        // --- Footer Section ---
        Column(
            modifier = Modifier
                .background(Color.Gray.copy(alpha = 0.05f))
                .padding(4.dp)
        ) {
            DrawerItem(
                label = "System Settings",
                icon = Icons.Default.Settings,
                isSelected = currentRoute == AdminScreen.Settings.route,
                onClick = { onNavigate(AdminScreen.Settings.route) },
                primaryColor = primaryColor
            )
            DrawerItem(
                label = "Support",
                icon = Icons.Default.Help,
                isSelected = false,
                onClick = { /* Demo support */ },
                primaryColor = primaryColor
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Logout Button
            Surface(
                onClick = onLogout,
                shape = RoundedCornerShape(12.dp),
                color = Color.Transparent,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp, 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.ExitToApp, // logout
                        contentDescription = null,
                        tint = Color.Red,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Log Out",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                        color = Color.Red
                    )
                }
            }
            

        }
    }
}

@Composable
fun DrawerSectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

@Composable
fun DrawerItem(
    label: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    primaryColor: Color,
    badgeCount: Int? = null
) {
    val bgColor = if (isSelected) primaryColor.copy(alpha = 0.1f) else Color.Transparent
    val contentColor = if (isSelected) primaryColor else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
    val iconColor = if (isSelected) primaryColor else Color.Gray

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = bgColor,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium
                ),
                color = contentColor,
                modifier = Modifier.weight(1f)
            )
            
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .background(primaryColor, CircleShape)
                )
            } else if (badgeCount != null && badgeCount > 0) {
                 Surface(
                    color = primaryColor,
                    shape = RoundedCornerShape(100.dp)
                ) {
                    Text(
                        text = badgeCount.toString(),
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, fontWeight = FontWeight.Bold),
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
        }
    }
}
