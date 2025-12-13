package com.group1.pandqapplication.ui.customer

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.group1.pandqapplication.shared.ui.theme.CustomerAvatarBlueDark
import com.group1.pandqapplication.shared.ui.theme.CustomerAvatarBlueLight
import com.group1.pandqapplication.shared.ui.theme.CustomerAvatarOrangeDark
import com.group1.pandqapplication.shared.ui.theme.CustomerAvatarOrangeLight
import com.group1.pandqapplication.shared.ui.theme.CustomerAvatarPurpleDark
import com.group1.pandqapplication.shared.ui.theme.CustomerAvatarPurpleLight
import com.group1.pandqapplication.shared.ui.theme.CustomerAvatarTextBlue
import com.group1.pandqapplication.shared.ui.theme.CustomerAvatarTextOrange
import com.group1.pandqapplication.shared.ui.theme.CustomerAvatarTextPurple
import com.group1.pandqapplication.shared.ui.theme.CustomerStatusActive
import com.group1.pandqapplication.shared.ui.theme.CustomerStatusLocked
import com.group1.pandqapplication.shared.ui.theme.CustomerStatusPending
import com.group1.pandqapplication.shared.ui.theme.CustomerTextPrimaryDark
import com.group1.pandqapplication.shared.ui.theme.CustomerTextPrimaryLight
import com.group1.pandqapplication.shared.ui.theme.CustomerTextSecondaryDark
import com.group1.pandqapplication.shared.ui.theme.CustomerTextSecondaryLight
import com.group1.pandqapplication.shared.ui.theme.ProductPrimary

