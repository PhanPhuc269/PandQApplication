package com.group1.pandqapplication.admin.ui.promotions

import androidx.compose.ui.graphics.Color
import java.time.LocalDate
import java.time.format.DateTimeFormatter

enum class PromotionStatus(val displayName: String, val color: Color, val containerColor: Color) {
    ACTIVE("Active", Color(0xFF059669), Color(0xFFD1FAE5)), // Emerald
    SCHEDULED("Scheduled", Color(0xFFD97706), Color(0xFFFEF3C7)), // Amber
    EXPIRED("Inactive", Color(0xFF64748B), Color(0xFFF1F5F9)), // Slate
    DRAFT("Draft", Color(0xFF2563EB), Color(0xFFDBEAFE)); // Blue
}

data class Promotion(
    val title: String,
    val description: String, // e.g., "Created Oct 01, 2023"
    val status: PromotionStatus,
    val discountText: String, // "50% OFF"
    val code: String?, // "MEGA50", null if not set
    val extraInfo: String, // "Ends Oct 30" or "Starts Nov 01"
    val usageLabel: String, // "Used:" or "Limit:" or "Total Used:"
    val usageValue: String // "45/100" or "500"
)

val dummyPromotions = listOf(
    Promotion(
        title = "Mega Sale Electronics",
        description = "Created Oct 01, 2023",
        status = PromotionStatus.ACTIVE,
        discountText = "50% OFF",
        code = "MEGA50",
        extraInfo = "Ends Oct 30",
        usageLabel = "Used:",
        usageValue = "45/100"
    ),
    Promotion(
        title = "Black Friday Preview",
        description = "Created yesterday",
        status = PromotionStatus.SCHEDULED,
        discountText = "$20.00 OFF",
        code = "BLKFR1",
        extraInfo = "Starts Nov 01",
        usageLabel = "Limit:",
        usageValue = "500"
    ),
    Promotion(
        title = "Flash Sale SSD",
        description = "Ended yesterday",
        status = PromotionStatus.EXPIRED,
        discountText = "15% OFF",
        code = "SSDFLASH", // Should be strikethrough in UI if expired
        extraInfo = "", // No extra info for expired in HTML, maybe just status
        usageLabel = "Total Used:",
        usageValue = "215"
    ),
    Promotion(
        title = "Year End Clearance",
        description = "Draft • Modified 2h ago",
        status = PromotionStatus.DRAFT,
        discountText = "Up to 70%",
        code = null, // Not set
        extraInfo = "",
        usageLabel = "",
        usageValue = "" // No usage for draft
    )
)
