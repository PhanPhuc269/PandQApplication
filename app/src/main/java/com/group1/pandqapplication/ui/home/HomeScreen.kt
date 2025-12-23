package com.group1.pandqapplication.ui.home

import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.ElectricalServices
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.group1.pandqapplication.shared.data.remote.dto.CategoryDto
import com.group1.pandqapplication.shared.data.remote.dto.ProductDto
import java.text.NumberFormat
import java.util.Locale

// Data Model for Banner (keep mock for now)
data class Banner(
    val title: String,
    val subtitle: String,
    val imageUrl: String
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    onLogout: () -> Unit,
    onProductClick: (String) -> Unit,
    onSearchClick: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val primaryColor = Color(0xFFec3713)

    // Mock Banners (keep for promotional content)
    val banners = listOf(
        Banner("iPhone 15 Pro Max", "Khám phá sức mạnh titan.", "https://lh3.googleusercontent.com/aida-public/AB6AXuAY8N6iyZ08u0SWafLXu9qKNEsbrwHuGcz3Hja6KeV4O4hutLoqq7UteVA4YOxkLe_mYFe-_E00HU4DJPrFiE_JQ_BMJDDxawi_7x7vdjVxdy4XduOnHDFtlOgJG2seHE0cIm8FCjLBfSWYlvTmBtKsCu4T7l8CbuLwyjBtkYaVYSng6FgEiZzeXuIGE96fkmh1Ph5oeO2Q8rFhO-lUFnREG9qTvBliG1WV8QTVD2kDSE-C9_eKqZEIvIkg8P94lMmWVZczwITcPkU"),
        Banner("Macbook Air M3", "Giảm giá tới 20% cho các dòng máy.", "https://lh3.googleusercontent.com/aida-public/AB6AXuBUBzal0oPpdf4ZTJwUzLahKqyyLsCnbwTNgeDQ0BK8F0W4J8WRYRBoMwxavtIOdE3NTJrJH0ZyXtu5xQJcQl_DYIsj_6JyEiinHwIDd99oFRFXT2l3xLKO-AnEqnSanLm4OCkZTRlOrwAw9-tW28zz9sRdTYLuuzu9ip-kSu7MgR234PwCK5N0NbWJOP88t0xq6V8hXbHmyeVPtyi_L0395naHqu65MkTuduPsoh6ZQDII0rurFvr93phaS-Qp_YFIz-jvJB8-ZBc"),
        Banner("Phụ kiện mới", "Nâng tầm trải nghiệm âm thanh.", "https://lh3.googleusercontent.com/aida-public/AB6AXuAMMPsPxp4PKLsvL6GNj5PL2mRZa2kRI5BbvBKZDOHUERj1NIO8jdNN56RQapffvJ2IIQThtuO7sbaOAD9x3WM0sDM-ktRS4Gcc5DZM9x1AizCwikcyd8jugubd_Hser1-ju7N44LV5Q65MEKMPE9uPIaPvdbaVg0PH6rLmW4A2O7NZiJzQQiSxAKWdlCHC6VuO2kIzgA1KE46ZZ5nWF8LgjwQ79Jy3H22846toBdD655JWRP50eRc0GCyWna-BOFxr60Ghg8fHNBU")
    )

    Scaffold(
        containerColor = Color(0xFFF8F6F6),
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF8F6F6))
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    imageVector = Icons.Filled.ElectricalServices,
                    contentDescription = "Logo",
                    tint = Color(0xFF1F2937),
                    modifier = Modifier.size(28.dp)
                )
                Text(
                    text = "ElectroStore",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111827)
                )
                IconButton(
                    onClick = { /* TODO: Cart */ },
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.ShoppingCart,
                        contentDescription = "Cart",
                        tint = Color(0xFF1F2937)
                    )
                }
            }
        }
    ) { paddingValues ->
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = primaryColor)
                }
            }
            uiState.error != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Đã xảy ra lỗi",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF111827)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = uiState.error ?: "Vui lòng thử lại",
                            fontSize = 14.sp,
                            color = Color(0xFF6B7280),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Thử lại",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = primaryColor,
                            modifier = Modifier.clickable { viewModel.refresh() }
                        )
                    }
                }
            }
            else -> {
                val gridState = rememberLazyGridState()
                
                // Detect when scrolled near the end
                val shouldLoadMore = remember {
                    derivedStateOf {
                        val layoutInfo = gridState.layoutInfo
                        val totalItems = layoutInfo.totalItemsCount
                        val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                        // Load more when 3 items from the end
                        lastVisibleItem >= totalItems - 3 && totalItems > 0
                    }
                }
                
                LaunchedEffect(shouldLoadMore.value) {
                    if (shouldLoadMore.value && uiState.hasMore && !uiState.isLoadingMore) {
                        viewModel.loadMoreProducts()
                    }
                }
                
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    state = gridState,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Header Content Spanning Full Width
                    item(span = { GridItemSpan(2) }) {
                        Column {
                            // Search Bar
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFFE5E7EB))
                                    .clickable { onSearchClick() }
                                    .padding(horizontal = 16.dp),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Filled.Search, contentDescription = null, tint = Color(0xFF6B7280))
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text("Tìm kiếm sản phẩm...", color = Color(0xFF6B7280))
                                }
                            }
                            Spacer(modifier = Modifier.height(16.dp))

                            // Banner Carousel
                            val pagerState = rememberPagerState(pageCount = { banners.size })
                            HorizontalPager(
                                state = pagerState,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .clip(RoundedCornerShape(12.dp))
                            ) { page ->
                                val banner = banners[page]
                                Box(modifier = Modifier.fillMaxSize()) {
                                    AsyncImage(
                                        model = banner.imageUrl,
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(Color.Black.copy(alpha = 0.3f))
                                    )
                                    Column(
                                        modifier = Modifier
                                            .align(Alignment.BottomStart)
                                            .padding(16.dp)
                                    ) {
                                        Text(
                                            text = banner.title,
                                            color = Color.White,
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = banner.subtitle,
                                            color = Color(0xFFE5E7EB),
                                            fontSize = 14.sp
                                        )
                                    }
                                }
                            }
                            
                            // Indicators
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                repeat(banners.size) { iteration ->
                                    val color = if (pagerState.currentPage == iteration) primaryColor else Color(0xFFD1D5DB)
                                    val width = if (pagerState.currentPage == iteration) 20.dp else 8.dp
                                    Box(
                                        modifier = Modifier
                                            .padding(4.dp)
                                            .clip(CircleShape)
                                            .background(color)
                                            .size(width = width, height = 8.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(16.dp))

                            // Categories from API
                            if (uiState.categories.isNotEmpty()) {
                                Text(
                                    text = "Danh mục",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF111827)
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    items(uiState.categories) { category ->
                                        CategoryItem(
                                            category = category,
                                            isSelected = uiState.selectedCategoryId == category.id,
                                            onClick = {
                                                if (uiState.selectedCategoryId == category.id) {
                                                    viewModel.clearCategoryFilter()
                                                } else {
                                                    viewModel.selectCategory(category.id, category.name)
                                                }
                                            }
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(24.dp))
                            }

                            // Section Title
                            val selectedCategoryName = uiState.selectedCategoryName
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (selectedCategoryName != null) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = selectedCategoryName,
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF111827)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Box(
                                            modifier = Modifier
                                                .size(24.dp)
                                                .clip(CircleShape)
                                                .background(Color(0xFFE5E7EB))
                                                .clickable { viewModel.clearCategoryFilter() },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Filled.Close,
                                                contentDescription = "Xóa bộ lọc",
                                                modifier = Modifier.size(16.dp),
                                                tint = Color(0xFF6B7280)
                                            )
                                        }
                                    }
                                } else {
                                    Text(
                                        text = "Sản phẩm nổi bật",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF111827)
                                    )
                                }
                                Text(
                                    text = "Xem tất cả",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = primaryColor
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }

                    // Product Grid from API
                    items(uiState.products) { product ->
                        ProductItem(product = product, onClick = { onProductClick(product.id) })
                    }
                    
                    // Loading More Indicator
                    if (uiState.isLoadingMore) {
                        item(span = { GridItemSpan(2) }) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    color = primaryColor,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }
                    }
                    
                    // Bottom Padding
                    item(span = { GridItemSpan(2) }) {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryItem(
    category: CategoryDto,
    isSelected: Boolean = false,
    onClick: () -> Unit = {}
) {
    val primaryColor = Color(0xFFec3713)
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .width(72.dp)
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(if (isSelected) primaryColor.copy(alpha = 0.1f) else Color(0xFFE5E7EB))
                .then(
                    if (isSelected) Modifier.background(
                        color = Color.Transparent,
                        shape = RoundedCornerShape(12.dp)
                    ) else Modifier
                ),
            contentAlignment = Alignment.Center
        ) {
            if (category.imageUrl != null) {
                AsyncImage(
                    model = category.imageUrl,
                    contentDescription = category.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Icon(
                    imageVector = Icons.Filled.Category,
                    contentDescription = null,
                    tint = if (isSelected) primaryColor else Color(0xFF4B5563)
                )
            }
            // Selection overlay
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(primaryColor.copy(alpha = 0.3f))
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = category.name,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) primaryColor else Color(0xFF1F2937),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun ProductItem(product: ProductDto, onClick: () -> Unit) {
    val formattedPrice = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
        .format(product.price)
        .replace("₫", "đ")

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .clickable { onClick() },
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE5E7EB))
        ) {
            AsyncImage(
                model = product.thumbnailUrl,
                contentDescription = product.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = product.name,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF1F2937),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = formattedPrice,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF111827)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    // Preview without ViewModel
}
