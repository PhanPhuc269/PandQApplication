package com.group1.pandqapplication.ui.search

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.border
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.group1.pandqapplication.shared.data.repository.CategoryItem

data class SearchProduct(
    val id: String,
    val name: String,
    val price: String,
    val rating: Double,
    val reviews: String,
    val imageUrl: String,
    val isBestSeller: Boolean = false,
    val isLowStock: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onBackClick: () -> Unit,
    onProductClick: (String) -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val backgroundColor = Color(0xFFF8F6F6)
    val primaryColor = Color(0xFFec3713)

    val uiState by viewModel.uiState.collectAsState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            // Đặt màu trong suốt cho cả Status Bar và Navigation Bar
            window.statusBarColor = Color.Transparent.toArgb()
            window.navigationBarColor = Color.Transparent.toArgb()

            // Cho phép vẽ tràn viền (Edge-to-Edge)
            WindowCompat.setDecorFitsSystemWindows(window, false)

            // Cấu hình icon màu tối (đen) vì nền app sáng
            val insetsController = WindowCompat.getInsetsController(window, view)
            insetsController.isAppearanceLightStatusBars = true
            insetsController.isAppearanceLightNavigationBars = true
        }
    }

    if (uiState.showFilterSheet) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.onDismissFilterSheet() },
            sheetState = sheetState,
            containerColor = Color.White,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
        ) {
            FilterBottomSheetContent(
                onApply = { viewModel.onApplyFilters() },
                onReset = { viewModel.onResetFilters() },
                primaryColor = primaryColor,
                priceRange = uiState.priceRange,
                onPriceRangeChange = { viewModel.onPriceRangeChange(it) },
                categories = uiState.categories,
                selectedCategoryId = uiState.selectedCategoryId,
                onCategorySelect = { viewModel.onCategorySelect(it) },
                selectedRatingOption = uiState.selectedRatingOption,
                onRatingOptionChange = { viewModel.onRatingOptionChange(it) },
                inStock = uiState.inStockOnly,
                onInStockChange = { viewModel.onInStockToggle(it) }
            )
        }
    }

    Scaffold(
        containerColor = backgroundColor,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            Column(modifier = Modifier.background(backgroundColor)) {
                // Top Navigation / Search Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(top = 8.dp, bottom = 8.dp, start = 16.dp, end = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Back Button
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .size(25.dp)
                            .clip(CircleShape)
                            .background(Color.Transparent)
                    ) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color(0xFF374151))
                    }

                    // Search Bar
                    Box(modifier = Modifier.weight(1f)) {
                        TextField(
                            value = uiState.searchQuery,
                            onValueChange = { viewModel.onSearchQueryChange(it) },
                            placeholder = { Text(
                                "Tìm theo tên sản phẩm...",
                                color = Color.Gray,
                                fontSize = 13.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )  },
                            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null, tint = Color(0xFF9CA3AF)) },
                            trailingIcon = if (uiState.searchQuery.isNotEmpty()) {
                                { 
                                    IconButton(onClick = { viewModel.onClearSearch() }) {
                                        Icon(Icons.Filled.Cancel, contentDescription = "Clear", tint = Color(0xFF9CA3AF)) 
                                    }
                                }
                            } else null,
                            singleLine = true,
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth().padding(0.dp),
                            textStyle = TextStyle(fontSize = 14.sp)
                        )
                    }

                }

                // Filters and Sort Bar
                Column(modifier = Modifier.padding(bottom = 4.dp)) {
                    // Chips - only show when there are active filters
                    if (uiState.activeFilters.isNotEmpty()) {
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            item {
                                Text(
                                    "Clear All", 
                                    fontSize = 12.sp, 
                                    fontWeight = FontWeight.SemiBold, 
                                    color = Color(0xFF6B7280),
                                    modifier = Modifier.padding(end = 4.dp).clickable { viewModel.onClearAllFilters() }
                                )
                            }
                            items(uiState.activeFilters.size) { index ->
                                Surface(
                                    color = primaryColor.copy(alpha = 0.1f),
                                    shape = CircleShape,
                                    border = androidx.compose.foundation.BorderStroke(1.dp, primaryColor.copy(alpha = 0.2f))
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(start = 12.dp, end = 8.dp, top = 6.dp, bottom = 6.dp)
                                    ) {
                                        Text(uiState.activeFilters[index], fontSize = 12.sp, fontWeight = FontWeight.Medium, color = primaryColor)
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Icon(
                                            Icons.Filled.Close, 
                                            contentDescription = null, 
                                            tint = primaryColor,
                                            modifier = Modifier.size(14.dp).clickable { viewModel.onRemoveFilter(index) }
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Results Count & Sort
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (uiState.isLoading) "Đang tải..." else "${uiState.totalResults} kết quả",
                            color = Color(0xFF6B7280),
                            fontSize = 14.sp
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // Sort dropdown
                            var showSortMenu by remember { mutableStateOf(false) }
                            Box {
                                Surface(
                                    onClick = { showSortMenu = true },
                                    shape = RoundedCornerShape(20.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5E7EB)),
                                    color = Color.White
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                    ) {
                                        Icon(
                                            Icons.Filled.Tune,
                                            contentDescription = null,
                                            tint = primaryColor,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = when (uiState.sortBy) {
                                                "newest" -> "Mới nhất"
                                                "price_asc" -> "Giá tăng dần"
                                                "price_desc" -> "Giá giảm dần"
                                                "rating" -> "Đánh giá"
                                                else -> "Sắp xếp"
                                            },
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = Color(0xFF374151)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Icon(
                                            Icons.Outlined.ExpandMore,
                                            contentDescription = null,
                                            tint = Color(0xFF9CA3AF),
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }

                                DropdownMenu(
                                    expanded = showSortMenu,
                                    onDismissRequest = { showSortMenu = false },
                                    modifier = Modifier.background(Color.White).width(180.dp)
                                ) {
                                    @Composable
                                    fun SortMenuItem(
                                        text: String,
                                        icon: androidx.compose.ui.graphics.vector.ImageVector,
                                        isSelected: Boolean,
                                        onClick: () -> Unit
                                    ) {
                                        DropdownMenuItem(
                                            text = { 
                                                Text(
                                                    text, 
                                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                                    color = if (isSelected) primaryColor else Color(0xFF374151)
                                                ) 
                                            },
                                            onClick = onClick,
                                            leadingIcon = {
                                                Icon(
                                                    icon, 
                                                    contentDescription = null, 
                                                    tint = if (isSelected) primaryColor else Color(0xFF9CA3AF),
                                                    modifier = Modifier.size(20.dp)
                                                )
                                            },
                                            trailingIcon = if (isSelected) {
                                                { Icon(Icons.Filled.Check, contentDescription = null, tint = primaryColor, modifier = Modifier.size(16.dp)) }
                                            } else null
                                        )
                                    }

                                    SortMenuItem(
                                        text = "Mới nhất",
                                        icon = Icons.Filled.AccessTime,
                                        isSelected = uiState.sortBy == "newest",
                                        onClick = { viewModel.onSortChange("newest"); showSortMenu = false }
                                    )
                                    SortMenuItem(
                                        text = "Giá tăng dần",
                                        icon = Icons.Filled.TrendingUp,
                                        isSelected = uiState.sortBy == "price_asc",
                                        onClick = { viewModel.onSortChange("price_asc"); showSortMenu = false }
                                    )
                                    SortMenuItem(
                                        text = "Giá giảm dần",
                                        icon = Icons.Filled.TrendingDown,
                                        isSelected = uiState.sortBy == "price_desc",
                                        onClick = { viewModel.onSortChange("price_desc"); showSortMenu = false }
                                    )
                                    SortMenuItem(
                                        text = "Đánh giá cao",
                                        icon = Icons.Filled.Star,
                                        isSelected = uiState.sortBy == "rating",
                                        onClick = { viewModel.onSortChange("rating"); showSortMenu = false }
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(8.dp))

                            // Filter Button
                            Box(modifier = Modifier.size(38.dp)) {
                                IconButton(
                                    onClick = { viewModel.onShowFilterSheet() },
                                    modifier = Modifier
                                        .fillMaxSize()
                                ) {
                                    Icon(Icons.Filled.Tune, contentDescription = "Filter")
                                }
                                // Badge - only show when there are active filters
                                if (uiState.activeFilters.isNotEmpty()) {
                                    Box(modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .offset(x = 4.dp, y = (-4).dp)
                                        .size(20.dp)
                                        .clip(CircleShape)
                                        .background(Color.White)
                                        .padding(2.dp)) {
                                        Box(modifier = Modifier.fillMaxSize().clip(CircleShape).background(primaryColor), contentAlignment = Alignment.Center) {
                                            Text("${uiState.activeFilters.size}", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                // Loading state
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = primaryColor)
                    }
                }
                // Error state
                uiState.errorMessage != null -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "😢",
                            fontSize = 48.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Đã xảy ra lỗi",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF111827)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = uiState.errorMessage ?: "",
                            fontSize = 14.sp,
                            color = Color(0xFF6B7280),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(
                            onClick = { viewModel.onSearchQueryChange(uiState.searchQuery) },
                            colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Thử lại")
                        }
                    }
                }
                // Empty state
                uiState.products.isEmpty() -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "🔍",
                            fontSize = 48.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Không tìm thấy sản phẩm",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF111827)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Hãy thử tìm kiếm với từ khóa khác hoặc điều chỉnh bộ lọc",
                            fontSize = 14.sp,
                            color = Color(0xFF6B7280),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
                // Success state with products
                else -> {
                    val bottomPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        contentPadding = PaddingValues(bottom = bottomPadding + 16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(uiState.products) { product ->
                            ProductGridItem(
                                product = product, 
                                primaryColor = primaryColor,
                                onClick = { onProductClick(product.id) }
                            )
                        }
                        
                        // Load more trigger - when this item becomes visible, load more
                        if (uiState.hasMore && !uiState.isLoadingMore) {
                            item(span = { GridItemSpan(2) }) {
                                LaunchedEffect(Unit) {
                                    viewModel.loadMoreProducts()
                                }
                                Box(
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(
                                        color = primaryColor,
                                        modifier = Modifier.size(32.dp)
                                    )
                                }
                            }
                        }
                        
                        // Loading more indicator
                        if (uiState.isLoadingMore) {
                            item(span = { GridItemSpan(2) }) {
                                Box(
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(
                                        color = primaryColor,
                                        modifier = Modifier.size(32.dp)
                                    )
                                }
                            }
                        }
                        
                        // End of results
                        if (!uiState.hasMore && uiState.products.isNotEmpty()) {
                            item(span = { GridItemSpan(2) }) {
                                Box(
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp), 
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        "ĐÃ HẾT KẾT QUẢ", 
                                        fontSize = 12.sp, 
                                        fontWeight = FontWeight.Medium, 
                                        color = Color(0xFF9CA3AF),
                                        letterSpacing = 2.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProductGridItem(
    product: SearchProduct, 
    primaryColor: Color,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier.clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.8f) // 4:5 Aspect Ratio
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White)
        ) {
            AsyncImage(
                model = product.imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Badges
            if (product.isBestSeller) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(12.dp)
                        .background(primaryColor, RoundedCornerShape(4.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        "BEST SELLER", 
                        fontSize = 10.sp, 
                        fontWeight = FontWeight.Bold, 
                        color = Color.White
                    )
                }
            } else if (product.isLowStock) {
                 Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .background(androidx.compose.ui.graphics.Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f))
                        ))
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Low Stock", 
                        fontSize = 12.sp, 
                        fontWeight = FontWeight.Medium, 
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Title
        Text(
            text = product.name,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF111827),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            lineHeight = 18.sp
        )

        // Rating - only show if product has reviews
        if (product.rating > 0) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
                Icon(Icons.Filled.Star, contentDescription = null, tint = Color(0xFFFACC15), modifier = Modifier.size(16.dp))
                Text(product.rating.toString(), fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color(0xFF4B5563), modifier = Modifier.padding(horizontal = 4.dp))
                Text(product.reviews, fontSize = 12.sp, color = Color(0xFF9CA3AF))
            }
        } else {
            Spacer(modifier = Modifier.height(4.dp))
        }

        // Price
        Text(
            text = product.price, 
            fontSize = 16.sp, 
            fontWeight = FontWeight.Bold, 
            color = primaryColor
        )
    }
}

@Preview
@Composable
fun SearchScreenPreview() {
    SearchScreen(onBackClick = {}, onProductClick = {})
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBottomSheetContent(
    onApply: () -> Unit,
    onReset: () -> Unit,
    primaryColor: Color,
    priceRange: ClosedFloatingPointRange<Float>,
    onPriceRangeChange: (ClosedFloatingPointRange<Float>) -> Unit,
    categories: List<CategoryItem>,
    selectedCategoryId: String?,
    onCategorySelect: (String?) -> Unit,
    selectedRatingOption: Int,
    onRatingOptionChange: (Int) -> Unit,
    inStock: Boolean,
    onInStockChange: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(bottom = 24.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Bộ lọc", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF111827))
            Text(
                "Đặt lại",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = primaryColor,
                modifier = Modifier.clickable { onReset() }
            )
        }

        // Categories
        if (categories.isNotEmpty()) {
            Text("Danh mục", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFF374151), modifier = Modifier.padding(bottom = 12.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(categories.size) { index ->
                    val category = categories[index]
                    val isSelected = selectedCategoryId == category.id
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .border(1.dp, if (isSelected) primaryColor else Color(0xFFE5E7EB), RoundedCornerShape(8.dp))
                            .background(if (isSelected) primaryColor.copy(alpha = 0.1f) else Color.Transparent)
                            .clickable { onCategorySelect(category.id) }
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Text(category.name, fontSize = 14.sp, color = if (isSelected) primaryColor else Color(0xFF4B5563))
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Price Range
        val priceFormatter = java.text.NumberFormat.getInstance(java.util.Locale("vi", "VN"))
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Khoảng giá", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFF374151))
            Text("${priceFormatter.format(priceRange.start.toLong())}đ - ${priceFormatter.format(priceRange.endInclusive.toLong())}đ", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = primaryColor)
        }

        RangeSlider(
            value = priceRange,
            onValueChange = onPriceRangeChange,
            valueRange = 0f..50000000f,
            colors = SliderDefaults.colors(
                thumbColor = Color.White,
                activeTrackColor = primaryColor,
                inactiveTrackColor = Color(0xFFE5E7EB),
                activeTickColor = Color.Transparent,
                inactiveTickColor = Color.Transparent
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            // Min
            Row(
                modifier = Modifier
                    .weight(1f)
                    .background(Color(0xFFF9FAFB), RoundedCornerShape(8.dp))
                    .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(8.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Min", fontSize = 12.sp, color = Color(0xFF6B7280))
                Spacer(Modifier.width(8.dp))
                Text("${priceFormatter.format(priceRange.start.toLong())}đ", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF111827))
            }
            // Max
            Row(
                modifier = Modifier
                    .weight(1f)
                    .background(Color(0xFFF9FAFB), RoundedCornerShape(8.dp))
                    .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(8.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Max", fontSize = 12.sp, color = Color(0xFF6B7280))
                Spacer(Modifier.width(8.dp))
                Text("${priceFormatter.format(priceRange.endInclusive.toLong())}đ", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF111827))
            }
        }

        // Rating
        Text("Rating", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFF374151), modifier = Modifier.padding(top = 24.dp, bottom = 12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
            val ratingOptions = listOf(
                Triple(1, "4.0", true),
                Triple(2, "3.0", true),
                Triple(3, "Any", true)
            )
            ratingOptions.forEach { (id, label, hasStar) ->
                val isSelected = selectedRatingOption == id
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .border(1.dp, if (isSelected) primaryColor else Color(0xFFE5E7EB), RoundedCornerShape(12.dp))
                        .background(if (isSelected) primaryColor.copy(alpha = 0.1f) else Color.Transparent)
                        .clickable { onRatingOptionChange(id) }
                        .padding(vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(label, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = if (isSelected) primaryColor else Color(0xFF4B5563))
                        if (hasStar && id != 3) {
                            Icon(Icons.Filled.Star, contentDescription = null, tint = if (isSelected) primaryColor else Color(0xFFFACC15), modifier = Modifier.size(14.dp).padding(start = 2.dp))
                        } else if (id == 3) {
                             Icon(Icons.Filled.Star, contentDescription = null, tint = if (isSelected) primaryColor else Color(0xFF9CA3AF), modifier = Modifier.size(14.dp).padding(start = 2.dp))
                        }
                    }
                    if (id != 3) {
                        Text("& Up", fontSize = 10.sp, color = if (isSelected) primaryColor else Color(0xFF9CA3AF))
                    }
                }
            }
        }

        // Stock Switch
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 24.dp, bottom = 32.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("In Stock Only", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFF374151))
            Switch(
                checked = inStock,
                onCheckedChange = onInStockChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = primaryColor,
                    uncheckedThumbColor = Color.White,
                    uncheckedTrackColor = Color(0xFFE5E7EB),
                    uncheckedBorderColor = Color.Transparent
                )
            )
        }

        // Apply Button
        Button(
            onClick = onApply,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
            shape = RoundedCornerShape(12.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
        ) {
            Text("Áp dụng bộ lọc", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}
