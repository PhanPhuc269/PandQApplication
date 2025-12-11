package com.group1.pandqapplication.ui.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cable
import androidx.compose.material.icons.filled.ElectricalServices
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.LaptopMac
import androidx.compose.material.icons.filled.PhoneIphone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.TabletMac
import androidx.compose.material.icons.filled.Watch
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

// Data Models
data class Banner(
    val title: String,
    val subtitle: String,
    val imageUrl: String
)

data class Category(
    val name: String,
    val icon: ImageVector
)

data class Product(
    val name: String,
    val price: String,
    val imageUrl: String
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    onLogout: () -> Unit,
    onProductClick: () -> Unit,
    onSearchClick: () -> Unit
) {
    val primaryColor = Color(0xFFec3713)

    // Mock Data
    val banners = listOf(
        Banner("iPhone 15 Pro Max", "Khám phá sức mạnh titan.", "https://lh3.googleusercontent.com/aida-public/AB6AXuAY8N6iyZ08u0SWafLXu9qKNEsbrwHuGcz3Hja6KeV4O4hutLoqq7UteVA4YOxkLe_mYFe-_E00HU4DJPrFiE_JQ_BMJDDxawi_7x7vdjVxdy4XduOnHDFtlOgJG2seHE0cIm8FCjLBfSWYlvTmBtKsCu4T7l8CbuLwyjBtkYaVYSng6FgEiZzeXuIGE96fkmh1Ph5oeO2Q8rFhO-lUFnREG9qTvBliG1WV8QTVD2kDSE-C9_eKqZEIvIkg8P94lMmWVZczwITcPkU"),
        Banner("Macbook Air M3", "Giảm giá tới 20% cho các dòng máy.", "https://lh3.googleusercontent.com/aida-public/AB6AXuBUBzal0oPpdf4ZTJwUzLahKqyyLsCnbwTNgeDQ0BK8F0W4J8WRYRBoMwxavtIOdE3NTJrJH0ZyXtu5xQJcQl_DYIsj_6JyEiinHwIDd99oFRFXT2l3xLKO-AnEqnSanLm4OCkZTRlOrwAw9-tW28zz9sRdTYLuuzu9ip-kSu7MgR234PwCK5N0NbWJOP88t0xq6V8hXbHmyeVPtyi_L0395naHqu65MkTuduPsoh6ZQDII0rurFvr93phaS-Qp_YFIz-jvJB8-ZBc"),
        Banner("Phụ kiện mới", "Nâng tầm trải nghiệm âm thanh.", "https://lh3.googleusercontent.com/aida-public/AB6AXuAMMPsPxp4PKLsvL6GNj5PL2mRZa2kRI5BbvBKZDOHUERj1NIO8jdNN56RQapffvJ2IIQThtuO7sbaOAD9x3WM0sDM-ktRS4Gcc5DZM9x1AizCwikcyd8jugubd_Hser1-ju7N44LV5Q65MEKMPE9uPIaPvdbaVg0PH6rLmW4A2O7NZiJzQQiSxAKWdlCHC6VuO2kIzgA1KE46ZZ5nWF8LgjwQ79Jy3H22846toBdD655JWRP50eRc0GCyWna-BOFxr60Ghg8fHNBU")
    )

    val categories = listOf(
        Category("Điện thoại", Icons.Filled.PhoneIphone),
        Category("Laptop", Icons.Filled.LaptopMac),
        Category("Âm thanh", Icons.Filled.Headphones),
        Category("Phụ kiện", Icons.Filled.Cable),
        Category("Đồng hồ", Icons.Filled.Watch),
        Category("Tablet", Icons.Filled.TabletMac)
    )

    val products = listOf(
        Product("Galaxy S24 Ultra", "33.990.000₫", "https://lh3.googleusercontent.com/aida-public/AB6AXuCpKnFbz5VpYGTmOwjusnAu0056ay5sGoKcjEFC229fglfeTWtZIvVkhlbA6unqdpQ7-be9tlrCjp1_CUcjkhjm4R6ojs_TR23B0BrrzlG0X-3SBMK23jsHY2MB-IagBOBA5nGv-UcliyyMAgfpjbj3xwPJfGOk7LkX8EzEI3QRfHWCrcc7m0qg9zYLfiHPPQYp9-dahJntr-FZO4R2DbE7bRyqi22YdJWejP-DM-xLUr8uOfqRpz-yUmm0ydggJSWnAcJTZivvs5E"),
        Product("MacBook Pro 14\"", "41.990.000₫", "https://lh3.googleusercontent.com/aida-public/AB6AXuAaNRhE1aViGt5fY_suEac4QHK29hI7xANETi6Louoa5ZpEJicUrkZuOBTWJrBn72b4B3XQqmXxGngO5twHWEQx29xLQil6E4A66As1PbnDRI31h3dNcVQweKwJ0AtAFv-cN0-eecrIEICCfcKnMJuvQYBJxSp4IZ9NBJgdkIuaptOglQiKmSq_Rw0jvXagmBVIFPSTd1Elau5ygvwddWYQQXeskdWlPSgWGXMKfLiNYy4OMa3_tiVWRHDrEi88NuHs2_m7iEnS_4w"),
        Product("AirPods Pro 2", "5.890.000₫", "https://lh3.googleusercontent.com/aida-public/AB6AXuAPwX8_D7wFmh7MqwaSWaQfzTGeCBIFrb17_4PX7C5U_ICxPViLAqdClJsnsXVr0F_0KrJtnb_clXEJ8_H5I_I_e4G7Wi6KP87O7a32LProhznomhxIQ4PxcErTbKlD7-ADxzVcBc36oH0UJAlimuPiWp8ulnEkANS-gIG-sP-JzXrgIRUmQRNc6Qst59DjaGtkXPwuyBnP1IKE8M5AlwHV8LKYmK_313gN3csJpJUjTI1ws-yBSVVB6ftOZA46kvtEZ-diFpoEWqQ"),
        Product("Apple Watch Ultra", "19.990.000₫", "https://lh3.googleusercontent.com/aida-public/AB6AXuCAgMaKRj-AcodEzbEuNQ_HRaAehkbF0fy8Z4GRr8Sw9Fn0El0arB02xvKkDm3hzl-8V0uwOa27i2q7YyNBpicU2BCwajxAuEty9g2A73fi0AgTZBgbZ5rPqyN4wwtsajhrNZzshjOmknUPqJ896c0_9gCbMK9xm_3xhSgKbFxK4pfGwyLuZtJTsBaz2btg59ZXVjdYjRq9PwnFetpxmRBudxdJjQBg26BJ17MecoxtpA1puUDUIfFoxb7rfrIPWZUbt2Ij7Ou4Pqs")
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
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
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

                    // Categories
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(categories) { category ->
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier.width(72.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(56.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color(0xFFE5E7EB)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = category.icon,
                                        contentDescription = null,
                                        tint = Color(0xFF4B5563)
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = category.name,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFF1F2937),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))

                    // Section Title
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Sản phẩm nổi bật",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF111827)
                        )
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

            // Product Grid
            items(products) { product ->
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                            .clickable { onProductClick() },
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE5E7EB))
                    ) {
                        AsyncImage(
                            model = product.imageUrl,
                            contentDescription = null,
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
                        text = product.price,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF111827)
                    )
                }
            }
            
            // Bottom Padding
            item(span = { GridItemSpan(2) }) {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(onLogout = {}, onProductClick = {}, onSearchClick = {})
}
