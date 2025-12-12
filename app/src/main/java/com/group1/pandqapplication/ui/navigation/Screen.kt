package com.group1.pandqapplication.ui.navigation

sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object Home : Screen("home")
    data object Onboarding : Screen("onboarding")
    data object ProductDetail : Screen("product_detail")
    data object Cart : Screen("cart_screen")
    data object Search : Screen("search_screen")
    data object Splash : Screen("splash")
}
