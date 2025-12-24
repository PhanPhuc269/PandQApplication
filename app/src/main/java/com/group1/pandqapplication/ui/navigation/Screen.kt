package com.group1.pandqapplication.ui.navigation

sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object Home : Screen("home")
    data object Onboarding : Screen("onboarding")
    data object ProductDetail : Screen("product_detail/{productId}") {
        fun createRoute(productId: String) = "product_detail/$productId"
    }
    data object Cart : Screen("cart_screen")
    data object Search : Screen("search_screen")
    data object Splash : Screen("splash")
    data object Checkout : Screen("checkout/{orderId}") {
        fun createRoute(orderId: String) = "checkout/$orderId"
    }
    data object OrderTracking : Screen("order_tracking?orderId={orderId}") {
        fun createRoute(orderId: String? = null): String {
            return if (orderId != null) {
                "order_tracking?orderId=$orderId"
            } else {
                "order_tracking"
            }
        }
    }
    data object ShippingAddress : Screen("shipping_address")
    data object PersonalInfo : Screen("personal_info")
    data object AddressList : Screen("address_list")
    data object AddEditAddress : Screen("add_edit_address?addressId={addressId}") {
        fun createRoute(addressId: String? = null): String {
            return if (addressId != null) {
                "add_edit_address?addressId=$addressId"
            } else {
                "add_edit_address"
            }
        }
    }
    data object MapPicker : Screen("map_picker")
    data object OrderSuccess : Screen("order_success/{orderId}") {
        fun createRoute(orderId: String) = "order_success/$orderId"
    }
}
