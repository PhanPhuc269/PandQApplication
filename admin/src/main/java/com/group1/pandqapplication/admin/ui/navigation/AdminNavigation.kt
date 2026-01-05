package com.group1.pandqapplication.admin.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddBox
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Email
import androidx.compose.ui.graphics.vector.ImageVector

sealed class AdminScreen(val route: String, val title: String, val icon: ImageVector) {
    object Dashboard : AdminScreen("dashboard", "Dashboard", Icons.Default.Dashboard)
    object Orders : AdminScreen("orders", "Orders", Icons.Default.ShoppingCart)
    object Promotions : AdminScreen("promotions", "Promotions", Icons.Default.LocalOffer)
    object Analytics : AdminScreen("analytics", "Analytics", Icons.Default.BarChart)
    
    // Sub-screens (hidden from bottom bar usually, or just part of navigation graph)
    object CategoryManagement : AdminScreen("category_management", "Category", Icons.Default.Widgets)
    object SupplierManagement : AdminScreen("supplier_management", "Supplier", Icons.Default.Inventory)
    object RoleManagement : AdminScreen("role_management", "Roles", Icons.Default.ManageAccounts)
    object BranchManagement : AdminScreen("branch_management", "Branches", Icons.Default.Storefront)
    object Inventory : AdminScreen("inventory", "Inventory", Icons.Default.Inventory)
    object AddProduct : AdminScreen("add_product?productId={productId}", "Add Product", Icons.Default.AddBox)

    object OrderDetails : AdminScreen("order_details", "Order Details", Icons.Default.Receipt)
    object CreatePromotion : AdminScreen("create_promotion", "Create Promotion", Icons.Default.Add)
    object Profile : AdminScreen("profile", "Profile", Icons.Default.ManageAccounts)
    object Settings : AdminScreen("settings", "Settings", Icons.Default.Inventory) // Icon placeholder
    object SalesAnalysis : AdminScreen("sales_analysis", "Sales Analysis", Icons.Default.BarChart)
    object CustomerList : AdminScreen("customer_list", "Customer List", Icons.Default.ManageAccounts)
    object ShippingManagement : AdminScreen("shipping_management", "Shipping Management", Icons.Default.LocalShipping)
    object Login : AdminScreen("login", "Login", Icons.Default.Login)
    object ChangePassword : AdminScreen("change_password", "Change Password", Icons.Default.ManageAccounts)
    object NotificationTemplates : AdminScreen("notification_templates", "Push Campaigns", Icons.Default.Email)
    object NotificationList : AdminScreen("notification_list", "Inbox", Icons.Default.Notifications)
    object ReturnScreen : AdminScreen("return_screen", "Return", Icons.Default.Login)
}
