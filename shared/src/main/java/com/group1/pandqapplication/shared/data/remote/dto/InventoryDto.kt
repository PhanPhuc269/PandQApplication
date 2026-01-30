package com.group1.pandqapplication.shared.data.remote.dto

data class InventoryItemDto(
    val id: String,
    val branchId: String?,
    val branchName: String?,
    val productId: String,
    val productName: String,
    val productThumbnail: String?,
    val productSku: String?,
    val productPrice: Double?,
    val quantity: Int,
    val minStock: Int?,
    val reservedQuantity: Int?
)

data class InventoryStatsDto(
    val totalInventoryValue: Double,
    val totalProductsInStock: Int,
    val lowStockCount: Int,
    val lowStockItems: List<InventoryItemDto>,
    val allItems: List<InventoryItemDto>
)
