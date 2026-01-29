package com.group1.pandqapplication.admin.ui.customer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.group1.pandqapplication.admin.data.remote.dto.CustomerTier
import com.group1.pandqapplication.shared.ui.theme.*
import java.text.NumberFormat
import java.util.*

@Composable
fun TierConfigScreen(
    onBackClick: () -> Unit = {},
    viewModel: TierConfigViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    val backgroundColor = Color(0xFFF5F5F5)
    val cardColor = Color.White
    val textPrimary = Color(0xFF1A1A1A)
    val textSecondary = Color(0xFF666666)
    val borderColor = Color(0xFFE0E0E0)
    val primaryColor = Color(0xFF2196F3)

    // Show success message
    LaunchedEffect(uiState.successMessage) {
        if (uiState.successMessage != null) {
            kotlinx.coroutines.delay(2000)
            viewModel.clearMessages()
        }
    }

    Scaffold(
        containerColor = backgroundColor,
        topBar = {
            Column(modifier = Modifier.statusBarsPadding()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(cardColor)
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
                        text = "Cấu hình hạng khách hàng",
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = primaryColor
                    )
                }
                uiState.error != null -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = uiState.error ?: "Đã có lỗi xảy ra",
                            color = Color.Red,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.loadConfigs() }) {
                            Text("Thử lại")
                        }
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Info card
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = primaryColor.copy(alpha = 0.1f)),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Info,
                                        contentDescription = null,
                                        tint = primaryColor
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = "Chỉnh sửa mốc chi tiêu để phân hạng khách hàng. Thay đổi sẽ được áp dụng ngay.",
                                        fontSize = 14.sp,
                                        color = textPrimary
                                    )
                                }
                            }
                        }

                        // Tier config cards
                        val tiers = listOf(
                            CustomerTier.BRONZE to Triple("🥉", "Đồng", Color(0xFFCD7F32)),
                            CustomerTier.SILVER to Triple("🥈", "Bạc", Color(0xFFC0C0C0)),
                            CustomerTier.GOLD to Triple("🥇", "Vàng", Color(0xFFFFD700)),
                            CustomerTier.PLATINUM to Triple("💎", "Kim cương", Color(0xFF00BCD4))
                        )

                        items(tiers) { (tier, info) ->
                            val (emoji, name, color) = info
                            val config = uiState.editingConfigs[tier]
                            
                            if (config != null) {
                                TierConfigCard(
                                    emoji = emoji,
                                    tierName = name,
                                    tierColor = color,
                                    minSpent = config.minSpent,
                                    maxSpent = config.maxSpent,
                                    isLastTier = tier == CustomerTier.PLATINUM,
                                    cardColor = cardColor,
                                    textPrimary = textPrimary,
                                    textSecondary = textSecondary,
                                    onMinSpentChange = { viewModel.updateMinSpent(tier, it) },
                                    onMaxSpentChange = { viewModel.updateMaxSpent(tier, it) }
                                )
                            }
                        }

                        // Save button
                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = { viewModel.saveConfigs() },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(52.dp),
                                enabled = !uiState.isSaving,
                                colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                if (uiState.isSaving) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(24.dp),
                                        color = Color.White,
                                        strokeWidth = 2.dp
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.Save,
                                        contentDescription = null,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Lưu thay đổi",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Success snackbar
            if (uiState.successMessage != null) {
                Card(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                        .fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF4CAF50)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = uiState.successMessage ?: "",
                            color = Color.White,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TierConfigCard(
    emoji: String,
    tierName: String,
    tierColor: Color,
    minSpent: String,
    maxSpent: String,
    isLastTier: Boolean,
    cardColor: Color,
    textPrimary: Color,
    textSecondary: Color,
    onMinSpentChange: (String) -> Unit,
    onMaxSpentChange: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = emoji,
                    fontSize = 28.sp
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = tierName,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = tierColor
                    )
                    Text(
                        text = if (isLastTier) "Hạng cao nhất" else "Mức chi tiêu",
                        fontSize = 12.sp,
                        color = textSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Input fields
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Min spent
                OutlinedTextField(
                    value = minSpent,
                    onValueChange = { value ->
                        if (value.all { it.isDigit() }) {
                            onMinSpentChange(value)
                        }
                    },
                    label = { Text("Từ (đ)") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = tierColor,
                        focusedLabelColor = tierColor
                    )
                )

                // Max spent
                OutlinedTextField(
                    value = if (isLastTier) "∞" else maxSpent,
                    onValueChange = { value ->
                        if (!isLastTier && value.all { it.isDigit() }) {
                            onMaxSpentChange(value)
                        }
                    },
                    label = { Text("Đến (đ)") },
                    modifier = Modifier.weight(1f),
                    enabled = !isLastTier,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = tierColor,
                        focusedLabelColor = tierColor,
                        disabledBorderColor = Color.LightGray,
                        disabledTextColor = textSecondary
                    )
                )
            }

            // Formatted display
            Spacer(modifier = Modifier.height(8.dp))
            val formatter = NumberFormat.getNumberInstance(Locale("vi", "VN"))
            val minFormatted = minSpent.toLongOrNull()?.let { formatter.format(it) } ?: "0"
            val maxFormatted = if (isLastTier) "không giới hạn" else (maxSpent.toLongOrNull()?.let { formatter.format(it) } ?: "0")
            
            Text(
                text = "Phạm vi: $minFormatted đ → $maxFormatted đ",
                fontSize = 12.sp,
                color = textSecondary
            )
        }
    }
}
