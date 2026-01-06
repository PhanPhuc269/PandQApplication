package com.group1.pandqapplication.admin.ui.customer

import androidx.compose.runtime.collectAsState
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import com.group1.pandqapplication.admin.data.remote.dto.CustomerTier
import com.group1.pandqapplication.admin.data.remote.dto.AccountStatus

@Composable
fun CustomerListScreen(
    onBackClick: () -> Unit = {},
    onCustomerClick: (String) -> Unit = {},
    viewModel: CustomerListViewModel = androidx.hilt.navigation.compose.hiltViewModel()
) {
    val isDarkTheme = false
    val uiState by viewModel.uiState.collectAsState()
    
    val backgroundColor = if (isDarkTheme) CheckoutBackgroundDark else CheckoutBackgroundLight
    val textPrimary = if (isDarkTheme) CustomerTextPrimaryDark else CustomerTextPrimaryLight
    val textSecondary = if (isDarkTheme) CustomerTextSecondaryDark else CustomerTextSecondaryLight
    val borderColor = if (isDarkTheme) Color(0xFF374151) else Color(0xFFE5E7EB)
    val inputBg = if (isDarkTheme) Color(0xFF1F2937) else Color(0xFFF3F4F6)

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
                    BasicTextField(
                        value = uiState.searchQuery,
                        onValueChange = { 
                            viewModel.onSearchQueryChanged(it)
                            viewModel.performSearch()
                        },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        decorationBox = { innerTextField ->
                            if (uiState.searchQuery.isEmpty()) {
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

                // Filter and Sort Buttons with Dropdown Menus
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Filter Button with Dropdown
                    var showFilterMenu by remember { mutableStateOf(false) }
                    Box {
                        FilterChip(
                            icon = Icons.Default.Tune,
                            label = if (uiState.selectedTier != null || uiState.selectedStatus != null) 
                                "Đang lọc" else "Lọc",
                            color = if (uiState.selectedTier != null || uiState.selectedStatus != null) 
                                ProductPrimary else textSecondary,
                            bgColor = if (uiState.selectedTier != null || uiState.selectedStatus != null) 
                                ProductPrimary.copy(alpha = 0.1f) else inputBg,
                            onClick = { showFilterMenu = true }
                        )
                        DropdownMenu(
                            expanded = showFilterMenu,
                            onDismissRequest = { showFilterMenu = false }
                        ) {
                            // Tier Filters
                            Text(
                                "Hạng khách hàng",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = textSecondary,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                            )
                            DropdownMenuItem(
                                text = { Text("Tất cả hạng") },
                                onClick = { 
                                    viewModel.filterByTier(null)
                                    showFilterMenu = false
                                },
                                leadingIcon = {
                                    if (uiState.selectedTier == null) Icon(Icons.Default.Check, null, tint = ProductPrimary)
                                }
                            )
                            CustomerTier.values().forEach { tier ->
                                val tierName = when (tier) {
                                    CustomerTier.BRONZE -> "Đồng"
                                    CustomerTier.SILVER -> "Bạc"
                                    CustomerTier.GOLD -> "Vàng"
                                    CustomerTier.PLATINUM -> "Bạch Kim"
                                }
                                DropdownMenuItem(
                                    text = { Text(tierName) },
                                    onClick = { 
                                        viewModel.filterByTier(tier)
                                        showFilterMenu = false
                                    },
                                    leadingIcon = {
                                        if (uiState.selectedTier == tier) Icon(Icons.Default.Check, null, tint = ProductPrimary)
                                    }
                                )
                            }
                            
                            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                            
                            // Status Filters
                            Text(
                                "Trạng thái",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = textSecondary,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                            )
                            DropdownMenuItem(
                                text = { Text("Tất cả trạng thái") },
                                onClick = { 
                                    viewModel.filterByStatus(null)
                                    showFilterMenu = false
                                },
                                leadingIcon = {
                                    if (uiState.selectedStatus == null) Icon(Icons.Default.Check, null, tint = ProductPrimary)
                                }
                            )
                            // Only ACTIVE and BANNED status options
                            DropdownMenuItem(
                                text = { Text("Hoạt động") },
                                onClick = { 
                                    viewModel.filterByStatus(AccountStatus.ACTIVE)
                                    showFilterMenu = false
                                },
                                leadingIcon = {
                                    if (uiState.selectedStatus == AccountStatus.ACTIVE) Icon(Icons.Default.Check, null, tint = ProductPrimary)
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Đã khóa") },
                                onClick = { 
                                    viewModel.filterByStatus(AccountStatus.BANNED)
                                    showFilterMenu = false
                                },
                                leadingIcon = {
                                    if (uiState.selectedStatus == AccountStatus.BANNED) Icon(Icons.Default.Check, null, tint = ProductPrimary)
                                }
                            )
                        }
                    }
                    
                    // Sort Button with Dropdown
                    var showSortMenu by remember { mutableStateOf(false) }
                    Box {
                        FilterChip(
                            icon = Icons.Default.SwapVert,
                            label = uiState.sortOption.label,
                            color = textSecondary,
                            bgColor = inputBg,
                            onClick = { showSortMenu = true }
                        )
                        DropdownMenu(
                            expanded = showSortMenu,
                            onDismissRequest = { showSortMenu = false }
                        ) {
                            SortOption.values().forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option.label) },
                                    onClick = { 
                                        viewModel.setSortOption(option)
                                        showSortMenu = false
                                    },
                                    leadingIcon = {
                                        if (uiState.sortOption == option) Icon(Icons.Default.Check, null, tint = ProductPrimary)
                                    }
                                )
                            }
                        }
                    }
                }
            }

            // Customer List
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    androidx.compose.material3.CircularProgressIndicator()
                }
            } else if (uiState.error != null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Lỗi: ${uiState.error}",
                        color = Color.Red,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            } else if (uiState.customers.isEmpty()) {
                // Empty state when no customers match filter
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.SearchOff,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = textSecondary.copy(alpha = 0.5f)
                        )
                        Text(
                            text = "Không có khách hàng phù hợp",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = textPrimary
                        )
                        if (uiState.selectedTier != null || uiState.selectedStatus != null || uiState.searchQuery.isNotEmpty()) {
                            Text(
                                text = "Thử thay đổi bộ lọc hoặc từ khóa tìm kiếm",
                                fontSize = 14.sp,
                                color = textSecondary
                            )
                        }
                    }
                }
            } else {
                LazyColumn {
                    items(uiState.customers.size) { index ->
                        val customer = uiState.customers[index]
                        if (index > 0) {
                            HorizontalDivider(color = borderColor)
                        }
                        CustomerItem(
                            customer = customer,
                            textPrimary = textPrimary,
                            textSecondary = textSecondary,
                            isDarkTheme = isDarkTheme,
                            onClick = { onCustomerClick(customer.id) }
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
    bgColor: Color,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .height(36.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp),
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
    customer: com.group1.pandqapplication.admin.data.remote.dto.CustomerListItemDto,
    textPrimary: Color,
    textSecondary: Color,
    isDarkTheme: Boolean,
    onClick: () -> Unit
) {
    // Tier colors and icons
    val (tierText, tierBgColor, tierTextColor) = when (customer.customerTier) {
        com.group1.pandqapplication.admin.data.remote.dto.CustomerTier.BRONZE -> Triple("Đồng", Color(0xFFCD7F32).copy(alpha = 0.15f), Color(0xFF8B4513))
        com.group1.pandqapplication.admin.data.remote.dto.CustomerTier.SILVER -> Triple("Bạc", Color(0xFFC0C0C0).copy(alpha = 0.25f), Color(0xFF6B6B6B))
        com.group1.pandqapplication.admin.data.remote.dto.CustomerTier.GOLD -> Triple("Vàng", Color(0xFFFFD700).copy(alpha = 0.2f), Color(0xFFB8860B))
        com.group1.pandqapplication.admin.data.remote.dto.CustomerTier.PLATINUM -> Triple("Bạch Kim", Color(0xFF9B59B6).copy(alpha = 0.15f), Color(0xFF8E44AD))
        null -> Triple("Đồng", Color(0xFFCD7F32).copy(alpha = 0.15f), Color(0xFF8B4513))
    }

    // Status color
    val statusColor = when (customer.accountStatus) {
        com.group1.pandqapplication.admin.data.remote.dto.AccountStatus.ACTIVE -> CustomerStatusActive
        com.group1.pandqapplication.admin.data.remote.dto.AccountStatus.INACTIVE -> CustomerStatusPending
        com.group1.pandqapplication.admin.data.remote.dto.AccountStatus.BANNED -> CustomerStatusLocked
        null -> CustomerStatusActive
    }

    val statusText = when (customer.accountStatus) {
        com.group1.pandqapplication.admin.data.remote.dto.AccountStatus.ACTIVE -> "Hoạt động"
        com.group1.pandqapplication.admin.data.remote.dto.AccountStatus.INACTIVE -> "Không hoạt động"
        com.group1.pandqapplication.admin.data.remote.dto.AccountStatus.BANNED -> "Đã khóa"
        null -> "Hoạt động"
    }

    // Avatar colors based on name
    val avatarColors = listOf(
        Pair(if (isDarkTheme) CustomerAvatarOrangeDark else CustomerAvatarOrangeLight, if (isDarkTheme) Color(0xFFFDBA74) else CustomerAvatarTextOrange),
        Pair(if (isDarkTheme) CustomerAvatarBlueDark else CustomerAvatarBlueLight, if (isDarkTheme) Color(0xFF93C5FD) else CustomerAvatarTextBlue),
        Pair(if (isDarkTheme) CustomerAvatarPurpleDark else CustomerAvatarPurpleLight, if (isDarkTheme) Color(0xFFD8B4FE) else CustomerAvatarTextPurple)
    )
    val colorIndex = (customer.fullName?.hashCode() ?: 0).rem(3).let { if (it < 0) it + 3 else it }
    val avatarBg = avatarColors[colorIndex].first
    val avatarText = avatarColors[colorIndex].second

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar
        if (customer.avatarUrl != null) {
            AsyncImage(
                model = customer.avatarUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color.Gray.copy(alpha = 0.1f)),
                contentScale = ContentScale.Crop
            )
        } else {
            val initials = customer.fullName?.split(" ")?.mapNotNull { it.firstOrNull() }?.take(2)?.joinToString("") ?: "?"
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
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(customer.fullName ?: "N/A", fontWeight = FontWeight.Medium, fontSize = 16.sp, color = textPrimary)
                Spacer(modifier = Modifier.width(8.dp))
                // Tier badge as colored chip
                Box(
                    modifier = Modifier
                        .background(tierBgColor, RoundedCornerShape(4.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = tierText,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = tierTextColor
                    )
                }
            }
            Text(customer.email, fontSize = 14.sp, color = textSecondary)
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                // Status
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(8.dp).background(statusColor, CircleShape))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(statusText, fontSize = 12.sp, color = statusColor)
                }
                // Created date (show first)
                val createdDate = customer.createdAt?.let {
                    try {
                        val inputFormat = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", java.util.Locale.getDefault())
                        val outputFormat = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
                        outputFormat.format(inputFormat.parse(it) ?: it)
                    } catch (e: Exception) {
                        it.take(10)
                    }
                } ?: ""
                if (createdDate.isNotEmpty()) {
                    Text(
                        text = "• $createdDate",
                        fontSize = 12.sp,
                        color = textSecondary
                    )
                }
                // Total spent (show after date)
                Text(
                    text = "• ${java.text.NumberFormat.getCurrencyInstance(java.util.Locale("vi", "VN")).format(customer.totalSpent ?: 0.0)}",
                    fontSize = 12.sp,
                    color = textSecondary
                )
            }
        }
        
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = textSecondary.copy(alpha = 0.5f)
        )
    }
}

@Composable
fun OldCustomerItem(
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
