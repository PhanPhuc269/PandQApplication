package com.group1.pandqapplication.admin.ui.promotions

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.LocalActivity
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.TimerOff
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.group1.pandqapplication.admin.data.remote.dto.PromotionDto
import com.group1.pandqapplication.admin.data.remote.dto.DiscountType as DtoDiscountType
import com.group1.pandqapplication.admin.data.remote.dto.PromotionStatus as DtoPromotionStatus
import com.group1.pandqapplication.shared.ui.theme.PandQApplicationTheme
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun AdminPromotionsScreen(
    onNavigateToCreatePromotion: () -> Unit = {},
    onBackClick: () -> Unit = {},
    viewModel: PromotionViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    // Reactive filtered list - tự động cập nhật khi uiState thay đổi
    val filteredPromotions by remember(uiState.promotions, uiState.searchQuery, uiState.selectedFilter) {
        derivedStateOf { viewModel.getFilteredPromotions() }
    }
    
    var selectedFilter by remember { mutableStateOf("All") }
    val filters = listOf("All", "Active", "Scheduled", "Expired")

    // Map filter string to enum
    LaunchedEffect(selectedFilter) {
        val filter = when (selectedFilter) {
            "Active" -> PromotionFilter.ACTIVE
            "Scheduled" -> PromotionFilter.SCHEDULED
            "Expired" -> PromotionFilter.EXPIRED
            else -> PromotionFilter.ALL
        }
        viewModel.setFilter(filter)
    }

    Scaffold(
        topBar = {
            PromotionTopBar(
                onBackClick = onBackClick,
                onAddClick = onNavigateToCreatePromotion
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Stats Cards Section
            val activeCount = uiState.promotions.count { promotion ->
                promotion.status == com.group1.pandqapplication.admin.data.remote.dto.PromotionStatus.ACTIVE
            }
            val usedTodayCount = uiState.promotions.sumOf { it.usageCount ?: 0 }
            
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Active promotions card
                StatsCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.LocalActivity,
                    iconBackgroundColor = Color(0xFF2196F3),
                    title = "Đang chạy",
                    value = activeCount.toString(),
                    change = "+5% tuần này",
                    changeColor = Color(0xFF4CAF50)
                )
                
                // Used today card
                StatsCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.CalendarMonth,
                    iconBackgroundColor = Color(0xFFFF9800),
                    title = "Đã dùng hôm nay",
                    value = usedTodayCount.toString(),
                    change = "+12% hôm qua",
                    changeColor = Color(0xFF4CAF50)
                )
            }
            
            // Search and Chips Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                // Search Bar
                OutlinedTextField(
                    value = uiState.searchQuery,
                    onValueChange = { viewModel.setSearchQuery(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(12.dp),
                    placeholder = { Text("Search by code or name...", color = Color.Gray) },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.Gray)
                    },
                    trailingIcon = {
                        if (uiState.searchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.setSearchQuery("") }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Clear",
                                    tint = Color.Gray
                                )
                            }
                        }
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(
                        onSearch = { /* Search is live, no action needed */ }
                    )
                )

                // Chips
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filters) { filter ->
                        PromotionFilterChip(
                            text = filter,
                            isSelected = filter == selectedFilter,
                            onClick = { selectedFilter = filter }
                        )
                    }
                }
            }

            // Loading indicator
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFFec3713))
                }
            }

            // Error message
            uiState.error?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp)
                )
            }

            // Promotions List
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(filteredPromotions) { promotionDto ->
                    val promotion = promotionDto.toUiModel()
                    PromotionCard(promotion)
                }
            }
        }
    }
}

/**
 * Convert PromotionDto từ API sang UI model
 */
private fun PromotionDto.toUiModel(): Promotion {
    val uiStatus = when {
        status == DtoPromotionStatus.INACTIVE -> PromotionStatus.EXPIRED
        startDate != null && isDateInFuture(startDate) -> PromotionStatus.SCHEDULED
        status == DtoPromotionStatus.ACTIVE -> PromotionStatus.ACTIVE
        else -> PromotionStatus.DRAFT
    }

    val discountText = when (type) {
        DtoDiscountType.PERCENTAGE -> "${value?.toInt() ?: 0}% OFF"
        DtoDiscountType.FIXED_AMOUNT -> "${formatCurrency(value)} OFF"
        DtoDiscountType.FREE_SHIPPING -> "Free Shipping"
    }

    val extraInfo = when (uiStatus) {
        PromotionStatus.ACTIVE -> endDate?.let { "Ends ${formatDate(it)}" } ?: ""
        PromotionStatus.SCHEDULED -> startDate?.let { "Starts ${formatDate(it)}" } ?: ""
        else -> ""
    }

    val usageLabel = when (uiStatus) {
        PromotionStatus.ACTIVE -> "Used:"
        PromotionStatus.SCHEDULED -> "Limit:"
        PromotionStatus.EXPIRED -> "Total Used:"
        else -> ""
    }

    val usageValue = if (quantityLimit != null && quantityLimit > 0) {
        "${usageCount ?: 0}/$quantityLimit"
    } else {
        "${usageCount ?: 0}"
    }

    return Promotion(
        title = name,
        description = description ?: "Created recently",
        status = uiStatus,
        discountText = discountText,
        code = code,
        extraInfo = extraInfo,
        usageLabel = usageLabel,
        usageValue = usageValue
    )
}

