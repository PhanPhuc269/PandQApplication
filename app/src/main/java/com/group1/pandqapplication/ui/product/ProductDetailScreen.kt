package com.group1.pandqapplication.ui.product

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.group1.pandqapplication.shared.data.remote.dto.ProductDetailDto
import com.group1.pandqapplication.shared.data.remote.dto.ProductSpecificationDto
import com.group1.pandqapplication.shared.data.remote.dto.RelatedProductDto
import com.group1.pandqapplication.shared.data.remote.dto.ReviewDto
import java.text.NumberFormat
import java.util.Locale
import android.app.Activity
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProductDetailScreen(
    onBackClick: () -> Unit,
    onCartClick: () -> Unit,
    onProductClick: (String) -> Unit = {},
    userId: String = "",
    viewModel: ProductDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val primaryColor = Color(0xFFec3713)
    val scrollState = rememberScrollState()
    val snackbarHostState = remember { SnackbarHostState() }

    val tabs = listOf("Description", "Specifications", "Reviews")

    // Handle add to cart success/error
    LaunchedEffect(uiState.addToCartSuccess, uiState.addToCartError) {
        if (uiState.addToCartSuccess) {
            snackbarHostState.showSnackbar("Đã thêm vào giỏ hàng thành công!")
            viewModel.clearAddToCartState()
        }
        if (uiState.addToCartError != null) {
            snackbarHostState.showSnackbar(uiState.addToCartError ?: "Có lỗi xảy ra")
            viewModel.clearAddToCartState()
        }
    }

    // Handle review submission success - error is handled by AlertDialog in WriteReviewScreen
    LaunchedEffect(uiState.reviewSubmitSuccess) {
        if (uiState.reviewSubmitSuccess) {
            snackbarHostState.showSnackbar("Đánh giá của bạn đã được gửi thành công!")
            viewModel.clearReviewSubmitState()
        }
    }



    // Configure status bar for this screen (transparent with white icons)
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Transparent.toArgb()
            WindowCompat.setDecorFitsSystemWindows(window, false)
            // White icons for dark image header
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    // Main Content
    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            contentWindowInsets = WindowInsets(0.dp),
            bottomBar = {
                if (!uiState.isLoading && uiState.product != null) {
                    BottomCartBar(
                        price = uiState.product!!.price,
                        quantity = uiState.quantity,
                        onIncrease = { viewModel.increaseQuantity() },
                        onDecrease = { viewModel.decreaseQuantity() },
                        primaryColor = primaryColor,
                        onCartClick = onCartClick,
                        onAddToCart = { viewModel.addToCart(userId) }
                    )
                }
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                if (uiState.isLoading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = primaryColor)
                    }
                } else if (uiState.product != null) {
                    val product = uiState.product!!
                    
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(scrollState)
                    ) {
                        // Header Image Section
                        ProductImageHeader(
                            product = product,
                            primaryColor = primaryColor,
                            onBackClick = onBackClick,
                            onCartClick = onCartClick
                        )

                        // Product Details Content
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text(
                                text = product.name,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF18181B),
                                lineHeight = 32.sp
                            )
                            
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = formatPrice(product.price),
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = primaryColor
                                )
                                Surface(
                                    color = Color(0xFF22C55E).copy(alpha = 0.1f),
                                    shape = RoundedCornerShape(100.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF22C55E).copy(alpha = 0.2f))
                                ) {
                                    Text(
                                        text = if (product.status == "ACTIVE") "Còn hàng" else "Hết hàng",
                                        color = Color(0xFF16A34A),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.dp)
                                    )
                                }
                            }
    
                            // Rating
                            Row(
                                modifier = Modifier.padding(top = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Filled.Star, contentDescription = null, tint = Color(0xFFEAB308), modifier = Modifier.size(18.dp))
                                Text(
                                    text = String.format("%.1f", product.averageRating ?: 0.0),
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 4.dp)
                                )
                                Text(
                                    text = "(${product.reviewCount ?: 0} đánh giá)",
                                    color = Color.Gray,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = "Xem tất cả",
                                    color = primaryColor,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier
                                        .padding(start = 8.dp)
                                        .clickable { viewModel.selectTab(2) }
                                )
                            }

    
                            // Tabs
                            Spacer(modifier = Modifier.height(24.dp))
                            Row(modifier = Modifier.fillMaxWidth()) {
                                tabs.forEachIndexed { index, title ->
                                    Column(
                                        modifier = Modifier
                                            .padding(end = 24.dp)
                                            .clickable { viewModel.selectTab(index) }
                                    ) {
                                        Text(
                                            text = title,
                                            fontSize = 14.sp,
                                            fontWeight = if (uiState.selectedTab == index) FontWeight.SemiBold else FontWeight.Medium,
                                            color = if (uiState.selectedTab == index) primaryColor else Color(0xFF71717A)
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        if (uiState.selectedTab == index) {
                                            Box(
                                                modifier = Modifier
                                                    .width(24.dp)
                                                    .height(2.dp)
                                                    .background(primaryColor)
                                            )
                                        }
                                    }
                                }
                            }
                            HorizontalDivider(color = Color(0xFFE4E4E7))
    
                            Spacer(modifier = Modifier.height(24.dp))
                            
    
                            when (uiState.selectedTab) {
                                0 -> DescriptionSection(product.description)
                                1 -> SpecificationsSection(product.specifications ?: emptyList())
                                2 -> ReviewsSection(
                                    reviews = uiState.reviews,
                                    averageRating = product.averageRating,
                                    reviewCount = product.reviewCount,
                                    isLoading = uiState.isLoadingReviews,
                                    filterByRating = uiState.filterByRating,
                                    sortBy = uiState.sortBy,
                                    onFilterChange = { viewModel.filterReviews(it) },
                                    onSortChange = { viewModel.sortReviews(it) }
                                )
                            }
    
                            // Related Products
                            product.relatedProducts?.let { related ->
                                if (related.isNotEmpty()) {
                                    Spacer(modifier = Modifier.height(32.dp))
                                    RelatedProductsSection(
                                        relatedProducts = related,
                                        onProductClick = onProductClick
                                    )
                                }
                            }
                            
                            // Bottom spacer to clear the floating bottom bar
                            Spacer(modifier = Modifier.height(100.dp))
                        }
                    }
                }
            }
        }
        
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 100.dp)
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProductImageHeader(
    product: ProductDetailDto,
    primaryColor: Color,
    onBackClick: () -> Unit,
    onCartClick: () -> Unit
) {
    val images = product.images?.map { it.imageUrl } ?: listOf(product.thumbnailUrl ?: "")
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(4f / 5f)
    ) {
        val pagerState = rememberPagerState(pageCount = { images.size })
        HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) { page ->
            AsyncImage(
                model = images[page],
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
        
        // Indicators
        if (images.size > 1) {
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(images.size) { iteration ->
                    val color = if (pagerState.currentPage == iteration) primaryColor else Color.White.copy(alpha = 0.5f)
                    Box(
                        modifier = Modifier
                            .padding(2.dp)
                            .clip(CircleShape)
                            .background(color)
                            .size(if (pagerState.currentPage == iteration) 24.dp else 6.dp, 6.dp)
                    )
                }
            }
        }
        
        // Top Bar Overlay
        val context = androidx.compose.ui.platform.LocalContext.current
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.verticalGradient(listOf(Color.Black.copy(alpha = 0.5f), Color.Transparent)))
                .statusBarsPadding()
                .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.White.copy(alpha = 0.2f), CircleShape)
            ) {
                Icon(Icons.Filled.ArrowBackIosNew, contentDescription = "Back", tint = Color.White)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                IconButton(
                    onClick = onCartClick,
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.White.copy(alpha = 0.2f), CircleShape)
                ) {
                    Icon(Icons.Filled.ShoppingBag, contentDescription = "Cart", tint = Color.White)
                }
                IconButton(
                    onClick = {
                         val productName = product.name // Capture for lambda
                         val productId = product.id
                         shareProduct(context, productId, productName)
                    },
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.White.copy(alpha = 0.2f), CircleShape)
                ) {
                    Icon(Icons.Filled.Share, contentDescription = "Share", tint = Color.White)
                }
                IconButton(
                    onClick = {},
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.White.copy(alpha = 0.2f), CircleShape)
                ) {
                    Icon(Icons.Filled.FavoriteBorder, contentDescription = "Favorite", tint = Color.White)
                }
            }
        }
    }
}

