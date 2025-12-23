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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProductDetailScreen(
    onBackClick: () -> Unit,
    onCartClick: () -> Unit,
    onProductClick: (String) -> Unit = {},
    viewModel: ProductDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val primaryColor = Color(0xFFec3713)
    val scrollState = rememberScrollState()
    var showWriteReviewDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    val tabs = listOf("Description", "Specifications", "Reviews")

    // Handle review submission success/error
    LaunchedEffect(uiState.reviewSubmitSuccess, uiState.reviewSubmitError) {
        if (uiState.reviewSubmitSuccess) {
            snackbarHostState.showSnackbar("Đánh giá của bạn đã được gửi thành công!")
            viewModel.clearReviewSubmitState()
            showWriteReviewDialog = false
        }
        if (uiState.reviewSubmitError != null) {
            snackbarHostState.showSnackbar(uiState.reviewSubmitError ?: "Có lỗi xảy ra")
            viewModel.clearReviewSubmitState()
        }
    }

    // Write Review Dialog
    if (showWriteReviewDialog) {
        WriteReviewDialog(
            onDismiss = { 
                showWriteReviewDialog = false
                viewModel.clearReviewSubmitState()
            },
            onSubmit = { rating, comment ->
                showWriteReviewDialog = false  // Đóng dialog ngay khi submit
                viewModel.submitReview(rating, comment)
            },
            primaryColor = primaryColor,
            isSubmitting = uiState.isSubmittingReview
        )
    }

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFFF8F6F6))) {
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 100.dp)
        )
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = primaryColor)
                }
            }
            uiState.error != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
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
                            modifier = Modifier.clickable { viewModel.retry() }
                        )
                    }
                }
            }
            uiState.product != null -> {
                val product = uiState.product!!
                
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 80.dp)
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
                        
                        // Description Preview
                        product.description?.let { desc ->
                            if (desc.isNotEmpty()) {
                                Text(
                                    text = desc,
                                    color = Color(0xFF52525B),
                                    fontSize = 14.sp,
                                    lineHeight = 20.sp,
                                    maxLines = 4,
                                    modifier = Modifier.padding(top = 16.dp)
                                )
                            }
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
                                onSortChange = { viewModel.sortReviews(it) },
                                onWriteReviewClick = { showWriteReviewDialog = true }
                            )
                        }
                        
                        // Related Products
                        product.relatedProducts?.let { related ->
                            if (related.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(24.dp))
                                RelatedProductsSection(
                                    relatedProducts = related,
                                    onProductClick = onProductClick
                                )
                            }
                        }
                    }
                }
                
                // Sticky Bottom Bar
                BottomCartBar(
                    price = product.price,
                    quantity = uiState.quantity,
                    onIncrease = { viewModel.increaseQuantity() },
                    onDecrease = { viewModel.decreaseQuantity() },
                    primaryColor = primaryColor,
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }
        }
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.verticalGradient(listOf(Color.Black.copy(alpha = 0.5f), Color.Transparent)))
                .padding(top = 40.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
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
                    onClick = {},
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
    onSortChange: (String) -> Unit = {},
    onWriteReviewClick: () -> Unit = {}
) {
    var showSortMenu by remember { mutableStateOf(false) }
    Column {
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Đánh giá & Xếp hạng", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(
                "Viết đánh giá", 
                color = Color(0xFFec3713), 
                fontSize = 14.sp, 
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable { onWriteReviewClick() }
            )
        }
        
        // Filter Chips
        LazyRow(
            modifier = Modifier.padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                FilterChip(
                    selected = filterByRating == null,
                    onClick = { onFilterChange(null) },
                    label = { Text("Tất cả") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFFec3713),
                        selectedLabelColor = Color.White
                    )
                )
            }
            items(listOf(5, 4, 3, 2, 1)) { rating ->
                FilterChip(
                    selected = filterByRating == rating,
                    onClick = { onFilterChange(rating) },
                    label = { 
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("$rating")
                            Icon(
                                Icons.Filled.Star, 
                                contentDescription = null, 
                                tint = if (filterByRating == rating) Color.White else Color(0xFFEAB308),
                                modifier = Modifier.size(16.dp).padding(start = 2.dp)
                            )
                        }
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFFec3713),
                        selectedLabelColor = Color.White
                    )
                )
            }
        }

        // Sort Dropdown
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Box {
                OutlinedButton(
                    onClick = { showSortMenu = true },
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
                ) {
                    Icon(Icons.Filled.Sort, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        when (sortBy) {
                            "highest" -> "Cao nhất"
                            "lowest" -> "Thấp nhất"
                            else -> "Mới nhất"
                        },
                        fontSize = 14.sp
                    )
                }
                DropdownMenu(
                    expanded = showSortMenu,
                    onDismissRequest = { showSortMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Mới nhất") },
                        onClick = {
                            onSortChange("newest")
                            showSortMenu = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Đánh giá cao nhất") },
                        onClick = {
                            onSortChange("highest")
                            showSortMenu = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Đánh giá thấp nhất") },
                        onClick = {
                            onSortChange("lowest")
                            showSortMenu = false
                        }
                    )
                }
            }
        }

        // Summary Card
        Surface(
            color = Color.White,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
        ) {
            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = String.format("%.1f", averageRating ?: 0.0),
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Row {
                        val rating = (averageRating ?: 0.0).toInt()
                        repeat(rating) { Icon(Icons.Filled.Star, null, tint = Color(0xFFEAB308), modifier = Modifier.size(16.dp)) }
                        if ((averageRating ?: 0.0) - rating >= 0.5) {
                            Icon(Icons.Filled.StarHalf, null, tint = Color(0xFFEAB308), modifier = Modifier.size(16.dp))
                        }
                    }
                    Text("${reviewCount ?: 0} đánh giá", fontSize = 12.sp, color = Color.Gray)
                }
                Spacer(modifier = Modifier.width(24.dp))
                // Rating distribution would go here
                Column(modifier = Modifier.weight(1f)) {
                    RatingBar(5, 0.7f)
                    RatingBar(4, 0.2f)
                    RatingBar(3, 0.05f)
                    RatingBar(2, 0.03f)
                    RatingBar(1, 0.02f)
                }
            }
        }
        
        when {
            isLoading -> {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFFec3713), modifier = Modifier.size(32.dp))
                }
            }
            reviews.isEmpty() -> {
                Text(
                    text = "Chưa có đánh giá nào cho sản phẩm này.",
                    color = Color(0xFF6B7280),
                    fontSize = 14.sp,
                    modifier = Modifier.padding(16.dp)
                )
            }
            else -> {
                reviews.take(3).forEachIndexed { index, review ->
                    ReviewItem(review)
                    if (index < reviews.size - 1 && index < 2) {
                        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = Color(0xFFF1F5F9))
                    }
                }
                
                if (reviews.size > 3) {
                    OutlinedButton(
                        onClick = {},
                        modifier = Modifier.fillMaxWidth().padding(top = 24.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
                    ) {
                        Text("Xem tất cả ${reviews.size} đánh giá", color = Color(0xFF0F172A))
                    }
                }
            }
        }
    }
}

