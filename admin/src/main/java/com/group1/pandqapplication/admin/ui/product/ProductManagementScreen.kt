package com.group1.pandqapplication.admin.ui.product

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
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import com.group1.pandqapplication.shared.data.remote.dto.ProductDto
import com.group1.pandqapplication.shared.ui.theme.BranchTextMainLight
import com.group1.pandqapplication.shared.ui.theme.BranchTextSubLight
import com.group1.pandqapplication.shared.ui.theme.ProductChipUnselectedLight
import com.group1.pandqapplication.shared.ui.theme.ProductPrimary
import com.group1.pandqapplication.shared.ui.theme.ProductStatusGreen
import com.group1.pandqapplication.shared.ui.theme.ProductStatusOrange
import com.group1.pandqapplication.shared.ui.theme.ProductStatusRed

@Composable
fun ProductManagementScreen(
    viewModel: AdminProductViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    onAddProductClick: () -> Unit = {},
    onProductClick: (String) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    // Consumed operation state logic remains below...
    
    // Consume operation state (e.g. after delete)
    LaunchedEffect(uiState.operationSuccess) {
        if (uiState.operationSuccess) {
            viewModel.clearOperationState()
        }
    }
    
    // Delete Confirmation State
    var productToDelete by remember { mutableStateOf<ProductDto?>(null) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    val backgroundColor = com.group1.pandqapplication.shared.ui.theme.CheckoutBackgroundLight
    val textMain = BranchTextMainLight
    val textSub = BranchTextSubLight
    val surfaceColor = Color.White
    val searchBg = Color(0xFFF3F4F6)

    Scaffold(
        containerColor = backgroundColor,
        topBar = {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(backgroundColor.copy(alpha = 0.8f))
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back", tint = textMain)
                    }
                    
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
                        modifier = Modifier.size(40.dp)
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
                        .background(Color.Transparent),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = textSub
                    )
                }
                TextField(
                    value = uiState.searchQuery,
                    onValueChange = { viewModel.onSearchQueryChanged(it) },
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
                item { FilterChip(text = "Tất cả", isSelected = uiState.filterStatus == "ALL") { viewModel.onFilterStatusChanged("ALL") } }
                item { FilterChip(text = "Còn hàng", isSelected = uiState.filterStatus == "IN_STOCK") { viewModel.onFilterStatusChanged("IN_STOCK") } }
                item { FilterChip(text = "Sắp hết", isSelected = uiState.filterStatus == "LOW_STOCK") { viewModel.onFilterStatusChanged("LOW_STOCK") } }
                item { FilterChip(text = "Hết hàng", isSelected = uiState.filterStatus == "OUT_OF_STOCK") { viewModel.onFilterStatusChanged("OUT_OF_STOCK") } }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Product List Content
            Box(modifier = Modifier.weight(1f)) {
                if (uiState.filteredProducts.isEmpty() && !uiState.isLoading) {
                    // Empty State
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = androidx.compose.material.icons.Icons.Filled.Info,
                            contentDescription = "Empty",
                            tint = Color.Gray,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Chưa có sản phẩm nào", color = Color.Gray, fontSize = 16.sp)
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(uiState.filteredProducts) { product ->
                            ProductItem(
                                product = product,
                                backgroundColor = surfaceColor,
                                textMain = textMain,
                                textSub = textSub,
                                onClick = { onProductClick(product.id) },
                                onDeleteClick = { 
                                    productToDelete = product
                                    showDeleteDialog = true
                                }
                            )
                        }
                    }
                }
                
                // Overlay Loader (Non-blocking or at least keeping list visible)
                if (uiState.isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White.copy(alpha = 0.3f)),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = ProductPrimary)
                    }
                }
                
                // Error Message
                if (uiState.error != null) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = uiState.error ?: "Error", color = Color.Red)
                    }
                }
            }
        }
        
        // Delete Confirmation Dialog
        if (showDeleteDialog && productToDelete != null) {
            androidx.compose.material3.AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("Xác nhận xóa") },
                text = { Text("Bạn có chắc chắn muốn xóa sản phẩm \"${productToDelete?.name}\" không?") },
                confirmButton = {
                    androidx.compose.material3.TextButton(
                        onClick = {
                            productToDelete?.let { viewModel.deleteProduct(it.id) }
                            showDeleteDialog = false
                            productToDelete = null
                        }
                    ) {
                        Text("Xóa", color = Color.Red)
                    }
                },
                dismissButton = {
                    androidx.compose.material3.TextButton(onClick = { showDeleteDialog = false }) {
                        Text("Hủy")
                    }
                }
            )
        }
    }
}

@Composable
fun FilterChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) ProductPrimary else ProductChipUnselectedLight
    val textColor = if (isSelected) Color.White else Color(0xFF1F2937)

    Box(
        modifier = Modifier
            .height(32.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .clickable { onClick() }
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
    onClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val statusColor = when(product.status) {
        "IN_STOCK" -> ProductStatusGreen
        "LOW_STOCK" -> ProductStatusOrange
        "OUT_OF_STOCK" -> ProductStatusRed
        else -> ProductStatusGreen
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
                text = "Giá: ${product.price}",
                fontSize = 14.sp,
                color = textSub,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(28.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .background(statusColor, CircleShape)
                )
            }
            // Delete Icon
            IconButton(onClick = onDeleteClick) {
                Icon(
                    imageVector = androidx.compose.material.icons.Icons.Filled.Delete,
                    contentDescription = "Delete",
                    tint = Color.Red.copy(alpha = 0.7f)
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