private fun shareProduct(context: android.content.Context, productId: String, productName: String) {
    // Use centralized config for deep link domain (from local.properties)
    val deepLink = com.group1.pandqapplication.util.AppConfig.getProductDeepLink(productId)
    val shareText = "Xem sản phẩm $productName tại ${com.group1.pandqapplication.util.AppConfig.APP_NAME}: $deepLink"
    
    val sendIntent = android.content.Intent().apply {
        action = android.content.Intent.ACTION_SEND
        putExtra(android.content.Intent.EXTRA_TEXT, shareText)
        type = "text/plain"
    }
    
    val shareIntent = android.content.Intent.createChooser(sendIntent, "Chia sẻ sản phẩm qua")
    context.startActivity(shareIntent)
}

@Composable
fun DescriptionSection(description: String?) {
    Text(
        text = description ?: "Chưa có mô tả cho sản phẩm này.",
        color = Color(0xFF52525B),
        fontSize = 14.sp,
        lineHeight = 22.sp
    )
}

@Composable
fun SpecificationsSection(specifications: List<ProductSpecificationDto>) {
    if (specifications.isEmpty()) {
        Text(
            text = "Chưa có thông số kỹ thuật.",
            color = Color(0xFF52525B),
            fontSize = 14.sp
        )
        return
    }
    
    Column {
        val rows = specifications.chunked(2)
        rows.forEach { rowSpecs ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                rowSpecs.forEach { spec ->
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White)
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = getSpecIcon(spec.specKey),
                            contentDescription = null,
                            tint = Color(0xFF94A3B8),
                            modifier = Modifier.size(24.dp)
                        )
                        Column(modifier = Modifier.padding(start = 12.dp)) {
                            Text(spec.specKey.uppercase(), fontSize = 10.sp, color = Color(0xFF64748B))
                            Text(spec.specValue, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF0F172A))
                        }
                    }
                }
                // Fill remaining space if odd number of specs
                if (rowSpecs.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

fun getSpecIcon(specKey: String): ImageVector {
    return when (specKey.lowercase()) {
        "pin", "battery" -> Icons.Filled.BatteryFull
        "ram" -> Icons.Filled.Memory
        "cpu", "chip" -> Icons.Filled.Speed
        "màn hình", "screen", "display" -> Icons.Filled.Smartphone
        "camera" -> Icons.Filled.CameraAlt
        "bộ nhớ", "storage" -> Icons.Filled.Storage
        "kết nối", "connect", "bluetooth" -> Icons.Filled.Bluetooth
        "khối lượng", "weight" -> Icons.Filled.MonitorWeight
        "màu sắc", "color" -> Icons.Filled.Palette
        else -> Icons.Filled.Info
    }
}

@Composable
fun ReviewsSection(
    reviews: List<ReviewDto>,
    averageRating: Double?,
    reviewCount: Int?,
    isLoading: Boolean,
    filterByRating: Int? = null,
    sortBy: String = "newest",
    onFilterChange: (Int?) -> Unit = {},
    onSortChange: (String) -> Unit = {}
) {
    var showSortMenu by remember { mutableStateOf(false) }
    
    Column(modifier = Modifier.fillMaxWidth()) {
         // Header
        Text(
            "Đánh giá & Xếp hạng",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF18181B),
            modifier = Modifier.padding(bottom = 24.dp, start = 4.dp, end = 4.dp)
        )

        // Rating Summary Block (Big score left, Bars right)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left: Big Score
            Column(
                modifier = Modifier.padding(end = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = String.format("%.1f", averageRating ?: 0.0),
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF18181B),
                    lineHeight = 48.sp
                )
                Row(modifier = Modifier.padding(vertical = 4.dp)) {
                    val rating = (averageRating ?: 0.0).toInt()
                    repeat(5) { index ->
                        val icon = when {
                            index < rating -> Icons.Filled.Star
                            (averageRating ?: 0.0) - index >= 0.5 -> Icons.Filled.StarHalf
                            else -> Icons.Filled.StarBorder // Hoặc icon star outline
                        }
                        // Dùng StarBorder hoặc tint màu khác cho sao rỗng nếu muốn chuẩn hơn
                        Icon(
                            imageVector = if(index < rating || (averageRating ?: 0.0) - index >= 0.5) Icons.Filled.Star else Icons.Filled.Star, // Tạm dùng Star hết và tint màu
                            contentDescription = null, 
                            tint = if(index < rating || (averageRating ?: 0.0) - index >= 0.5) Color(0xFFec3713) else Color(0xFFE4E4E7),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                Text(
                    "${reviewCount ?: 0} đánh giá", 
                    fontSize = 14.sp, 
                    color = Color(0xFF71717A),
                    fontWeight = FontWeight.Medium
                )
            }

            // Right: Progress Bars
            Column(modifier = Modifier.weight(1f)) {
                val totalReviews = reviews.size
                val distribution = reviews.groupingBy { it.rating }.eachCount()
                
                (5 downTo 1).forEach { star ->
                    val count = distribution[star] ?: 0
                    val percentage = if (totalReviews > 0) (count * 100 / totalReviews) else 0
                    RatingBarRow(star, percentage)
                }
            }
        }
        
        HorizontalDivider(color = Color(0xFFE4E4E7), modifier = Modifier.padding(bottom = 24.dp))

        // Filter Chips & Sort
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                item {
                    FilterChipPill(
                        selected = filterByRating == null,
                        label = "Tất cả",
                        onClick = { onFilterChange(null) }
                    )
                }
                items(listOf(5, 4, 3, 2, 1)) { rating ->
                    FilterChipPill(
                        selected = filterByRating == rating,
                        label = "$rating sao",
                        onClick = { onFilterChange(rating) }
                    )
                }
            }
            
            // Sort Icon only to save space or Icon + Text shortened
            Box {
                 Row(
                    modifier = Modifier
                        .clickable { showSortMenu = true }
                        .padding(start = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Filled.Sort, contentDescription = null, tint = Color(0xFF18181B))
                    Text(
                        "Lọc", 
                        fontSize = 14.sp, 
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF18181B),
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
                DropdownMenu(
                    expanded = showSortMenu,
                    onDismissRequest = { showSortMenu = false },
                    modifier = Modifier.background(Color.White)
                ) {
                    DropdownMenuItem(text = { Text("Mới nhất") }, onClick = { onSortChange("newest"); showSortMenu = false })
                    DropdownMenuItem(text = { Text("Cao nhất") }, onClick = { onSortChange("highest"); showSortMenu = false })
                    DropdownMenuItem(text = { Text("Thấp nhất") }, onClick = { onSortChange("lowest"); showSortMenu = false })
                }
            }
        }
        
        // Reviews List
        when {
            isLoading -> {
                Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color(0xFFec3713))
                }
            }
            reviews.isEmpty() -> {
                Text(
                    text = "Chưa có đánh giá nào.",
                    color = Color(0xFF71717A),
                    fontSize = 14.sp,
                    modifier = Modifier.fillMaxWidth().padding(32.dp),
                    textAlign = TextAlign.Center
                )
            }
            else -> {
                reviews.forEachIndexed { index, review ->
                    ReviewItem(review)
                    if (index < reviews.size - 1) {
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 16.dp),
                            color = Color(0xFFF4F4F5)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RatingBarRow(star: Int, percentage: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp)
    ) {
        Text(
            text = "$star", 
            fontSize = 12.sp, 
            fontWeight = FontWeight.Medium,
            color = Color(0xFF18181B),
            modifier = Modifier.width(16.dp)
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .height(6.dp)
                .clip(RoundedCornerShape(100))
                .background(Color(0xFFE4E4E7))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(percentage / 100f)
                    .fillMaxHeight()
                    .background(Color(0xFFec3713))
            )
        }
        Text(
            text = "$percentage%", 
            fontSize = 12.sp, 
            color = Color(0xFF71717A),
            modifier = Modifier.width(50.dp).padding(start = 8.dp),
            textAlign = TextAlign.End
        )
    }
}