@Composable
fun CustomerListScreen(
    onBackClick: () -> Unit = {}
) {
    val isDarkTheme = false
    
    val backgroundColor = if (isDarkTheme) CheckoutBackgroundDark else CheckoutBackgroundLight
    val textPrimary = if (isDarkTheme) CustomerTextPrimaryDark else CustomerTextPrimaryLight
    val textSecondary = if (isDarkTheme) CustomerTextSecondaryDark else CustomerTextSecondaryLight
    val borderColor = if (isDarkTheme) Color(0xFF374151) else Color(0xFFE5E7EB)
    val inputBg = if (isDarkTheme) Color(0xFF1F2937) else Color(0xFFF3F4F6) // gray-800 : gray-100

    Scaffold(
        containerColor = backgroundColor,
        topBar = {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(backgroundColor.copy(alpha = 0.8f))
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier.align(Alignment.CenterStart)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = "Back",
                            tint = textSecondary
                        )
                    }
                    Text(
                        text = "Danh sách Khách hàng",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = textPrimary,
                        modifier = Modifier.align(Alignment.Center)
                    )
                    IconButton(
                        onClick = { /* Add */ },
                        modifier = Modifier.align(Alignment.CenterEnd)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add",
                            tint = ProductPrimary
                        )
                    }
                }
                HorizontalDivider(color = borderColor)
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Search and Filters
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Search Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(inputBg)
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = textSecondary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    var searchText by remember { mutableStateOf("") }
                    BasicTextField(
                        value = searchText,
                        onValueChange = { searchText = it },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        decorationBox = { innerTextField ->
                            if (searchText.isEmpty()) {
                                Text(
                                    text = "Tìm kiếm theo tên, email, SĐT...",
                                    color = textSecondary,
                                    fontSize = 16.sp
                                )
                            }
                            innerTextField()
                        }
                    )
                }

                // Filter Buttons
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        FilterChip(
                            icon = Icons.Default.Tune,
                            label = "Lọc",
                            color = ProductPrimary,
                            bgColor = ProductPrimary.copy(alpha = 0.1f)
                        )
                    }
                    item {
                        FilterChip(
                            icon = Icons.Default.SwapVert,
                            label = "Sắp xếp",
                            color = textSecondary,
                            bgColor = inputBg
                        )
                    }
                }
            }

            // Customer List
            LazyColumn {
                item {
                    CustomerItem(
                        initials = "TN",
                        name = "Trần Nguyễn",
                        subtext = "tran.nguyen@example.com",
                        status = "Hoạt động",
                        statusColor = CustomerStatusActive,
                        avatarBg = if (isDarkTheme) CustomerAvatarOrangeDark else CustomerAvatarOrangeLight,
                        avatarText = if (isDarkTheme) Color(0xFFFDBA74) else CustomerAvatarTextOrange, // orange-300 : orange-600
                        textPrimary = textPrimary,
                        textSecondary = textSecondary
                    )
                }
                item {
                    Column {
                        HorizontalDivider(color = borderColor)
                        CustomerItem(
                            initials = "LA",
                            name = "Lê Thị An",
                            subtext = "0912345678",
                            status = "Đã khóa",
                            statusColor = CustomerStatusLocked,
                            avatarBg = if (isDarkTheme) CustomerAvatarBlueDark else CustomerAvatarBlueLight,
                            avatarText = if (isDarkTheme) Color(0xFF93C5FD) else CustomerAvatarTextBlue, // blue-300 : blue-600
                            textPrimary = textPrimary,
                            textSecondary = textSecondary
                        )
                    }
                }
                item {
                    Column {
                         HorizontalDivider(color = borderColor)
                         CustomerItem(
                            imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuAFLAu-MoPemh4lNIcKX30bEZXFH3yg0g-mKYOg01EnQTosnZUc6Y18Wl4edK9x80hWwneeJjG0xHVuEU74ZK6QvDxgy6mVNf3VKvHzcZEYPfvYnQSojxV84Msiy-nD7yl-DuJMrhiW1-lmBsQq9kbb-UrttSe97PByEABFKFQLCKzhzqgvRDQXmVmKMQIEvTn4xoNq5wNOkDwqlmnERZ1W2_EoHzS6TdB3qy6qZrU_3ZSbqcfJIHmsC6_dz_n3TsFLUF1zwnvA6Do",
                            name = "Phạm Văn Bình",
                            subtext = "pham.binh@example.com",
                            status = "Hoạt động",
                            statusColor = CustomerStatusActive,
                            textPrimary = textPrimary,
                            textSecondary = textSecondary
                        )
                    }
                }
                item {
                    Column {
                        HorizontalDivider(color = borderColor)
                        CustomerItem(
                            initials = "MC",
                            name = "Mai Anh Châu",
                            subtext = "0987654321",
                            status = "Chờ xác minh",
                            statusColor = CustomerStatusPending,
                            avatarBg = if (isDarkTheme) CustomerAvatarPurpleDark else CustomerAvatarPurpleLight,
                            avatarText = if (isDarkTheme) Color(0xFFD8B4FE) else CustomerAvatarTextPurple, // purple-300 : purple-600
                            textPrimary = textPrimary,
                            textSecondary = textSecondary
                        )
                    }
                }
                 item {
                    Column {
                        HorizontalDivider(color = borderColor)
                        CustomerItem(
                            imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuAea5zT71RumLhWB6y5Bj2T2awrVWV7uS3_OwHvMYOrdkNyCKjBYGwLfoKjzS1kENyNNGzaJgVRyKtJ4Hb_eH54rzwa1gMG9dnN2pPEB24dAORA4PEjsQ1pzJi5ZpivRvekIlfMLSW4B-W-OuCG0yQzD7bfFEVGSOSpmJMNrINSb8MBmeIpnCTtviZD9U0YJcD04b9yXCIknQ8lCXpbBpdG_x-5TdvXIpXDQQYPD-nwr3jjpNizGUkTmJgIiNhiTGgZ5kbWxHayAMQ",
                            name = "Nguyễn Hoàng Long",
                            subtext = "long.nguyen@example.com",
                            status = "Hoạt động",
                            statusColor = CustomerStatusActive,
                            textPrimary = textPrimary,
                            textSecondary = textSecondary
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FilterChip(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    color: Color,
    bgColor: Color
) {
    Row(
        modifier = Modifier
            .height(36.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor)
            .padding(horizontal = 12.dp)
            .clickable { },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(16.dp))
        Text(text = label, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = color)
        Icon(imageVector = Icons.Default.ExpandMore, contentDescription = null, tint = color, modifier = Modifier.size(16.dp))
    }
}

@Composable
fun CustomerItem(
    initials: String? = null,
    imageUrl: String? = null,
    name: String,
    subtext: String,
    status: String,
    statusColor: Color,
    avatarBg: Color = Color.Transparent,
    avatarText: Color = Color.Transparent,
    textPrimary: Color,
    textSecondary: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (imageUrl != null) {
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color.Gray.copy(alpha = 0.1f)),
                contentScale = ContentScale.Crop
            )
        } else if (initials != null) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(avatarBg, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = initials,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = avatarText
                )
            }
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(name, fontWeight = FontWeight.Medium, fontSize = 16.sp, color = textPrimary)
            Text(subtext, fontSize = 14.sp, color = textSecondary)
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(8.dp).background(statusColor, CircleShape))
                Spacer(modifier = Modifier.width(6.dp))
                Text(status, fontSize = 12.sp, color = statusColor)
            }
        }
        
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = textSecondary.copy(alpha = 0.5f)
        )
    }
}

@Preview
@Composable
fun PreviewCustomerListScreen() {
    CustomerListScreen()
}
