package com.group1.pandqapplication.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.border
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

data class SearchProduct(
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
    onBackClick: () -> Unit
) {
    val backgroundColor = Color(0xFFF8F6F6)
    val primaryColor = Color(0xFFec3713)

    var searchQuery by remember { mutableStateOf("Samsung Smartphones") }
    var showFilterSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    if (showFilterSheet) {
        ModalBottomSheet(
            onDismissRequest = { showFilterSheet = false },
            sheetState = sheetState,
            containerColor = Color.White,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
        ) {
            FilterBottomSheetContent(
                onApply = { showFilterSheet = false },
                onReset = { },
                primaryColor = primaryColor
            )
        }
    }

    // Mock Data from HTML
    val products = listOf(
        SearchProduct("Samsung Galaxy S23 5G 256GB", "$499.00", 4.8, "(1.2k)", "https://lh3.googleusercontent.com/aida-public/AB6AXuCcKfPXdSVjq5gr5v4FkVRNbcGybnRuGWRngLqO2UBvlDsw6noFYd7zZlrWKUpecnmudm-YmRiZp0og8xu_f2JnFiukXXJaUhmn58WEBy2SsklOs6Uehf6CrfJxsRdBFbNmBNy4TwO1n4pBiELqNbj1t-gUozWP-b0vXGQZrnn_NzsjIziSAzeg-05t9jfsVpZX2C9iQ3A_Pz-hEafFiEUAClbXIGzbA7TxCsjDpk0Aw9EOaIpnY02KibkUra4mgfoXeRPTZVIY6xY", isBestSeller = true),
        SearchProduct("Samsung Galaxy A54 Enterprise Edition", "$349.99", 4.5, "(850)", "https://lh3.googleusercontent.com/aida-public/AB6AXuArjXPzTrCZcNaOX7SQwfkw86OIgQkOUOfRmoMczmWwlCgx-cKZm2u37FqtP8jX1QWE1QJn11wOr3zcKYJj8ZIx4eVX_rOf1BFDGUpgTCnSOkiy5EUXG3G75ISUedGoOl8eTfDjlLHfdwWe92H0dg0f9RCrz94WNyNMM8-yhaPgMXgh-6C_lHKgXRt9YMCsoR-CC5htXaq0nKpk2J4xg_dVtEow0KjgUlXeVvu02_rUbo4IDRQkMGY50ziqwolr4wIFL7-eE8FQKqU"),
        SearchProduct("Galaxy Buds2 Pro Noise Cancelling", "$120.00", 4.9, "(2.3k)", "https://lh3.googleusercontent.com/aida-public/AB6AXuAhlyCg3rf5niMXg254zrkfoK7aYqXpoFRSoLvOlEs2v2nn5nm7t4FpCarU3MtAzP-2H0af5nC3gC4cEB75GOG8oJk_RLYEvqiRx9V4uJRZ97FoU_fs03VLNtOKguWTt8mtaRe6lP8yCCDW0EgCORNElP5KTpwYWnaoTDGkaiBxFpc8UOokDShOpupqxbabzwnkxZkRVkmvVsSqRW9oERrBmI5oaIy5vWGOnQjOjUHbuWdhWwqvHaqF2d5uMWcTU4WhNNv9pKBaCZ4", isLowStock = true),
        SearchProduct("Samsung Galaxy Watch 6 Classic", "$299.00", 4.7, "(500+)", "https://lh3.googleusercontent.com/aida-public/AB6AXuA_OLU1m8XylSWna5qOhR-VWhLUGFtj0EwArwhzT7haIiaCKfFZa0ADvIpXEl1K9exgSE1mtcEcIKksrwvExarbimLSNUzH-whkUSOjPueLUC3l6p3FYIiuhZOjEc5i6R9H7abJfmpRa74Tx6s3mcapPdjAIUfZvFclJycUhrjlAgzEXAIOqnnipP0O0sNXBDq6pO8eZEPjD55NGP2g1GAjUr_GvVkNo7euye334LbP-x3hmtp_5WTLRdWLK5MIjktg2FIIYx11keg"),
        SearchProduct("Samsung Galaxy Z Flip5", "$999.00", 4.6, "(320)", "https://lh3.googleusercontent.com/aida-public/AB6AXuCkY-9if7PgQKlnaQpaVnUK8nPfUTj8qB-m8-dlyTKZUu3gvcifrY8nv1HS3bVrcSt22Q-MxkAjFFLuMI_fx9sJBUM0IgvR4Hlrqh3XamphbWW2f48lYX8PMyKBNhUYTm-AdBQjX6bq1uA12nOAeeIDtEZf-20ZQjZHDl4cY15CqASMRzT9diYIA3OLrGM1a8aJjx_B2SNeDUIqO4XtZes2DVrGrL0zw2PldxtcMTOvlA3txbvR5M_W-4mDDYkjwOTZwZxlpFMhkp8"),
        SearchProduct("Samsung Galaxy S23 Ultra", "$1,199.00", 4.9, "(4.1k)", "https://lh3.googleusercontent.com/aida-public/AB6AXuD4fgjCSBeOMmXAiLxspD5tyMO9K1vIwrSVN344lqgz70fwjskqFIg9vMVCwHBby28Zn1oWiv2nGvHXhpFZ2mo4p1l-miuVuUYoj6gHE-Azkc1Lprq-S2_jdhWExQ1gJ0sMRQyrBsmx-N5waZR_UE9363U1TFnAeg929-G7u3kd6Luy5nLJh-5iI4uX-TSg6Tq40N0Ersxeew02RQ7OwLcXMSXLa5DcxMbrhYMpGhlXi682cLLBEVc4JDijXemYW4I90Vkpd5nLgIw")
    )

    val filters = listOf("Samsung", "Under $500", "4 Stars+")

    Scaffold(
        containerColor = backgroundColor,
        topBar = {
            Column(modifier = Modifier.background(backgroundColor)) {
                // Top Navigation / Search Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 48.dp, bottom = 8.dp, start = 16.dp, end = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Back Button
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.Transparent)
                    ) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color(0xFF374151))
                    }

                    // Search Bar
                    Box(modifier = Modifier.weight(1f)) {
                        TextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text(
                                "Tìm theo tên sản phẩm...",
                                color = Color.Gray,
                                fontSize = 13.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )  },
                            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null, tint = Color(0xFF9CA3AF)) },
                            trailingIcon = { 
                                IconButton(onClick = { searchQuery = "" }) {
                                    Icon(Icons.Filled.Cancel, contentDescription = "Clear", tint = Color(0xFF9CA3AF)) 
                                }
                            },
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

                    // Filter Button
                    Box(modifier = Modifier.size(44.dp)) {
                         IconButton(
                            onClick = { showFilterSheet = true },
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(12.dp))
                                .background(primaryColor)
                        ) {
                            Icon(Icons.Filled.Tune, contentDescription = "Filter", tint = Color.White)
                        }
                        // Badge
                        Box(modifier = Modifier
                            .align(Alignment.TopEnd)
                            .offset(x = 4.dp, y = (-4).dp)
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .padding(2.dp)) {
                                Box(modifier = Modifier.fillMaxSize().clip(CircleShape).background(primaryColor), contentAlignment = Alignment.Center) {
                                     Text("3", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                        }
                    }
                }

                // Filters and Sort Bar
                Column(modifier = Modifier.padding(bottom = 8.dp)) {
                    // Chips
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(vertical = 8.dp)
                    ) {
                        item {
                            Text(
                                "Clear All", 
                                fontSize = 12.sp, 
                                fontWeight = FontWeight.SemiBold, 
                                color = Color(0xFF6B7280),
                                modifier = Modifier.padding(end = 4.dp).clickable { }
                            )
                        }
                        items(filters.size) { index ->
                            Surface(
                                color = primaryColor.copy(alpha = 0.1f),
                                shape = CircleShape,
                                border = androidx.compose.foundation.BorderStroke(1.dp, primaryColor.copy(alpha = 0.2f))
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(start = 12.dp, end = 8.dp, top = 6.dp, bottom = 6.dp)
                                ) {
                                    Text(filters[index], fontSize = 12.sp, fontWeight = FontWeight.Medium, color = primaryColor)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Icon(
                                        Icons.Filled.Close, 
                                        contentDescription = null, 
                                        tint = primaryColor,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            }
                        }
                    }

                    // Results Count & Sort
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "24 results found",
                            color = Color(0xFF6B7280),
                            fontSize = 14.sp
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Sort", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFF4B5563))
                            Icon(Icons.Outlined.ExpandMore, contentDescription = null, tint = Color(0xFF4B5563))
                            Spacer(modifier = Modifier.width(12.dp))
                            Divider(
                                color = Color(0xFFD1D5DB),
                                modifier = Modifier.height(16.dp).width(1.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Icon(Icons.Filled.GridView, contentDescription = null, tint = Color(0xFF4B5563))
                        }
                    }
                }
            }
        }

    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(products) { product ->
                ProductGridItem(product = product, primaryColor = primaryColor)
            }
            item(span = { GridItemSpan(2) }) {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp), 
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "END OF RESULTS", 
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

