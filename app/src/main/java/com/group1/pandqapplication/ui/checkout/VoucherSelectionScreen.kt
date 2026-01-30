package com.group1.pandqapplication.ui.checkout

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.group1.pandqapplication.shared.data.remote.dto.PromotionDto
import com.group1.pandqapplication.shared.ui.theme.CheckoutPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoucherSelectionScreen(
    vouchers: List<PromotionDto>,
    isLoading: Boolean,
    selectedShippingVoucher: PromotionDto?,
    selectedDiscountVoucher: PromotionDto?,
    promoCode: String,
    isValidating: Boolean,
    onVoucherToggle: (PromotionDto) -> Unit,
    onPromoCodeChange: (String) -> Unit,
    onApplyPromoCode: () -> Unit,
    isVoucherEligible: (PromotionDto) -> Boolean,
    onConfirm: () -> Unit,
    onBack: () -> Unit
) {
    val backgroundColor = Color(0xFFF5F5F5)
    val surfaceColor = Color.White
    val textPrimary = Color(0xFF1F2937)
    val textSecondary = Color(0xFF6B7280)
    
    // Check if any voucher is selected
    val hasSelection = selectedShippingVoucher != null || selectedDiscountVoucher != null
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Chọn Voucher",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Quay lại"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = surfaceColor
                )
            )
        },
        bottomBar = {
            // Bottom bar with Confirm button
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shadowElevation = 8.dp,
                color = surfaceColor
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    // Show selected vouchers summary
                    if (hasSelection) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp)
                        ) {
                            if (selectedShippingVoucher != null) {
                                Text(
                                    text = "✓ ${selectedShippingVoucher.name}",
                                    fontSize = 13.sp,
                                    color = Color(0xFF22C55E),
                                    fontWeight = FontWeight.Medium
                                )
                            }
                            if (selectedDiscountVoucher != null) {
                                Text(
                                    text = "✓ ${selectedDiscountVoucher.name}",
                                    fontSize = 13.sp,
                                    color = CheckoutPrimary,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                    
                    Button(
                        onClick = onConfirm,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CheckoutPrimary
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        if (isValidating) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                text = "Đồng ý",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        },
        containerColor = backgroundColor
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Input mã voucher
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(surfaceColor)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = promoCode,
                    onValueChange = onPromoCodeChange,
                    placeholder = { Text("Nhập mã voucher", color = textSecondary) },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CheckoutPrimary,
                        unfocusedBorderColor = Color(0xFFE5E7EB)
                    )
                )
                
                Button(
                    onClick = onApplyPromoCode,
                    enabled = promoCode.isNotBlank() && !isValidating,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CheckoutPrimary,
                        disabledContainerColor = Color(0xFFE5E7EB)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Áp dụng", fontWeight = FontWeight.SemiBold)
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Loading state
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = CheckoutPrimary)
                }
            } else {
                // Group vouchers by type
                val shippingVouchers = vouchers.filter { it.type?.uppercase() == "FREE_SHIPPING" }
                val discountVouchers = vouchers.filter { it.type?.uppercase() != "FREE_SHIPPING" }

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Shipping Vouchers Section
                    if (shippingVouchers.isNotEmpty()) {
                        item {
                            Text(
                                text = "Ưu đãi phí vận chuyển",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = textPrimary,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }
                        
                        items(shippingVouchers) { voucher ->
                            VoucherCard(
                                voucher = voucher,
                                isSelected = selectedShippingVoucher?.id == voucher.id,
                                isEligible = isVoucherEligible(voucher),
                                onSelect = { onVoucherToggle(voucher) },
                                isShipping = true
                            )
                        }
                    }
                    
                    // Discount Vouchers Section
                    if (discountVouchers.isNotEmpty()) {
                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Mã giảm giá",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = textPrimary,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }
                        
                        items(discountVouchers) { voucher ->
                            VoucherCard(
                                voucher = voucher,
                                isSelected = selectedDiscountVoucher?.id == voucher.id,
                                isEligible = isVoucherEligible(voucher),
                                onSelect = { onVoucherToggle(voucher) },
                                isShipping = false
                            )
                        }
                    }
                    
                    // Empty state
                    if (vouchers.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Không có voucher khả dụng",
                                    color = textSecondary,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                    
                    // Add bottom padding for the fixed bottom bar
                    item {
                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun VoucherCard(
    voucher: PromotionDto,
    isSelected: Boolean,
    isEligible: Boolean,
    onSelect: () -> Unit,
    isShipping: Boolean
) {
    val borderColor = when {
        isSelected -> CheckoutPrimary
        !isEligible -> Color(0xFFE5E7EB)
        else -> Color(0xFFE5E7EB)
    }
    
    val alphaValue = if (isEligible) 1f else 0.5f
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(alphaValue)
            .then(
                if (isEligible) Modifier.clickable { onSelect() }
                else Modifier
            )
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(12.dp)
            ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon/Badge
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(
                        if (isShipping) Color(0xFF22C55E).copy(alpha = 0.1f)
                        else CheckoutPrimary.copy(alpha = 0.1f),
                        RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isShipping) Icons.Default.LocalShipping else Icons.Default.LocalOffer,
                    contentDescription = null,
                    tint = if (isShipping) Color(0xFF22C55E) else CheckoutPrimary,
                    modifier = Modifier.size(28.dp)
                )
            }
            
            // Voucher Info
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = voucher.name,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1F2937),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                // Discount info
                val discountText = when (voucher.type?.uppercase()) {
                    "PERCENTAGE" -> "Giảm ${voucher.value?.toInt() ?: 0}%" + 
                        (voucher.maxDiscountAmount?.let { " Tối đa ${String.format("%,.0f₫", it)}" } ?: "")
                    "FIXED_AMOUNT" -> "Giảm ${String.format("%,.0f₫", voucher.value)}"
                    "FREE_SHIPPING" -> "Miễn phí vận chuyển"
                    else -> voucher.name
                }
                Text(
                    text = discountText,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (isShipping) Color(0xFF22C55E) else CheckoutPrimary
                )
                
                // Min order value
                voucher.minOrderValue?.let { minOrder ->
                    if (minOrder.toInt() > 0) {
                        Text(
                            text = "Đơn tối thiểu ${String.format("%,.0f₫", minOrder)}",
                            fontSize = 12.sp,
                            color = Color(0xFF6B7280)
                        )
                    }
                }
                
                // Status / Expiry
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (!isEligible) {
                        Text(
                            text = "Không khả dụng",
                            fontSize = 11.sp,
                            color = Color(0xFFEF4444),
                            fontWeight = FontWeight.Medium
                        )
                    }
                    voucher.endDate?.let { endDate ->
                        Text(
                            text = "HSD: $endDate",
                            fontSize = 11.sp,
                            color = Color(0xFF9CA3AF)
                        )
                    }
                }
            }
            
            // Selection indicator - Checkbox instead of RadioButton
            Checkbox(
                checked = isSelected,
                onCheckedChange = if (isEligible) { _ -> onSelect() } else null,
                colors = CheckboxDefaults.colors(
                    checkedColor = if (isShipping) Color(0xFF22C55E) else CheckoutPrimary,
                    uncheckedColor = Color(0xFFD1D5DB)
                )
            )
        }
    }
}
