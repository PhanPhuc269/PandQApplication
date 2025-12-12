package com.group1.pandqapplication.ui.guide

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.group1.pandqapplication.ui.theme.CheckoutBackgroundDark
import com.group1.pandqapplication.ui.theme.CheckoutBackgroundLight
import com.group1.pandqapplication.ui.theme.GuideTextPrimaryDark
import com.group1.pandqapplication.ui.theme.GuideTextPrimaryLight
import com.group1.pandqapplication.ui.theme.GuideTextSecondaryDark
import com.group1.pandqapplication.ui.theme.GuideTextSecondaryLight
import com.group1.pandqapplication.ui.theme.ProductPrimary

@Composable
fun UserGuideScreen(
    onBackClick: () -> Unit = {}
) {
    val isDarkTheme = false
    
    val backgroundColor = if (isDarkTheme) CheckoutBackgroundDark else CheckoutBackgroundLight
    val cardColor = if (isDarkTheme) Color(0xFF27272A) else Color.White // zinc-800/20 approx or white
    val textPrimary = if (isDarkTheme) GuideTextPrimaryDark else GuideTextPrimaryLight
    val textSecondary = if (isDarkTheme) GuideTextSecondaryDark else GuideTextSecondaryLight
    val borderColor = if (isDarkTheme) Color(0xFF374151) else Color(0xFFE5E7EB)

    Scaffold(
        containerColor = backgroundColor,
        topBar = {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(backgroundColor)
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier.align(Alignment.CenterStart)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = textPrimary
                        )
                    }
                    Text(
                        text = "Hướng dẫn sử dụng",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = textPrimary,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                HorizontalDivider(color = borderColor)
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(16.dp)
        ) {
            item {
                GuideCard(
                    title = "Tìm kiếm & Lọc sản phẩm",
                    description = "Sử dụng thanh tìm kiếm để tìm sản phẩm và áp dụng các bộ lọc như thương hiệu, giá cả hoặc danh mục.",
                    imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuAOGmT6XSrSXQi-5x0NluBBrZBvph3oaww0EpTDVYR2ybFzokcIuW-iL_8oFMtoLEk1rksmdQbXRZljJQqlj3hAQ5FWUu_Q6IRDfMgo19CDOnGc8GatoDavcm_8_i1O46JWOl_CyFzrwu76JB9NP-aFbyfHg4883Wr4ocDK9KzDSad5qJRPQ2sSkKFKD38qIgyGPs-L2Mjj1Y1TU7TOtyEe-64cpGRPwkIxv-Rx4MKUgl0fWUCf5RzWWUpvMYfKKDAyEU2sf5ZSrMY",
                    cardColor = cardColor,
                    textPrimary = textPrimary,
                    textSecondary = textSecondary
                )
            }
            item {
                GuideCard(
                    title = "Xem chi tiết sản phẩm",
                    description = "Vuốt qua hình ảnh, đọc thông số kỹ thuật và kiểm tra đánh giá của người dùng để hiểu rõ về sản phẩm.",
                    imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuCmmjUFjsUfzG-40XSHhV6ouiU-WJcQWsk_FgLNErmBCvq1h6J37kDECm9RE3ErwWMDKNiujeM6TB-Q2ZQ3Uy9p5y8hI_VW-HafdZwBaZlHtm0W8tXx89YK2vqy3wlIz286b5ja8HFwRI8kQpEAX0zM2PI-1A_uKWOm2f8kvnp623DPAyRWC9s4loXonkwgcfGryBjBmfDdUvQZwQnJeab4sMBSIVblU6DXWA-ktS3aXF1d2dkte6aYDkncOo6Dfy6Dy-ldZmF3ciA",
                    cardColor = cardColor,
                    textPrimary = textPrimary,
                    textSecondary = textSecondary
                )
            }
            item {
                GuideCard(
                    title = "Thêm vào giỏ hàng & Mua hàng",
                    description = "Thêm các mặt hàng vào giỏ hàng của bạn và tiến hành quy trình thanh toán một cách dễ dàng.",
                    imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuDUoXuiDDcy7hwiRsdmKgvX2_GUk_FoY9JsT0i6FuWC99WVFbReei0xHZGyk2q-9eKR8R8rbWUiGLlS37z02whcsqdrrgWeQpeIfvXucdp0x3d3f0QtH0fZ7ql3TVfm41qN9wyzddSmk7QXFLHY5SYaaFf6LNCB80YxTOG27aETXHm4lNW-CPBADlo_G1bjKg1HicD7EAZzVjtAaHU1gYi-IYT6tPzRKidd58uE-B5dgOpit76lHpB4uT46FX5oWMiZEm42Y4Z1KKU",
                    cardColor = cardColor,
                    textPrimary = textPrimary,
                    textSecondary = textSecondary
                )
            }
            item {
                GuideCard(
                    title = "Theo dõi đơn hàng",
                    description = "Tìm lịch sử đặt hàng của bạn và kiểm tra trạng thái hiện tại của việc giao hàng trong phần đơn hàng của tôi.",
                    imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuCzAw-DAkrH7q0Hc833qfYAdjt6Mo5Ak4uhhqWIz-24wjdhFiluPKilyig1WEV0WSqAtF_i0u82_3WfQjbCSLrxyEIWyJrrTCvWyutpMA1l9Nc4E4ABSy6Tl7TtWsKRlRXkVv2hO8jqp4nMERrBEZASDntKkTO7PRA-Gsi-uRv0wRm4nCge7w0YnSu77w6bM74thVAXInp4it7LyohfoJj4Kqfg3aM0IlR_FInavgPnStyOS9t7E5oWNwQNVOZaHpRSY58_4RmhIQQ",
                    cardColor = cardColor,
                    textPrimary = textPrimary,
                    textSecondary = textSecondary
                )
            }
            item {
                GuideCard(
                    title = "Quản lý tài khoản",
                    description = "Dễ dàng cập nhật thông tin cá nhân, địa chỉ, phương thức thanh toán và xem danh sách yêu thích của bạn.",
                    imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuCopfbNUQ_MM_o3zs6X0aPsufd-zUtYtnrcDMppkWszua5vofsjpA0itgufLybvAnS0xgORmg6lJ-MnnGmt1HrJzVxSYgFgsdVdGeBXF_KYPt25TEohhvhYPVxSBMPOdyNc_wQOO9cW86pTLREREoRqui5xojUIuho2WIY9QfoZPcH4Dq895LpqOEusvnbTj2zRVg-gxnU_hCuQYXL0-1VUefqg7rg_OmSzRZJeAWOSnPKEUvTILMWM6PAXMSZ_W9p-vCO5z1pRPPA",
                    cardColor = cardColor,
                    textPrimary = textPrimary,
                    textSecondary = textSecondary
                )
            }
            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
fun GuideCard(
    title: String,
    description: String,
    imageUrl: String,
    cardColor: Color,
    textPrimary: Color,
    textSecondary: Color
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(cardColor)
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 10f)
                .background(Color.Gray.copy(alpha = 0.1f)),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = textPrimary)
            Spacer(modifier = Modifier.height(8.dp))
            Text(description, fontSize = 16.sp, color = textSecondary)
            Spacer(modifier = Modifier.height(16.dp))
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                Button(
                    onClick = { /* Find out more */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ProductPrimary,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.height(40.dp)
                ) {
                    Text("Tìm hiểu thêm", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewUserGuideScreen() {
    UserGuideScreen()
}