@Composable
fun ProductGridItem(product: SearchProduct, primaryColor: Color) {
    Column {
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
            // Heart Icon
            IconButton(
                onClick = {},
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.2f))
            ) {
                 Icon(Icons.Filled.Favorite, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
            }

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

        // Rating
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
            Icon(Icons.Filled.Star, contentDescription = null, tint = Color(0xFFFACC15), modifier = Modifier.size(16.dp))
            Text(product.rating.toString(), fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color(0xFF4B5563), modifier = Modifier.padding(horizontal = 4.dp))
            Text(product.reviews, fontSize = 12.sp, color = Color(0xFF9CA3AF))
        }

        // Price and Add Button
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = product.price, 
                fontSize = 16.sp, 
                fontWeight = FontWeight.Bold, 
                color = primaryColor
            )
            IconButton(
                onClick = {},
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFF3F4F6))
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add", tint = Color(0xFF111827), modifier = Modifier.size(18.dp))
            }
        }
    }
}

@Preview
@Composable
fun SearchScreenPreview() {
    SearchScreen(onBackClick = {})
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBottomSheetContent(
    onApply: () -> Unit,
    onReset: () -> Unit,
    primaryColor: Color
) {
    var priceRange by remember { mutableStateOf(0f..5000f) }
    var selectedBrands by remember { mutableStateOf(setOf("Apple")) }
    var selectedRatingOption by remember { mutableIntStateOf(1) } // 1: 4.0+, 2: 3.0+, 3: Any
    var inStock by remember { mutableStateOf(true) }

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
            Text("Filters", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF111827))
            Text(
                "Reset",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = primaryColor,
                modifier = Modifier.clickable {
                    priceRange = 0f..5000f
                    selectedBrands = emptySet()
                    selectedRatingOption = 3
                    inStock = false
                    onReset()
                }
            )
        }

        // Price Range
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Price Range", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFF374151))
            Text("$${priceRange.start.toInt()} - $${priceRange.endInclusive.toInt()}", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = primaryColor)
        }

        // Range Slider Customization to look like the design
        // Since standard RangeSlider thumb is simple, we use it as is but with colors
        RangeSlider(
            value = priceRange,
            onValueChange = { priceRange = it },
            valueRange = 0f..5000f,
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
                Text("$ ${priceRange.start.toInt()}", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF111827))
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
                Text("$ ${priceRange.endInclusive.toInt()}", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF111827))
            }
        }

        // Brands
        Text("Brand", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFF374151), modifier = Modifier.padding(top = 16.dp, bottom = 12.dp))
        val brands = listOf("Apple", "Samsung", "Sony", "LG")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            brands.forEach { brand ->
                val isSelected = selectedBrands.contains(brand)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .border(1.dp, if (isSelected) primaryColor else Color(0xFFE5E7EB), RoundedCornerShape(8.dp))
                        .background(if (isSelected) primaryColor.copy(alpha = 0.1f) else Color.Transparent)
                        .clickable {
                            if (isSelected) selectedBrands = selectedBrands - brand
                            else selectedBrands = selectedBrands + brand
                        }
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(brand, fontSize = 14.sp, color = if (isSelected) primaryColor else Color(0xFF4B5563))
                }
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
                        .clickable { selectedRatingOption = id }
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
                onCheckedChange = { inStock = it },
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
            Text("Apply Filters (124)", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}
