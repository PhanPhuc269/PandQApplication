package com.group1.pandqapplication.ui.review

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarHalf
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Divider
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
import com.group1.pandqapplication.shared.ui.theme.CheckoutBackgroundDark
import com.group1.pandqapplication.shared.ui.theme.CheckoutBackgroundLight
import com.group1.pandqapplication.shared.ui.theme.ProductPrimary
import com.group1.pandqapplication.shared.ui.theme.ReviewSurfaceDark
import com.group1.pandqapplication.shared.ui.theme.ReviewSurfaceLight
import com.group1.pandqapplication.shared.ui.theme.ReviewTextPrimaryDark
import com.group1.pandqapplication.shared.ui.theme.ReviewTextPrimaryLight
import com.group1.pandqapplication.shared.ui.theme.ReviewTextSecondaryDark
import com.group1.pandqapplication.shared.ui.theme.ReviewTextSecondaryLight

@Composable
fun ReviewDetailsScreen(
    onBackClick: () -> Unit = {}
) {
    val isDarkTheme = false
    
    val backgroundColor = if (isDarkTheme) CheckoutBackgroundDark else CheckoutBackgroundLight
    val textPrimary = if (isDarkTheme) ReviewTextPrimaryDark else ReviewTextPrimaryLight
    val textSecondary = if (isDarkTheme) ReviewTextSecondaryDark else ReviewTextSecondaryLight
    val surfaceColor = if (isDarkTheme) ReviewSurfaceDark else ReviewSurfaceLight
    val borderColor = if (isDarkTheme) Color(0xFF374151) else Color(0xFFE5E7EB)

    Scaffold(
        containerColor = backgroundColor,
        topBar = {
            Column(modifier = Modifier.background(backgroundColor)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
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
                        text = "Chi tiết Đánh giá",
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
        ) {
            // Rating Summary
            item {
                RatingSummary(textPrimary, textSecondary, surfaceColor)
                HorizontalDivider(color = borderColor, modifier = Modifier.padding(horizontal = 16.dp))
            }
            
            // Filter Chips
            item {
                LazyRow(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item { FilterChip("Tất cả", true, textPrimary, surfaceColor) }
                    item { FilterChip("5 sao", false, textPrimary, surfaceColor) }
                    item { FilterChip("4 sao", false, textPrimary, surfaceColor) }
                    item { FilterChip("3 sao", false, textPrimary, surfaceColor) }
                    item { FilterChip("2 sao", false, textPrimary, surfaceColor) }
                    item { FilterChip("1 sao", false, textPrimary, surfaceColor) }
                }
            }
            
            // Sort
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(surfaceColor),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.SwapVert, contentDescription = null, tint = textPrimary)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Sắp xếp theo: Hữu ích nhất", fontSize = 16.sp, color = textPrimary)
                    }
                    Icon(Icons.Default.ExpandMore, contentDescription = null, tint = textPrimary)
                }
            }
            
            // Reviews
            item {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(24.dp)) {
                    ReviewItem(
                        name = "John Doe",
                        time = "2 days ago",
                        rating = 5,
                        content = "This is the best product I've ever used. The build quality is amazing and it exceeded all my expectations. I would highly recommend this to anyone looking for a premium experience.",
                        imageUrls = listOf(
                            "https://lh3.googleusercontent.com/aida-public/AB6AXuBjp2N_4F6dwo7qbtqkD0NU-1dWeCwI4B3_gqNlAwEnqclHmANUwkXf87ZjD9QPb1EMNt5N3hx30kZgq6D2p0Tx61tvHk3PUUP0SldX0MdWrEn1optnFo1JgUZ212yqlpijcdGAMTqeL3QoqlfrNEbJDrH70HHoq9BXvgUBqFWRYf7QaOjUHKmu1QcagLgP1mRtCjnFrgSOzPUokfN6tNA4qzTZtix9PXFCl4IXS2dtocJoHCWWh9yeAxc0RNjubgsPScEkkYe2xB4",
                            "https://lh3.googleusercontent.com/aida-public/AB6AXuDtg7bu-4dlOnAvhzQEJJfqDtZE-VSIJMjxDngxaSDJJFItr1dT8sAchmTjgYzlU3RkcLWXW6zeAmTshPuOP3jNWsQYEmRNtQ3yoa7NLcBzfNB1BuFCX3MDs52_vOlBZUqjq7ENDKAorjjTC-w8j3nr_ImotZJ_18bAhwmyrD95EPpq17J95rqBX-Uxrs3bfIFv6m9-yToqGRnPxv1nSRIgzpGEmGVupLsHn7QrP3ZuaC8pskUEW63gADuMRX5dvXyErNz6s_dt_k4"
                        ),
                        avatarUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuDW_znr8pAjnmgsRn6XQkeo6_tDTpFhQpTMpgJwnL1dU0GG2c3UMhB0N3r26jSoVHEvhcGwmo8-WfgQPFP1Z33IdSGumhkg6VslG8mLASTDefaK9lq43VIk7F6c_Wer4geR9oMt5bibKTK_Nz_007z_csK-SDqzyWDbeJVvgJtS7hLXpAILnpDglSCuX-nObXvqbAj1LxW8Xiw637guoPTXgOl3x9BRrzz2V4ppqV2rVp0DPw0pR8ata-rwXHkh698jjfnIQS0qO9Y",
                        likes = 12,
                        textPrimary = textPrimary,
                        textSecondary = textSecondary,
                        surfaceColor = surfaceColor
                    )
                    
                    HorizontalDivider(color = borderColor)
                    
                    ReviewItem(
                        name = "Jane Smith",
                        time = "1 week ago",
                        rating = 4,
                        content = "A really solid product for the price. It does everything it claims to do, although the battery life could be a bit better. Overall, I'm very satisfied with my purchase.",
                        imageUrls = emptyList(),
                        avatarUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuAxt0-sEGknawuFQy69F0UGVJjnlqzfBf7QlxX9zotW0yHr0DXuqIM7K3vChX8FcLl6voUVsBeDal0TF43oL8LVvy8CyXdJHULcgJOD7S1Ezxnq5pppYNasN2kkF3tDqAi39qGX1m62aUacPOqynjECSuUlCYLzxY59yFcm82kKvlP-zhCnNVTqONFmK2mHjsGzCWzHRT5s1fTBG6fBhPpuWYTUKLV_0g-eUWazeg1PFFLrw_6RPfSMSGxGstGOp1ndT7klVhnusK0",
                        likes = 8,
                        textPrimary = textPrimary,
                        textSecondary = textSecondary,
                        surfaceColor = surfaceColor
                    )
                }
            }
        }
    }
}

