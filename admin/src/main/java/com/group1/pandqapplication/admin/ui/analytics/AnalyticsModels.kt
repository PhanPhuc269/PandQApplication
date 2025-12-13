package com.group1.pandqapplication.admin.ui.analytics

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material.icons.outlined.GroupAdd

data class AnalysisMetric(
    val title: String,
    val value: String,
    val icon: ImageVector,
    val iconBgColor: Color,
    val iconTint: Color,
    val changeText: String,
    val isPositiveChange: Boolean
)

data class TopProduct(
    val name: String,
    val variant: String,
    val price: String,
    val soldCount: String,
    val imageUrl: String
)

val dummyMetrics = listOf(
    AnalysisMetric(
        title = "Tổng đơn",
        value = "1,240",
        icon = Icons.Outlined.ShoppingBag,
        iconBgColor = Color(0xFFEFF6FF), // Blue 50
        iconTint = Color(0xFF3B82F6), // Blue 500
        changeText = "5% so với tuần trước",
        isPositiveChange = true
    ),
    AnalysisMetric(
        title = "Khách mới",
        value = "328",
        icon = Icons.Outlined.GroupAdd,
        iconBgColor = Color(0xFFF3E8FF), // Purple 50
        iconTint = Color(0xFFA855F7), // Purple 500
        changeText = "2% so với tuần trước",
        isPositiveChange = false
    )
)

val dummyTopProducts = listOf(
    TopProduct(
        name = "iPhone 14 Pro Max",
        variant = "128GB - Deep Purple",
        price = "1.2B đ",
        soldCount = "50 đã bán",
        imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuDjZwsxNmWT6USWDYs6oHX9CRki9tvv0PT6DoyKH-LDsP429DBFl4HKE5ygfrutVbDl_KtrdXnvwP-B-fMNP0b2ySiPz62URkoU1eHKLVBPxFnaDzIGATvt4ARFQn7QlOT3e03T2wlkirX21JkqX9QQyT8d97_I827soafXfG7mFX3jJGhDss3HeAwHNawAkrgvppJKx8nmstzG5llOS87lk0QGCekTGHzZVMQFfSbiM7dJkGUaQb6LPBwxsqgcE_x0-3nkOXlnVXA"
    ),
    TopProduct(
        name = "Sony Bravia 4K",
        variant = "55 inch - OLED",
        price = "850tr đ",
        soldCount = "32 đã bán",
        imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuB1H20ov5eycCSFc1FdaOMkeI8P6NOIdbIJAXgDhMZUFUkFRoVaGGDIWkQg06H1jjh5_ue3LdmExyI8VT6dkffGQQUxnGVfpxmxjw_w8BBLnGAa7sQVqEH6oKlinzjr8IvMJU_WrByFo-0kVNesYG-hBj0kN-GOB1oht-OYAZi50l6IMVFKQF02uLfCiR6x3nvALQoZNpqxawAX2jYsgMv7V9FBXrqJ1mw7Rfurr79fKko3HHV8dfdgHOG8qoMSbK0YX7GqStxd4mY"
    ),
    TopProduct(
        name = "iPad Air 5",
        variant = "64GB - Blue",
        price = "420tr đ",
        soldCount = "28 đã bán",
        imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuC3GVLrxANOtqy_oS6NbYvCaoWAqMdpF9fBOT0A-Ovo7wjQ9ecr4Kem3D-OOV2EMRVOFIbvbvEuisUA-wRFA8yhXcWOn94NKzCbFA2rM6AAXLEVlBWW_VaMdWV_LpR0Ty0w3rI9IBVfkqhbyhW-F-w7Tyjt-tBInmk78h0f62HeqWbjX7ecTvTAmlkzkyww7cIAQz-LkGqok1FAI5OazTypywb3sGlfUYnsMNQsqpBSFHkO-HqMeLIU7wfucCPibtRVAwMRG7Rfm48"
    )
)
