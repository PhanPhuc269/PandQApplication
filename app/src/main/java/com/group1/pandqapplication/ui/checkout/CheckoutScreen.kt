package com.group1.pandqapplication.ui.checkout

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.group1.pandqapplication.shared.ui.theme.CheckoutBackgroundDark
import com.group1.pandqapplication.shared.ui.theme.CheckoutBackgroundLight
import com.group1.pandqapplication.shared.ui.theme.CheckoutBorderDark
import com.group1.pandqapplication.shared.ui.theme.CheckoutBorderLight
import com.group1.pandqapplication.shared.ui.theme.CheckoutPrimary
import com.group1.pandqapplication.shared.ui.theme.CheckoutSurfaceDark
import com.group1.pandqapplication.shared.ui.theme.CheckoutSurfaceLight
import com.group1.pandqapplication.shared.ui.theme.CheckoutTextDark
import com.group1.pandqapplication.shared.ui.theme.CheckoutTextLight
import com.group1.pandqapplication.shared.ui.theme.CheckoutTextSecondaryDark
import com.group1.pandqapplication.shared.ui.theme.CheckoutTextSecondaryLight

enum class PaymentMethod {
    ZALOPAY,
    SEPAY
}

@Composable
fun CheckoutScreen(
    onBackClick: () -> Unit = {},
    onEditAddressClick: () -> Unit = {},
    onPaymentSuccess: (String) -> Unit = {},
    orderId: String,  // Required: Order ID from cart
    viewModel: CheckoutViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    
    // Load payment details when screen appears
    LaunchedEffect(orderId) {
        viewModel.loadPaymentDetails(orderId)
    }
    
    val isDarkTheme = false // Toggle for testing
    
    val backgroundColor = if (isDarkTheme) CheckoutBackgroundDark else CheckoutBackgroundLight
    val surfaceColor = if (isDarkTheme) CheckoutSurfaceDark else CheckoutSurfaceLight
    val textPrimary = if (isDarkTheme) CheckoutTextDark else CheckoutTextLight
    val textSecondary = if (isDarkTheme) CheckoutTextSecondaryDark else CheckoutTextSecondaryLight
    val borderColor = if (isDarkTheme) CheckoutBorderDark else CheckoutBorderLight
    val dotsInactive = if (isDarkTheme) Color(0xFF673B32) else Color(0xFFD6D3D1)
    
    // Show SePay QR Dialog when QR URL is available
    if (uiState.sepayQrUrl != null) {
        SepayQRDialog(
            qrUrl = uiState.sepayQrUrl!!,
            amount = uiState.paymentDetails?.finalAmount ?: 0L,
            bankAccount = uiState.sepayBankAccount,
            accountName = uiState.sepayAccountName,
            content = uiState.sepayContent,
            transactionId = uiState.sepayTransactionId,
            onDismiss = { viewModel.resetPaymentState() },
            onCheckStatus = { 
                uiState.sepayTransactionId?.let { viewModel.checkSepayStatus(it) }
            }
        )
    }
    
    // Show error in snackbar
    LaunchedEffect(uiState.paymentError) {
        uiState.paymentError?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearPaymentError()
        }
    }
    
    // Handle payment success
    LaunchedEffect(uiState.paymentSuccess) {
        if (uiState.paymentSuccess) {
            onPaymentSuccess(orderId)
            viewModel.resetPaymentState()
        }
    }
    
    // Handle ZaloPay transaction token - call ZaloPay SDK to open sandbox app
    LaunchedEffect(uiState.zpTransToken) {
        uiState.zpTransToken?.let { token ->
            // Get Activity from context
            val activity = context as? android.app.Activity
            if (activity != null) {
                // Call ZaloPay SDK to pay order - opens ZaloPay Sandbox app
                vn.zalopay.sdk.ZaloPaySDK.getInstance().payOrder(
                    activity,
                    token,
                    "pandqapp://payment/callback",
                    object : vn.zalopay.sdk.listeners.PayOrderListener {
                        override fun onPaymentSucceeded(transactionId: String, transToken: String, appTransId: String) {
                            viewModel.handlePaymentResult(PaymentResult.Success)
                        }

                        override fun onPaymentCanceled(zpTransToken: String, appTransId: String) {
                            viewModel.handlePaymentResult(PaymentResult.Cancelled)
                        }

                        override fun onPaymentError(zaloPayError: vn.zalopay.sdk.ZaloPayError, zpTransToken: String, appTransId: String) {
                            if (zaloPayError == vn.zalopay.sdk.ZaloPayError.PAYMENT_APP_NOT_FOUND) {
                                viewModel.handlePaymentResult(PaymentResult.ZaloPayNotInstalled)
                            } else {
                                viewModel.handlePaymentResult(PaymentResult.Error("Lỗi thanh toán: ${zaloPayError.name}"))
                            }
                        }
                    }
                )
                // Clear only zpTransToken to prevent this LaunchedEffect from re-triggering
                // Keep appTransId for payment status checking
                viewModel.clearZpTransToken()
            }
        }
    }

    Scaffold(
        containerColor = backgroundColor,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(backgroundColor)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = textPrimary
                    )
                }
                Text(
                    text = "Thanh toán",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = textPrimary,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        },
        bottomBar = {
            // Check if address is available
            val hasAddress = uiState.paymentDetails?.shippingAddress?.isNotBlank() == true
            
            Box(
                modifier = Modifier
                    .background(surfaceColor.copy(alpha = 0.8f))
                    .padding(16.dp)
            ) {
                Button(
                    onClick = { 
                        if (!hasAddress) {
                            // Show error if no address
                            viewModel.setPaymentError("Vui lòng thêm địa chỉ giao hàng trước khi thanh toán")
                        } else {
                            viewModel.initiatePayment(orderId)
                        }
                    },
                    enabled = !uiState.isProcessingPayment && uiState.paymentDetails != null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (hasAddress) CheckoutPrimary else CheckoutPrimary.copy(alpha = 0.5f)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    if (uiState.isProcessingPayment) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        val amount = if (uiState.paymentDetails != null) {
                            formatPrice(uiState.paymentDetails!!.finalAmount)
                        } else {
                            "..."
                        }
                        Text(
                            text = "Xác nhận - $amount",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Page Indicators
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier
                    .size(width = 32.dp, height = 8.dp)
                    .background(dotsInactive, CircleShape))
                Spacer(modifier = Modifier.width(12.dp))
                Box(modifier = Modifier
                    .size(width = 32.dp, height = 8.dp)
                    .background(CheckoutPrimary, CircleShape))
                Spacer(modifier = Modifier.width(12.dp))
                Box(modifier = Modifier
                    .size(width = 32.dp, height = 8.dp)
                    .background(dotsInactive, CircleShape))
            }
            
            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Delivery Address Section
                Column {
                    Text(
                        text = "Địa chỉ Giao hàng",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = textPrimary,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(surfaceColor, RoundedCornerShape(12.dp))
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(CheckoutPrimary.copy(alpha = 0.2f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = CheckoutPrimary
                            )
                        }
                        
                        Spacer(modifier = Modifier.width(16.dp))
                        
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = uiState.paymentDetails?.userName ?: "Đang tải...",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                color = textPrimary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "${uiState.paymentDetails?.userPhone?.let { "(+84) $it, " } ?: ""} ${uiState.paymentDetails?.shippingAddress ?: ""}, ${uiState.paymentDetails?.shippingDistrict ?: ""}, ${uiState.paymentDetails?.shippingCity ?: ""}",
                                fontSize = 14.sp,
                                color = textSecondary,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        
                        Spacer(modifier = Modifier.width(8.dp))
                        
                        Text(
                            text = "Thay đổi",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = CheckoutPrimary,
                            modifier = Modifier.clickable { onEditAddressClick() }
                        )
                    }
                }

                // Payment Method Section
                Column {
                    Text(
                        text = "Phương thức Thanh toán",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = textPrimary,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        PaymentOption(
                            text = "Thanh toán bằng ZaloPay",
                            isSelected = uiState.selectedPaymentMethod == PaymentMethod.ZALOPAY,
                            surfaceColor = surfaceColor,
                            borderColor = if (uiState.selectedPaymentMethod == PaymentMethod.ZALOPAY) CheckoutPrimary else borderColor,
                            textPrimary = textPrimary,
                            onClick = { viewModel.selectPaymentMethod(PaymentMethod.ZALOPAY) }
                        )
                        PaymentOption(
                            text = "Thanh toán bằng SePay",
                            isSelected = uiState.selectedPaymentMethod == PaymentMethod.SEPAY,
                            surfaceColor = surfaceColor,
                            borderColor = if (uiState.selectedPaymentMethod == PaymentMethod.SEPAY) CheckoutPrimary else borderColor,
                            textPrimary = textPrimary,
                            onClick = { viewModel.selectPaymentMethod(PaymentMethod.SEPAY) }
                        )
                    }
                }

                // Discount Code Section
                Column {
                    Text(
                        text = "Mã giảm giá",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = textPrimary,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                       TextField(
                           value = "",
                           onValueChange = {},
                           placeholder = { Text("Nhập mã giảm giá", color = Color.Gray) },
                           modifier = Modifier
                               .weight(1f)
                               .border(1.dp, borderColor, RoundedCornerShape(12.dp))
                               .clip(RoundedCornerShape(12.dp)),
                           colors = TextFieldDefaults.colors(
                               focusedContainerColor = surfaceColor,
                               unfocusedContainerColor = surfaceColor,
                               focusedIndicatorColor = Color.Transparent,
                               unfocusedIndicatorColor = Color.Transparent,
                               disabledIndicatorColor = Color.Transparent // Hide underline
                           )
                       )
                       
                       Spacer(modifier = Modifier.width(8.dp))
                       
                       Box(
                           modifier = Modifier
                               .background(CheckoutPrimary.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                               .padding(horizontal = 20.dp, vertical = 16.dp)
                       ) {
                           Text(
                               text = "Áp dụng",
                               fontSize = 14.sp,
                               fontWeight = FontWeight.SemiBold,
                               color = CheckoutPrimary
                           )
                       }
                    }
                }

                // Order Summary Section
                Column(modifier = Modifier.padding(bottom = 100.dp)) {
                    Text(
                        text = "Tóm tắt Đơn hàng",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = textPrimary,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(surfaceColor, RoundedCornerShape(12.dp))
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        SummaryRow("Tạm tính", formatPrice(uiState.paymentDetails?.subtotal ?: 0), textSecondary, textPrimary)
                        SummaryRow("Phí vận chuyển", formatPrice(uiState.paymentDetails?.shippingFee ?: 0), textSecondary, textPrimary)
                        if ((uiState.paymentDetails?.discountAmount ?: 0) > 0) {
                            SummaryRow("Giảm giá", "-${formatPrice(uiState.paymentDetails?.discountAmount ?: 0)}", textSecondary, CheckoutPrimary)
                        }
                        
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 0.dp),
                            thickness = 1.dp,
                            color = borderColor
                        )
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Tổng cộng",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = textPrimary
                            )
                            Text(
                                text = formatPrice(uiState.paymentDetails?.finalAmount ?: 0),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = textPrimary
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PaymentOption(
    text: String,
    isSelected: Boolean,
    surfaceColor: Color,
    borderColor: Color,
    textPrimary: Color,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(12.dp)
            )
            .background(surfaceColor, RoundedCornerShape(12.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = CheckoutPrimary,
                unselectedColor = Color.Gray
            ),
            modifier = Modifier.size(20.dp)
        )
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = textPrimary
        )
    }
}

@Composable
fun SummaryRow(label: String, value: String, labelColor: Color, valueColor: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = labelColor
        )
        Text(
            text = value,
            fontSize = 14.sp,
            color = valueColor,
            fontWeight = if (valueColor == CheckoutPrimary) FontWeight.Normal else FontWeight.Normal
        )
    }
}

fun formatPrice(amount: Long): String {
    return String.format("%,d₫", amount).replace(',', '.')
}

fun formatCurrency(amount: Double): String {
    val longAmount = amount.toLong()
    return String.format("%,d", longAmount).replace(',', '.')
}

@Preview
@Composable
fun PreviewCheckoutScreen() {
    CheckoutScreen(orderId = "preview-order-id")
}
