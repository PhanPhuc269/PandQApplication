package com.group1.pandqapplication.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.group1.pandqapplication.shared.ui.theme.ProductPrimary
import com.group1.pandqapplication.shared.ui.theme.RoleBackgroundDark
import com.group1.pandqapplication.shared.ui.theme.RoleBackgroundLight
import com.group1.pandqapplication.shared.ui.theme.SupplierDividerDark
import com.group1.pandqapplication.shared.ui.theme.SupplierDividerLight
import com.group1.pandqapplication.shared.ui.theme.SupplierSurfaceDark
import com.group1.pandqapplication.shared.ui.theme.SupplierSurfaceLight
import com.group1.pandqapplication.shared.ui.theme.SupplierTextPrimaryDark
import com.group1.pandqapplication.shared.ui.theme.SupplierTextPrimaryLight
import com.group1.pandqapplication.shared.ui.theme.SupplierTextSecondaryDark
import com.group1.pandqapplication.shared.ui.theme.SupplierTextSecondaryLight

@Composable
fun SupplierManagementScreen(
    onBackClick: () -> Unit = {},
    onAddSupplierClick: () -> Unit = {}
) {
    val isDarkTheme = false
    
    val backgroundColor = if (isDarkTheme) RoleBackgroundDark else RoleBackgroundLight
    val textPrimary = if (isDarkTheme) SupplierTextPrimaryDark else SupplierTextPrimaryLight
    val textSecondary = if (isDarkTheme) SupplierTextSecondaryDark else SupplierTextSecondaryLight
    val surfaceColor = if (isDarkTheme) SupplierSurfaceDark else SupplierSurfaceLight
    val dividerColor = if (isDarkTheme) SupplierDividerDark else SupplierDividerLight

    Scaffold(
        containerColor = backgroundColor,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBackIosNew,
                        contentDescription = "Back",
                        tint = textPrimary
                    )
                }
                Text(
                    text = "Nhà cung cấp",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = textPrimary,
                    modifier = Modifier.weight(1f),
                    style = androidx.compose.ui.text.TextStyle(textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                )
                IconButton(onClick = onAddSupplierClick) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add",
                        tint = ProductPrimary,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Search Bar
            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(surfaceColor),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .width(48.dp)
                            .height(48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                         Icon(Icons.Default.Search, contentDescription = null, tint = textSecondary)
                    }
                    Text(
                        text = "Tìm kiếm theo tên, SĐT...",
                        color = textSecondary,
                        fontSize = 16.sp,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            
            // Filters
            LazyRow(
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    FilterChip(
                        text = "Tất cả",
                        isSelected = true,
                        backgroundColor = ProductPrimary,
                        textColor = Color.White
                    )
                }
                item {
                    FilterChip(
                        text = "Đang hoạt động",
                        isSelected = false,
                        backgroundColor = surfaceColor,
                        textColor = textPrimary
                    )
                }
                item {
                    FilterChip(
                        text = "Ngừng hoạt động",
                        isSelected = false,
                        backgroundColor = surfaceColor,
                        textColor = textPrimary
                    )
                }
            }
            
            HorizontalDivider(color = dividerColor, modifier = Modifier.padding(vertical = 4.dp))
            
            // Supplier List
            LazyColumn {
                items(getSampleSuppliers()) { supplier ->
                    SupplierItem(
                        supplier = supplier,
                        textPrimary = textPrimary,
                        textSecondary = textSecondary
                    )
                }
            }
        }
    }
}

@Composable
fun FilterChip(
    text: String,
    isSelected: Boolean,
    backgroundColor: Color,
    textColor: Color
) {
    Row(
        modifier = Modifier
            .height(32.dp)
            .clip(androidx.compose.foundation.shape.CircleShape)
            .background(backgroundColor)
            .padding(start = 16.dp, end = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(text = text, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = textColor)
        Spacer(modifier = Modifier.width(4.dp))
        Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = textColor, modifier = Modifier.size(20.dp))
    }
}


