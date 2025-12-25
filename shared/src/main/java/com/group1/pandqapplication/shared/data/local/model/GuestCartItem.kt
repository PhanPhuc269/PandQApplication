package com.group1.pandqapplication.shared.data.local.model

data class GuestCartItem(
    val productId: String,
    val productName: String = "",
    val quantity: Int,
    val price: Double = 0.0,
    val imageUrl: String? = null
)

data class GuestCart(
    val items: List<GuestCartItem> = emptyList()
)