@Composable
fun FilterChipPill(selected: Boolean, label: String, onClick: () -> Unit) {
    Surface(
        color = if (selected) Color(0xFFec3713) else Color(0xFFF4F4F5),
        shape = RoundedCornerShape(100),
        modifier = Modifier.clickable { onClick() }
    ) {
        Text(
            text = label,
            color = if (selected) Color.White else Color(0xFF18181B),
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

@Composable
fun ReviewItem(review: ReviewDto) {
    Column(modifier = Modifier.fillMaxWidth()) {
        // 1. Header: Avatar + Name + Time
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (!review.userAvatar.isNullOrEmpty()) {
                AsyncImage(
                    model = review.userAvatar, 
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF4F4F5)), // Placeholder color
                     placeholder = null,
                     error = null,
                     contentScale = ContentScale.Crop
                )
            } else {
                 Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE5E7EB)),
                    contentAlignment = Alignment.Center
                ) {
                     Text(
                        text = review.userName?.take(1)?.uppercase() ?: "U",
                        color = Color(0xFF6B7280),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
            
            Column(modifier = Modifier.padding(start = 12.dp)) {
                Text(
                    text = review.userName ?: "Người dùng",
                    fontWeight = FontWeight.Bold, // Medium -> Bold per design
                    fontSize = 15.sp,
                    color = Color(0xFF18181B)
                )
                Text(
                    text = formatRelativeTime(review.createdAt), // "2 days ago"
                    fontSize = 12.sp,
                    color = Color(0xFF71717A)
                )
            }
        }

        // 2. Stars Row (Separate row per HTML reference)
        Row(modifier = Modifier.padding(top = 8.dp)) {
             repeat(5) { index ->
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = null,
                    tint = if (index < review.rating) Color(0xFFec3713) else Color(0xFFE4E4E7), // Primary color stars
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        // 3. Content
        review.comment?.let { comment ->
            if (comment.isNotEmpty()) {
                Text(
                    text = comment,
                    fontSize = 15.sp,
                    lineHeight = 24.sp,
                    color = Color(0xFF3F3F46), // Zinc-700
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }

        // 4. Images
        review.imageUrls?.let { imageUrls ->
            if (imageUrls.isNotEmpty()) {
                LazyRow(
                    modifier = Modifier.padding(top = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(imageUrls) { imageUrl ->
                        AsyncImage(
                            model = imageUrl,
                            contentDescription = null,
                            modifier = Modifier
                                .size(80.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFF4F4F5)),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        }

        // 5. Helpful Button (Thumb Up)
        Row(
            modifier = Modifier.padding(top = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(4.dp),
                color = Color.Transparent,
                modifier = Modifier.clickable { /* TODO: Implement helpful */ }
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                     Icon(
                        imageVector = Icons.Outlined.ThumbUp,
                        contentDescription = "Helpful",
                        tint = Color(0xFF71717A),
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "Hữu ích", // Removed count "12"
                        fontSize = 13.sp,
                        color = Color(0xFF71717A),
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(start = 6.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun RelatedProductsSection(
    relatedProducts: List<RelatedProductDto>,
    onProductClick: (String) -> Unit = {}
) {
    Column {
        Text(
            text = "Sản phẩm liên quan",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF111827)
        )
        Spacer(modifier = Modifier.height(12.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(relatedProducts) { product ->
                Card(
                    modifier = Modifier
                        .width(140.dp)
                        .clickable { onProductClick(product.id) },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column {
                        AsyncImage(
                            model = product.thumbnailUrl,
                            contentDescription = product.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxWidth().height(120.dp)
                        )
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text(
                                text = product.name,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                maxLines = 2,
                                color = Color(0xFF1F2937)
                            )
                            Text(
                                text = formatPrice(product.price),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFec3713)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BottomCartBar(
    price: Double,
    quantity: Int,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    primaryColor: Color,
    onCartClick: () -> Unit = {},
    onAddToCart: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = Color.White.copy(alpha = 0.95f),
        shadowElevation = 8.dp,
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF1F5F9).copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Quantity Selector
            Surface(
                shape = CircleShape,
                color = Color(0xFFF1F5F9),
                modifier = Modifier.height(48.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 4.dp)
                ) {
                    IconButton(onClick = onDecrease) {
                        Icon(Icons.Filled.Remove, contentDescription = "Giảm", tint = Color.Gray)
                    }
                    Text(
                        text = quantity.toString(),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.width(20.dp),
                        textAlign = TextAlign.Center
                    )
                    IconButton(onClick = onIncrease) {
                        Icon(Icons.Filled.Add, contentDescription = "Tăng", tint = Color.Gray)
                    }
                }
            }
            
            // Add to Cart Button
            Button(
                onClick = onAddToCart,
                modifier = Modifier.weight(1f).height(48.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primaryColor)
            ) {
                Icon(Icons.Filled.ShoppingBag, contentDescription = null, modifier = Modifier.size(20.dp))
                Text("Thêm vào giỏ", modifier = Modifier.padding(start = 8.dp), fontWeight = FontWeight.Bold)
            }
        }
    }
}

fun formatPrice(price: Double): String {
    return NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
        .format(price)
        .replace("₫", "đ")
}

fun formatRelativeTime(dateString: String): String {
    // Simple relative time formatting
    return try {
        // Just show the date for now
        dateString.take(10)
    } catch (e: Exception) {
        dateString
    }
}