@Composable
fun RatingBar(stars: Int, progress: Float) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 2.dp)) {
        Text(stars.toString(), fontSize = 10.sp, color = Color.Gray, modifier = Modifier.width(12.dp))
        Box(modifier = Modifier.weight(1f).height(6.dp).clip(CircleShape).background(Color(0xFFF1F5F9))) {
            Box(modifier = Modifier.fillMaxWidth(progress).fillMaxHeight().background(Color(0xFFec3713)))
        }
    }
}

@Composable
fun ReviewItem(review: ReviewDto) {
    Column {
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(40.dp).clip(CircleShape).background(Color(0xFFE9D5FF)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = review.userName?.take(2)?.uppercase() ?: "U",
                        color = Color(0xFF7E22CE),
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
                Column(modifier = Modifier.padding(start = 12.dp)) {
                    Text(review.userName ?: "Người dùng", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                    Row {
                        repeat(review.rating) { Icon(Icons.Filled.Star, null, tint = Color(0xFFEAB308), modifier = Modifier.size(12.dp)) }
                    }
                }
            }
            Text(formatRelativeTime(review.createdAt), fontSize = 12.sp, color = Color.Gray)
        }
        review.comment?.let { comment ->
            if (comment.isNotEmpty()) {
                Text(comment, fontSize = 14.sp, color = Color(0xFF475569), modifier = Modifier.padding(top = 8.dp))
            }
        }
        review.imageUrls?.let { imageUrls ->
            if (imageUrls.isNotEmpty()) {
                LazyRow(
                    modifier = Modifier.padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(imageUrls) { imageUrl ->
                        AsyncImage(
                            model = imageUrl,
                            contentDescription = null,
                            modifier = Modifier.size(80.dp).clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }
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
                onClick = { /* TODO: Add to cart */ },
                modifier = Modifier.weight(1f).height(48.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primaryColor)
            ) {
                Icon(Icons.Filled.ShoppingBag, contentDescription = null, modifier = Modifier.size(20.dp))
                Text("Thêm vào giỏ", modifier = Modifier.padding(start = 8.dp), fontWeight = FontWeight.Bold)
                Text("| ${formatPrice(price * quantity)}", modifier = Modifier.padding(start = 8.dp), color = Color.White.copy(alpha = 0.8f))
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

@Composable
fun WriteReviewDialog(
    onDismiss: () -> Unit,
    onSubmit: (Int, String) -> Unit,
    primaryColor: Color,
    isSubmitting: Boolean = false
) {
    var rating by remember { mutableStateOf(5) }
    var comment by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        title = {
            Text(
                "Viết đánh giá",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                // Rating Stars
                Text("Chọn số sao:", fontWeight = FontWeight.Medium)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    (1..5).forEach { star ->
                        IconButton(
                            onClick = { rating = star },
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                imageVector = if (star <= rating) Icons.Filled.Star else Icons.Filled.StarOutline,
                                contentDescription = "Star $star",
                                tint = if (star <= rating) Color(0xFFEAB308) else Color.Gray,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                }
                
                // Comment
                Text("Nhận xét của bạn:", fontWeight = FontWeight.Medium)
                OutlinedTextField(
                    value = comment,
                    onValueChange = { comment = it },
                    placeholder = { Text("Chia sẻ trải nghiệm của bạn với sản phẩm...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = primaryColor,
                        cursorColor = primaryColor
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onSubmit(rating, comment) },
                colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                shape = RoundedCornerShape(8.dp),
                enabled = !isSubmitting
            ) {
                if (isSubmitting) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Gửi đánh giá", fontWeight = FontWeight.Bold)
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Hủy", color = Color.Gray)
            }
        }
    )
}