private fun isDateInFuture(dateStr: String): Boolean {
    return try {
        val date = LocalDateTime.parse(dateStr)
        date.isAfter(LocalDateTime.now())
    } catch (e: Exception) {
        false
    }
}

private fun formatDate(dateStr: String): String {
    return try {
        val date = LocalDateTime.parse(dateStr)
        date.format(DateTimeFormatter.ofPattern("MMM dd"))
    } catch (e: Exception) {
        dateStr
    }
}

private fun formatCurrency(value: java.math.BigDecimal?): String {
    return value?.let { "$${it.toInt()}" } ?: "$0"
}

@Composable
fun PromotionTopBar(
    onBackClick: () -> Unit,
    onAddClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        // Back button - left
        IconButton(
            onClick = onBackClick,
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBackIosNew,
                contentDescription = "Back",
                tint = Color(0xFFec3713),
                modifier = Modifier.size(24.dp)
            )
        }
        
        // Title - center
        Text(
            text = "Promotions",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.align(Alignment.Center)
        )
        
        // Add button - right
        IconButton(
            onClick = onAddClick,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .size(40.dp)
                .background(Color(0xFFec3713), CircleShape)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add", tint = Color.White)
        }
    }
}

@Composable
fun PromotionFilterChip(text: String, isSelected: Boolean, onClick: () -> Unit) {
    val backgroundColor = if (isSelected) Color(0xFFec3713) else MaterialTheme.colorScheme.surface
    val textColor = if (isSelected) Color.White else Color.Gray
    val borderColor = if (isSelected) Color.Transparent else Color.LightGray.copy(alpha = 0.5f)

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(backgroundColor)
            .border(width = 1.dp, color = borderColor, shape = RoundedCornerShape(50))
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = textColor,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
        )
    }
}

@Composable
fun PromotionCard(promotion: Promotion) {
    val icon = when (promotion.status) {
        PromotionStatus.ACTIVE -> Icons.Default.LocalActivity
        PromotionStatus.SCHEDULED -> Icons.Default.CalendarMonth
        PromotionStatus.EXPIRED -> Icons.Default.TimerOff
        PromotionStatus.DRAFT -> Icons.Default.EditNote
    }
    
    val iconTint = when(promotion.status) {
        PromotionStatus.ACTIVE -> Color(0xFFec3713) // Primary
        PromotionStatus.SCHEDULED -> Color(0xFFD97706) // Amber
        PromotionStatus.EXPIRED -> Color(0xFF64748B) // Slate
        PromotionStatus.DRAFT -> Color(0xFF2563EB) // Blue
    }
    
    val iconBg = iconTint.copy(alpha = 0.1f)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp))
            .border(1.dp, Color.LightGray.copy(alpha = 0.2f), RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(iconBg, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, contentDescription = null, tint = iconTint)
                }
                Column {
                    Text(
                        text = promotion.title,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = promotion.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }
            Icon(Icons.Default.MoreVert, contentDescription = "More", tint = Color.Gray)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Info Grid
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background.copy(alpha = 0.5f), RoundedCornerShape(8.dp)) // background-light
                .padding(12.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "DISCOUNT",
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                    color = Color.Gray
                )
                Text(
                    text = promotion.discountText,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = if (promotion.status == PromotionStatus.EXPIRED) Color.Gray else Color(0xFFec3713)
                )
            }
            
            // Vertical Divider
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(32.dp)
                    .background(Color.LightGray.copy(alpha = 0.5f))
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp)
            ) {
                Text(
                    text = "CODE",
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                    color = Color.Gray
                )
                Text(
                    text = promotion.code ?: "Not set",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Medium,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                        textDecoration = if (promotion.status == PromotionStatus.EXPIRED) TextDecoration.LineThrough else null,
                        fontStyle = if (promotion.code == null) androidx.compose.ui.text.font.FontStyle.Italic else null
                    ),
                    color = if (promotion.code == null) Color.Gray else MaterialTheme.colorScheme.onSurface
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Footer
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    color = promotion.status.containerColor,
                    shape = RoundedCornerShape(50),
                    border = androidx.compose.foundation.BorderStroke(1.dp, promotion.status.color.copy(alpha = 0.2f))
                ) {
                    Text(
                        text = promotion.status.displayName,
                        color = promotion.status.color,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        fontWeight = FontWeight.Medium
                    )
                }
                if (promotion.extraInfo.isNotEmpty()) {
                    Text(
                        text = " • ${promotion.extraInfo}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
            }

            if (promotion.status == PromotionStatus.DRAFT) {
                Text(
                    text = "Continue Editing",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = Color(0xFFec3713),
                    modifier = Modifier.clickable { /* Handle edit */ }
                )
            } else if (promotion.usageLabel.isNotEmpty()) {
                 Text(
                     text = buildString {
                         append(promotion.usageLabel)
                         append(" ")
                         append(promotion.usageValue)
                     },
                     style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                     color = Color.Gray
                 )
            }
        }
    }
}

@Composable
fun StatsCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    iconBackgroundColor: Color,
    title: String,
    value: String,
    change: String,
    changeColor: Color
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Icon
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(iconBackgroundColor.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconBackgroundColor,
                    modifier = Modifier.size(24.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Title
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            // Value
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            // Change indicator
            Text(
                text = change,
                style = MaterialTheme.typography.bodySmall,
                color = changeColor
            )
        }
    }
}

@Preview
@Composable
fun AdminPromotionsScreenPreview() {
    PandQApplicationTheme {
        AdminPromotionsScreen()
    }
}
