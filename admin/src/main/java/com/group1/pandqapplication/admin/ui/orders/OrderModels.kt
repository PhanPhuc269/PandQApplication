package com.group1.pandqapplication.admin.ui.orders

import androidx.compose.ui.graphics.Color
import java.time.LocalDate
import java.time.format.DateTimeFormatter

enum class OrderStatus(val displayName: String, val color: Color, val containerColor: Color) {
    PENDING("Chờ xử lý", Color(0xFFEAB308), Color(0x1AEAB308)), // Yellow
    SHIPPING("Đang giao", Color(0xFF3B82F6), Color(0x1A3B82F6)), // Blue
    COMPLETED("Hoàn thành", Color(0xFF22C55E), Color(0x1A22C55E)), // Green
    CANCELLED("Đã hủy", Color(0xFFEF4444), Color(0x1AEF4444)); // Red

    companion object {
        fun fromString(status: String): OrderStatus {
            return entries.find { it.displayName == status } ?: PENDING
        }
    }
}

data class Order(
    val id: String,
    val customerName: String,
    val customerAvatarInitials: String,
    val date: LocalDate,
    val status: OrderStatus,
    val productName: String,
    val productImageUrl: String,
    val quantity: Int,
    val price: Long,
    val originalPrice: Long? = null
) {
    val formattedDate: String
        get() = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))

    val formattedPrice: String
        get() = String.format("%,d đ", price).replace(',', '.')
        
    val formattedOriginalPrice: String?
        get() = originalPrice?.let { String.format("%,d đ", it).replace(',', '.') }
}

data class QuickStat(
    val title: String,
    val count: Int,
    val color: Color
)

// Dummy Data
val dummyOrders = listOf(
    Order(
        id = "ORD-2023-001",
        customerName = "Nguyễn Văn A",
        customerAvatarInitials = "NA",
        date = LocalDate.of(2023, 10, 12),
        status = OrderStatus.PENDING,
        productName = "iPhone 14 Pro Max - 256GB - Deep Purple",
        productImageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuAV7bGGlHzxWiMD2jW5kC9TTVWQJn1QT7PO5TO8t0TP_LtnIJq-f35T2F_B_umC-LuIHN6_j-QAfzWMdXzvgzXaxUJAB_0ssdNAUVqc-jO4jPcTWb1VnWmwS_43WrmJb-6lgtrIHKxRs0vCK1H7ez6RogSMg9L94aMHWIZa_t3f8F5LziIqO-EuwGcA2sr6XpBC57CjZo6IX8JGFmJ4CEsOMWafwHSWZMshlT9Wm9mrJ4LAu4AJOwMJeVEPM_RNMPmu5huMg1d5QGs",
        quantity = 1,
        price = 32990000
    ),
    Order(
        id = "ORD-2023-002",
        customerName = "Trần Thị B",
        customerAvatarInitials = "TB",
        date = LocalDate.of(2023, 10, 11),
        status = OrderStatus.SHIPPING,
        productName = "Samsung Galaxy S23 Ultra - 512GB - Black",
        productImageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuCgSXAnOvLJBO4sNvTPFbFQmmgIOvwWdTsBT0WgnEDS_rv1pHQ-PCOwKW1ehO6I-cjr5TssWQCa73WAAnF37RS7lQ9eAmtzFaFGbZGA0IJN4ucBrxxfsjONzTb7TxebZM1XC2petZg0KPDPWMumA9g-mSYC4_mQYaelez2UKE7Z-iGYmE1Avi4py2MD-yAJkz51C4_lyzj5NyzOHdoROAU66nOR0HPSaPKmAwihi3Uo3lJDpOyRgxJPekt69FVjmb9CnVCFY8SB4gU",
        quantity = 1,
        price = 26500000
    ),
    Order(
        id = "ORD-2023-003",
        customerName = "Lê Hoàng C",
        customerAvatarInitials = "LH",
        date = LocalDate.of(2023, 10, 10),
        status = OrderStatus.COMPLETED,
        productName = "Apple Watch Series 8 - 45mm",
        productImageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuC9fyOxEG9kX0MWRs2SkVqEppzivMXVP-KPsuPMX5uu-FXgd-y5IwlgzaRFmM95NMaufkkgLW6DCx6wZ0u1WXkfFZfHJFb9uZHKDWNeRAhdmN1PG4r4wjBxVVMMVr4PVH4nGki1fFnaR75uYOCiy7Ig8gfexLtLW6oPlFYDxKxeozvG2U6wzcBXkC2c2hMOHV8nxePc5Ar2tkWPfJu7-Qxh40Hf7CM4BvgkZMZEy0D5YNCB5jHc6-6XoAHthWCbjrnLzHUMRkz74rs",
        quantity = 2,
        price = 18980000
    ),
    Order(
        id = "ORD-2023-004",
        customerName = "Phạm Dũng",
        customerAvatarInitials = "PD",
        date = LocalDate.of(2023, 10, 9),
        status = OrderStatus.CANCELLED,
        productName = "Sony WH-1000XM5 Wireless Headphones",
        productImageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuDs1cJEx6Pjw06Fxl6aUaPhgbxUsnl49AR9kIyv-7YIGTTt0VpgIDazqYrrGGnmoi7SOB_SacpZhPR4Lq0hIqOIwUCVTx661AyJgaYphuXq1-DVpt3I5uvzAoOupqdakH4f9gAeEi1Gb5e_5F4pe5br8qQvJO7_eqhdIzVnvBIq2K4QNj9XvPFW0PSjha8Ev053r-b0Na5FbgcKajqtYyEjYq7WfgqWvihRdjZ817Szd90qFlw0Fm4XihY0tbmru_GU3VQt3GBDdqA",
        quantity = 1,
        price = 8490000,
        originalPrice = 8490000 // For strikethrough logic in UI, if needed, though usually strictkethrough means discounted. In the UI screenshot, Cancelled has a strikethrough price, which likely means the value of the cancelled order.
    )
)

val dummyQuickStats = listOf(
    QuickStat("Chờ xác nhận", 12, Color(0xFFEAB308)),
    QuickStat("Đang giao", 5, Color(0xFF3B82F6)),
    QuickStat("Hoàn thành", 128, Color(0xFF22C55E))
)
