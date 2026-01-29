package com.group1.pandqapplication.ui.voucher

import android.app.Activity
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel

// Setup colors based on reference
val VoucherPrimary = Color(0xFFEE4D2D)
val VoucherFreeShip = Color(0xFF00BFA5)
val VoucherBg = Color(0xFFF5F5F5)

@Composable
fun VoucherCenterScreen(
    onBackClick: () -> Unit,
    userId: String,
    viewModel: VoucherCenterViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    LaunchedEffect(userId) {
        viewModel.setUserId(userId)
    }

    // Configure edge-to-edge display like SearchScreen
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Transparent.toArgb()
            window.navigationBarColor = Color.Transparent.toArgb()
            
            WindowCompat.setDecorFitsSystemWindows(window, false)
            
            val insetsController = WindowCompat.getInsetsController(window, view)
            insetsController.isAppearanceLightStatusBars = true
            insetsController.isAppearanceLightNavigationBars = true
        }
    }

    Scaffold(
        containerColor = VoucherBg,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            Column(modifier = Modifier.background(Color.White)) {
                // Header with statusBarsPadding
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = VoucherPrimary
                        )
                    }
                    
                    Text(
                        text = "Kho Voucher",
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Outlined.HelpOutline,
                            contentDescription = "Help",
                            tint = VoucherPrimary
                        )
                    }
                }
                
                // Divider
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color(0xFFEEEEEE))
                )
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when {
                // Loading state
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = VoucherPrimary)
                    }
                }
                // Error state
                uiState.error != null -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = "😢", fontSize = 48.sp)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Đã xảy ra lỗi",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF111827)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = uiState.error ?: "",
                            fontSize = 14.sp,
                            color = Color(0xFF6B7280),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(
                            onClick = { viewModel.loadVouchers() },
                            colors = ButtonDefaults.buttonColors(containerColor = VoucherPrimary),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Thử lại")
                        }
                    }
                }
                // Empty state
                uiState.vouchers.isEmpty() -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = "🎟️", fontSize = 48.sp)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Chưa có voucher nào",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF111827)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Các voucher ưu đãi sẽ hiển thị ở đây",
                            fontSize = 14.sp,
                            color = Color(0xFF6B7280),
                            textAlign = TextAlign.Center
                        )
                    }
                }
                // Success state with vouchers
                else -> {
                    val bottomPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 12.dp),
                        contentPadding = PaddingValues(top = 12.dp, bottom = bottomPadding + 12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(uiState.vouchers) { voucher ->
                            VoucherCardItem(
                                voucher = voucher,
                                onClaim = { viewModel.claimVoucher(voucher.id) },
                                isClaiming = uiState.claimingVoucherId == voucher.id
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun VoucherCardItem(
    voucher: VoucherItem,
    onClaim: () -> Unit,
    isClaiming: Boolean
) {
    val isFreeShip = voucher.type == "FREE_SHIPPING"
    val mainColor = if (isFreeShip) VoucherFreeShip else VoucherPrimary
    
    // Card Container - Dynamic height with minimum
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 140.dp)
            .background(Color.White, RoundedCornerShape(4.dp))
    ) {
        // LEFT PART
        Column(
            modifier = Modifier
                .width(100.dp)
                .defaultMinSize(minHeight = 140.dp)
                .background(mainColor)
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (isFreeShip) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.LocalShipping,
                        contentDescription = null,
                        tint = mainColor,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Text(
                    text = "MIỄN PHÍ\nVẬN CHUYỂN",
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    lineHeight = 13.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            } else {
                // Discount type
                Text(
                    text = if (voucher.type == "PERCENTAGE") "${voucher.value.toInt()}%" else "GIẢM",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = "GIẢM",
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "TOÀN SÀN",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 11.sp,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }

        // DIVIDER LINE (Sawtooth effect simulated)
        Box(
            modifier = Modifier
                .width(2.dp)
                .defaultMinSize(minHeight = 140.dp)
                .background(Color.White)
        ) {
            // Draw dashed line
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawLine(
                    color = Color(0xFFE0E0E0),
                    start = Offset(0f, 0f),
                    end = Offset(0f, size.height),
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f),
                    strokeWidth = 2f
                )
            }
            
            // Punch holes based only on height
            val holeSize = 12.dp
            Box(
                modifier = Modifier
                    .size(holeSize)
                    .offset(x = (-6).dp, y = (-6).dp)
                    .background(VoucherBg, CircleShape)
            )
            Box(
                modifier = Modifier
                    .size(holeSize)
                    .align(Alignment.BottomStart)
                    .offset(x = (-6).dp, y = 6.dp)
                    .background(VoucherBg, CircleShape)
            )
        }

        // RIGHT PART - Use Box for better absolute positioning of button
        Box(
            modifier = Modifier
                .weight(1f)
                .defaultMinSize(minHeight = 140.dp)
                .padding(12.dp)
        ) {
            // Content Top
            Column(
                modifier = Modifier.fillMaxWidth().padding(bottom = 40.dp) // Reserve space for footer
            ) {
                Text(
                    text = voucher.name,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = voucher.description ?: "",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 4.dp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                // Expiry
                if (!isFreeShip) {
                    Text(
                        text = voucher.endDate?.let { "HSD: $it" } ?: "",
                        fontSize = 11.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            // Footer (Status + Button) - Aligned to Bottom
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (voucher.isClaimed) "Đã trong ví" else if (isFreeShip) "Sắp hết hạn" else "Có giới hạn", 
                    fontSize = 11.sp,
                    color = if (voucher.isClaimed) VoucherFreeShip else Color(0xFFFF9800)
                )
                
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(if (voucher.isClaimed) Color(0xFFE0E0E0) else mainColor)
                        .clickable(enabled = !voucher.isClaimed && !isClaiming) { onClaim() }
                        .padding(horizontal = 20.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (isClaiming) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(16.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = if (voucher.isClaimed) "Dùng ngay" else "Lưu",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (voucher.isClaimed) Color.Gray else Color.White
                        )
                    }
                }
            }
        }
    }
}
