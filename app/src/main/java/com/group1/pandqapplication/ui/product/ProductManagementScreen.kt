package com.group1.pandqapplication.ui.product

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.group1.pandqapplication.ui.admin.AdminProductViewModel
import com.group1.pandqapplication.shared.data.remote.dto.ProductDto

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.group1.pandqapplication.shared.ui.theme.BranchBackgroundDark
import com.group1.pandqapplication.shared.ui.theme.BranchBackgroundLight
import com.group1.pandqapplication.shared.ui.theme.BranchTextMainDark
import com.group1.pandqapplication.shared.ui.theme.BranchTextMainLight
import com.group1.pandqapplication.shared.ui.theme.BranchTextSubDark
import com.group1.pandqapplication.shared.ui.theme.BranchTextSubLight
import com.group1.pandqapplication.shared.ui.theme.ProductChipUnselectedDark
import com.group1.pandqapplication.shared.ui.theme.ProductChipUnselectedLight
import com.group1.pandqapplication.shared.ui.theme.ProductPrimary
import com.group1.pandqapplication.shared.ui.theme.ProductStatusGreen
import com.group1.pandqapplication.shared.ui.theme.ProductStatusOrange
import com.group1.pandqapplication.shared.ui.theme.ProductStatusRed

@Composable
fun ProductManagementScreen(
    onBackClick: () -> Unit = {},
    onAddProductClick: () -> Unit = {},
    onProductClick: (String) -> Unit = {},
    viewModel: AdminProductViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    val isDarkTheme = false
    
    // Reuse Branch background colors as they are also "f8f6f6" and "221310" (close enough or same)
    // HTML uses: bg-background-light dark:bg-background-dark for body
    // In Branch HTML: bg-background-light is #f6f6f8, dark is #101622
    // In Product HTML: bg-background-light is #f8f6f6, dark is #221310
    // Actually they are slightly different. Let's stick to the Product screen's specific background if possible.
    // Product screen BG light: #F8F6F6, Dark: #221310.
    // Wait, #F8F6F6 is `CheckoutBackgroundLight`. #221310 is `CheckoutBackgroundDark`.
    // Let's reuse Checkout background colors for perfect match.
    val backgroundColor = if (isDarkTheme) com.group1.pandqapplication.shared.ui.theme.CheckoutBackgroundDark else com.group1.pandqapplication.shared.ui.theme.CheckoutBackgroundLight
    val textMain = if (isDarkTheme) BranchTextMainDark else BranchTextMainLight // Reuse standard text colors match
    val textSub = if (isDarkTheme) BranchTextSubDark else BranchTextSubLight
    val surfaceColor = if (isDarkTheme) Color(0xFFFFFFFF).copy(alpha = 0.05f) else Color.White // List item bg
    val searchBg = if (isDarkTheme) Color(0xFFFFFFFF).copy(alpha = 0.1f) else Color(0xFFF3F4F6) // gray-100

    Scaffold(
        containerColor = backgroundColor,
        topBar = {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(backgroundColor.copy(alpha = 0.8f)) // Backdrop blur simulation
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.width(48.dp)) // Spacer for alignment or back button if needed, HTML shows "w-12" empty logic but also top bar structure. HTML: padding-left 12 is spacer.
                    // Actually HTML has a button on the right, but left is empty spacer div w-12.
                    // Let's assume onBackClick is needed but design hides it or puts placeholder? 
                    // HTML: <div class="w-12"></div>
                    // I will put an invisible box to match HTML.
                    
                    Text(
                        text = "Quản lý Sản phẩm",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = textMain,
                        modifier = Modifier.weight(1f),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                    
                    IconButton(
                        onClick = onAddProductClick,
                        modifier = Modifier
                            .size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add",
                            tint = ProductPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                HorizontalDivider(color = Color.Gray.copy(alpha = 0.1f))
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // Search Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(searchBg, RoundedCornerShape(8.dp)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .width(48.dp)
                        .fillMaxSize()
                        .background(Color.Transparent), // Inner bg in HTML is separate but here container handles it
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = textSub
                    )
                }
                TextField(
                    value = "",
                    onValueChange = {},
                    placeholder = { Text("Tìm theo tên sản phẩm hoặc SKU", color = textSub) },
                    modifier = Modifier.weight(1f),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedTextColor = textMain,
                        unfocusedTextColor = textMain
                    ),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Filter Chips
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    FilterChip(text = "Tất cả", isSelected = true, isDarkTheme = isDarkTheme)
                }
                item {
                    FilterChip(text = "Còn hàng", isSelected = false, isDarkTheme = isDarkTheme)
                }
                item {
                    FilterChip(text = "Sắp hết", isSelected = false, isDarkTheme = isDarkTheme)
                }
                item {
                    FilterChip(text = "Hết hàng", isSelected = false, isDarkTheme = isDarkTheme)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Product List
            // Product List
            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = ProductPrimary)
                }
            } else if (uiState.error != null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = uiState.error ?: "Error", color = Color.Red)
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.products) { product ->
                        ProductItem(
                            product = product,
                            backgroundColor = surfaceColor,
                            textMain = textMain,
                            textSub = textSub,
                            onClick = { onProductClick(product.id) }
                        )
                    }
                }
            }



        }
    }
}

@Composable
fun FilterChip(
    text: String,
    isSelected: Boolean,
    isDarkTheme: Boolean
) {
    val backgroundColor = if (isSelected) ProductPrimary else {
        if (isDarkTheme) ProductChipUnselectedDark else ProductChipUnselectedLight
    }
    val textColor = if (isSelected) Color.White else {
        if (isDarkTheme) Color(0xFFD1D5DB) else Color(0xFF1F2937) // gray-300 : gray-800
    }

    Box(
        modifier = Modifier
            .height(32.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .clickable { }
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = textColor
        )
    }
}

@Composable
fun ProductItem(
    product: ProductDto,
    backgroundColor: Color,
    textMain: Color,
    textSub: Color,
    onClick: () -> Unit
) {
    val statusColor = when(product.status) {
        "IN_STOCK" -> ProductStatusGreen
        "LOW_STOCK" -> ProductStatusOrange
        "OUT_OF_STOCK" -> ProductStatusRed
        else -> ProductStatusGreen // Default
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .clickable { onClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = product.thumbnailUrl,
            contentDescription = null,
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.Gray.copy(alpha = 0.1f)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = product.name,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                color = textMain,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "Giá: ${product.price} USD",
                fontSize = 14.sp,
                color = textSub,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }


        Spacer(modifier = Modifier.width(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(28.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .background(statusColor, CircleShape)
                )
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = textSub.copy(alpha = 0.5f)
            )
        }
    }
}



@Preview
@Composable
fun PreviewProductManagementScreen() {
    ProductManagementScreen()
}