@Composable
fun RatingSummary(textPrimary: Color, textSecondary: Color, surfaceColor: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        Column(verticalArrangement = Arrangement.Center) {
            Text("4.5", fontSize = 48.sp, fontWeight = FontWeight.Black, color = textPrimary)
            Row {
                repeat(4) { Icon(Icons.Default.Star, null, tint = ProductPrimary, modifier = Modifier.size(18.dp)) }
                Icon(Icons.Default.StarHalf, null, tint = ProductPrimary, modifier = Modifier.size(18.dp))
            }
            Text("1204 reviews", fontSize = 14.sp, color = textSecondary)
        }
        
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            RatingBarRow(5, 0.5f, textPrimary, textSecondary, surfaceColor)
            RatingBarRow(4, 0.25f, textPrimary, textSecondary, surfaceColor)
            RatingBarRow(3, 0.15f, textPrimary, textSecondary, surfaceColor)
            RatingBarRow(2, 0.05f, textPrimary, textSecondary, surfaceColor)
            RatingBarRow(1, 0.05f, textPrimary, textSecondary, surfaceColor)
        }
    }
}

@Composable
fun RatingBarRow(star: Int, progress: Float, textPrimary: Color, textSecondary: Color, surfaceColor: Color) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(star.toString(), fontSize = 14.sp, color = textPrimary, modifier = Modifier.width(12.dp))
        Box(
            modifier = Modifier
                .weight(1f)
                .height(8.dp)
                .clip(RoundedCornerShape(100))
                .background(surfaceColor)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress)
                    .fillMaxSize()
                    .background(ProductPrimary, RoundedCornerShape(100))
            )
        }
        Text("${(progress * 100).toInt()}%", fontSize = 14.sp, color = textSecondary, modifier = Modifier.width(32.dp))
    }
}

@Composable
fun FilterChip(label: String, selected: Boolean, textPrimary: Color, surfaceColor: Color) {
    Box(
        modifier = Modifier
            .height(32.dp)
            .clip(RoundedCornerShape(100))
            .background(if (selected) ProductPrimary else surfaceColor)
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = if (selected) Color.White else textPrimary
        )
    }
}

@Composable
fun ReviewItem(
    name: String,
    time: String,
    rating: Int,
    content: String,
    imageUrls: List<String>,
    avatarUrl: String,
    likes: Int,
    textPrimary: Color,
    textSecondary: Color,
    surfaceColor: Color
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            AsyncImage(
                model = avatarUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.Gray),
                contentScale = ContentScale.Crop
            )
            Column {
                Text(name, fontWeight = FontWeight.Medium, fontSize = 16.sp, color = textPrimary)
                Text(time, fontSize = 14.sp, color = textSecondary)
            }
        }
        
        Row(modifier = Modifier.height(20.dp)) {
            repeat(5) { index ->
                Icon(
                    imageVector = if (index < rating) Icons.Default.Star else Icons.Default.StarOutline,
                    contentDescription = null,
                    tint = if (index < rating) ProductPrimary else textSecondary.copy(alpha = 0.5f),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
        
        Text(content, fontSize = 16.sp, color = textPrimary)
        
        if (imageUrls.isNotEmpty()) {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(imageUrls.size) { index ->
                    AsyncImage(
                        model = imageUrls[index],
                        contentDescription = null,
                        modifier = Modifier
                            .size(96.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(surfaceColor),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
        
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(Icons.Default.ThumbUp, contentDescription = null, tint = textSecondary, modifier = Modifier.size(20.dp))
            Text(likes.toString(), fontSize = 14.sp, color = textSecondary)
        }
    }
}

@Preview
@Composable
fun PreviewReviewDetailsScreen() {
    ReviewDetailsScreen()
}