@Composable
fun SupplierItem(
    supplier: SupplierData,
    textPrimary: Color,
    textSecondary: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = supplier.imageUrl,
            contentDescription = null,
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.Gray),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = supplier.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = textPrimary,
                maxLines = 1
            )
            Text(
                text = supplier.contact,
                fontSize = 14.sp,
                color = textSecondary,
                maxLines = 1
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "${supplier.productCount} sản phẩm",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = textSecondary
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.Default.ArrowForwardIos,
                contentDescription = null,
                tint = textSecondary.copy(alpha = 0.5f),
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

data class SupplierData(
    val name: String,
    val contact: String,
    val productCount: Int,
    val imageUrl: String
)

fun getSampleSuppliers() = listOf(
    SupplierData("Techtronics Inc.", "contact@techtronics.com", 34, "https://lh3.googleusercontent.com/aida-public/AB6AXuD62oRWL_WobF0NhHC5tMvjGamc0Pxg5zt17rsn489-neR7aXzvaqL8T-vkgnMgeMgKJVzFMhBfgXv0dWx-zLdqPdE6-ZvfOcRgkzaUrgvKoH2jIV0lNC9BzPOfq0OHEWFuMQljzAPVT1bqKvZdrlJYLxpl2FHEOJT4qEzsMMYSkt4WvpVV_6VvLsXF3wMraBFm1cbh8eK6AM05F-2Bk2A7O8Mv9xCbf9JbkKpIhg3g1eokMplgN0SWNvDrHZ-FABsltqNPWgPWFvw"),
    SupplierData("Global Gadgets", "0912345678", 58, "https://lh3.googleusercontent.com/aida-public/AB6AXuBYrE54jphUgbyxpZ7SdfoEYtNTLdOiLiDZGGOUNhYeRPebF12FmRM_GWtMCBOGXcvwpxjh1Mv51pwRvvL4ZIN1oKBb1k5mc-dQgRAyuDo0GDZ-ghwzFaSV7WrDxyO0eYGCZpa4NDuapkUvpfaWdDJic5xWtuZoeg7IaGDbrVgKoVq_WxWN--Z9PiDHjOWHUOhwMVAdw97mk8ZtLpn1I08S8oAB69ID9GRenWvdct1jeHi6z6O6nS1VIhH-eOKy_TGFFMANOuXjTgw"),
    SupplierData("Innovate Supplies", "support@innovate.co", 112, "https://lh3.googleusercontent.com/aida-public/AB6AXuALai_VBbgNWAd7GSygM-M7ZVD4QQz41tfpXnk7Kp7P7NMBFxZ2WirXD6KQ0EPxifk1vtO9p7kx07-tpAU2cZpg8h5eNRK7YUEev8FBwOPI9gL5d73eo2cfFJfmDPbgKJGW66i1QLOlMRl1rFNMuXTVudWfYYuVVDImHr8Av11vHTmzs68kiErU-A0Lj-1qTYwo_JU9B4Lfcg6BZlfWQZpxLDpBXowBw7WeRHguldhLZQXqZ_wlG4oN0Tj9oBI1zHwtJwTWFDdeSwU"),
    SupplierData("Future Electronics Co.", "0987654321", 21, "https://lh3.googleusercontent.com/aida-public/AB6AXuA7RBfjfBLO9u8JcD_r5NMIDuFmiqcfL1v7QkHc1vtB0jTZR2UkFC2KKXAdI-QmRDqbP6roh7ibru6lGnWD7O3HHBWY5b0cSd0Zpm3vzF-cnf8B46XcManjH4czPvxp5rfZyJSxtev8HVX0ShwUMJvFRKNnLCCMPWnrcFNSh9zyqw9SzhIYGFxDi5qjyoLSsC0fp1ePI8TFYaJEQqs9N8T0-VunurkO4lCOQTPcr162eOBLi2bmILk-dapeD6iyGvW1qWcCMAZsQcQ")
)

@Preview
@Composable
fun PreviewSupplierManagementScreen() {
    SupplierManagementScreen()
}
