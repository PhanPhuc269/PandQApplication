package com.group1.pandqapplication.admin.ui.inventory

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.material3.Surface
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
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
import com.group1.pandqapplication.shared.data.remote.dto.InventoryItemDto
import com.group1.pandqapplication.shared.ui.theme.InventoryAlert
import com.group1.pandqapplication.shared.ui.theme.InventoryBackgroundDark
import com.group1.pandqapplication.shared.ui.theme.InventoryBackgroundLight
import com.group1.pandqapplication.shared.ui.theme.InventoryCardDark
import com.group1.pandqapplication.shared.ui.theme.InventoryCardLight
import com.group1.pandqapplication.shared.ui.theme.InventoryPrimary
import com.group1.pandqapplication.shared.ui.theme.InventoryTextPrimaryDark
import com.group1.pandqapplication.shared.ui.theme.InventoryTextPrimaryLight
import com.group1.pandqapplication.shared.ui.theme.InventoryTextSecondaryDark
import com.group1.pandqapplication.shared.ui.theme.InventoryTextSecondaryLight
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryScreen(
    onBackClick: () -> Unit = {},
    onNavigateToAddProduct: () -> Unit = {},
    onNavigateToEditProduct: (String) -> Unit = {},
    viewModel: InventoryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val isDarkTheme = false
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    
    val backgroundColor = if (isDarkTheme) InventoryBackgroundDark else InventoryBackgroundLight
    val cardColor = if (isDarkTheme) InventoryCardDark else InventoryCardLight
    val textPrimary = if (isDarkTheme) InventoryTextPrimaryDark else InventoryTextPrimaryLight
    val textSecondary = if (isDarkTheme) InventoryTextSecondaryDark else InventoryTextSecondaryLight
    val searchBg = if (isDarkTheme) Color(0xFF1E293B) else Color(0xFFE2E8F0)
    val dividerColor = if (isDarkTheme) Color(0xFF334155) else Color(0xFFE2E8F0)

    // Filter Bottom Sheet
    if (uiState.showFilterSheet) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.hideFilterSheet() },
            sheetState = sheetState,
            containerColor = cardColor
        ) {
            FilterBottomSheetContent(
                selectedFilter = uiState.selectedFilter,
                selectedSortBy = uiState.selectedSortBy,
                onFilterSelected = { viewModel.onFilterChanged(it) },
                onSortBySelected = { viewModel.onSortByChanged(it) },
                onApply = {
                    scope.launch {
                        sheetState.hide()
                        viewModel.hideFilterSheet()
                    }
                },
                textPrimary = textPrimary,
                textSecondary = textSecondary,
                cardColor = cardColor
            )
        }
    }

    Scaffold(
        containerColor = backgroundColor,
        topBar = {
            Surface(
                color = backgroundColor.copy(alpha = 0.8f), // Preserving background color
                shadowElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Spacer(modifier = Modifier.windowInsetsTopHeight(WindowInsets.statusBars))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .padding(horizontal = 8.dp)
                    ) {
                        IconButton(
                            onClick = onBackClick,
                            modifier = Modifier.align(Alignment.CenterStart)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBackIosNew,
                                contentDescription = "Back",
                                tint = textPrimary
                            )
                        }
                        Text(
                            text = "Thống kê tồn kho",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = textPrimary,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToAddProduct,
                containerColor = InventoryPrimary,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.size(56.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { paddingValues ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = InventoryPrimary)
            }
        } else if (uiState.error != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = uiState.error ?: "Đã xảy ra lỗi",
                        color = InventoryAlert,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Thử lại",
                        color = InventoryPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.clickable { viewModel.loadInventoryStats() }
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(bottom = 80.dp)
            ) {
                // Search Bar
                item {
                    Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
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
                                    .fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Search",
                                    tint = textSecondary
                                )
                            }
                            TextField(
                                value = uiState.searchQuery,
                                onValueChange = { viewModel.onSearchQueryChanged(it) },
                                placeholder = { Text("Tìm kiếm theo ID, tên, danh mục", color = textSecondary) },
                                modifier = Modifier.weight(1f),
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    focusedTextColor = textPrimary,
                                    unfocusedTextColor = textPrimary
                                ),
                                singleLine = true
                            )
                        }
                    }
                }

                // Stats Carousel
                item {
                    LazyRow(
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.padding(bottom = 16.dp)
                    ) {
                        item {
                            StatsCard("Giá trị tồn kho", uiState.totalInventoryValue, cardColor, textPrimary, textSecondary)
                        }
                        item {
                            StatsCard("Sản phẩm trong kho", uiState.totalProductsInStock, cardColor, textPrimary, textSecondary)
                        }
                        item {
                            StatsCard("Sắp hết hàng", uiState.lowStockCount, cardColor, InventoryAlert, textSecondary)
                        }
                    }
                }

                // Low Stock Section
                if (uiState.lowStockItems.isNotEmpty()) {
                    item {
                        Text(
                            text = "Cảnh báo tồn kho thấp",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = textPrimary,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }

                    items(uiState.lowStockItems.take(5)) { item ->
                        LowStockRow(
                            item = item,
                            backgroundColor = cardColor,
                            textPrimary = textPrimary,
                            alertColor = InventoryAlert,
                            onItemClick = { onNavigateToEditProduct(item.productId) }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }

                // All Products Header
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Tất cả sản phẩm",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = textPrimary
                            )
                            if (uiState.selectedFilter != InventoryFilter.ALL || uiState.selectedSortBy != InventorySortBy.NAME_ASC) {
                                Text(
                                    text = "${uiState.selectedFilter.displayName} • ${uiState.selectedSortBy.displayName}",
                                    fontSize = 12.sp,
                                    color = InventoryPrimary
                                )
                            }
                        }
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(100.dp))
                                .background(
                                    if (uiState.selectedFilter != InventoryFilter.ALL) 
                                        InventoryPrimary 
                                    else 
                                        InventoryPrimary.copy(alpha = 0.2f)
                                )
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                                .clickable { viewModel.toggleFilterSheet() },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.FilterList,
                                contentDescription = "Filter",
                                tint = if (uiState.selectedFilter != InventoryFilter.ALL) Color.White else InventoryPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (uiState.selectedFilter != InventoryFilter.ALL) 
                                    uiState.selectedFilter.displayName 
                                else 
                                    "Lọc",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = if (uiState.selectedFilter != InventoryFilter.ALL) Color.White else InventoryPrimary
                            )
                        }
                    }
                }

                // Active Filter Chips
                if (uiState.selectedFilter != InventoryFilter.ALL) {
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            FilterChip(
                                label = uiState.selectedFilter.displayName,
                                onRemove = { viewModel.onFilterChanged(InventoryFilter.ALL) },
                                textPrimary = textPrimary
                            )
                        }
                    }
                }

                // Result count
                item {
                    Text(
                        text = "Hiển thị ${uiState.filteredItems.size} sản phẩm",
                        fontSize = 12.sp,
                        color = textSecondary,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                    )
                }

                // Inventory List
                items(uiState.filteredItems) { item ->
                    InventoryItemRow(
                        item = item,
                        backgroundColor = cardColor,
                        dividerColor = dividerColor,
                        textPrimary = textPrimary,
                        textSecondary = textSecondary,
                        viewModel = viewModel,
                        onItemClick = { onNavigateToEditProduct(item.productId) }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
fun StatsCard(
    title: String,
    value: String,
    backgroundColor: Color,
    valueColor: Color,
    titleColor: Color
) {
    Column(
        modifier = Modifier
            .width(160.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = titleColor
        )
        Text(
            text = value,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = valueColor
        )
    }
}

@Composable
fun LowStockRow(
    item: InventoryItemDto,
    backgroundColor: Color,
    textPrimary: Color,
    alertColor: Color,
    onItemClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .clickable { onItemClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = item.productThumbnail ?: "https://via.placeholder.com/100",
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
                text = item.productName,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                color = textPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "Hiện có: ${item.quantity}",
                fontSize = 14.sp,
                color = alertColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = Color.Gray,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun InventoryItemRow(
    item: InventoryItemDto,
    backgroundColor: Color,
    dividerColor: Color,
    textPrimary: Color,
    textSecondary: Color,
    viewModel: InventoryViewModel,
    onItemClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .clickable { onItemClick() }
            .padding(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = item.productThumbnail ?: "https://via.placeholder.com/100",
                contentDescription = null,
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Gray.copy(alpha = 0.1f)),
                contentScale = ContentScale.Crop
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column {
                Text(
                    text = item.productName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = textPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = item.productSku ?: "SKU: N/A",
                    fontSize = 14.sp,
                    color = textSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        
        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = dividerColor)
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            InventoryStatColumn("Hiện có", item.quantity.toString(), textSecondary, textPrimary)
            InventoryStatColumn("Đang giữ", (item.reservedQuantity ?: 0).toString(), textSecondary, textPrimary)
            InventoryStatColumn("Giá trị", viewModel.formatItemValue(item.quantity, item.productPrice), textSecondary, textPrimary)
        }
    }
}

@Composable
fun InventoryStatColumn(
    label: String,
    value: String,
    labelColor: Color,
    valueColor: Color
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = labelColor
        )
        Text(
            text = value,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = valueColor
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FilterBottomSheetContent(
    selectedFilter: InventoryFilter,
    selectedSortBy: InventorySortBy,
    onFilterSelected: (InventoryFilter) -> Unit,
    onSortBySelected: (InventorySortBy) -> Unit,
    onApply: () -> Unit,
    textPrimary: Color,
    textSecondary: Color,
    cardColor: Color
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(bottom = 32.dp)
    ) {
        // Header
        Text(
            text = "Lọc & Sắp xếp",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = textPrimary,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        // Filter Section
        Text(
            text = "Trạng thái tồn kho",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = textSecondary,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(bottom = 24.dp)
        ) {
            InventoryFilter.entries.forEach { filter ->
                SelectableChip(
                    label = filter.displayName,
                    isSelected = selectedFilter == filter,
                    onClick = { onFilterSelected(filter) },
                    textPrimary = textPrimary
                )
            }
        }

        HorizontalDivider(color = textSecondary.copy(alpha = 0.2f))
        Spacer(modifier = Modifier.height(16.dp))

        // Sort Section
        Text(
            text = "Sắp xếp theo",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = textSecondary,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(bottom = 24.dp)
        ) {
            InventorySortBy.entries.forEach { sortBy ->
                SelectableChip(
                    label = sortBy.displayName,
                    isSelected = selectedSortBy == sortBy,
                    onClick = { onSortBySelected(sortBy) },
                    textPrimary = textPrimary
                )
            }
        }

        // Apply Button
        Button(
            onClick = onApply,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = InventoryPrimary),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Áp dụng",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun SelectableChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    textPrimary: Color
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(
                if (isSelected) InventoryPrimary else Color.Transparent
            )
            .border(
                width = 1.dp,
                color = if (isSelected) InventoryPrimary else textPrimary.copy(alpha = 0.3f),
                shape = RoundedCornerShape(20.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
            }
            Text(
                text = label,
                fontSize = 14.sp,
                color = if (isSelected) Color.White else textPrimary
            )
        }
    }
}

@Composable
fun FilterChip(
    label: String,
    onRemove: () -> Unit,
    textPrimary: Color
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(InventoryPrimary.copy(alpha = 0.2f))
            .padding(start = 12.dp, end = 4.dp, top = 6.dp, bottom = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = InventoryPrimary,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.width(4.dp))
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "Remove filter",
            tint = InventoryPrimary,
            modifier = Modifier
                .size(18.dp)
                .clip(CircleShape)
                .clickable { onRemove() }
                .padding(2.dp)
        )
    }
}
