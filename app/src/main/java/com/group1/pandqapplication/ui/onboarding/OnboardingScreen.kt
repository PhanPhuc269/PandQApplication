package com.group1.pandqapplication.ui.onboarding

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import kotlinx.coroutines.launch

data class OnboardingPage(
    val title: String,
    val description: String,
    val imageUrl: String,
    val steps: List<String> = emptyList()
)

@Composable
fun GuidePager(
    onFinish: () -> Unit,
    isReviewMode: Boolean = false
) {
    val primaryColor = Color(0xFFec3713)
    val pages = listOf(
        OnboardingPage(
            title = androidx.compose.ui.res.stringResource(com.group1.pandqapplication.R.string.slide_1_title),
            description = androidx.compose.ui.res.stringResource(com.group1.pandqapplication.R.string.slide_1_desc),
            steps = listOf(
                androidx.compose.ui.res.stringResource(com.group1.pandqapplication.R.string.slide_1_step_1),
                androidx.compose.ui.res.stringResource(com.group1.pandqapplication.R.string.slide_1_step_2),
                androidx.compose.ui.res.stringResource(com.group1.pandqapplication.R.string.slide_1_step_3)
            ),
            imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuBMCGdV_NsqxlXWNhWjWxYffYNuLUl4GHiB6W2p1xDI9Pr9bxgxOSdvUYKSa4Y7s3_dPqOtcvnScYiy9njJpZOQhE_PebmoMG8gUNzmjhWl6uEHqLOMoaBA8qwrw3IXmaYPjJ2SMyiwhwPnRDmXLFuGetVIxTwfmVjDBcu84hps2c9_j0T9JsdOhDQTz4GQq0bmBXhfyqncWdZ7-Q_a0F6Q1qAhAnsCRwz7LidGPGayCSdTBnJ4kXZTgkGbUJPmyxBVtlDf_YBW-IE"
        ),
        OnboardingPage(
            title = androidx.compose.ui.res.stringResource(com.group1.pandqapplication.R.string.slide_2_title),
            description = androidx.compose.ui.res.stringResource(com.group1.pandqapplication.R.string.slide_2_desc),
            steps = listOf(
                androidx.compose.ui.res.stringResource(com.group1.pandqapplication.R.string.slide_2_step_1),
                androidx.compose.ui.res.stringResource(com.group1.pandqapplication.R.string.slide_2_step_2),
                androidx.compose.ui.res.stringResource(com.group1.pandqapplication.R.string.slide_2_step_3),
                androidx.compose.ui.res.stringResource(com.group1.pandqapplication.R.string.slide_2_step_4)
            ),
            imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuAOGmT6XSrSXQi-5x0NluBBrZBvph3oaww0EpTDVYR2ybFzokcIuW-iL_8oFMtoLEk1rksmdQbXRZljJQqlj3hAQ5FWUu_Q6IRDfMgo19CDOnGc8GatoDavcm_8_i1O46JWOl_CyFzrwu76JB9NP-aFbyfHg4883Wr4ocDK9KzDSad5qJRPQ2sSkKFKD38qIgyGPs-L2Mjj1Y1TU7TOtyEe-64cpGRPwkIxv-Rx4MKUgl0fWUCf5RzWWUpvMYfKKDAyEU2sf5ZSrMY"
        ),
        OnboardingPage(
            title = androidx.compose.ui.res.stringResource(com.group1.pandqapplication.R.string.slide_3_title),
            description = androidx.compose.ui.res.stringResource(com.group1.pandqapplication.R.string.slide_3_desc),
            steps = listOf(
                androidx.compose.ui.res.stringResource(com.group1.pandqapplication.R.string.slide_3_step_1),
                androidx.compose.ui.res.stringResource(com.group1.pandqapplication.R.string.slide_3_step_2),
                androidx.compose.ui.res.stringResource(com.group1.pandqapplication.R.string.slide_3_step_3),
                androidx.compose.ui.res.stringResource(com.group1.pandqapplication.R.string.slide_3_step_4)
            ),
            imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuDUoXuiDDcy7hwiRsdmKgvX2_GUk_FoY9JsT0i6FuWC99WVFbReei0xHZGyk2q-9eKR8R8rbWUiGLlS37z02whcsqdrrgWeQpeIfvXucdp0x3d3f0QtH0fZ7ql3TVfm41qN9wyzddSmk7QXFLHY5SYaaFf6LNCB80YxTOG27aETXHm4lNW-CPBADlo_G1bjKg1HicD7EAZzVjtAaHU1gYi-IYT6tPzRKidd58uE-B5dgOpit76lHpB4uT46FX5oWMiZEm42Y4Z1KKU"
        ),
         OnboardingPage(
            title = androidx.compose.ui.res.stringResource(com.group1.pandqapplication.R.string.slide_4_title),
            description = androidx.compose.ui.res.stringResource(com.group1.pandqapplication.R.string.slide_4_desc),
            steps = listOf(
                androidx.compose.ui.res.stringResource(com.group1.pandqapplication.R.string.slide_4_step_1),
                androidx.compose.ui.res.stringResource(com.group1.pandqapplication.R.string.slide_4_step_2),
                androidx.compose.ui.res.stringResource(com.group1.pandqapplication.R.string.slide_4_step_3),
                androidx.compose.ui.res.stringResource(com.group1.pandqapplication.R.string.slide_4_step_4)
            ),
            imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuCzAw-DAkrH7q0Hc833qfYAdjt6Mo5Ak4uhhqWIz-24wjdhFiluPKilyig1WEV0WSqAtF_i0u82_3WfQjbCSLrxyEIWyJrrTCvWyutpMA1l9Nc4E4ABSy6Tl7TtWsKRlRXkVv2hO8jqp4nMERrBEZASDntKkTO7PRA-Gsi-uRv0wRm4nCge7w0YnSu77w6bM74thVAXInp4it7LyohfoJj4Kqfg3aM0IlR_FInavgPnStyOS9t7E5oWNwQNVOZaHpRSY58_4RmhIQQ"
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
                if (!isReviewMode) {
                    TextButton(onClick = onFinish) {
                        Text(
                            text = androidx.compose.ui.res.stringResource(com.group1.pandqapplication.R.string.guide_skip),
                            color = primaryColor,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else {
                    // Placeholder for spacing or a Close button if desired at top
                     TextButton(onClick = onFinish) {
                        Text(
                            text = androidx.compose.ui.res.stringResource(com.group1.pandqapplication.R.string.guide_close),
                            color = Color.Gray,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
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
                    verticalArrangement = Arrangement.Top // Push content to top
                ) {
                    // Image
                    AsyncImage(
                        model = page.imageUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(280.dp) // Slightly smaller to fit steps
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.Transparent),
                        contentScale = ContentScale.Fit
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Headline
                    Text(
                        text = page.title,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1F2937),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Description
                    Text(
                        text = page.description,
                        fontSize = 16.sp,
                        color = Color(0xFF4B5563),
                        textAlign = TextAlign.Center
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))

                    // Steps
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp), // Add horizontal padding
                        verticalArrangement = Arrangement.spacedBy(16.dp) // Increase spacing
                    ) {
                        page.steps.forEachIndexed { index, step ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.Top // Align top for multi-line text
                            ) {
                                // Styled Step Badge
                                Box(
                                    modifier = Modifier
                                        .size(24.dp) // Reduced size as requested
                                        .background(primaryColor, CircleShape), // Solid Primary Background
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "${index + 1}",
                                        color = Color.White, // White Text
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                
                                Spacer(modifier = Modifier.width(16.dp))
                                
                                // Step Text
                                Text(
                                    text = step.replaceFirst(Regex("^\\d+\\.\\s*"), ""), 
                                    fontSize = 16.sp,
                                    color = Color(0xFF374151),
                                    lineHeight = 24.sp,
                                    modifier = Modifier
                                        .align(Alignment.CenterVertically)
                                        .weight(1f) // Ensure text wraps correctly within available space
                                )
                            }
                        }
                    }
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
                    val buttonText = if (pagerState.currentPage == pages.size - 1) {
                        if (isReviewMode) androidx.compose.ui.res.stringResource(com.group1.pandqapplication.R.string.guide_close) 
                        else androidx.compose.ui.res.stringResource(com.group1.pandqapplication.R.string.guide_start)
                    } else {
                        androidx.compose.ui.res.stringResource(com.group1.pandqapplication.R.string.guide_next)
                    }
                    Text(
                        text = buttonText,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(
    onFinish: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    GuidePager(
        onFinish = {
            viewModel.completeOnboarding { onFinish() }
        },
        isReviewMode = false
    )
}


@Preview(showBackground = true)
@Composable
fun OnboardingScreenPreview() {
    OnboardingScreen(onFinish = {})
}
