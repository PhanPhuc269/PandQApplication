package com.group1.pandqapplication.ui.feedback

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.group1.pandqapplication.shared.ui.theme.BackgroundLight
import com.group1.pandqapplication.shared.ui.theme.CardLight
import com.group1.pandqapplication.shared.ui.theme.FeedbackAccent
import com.group1.pandqapplication.shared.ui.theme.FeedbackBackgroundDark
import com.group1.pandqapplication.shared.ui.theme.FeedbackBorderDark
import com.group1.pandqapplication.shared.ui.theme.FeedbackBorderLight
import com.group1.pandqapplication.shared.ui.theme.FeedbackPlaceholderDark
import com.group1.pandqapplication.shared.ui.theme.FeedbackPlaceholderLight
import com.group1.pandqapplication.shared.ui.theme.FeedbackSecondaryDark
import com.group1.pandqapplication.shared.ui.theme.Primary
import com.group1.pandqapplication.shared.ui.theme.TextDarkPrimary
import com.group1.pandqapplication.shared.ui.theme.TextLightPrimary

@Composable
fun FeedbackScreen(
    onBackClick: () -> Unit = {}
) {
    val isDarkTheme = false
    
    val backgroundColor = if (isDarkTheme) FeedbackBackgroundDark else BackgroundLight
    val secondaryBackgroundColor = if (isDarkTheme) FeedbackSecondaryDark else CardLight
    val textPrimary = if (isDarkTheme) TextDarkPrimary else TextLightPrimary
    val textSecondary = if (isDarkTheme) FeedbackPlaceholderDark else FeedbackPlaceholderLight
    val borderColor = if (isDarkTheme) FeedbackBorderDark else FeedbackBorderLight
    val starEmptyColor = if (isDarkTheme) FeedbackBorderDark else FeedbackBorderLight

    Scaffold(
        containerColor = backgroundColor,
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(secondaryBackgroundColor)
                    .border(width = 0.5.dp, color = borderColor)
                    .padding(start = 16.dp, end = 16.dp, top = 32.dp, bottom = 16.dp)
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBackIosNew,
                        contentDescription = "Back",
                        tint = Primary
                    )
                }
                Text(
                    text = "Đánh giá sản phẩm",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = textPrimary,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .background(secondaryBackgroundColor)
                    .border(width = 0.5.dp, color = borderColor)
                    .padding(16.dp)
            ) {
                Button(
                    onClick = { /* Submit Review */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Primary),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Gửi đánh giá",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            // Product Information
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(secondaryBackgroundColor, RoundedCornerShape(12.dp))
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = "https://lh3.googleusercontent.com/aida-public/AB6AXuA4ngQPYOocIctavJUQjszyNgZzE7m7_IwpKOa9y6D2MI79hOrsqk6vknHJQflyopi_k96DKM2UoSmPLfJnxgL6Vx0G9fqtg_ZmP88SuI6QebXxFXphtC4xiOIERevhv9F22Wdmnk0yh4P6lN9c0MvwGqiwp8MOSpAY0eyH5f-Lms85cF91z6HgJ5s-TREhyL3Qzf_jkX7fxQ6lt-jf5zcqkQuOCUnWKzS0r_vGkK-MXGL-9t9B0LbnFyZH-_FH9oHUQ3ROBEmFUbY",
                    contentDescription = null,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.Gray.copy(alpha = 0.1f)),
                    contentScale = ContentScale.Crop
                )
                
                Spacer(modifier = Modifier.width(16.dp))
                
                Column {
                    Text(
                        text = "Gaming Laptop Pro X",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = textPrimary,
                        maxLines = 1
                    )
                    Text(
                        text = "Màu: Đen, RAM: 16GB",
                        fontSize = 14.sp,
                        color = textSecondary,
                        maxLines = 1
                    )
                }
            }

            // Rating Section
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Chất lượng sản phẩm",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = textPrimary
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                var rating by remember { mutableIntStateOf(4) }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    repeat(5) { index ->
                        Icon(
                            imageVector = if (index < rating) Icons.Filled.Star else Icons.Outlined.Star,
                            contentDescription = null,
                            tint = if (index < rating) FeedbackAccent else starEmptyColor,
                            modifier = Modifier
                                .size(40.dp)
                                .clickable { rating = index + 1 }
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = when(rating) {
                        5 -> "Tuyệt vời"
                        4 -> "Rất tốt"
                        3 -> "Bình thường"
                        2 -> "Tệ"
                        else -> "Rất tệ"
                    },
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = textPrimary
                )
            }

            // Detailed Review Section
            Column {
                Text(
                    text = "Chia sẻ thêm",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = textPrimary,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                
                TextField(
                    value = "",
                    onValueChange = {},
                    placeholder = { Text("Hãy chia sẻ cảm nhận của bạn về sản phẩm...", color = textSecondary) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .border(1.dp, borderColor, RoundedCornerShape(8.dp))
                        .clip(RoundedCornerShape(8.dp)),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = secondaryBackgroundColor,
                        unfocusedContainerColor = secondaryBackgroundColor,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )
            }

            // Image Uploader Section
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Hình ảnh thực tế",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = textPrimary
                    )
                    Text(
                        text = "Gửi kèm ảnh để nhận thêm ưu đãi!",
                        fontSize = 14.sp,
                        color = textSecondary
                    )
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Image 1
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                    ) {
                        AsyncImage(
                            model = "https://lh3.googleusercontent.com/aida-public/AB6AXuC6JiuAJVB_W2kAf8MRhCM1yzDxh5G7SIpOdo7ia6RCmDaon9UY6zk57saaHfmS_DF9YvGpfVvX3s0ygTikSHhUaRXWtmlNDSt2TmPlqKxCP9-0yqaaQ2KZ349cf2z4APfOUyLqtM3l7zHkT28jTRbrS7Vn7DgG1g5ifdv3-IdEw11dox73yN2SMevnJgTKZDCHlG9OEGANdEc4IhgRrgEfE9Tj8a6tV3cfLuJZiEFSiffp_qXM_7HDdX_4c6KLv8lzFSAejOmSUtM",
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(4.dp)
                                .size(20.dp)
                                .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                                .clickable { },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Remove",
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }

                    // Image 2
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                    ) {
                        AsyncImage(
                            model = "https://lh3.googleusercontent.com/aida-public/AB6AXuBnL2RnSUguRmkh2uDloPu_gkLlUeHAglA4PfL-ICIrZI_WeANjF0u0RRC9DKBqDjKt1oUe_YjpFxIWoDYh5Z2MeDI_p0QVdlliQz1FrMpB9P1GOcKQmdKyJ4a88Z6W2yesXzJatm5clf06gUo8z5_dQ5_5uGaOXbZyOg_70vobS-a1vYBiw90Dtg0HD5GiTuevB6L0lbYpUlvS-G9a8k5h7wW3RW0aQEKldiF9-QoyR-CZHYajMfDh5t0WHyzngoTzLVJ60FCPpNU",
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(4.dp)
                                .size(20.dp)
                                .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                                .clickable { },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Remove",
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }

                    // Add Photo Button
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .background(backgroundColor)
                            .border(2.dp, borderColor, RoundedCornerShape(8.dp))
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { },
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddPhotoAlternate,
                            contentDescription = null,
                            tint = Primary,
                            modifier = Modifier.size(32.dp)
                        )
                        Text(
                            text = "Thêm ảnh",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = Primary
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewFeedbackScreen() {
    FeedbackScreen()
}
