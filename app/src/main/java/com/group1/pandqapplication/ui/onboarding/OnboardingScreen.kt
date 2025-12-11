package com.group1.pandqapplication.ui.onboarding

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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import kotlinx.coroutines.launch

data class OnboardingPage(
    val title: String,
    val description: String,
    val imageUrl: String
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(
    onFinish: () -> Unit
) {
    val primaryColor = Color(0xFFec3713)
    val pages = listOf(
        OnboardingPage(
            title = "Chào mừng đến với TechShop!",
            description = "Khám phá hàng ngàn sản phẩm điện tử mới nhất ngay trong tầm tay bạn.",
            imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuBMCGdV_NsqxlXWNhWjWxYffYNuLUl4GHiB6W2p1xDI9Pr9bxgxOSdvUYKSa4Y7s3_dPqOtcvnScYiy9njJpZOQhE_PebmoMG8gUNzmjhWl6uEHqLOMoaBA8qwrw3IXmaYPjJ2SMyiwhwPnRDmXLFuGetVIxTwfmVjDBcu84hps2c9_j0T9JsdOhDQTz4GQq0bmBXhfyqncWdZ7-Q_a0F6Q1qAhAnsCRwz7LidGPGayCSdTBnJ4kXZTgkGbUJPmyxBVtlDf_YBW-IE"
        ),
        OnboardingPage(
            title = "Mua sắm dễ dàng",
            description = "Trải nghiệm mua sắm mượt mà, thanh toán an toàn và giao hàng nhanh chóng.",
            imageUrl = "https://images.unsplash.com/photo-1542370285-d83025c556f2?q=80&w=2680&auto=format&fit=crop" // Placeholder
        ),
        OnboardingPage(
            title = "Hỗ trợ 24/7",
            description = "Chúng tôi luôn sẵn sàng hỗ trợ bạn mọi lúc, mọi nơi.",
            imageUrl = "https://images.unsplash.com/photo-1516321318423-f06f85e504b3?q=80&w=2670&auto=format&fit=crop" // Placeholder
        )
    )

    val pagerState = rememberPagerState(pageCount = { pages.size })
    val scope = rememberCoroutineScope()

    Scaffold(
        containerColor = Color.White // or match theme
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Skip Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onFinish) {
                    Text(
                        text = "Bỏ qua",
                        color = primaryColor,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Pager Content
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) { pageIndex ->
                val page = pages[pageIndex]
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Image
                    AsyncImage(
                        model = page.imageUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(320.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.Transparent),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Headline
                    Text(
                        text = page.title,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1F2937),
                        textAlign = TextAlign.Center,
                        lineHeight = 32.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    // Body
                    Text(
                        text = page.description,
                        fontSize = 16.sp,
                        color = Color(0xFF4B5563),
                        textAlign = TextAlign.Center,
                        lineHeight = 24.sp
                    )
                }
            }

            // Bottom Controls
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Indicators
                Row(
                    modifier = Modifier.padding(bottom = 20.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    repeat(pages.size) { iteration ->
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

                // Button
                Button(
                    onClick = {
                        scope.launch {
                            if (pagerState.currentPage < pages.size - 1) {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            } else {
                                onFinish()
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    colors = ButtonDefaults.buttonColors(containerColor = primaryColor)
                ) {
                    Text(
                        text = if (pagerState.currentPage == pages.size - 1) "Bắt đầu" else "Tiếp tục",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OnboardingScreenPreview() {
    OnboardingScreen(onFinish = {})
}
