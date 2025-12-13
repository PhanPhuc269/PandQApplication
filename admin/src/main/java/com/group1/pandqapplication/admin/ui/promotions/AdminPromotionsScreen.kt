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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.LocalActivity
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.TimerOff
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
import com.group1.pandqapplication.shared.ui.theme.PandQApplicationTheme

@Composable
fun AdminPromotionsScreen(onNavigateToCreatePromotion: () -> Unit = {}) {
    val promotions = remember { dummyPromotions }
    var selectedFilter by remember { mutableStateOf("All") }
    val filters = listOf("All", "Active", "Scheduled", "Expired")

    Scaffold(
        topBar = {
            PromotionTopBar(onAddClick = onNavigateToCreatePromotion)
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Search and Chips Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                // Search Bar
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(12.dp),
                    placeholder = { Text("Search by code or name...", color = Color.Gray) },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.Gray)
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    singleLine = true
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

            // Promotions List
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(promotions) { promotion ->
                    PromotionCard(promotion)
                }
            }
        }
    }
}

@Composable
fun PromotionTopBar(onAddClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Promotions",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground
        )
        IconButton(
            onClick = onAddClick,
            modifier = Modifier
                .size(40.dp)
                .background(Color(0xFFec3713), CircleShape) // Primary color
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

@Preview
@Composable
fun AdminPromotionsScreenPreview() {
    PandQApplicationTheme {
        AdminPromotionsScreen()
    }
}
