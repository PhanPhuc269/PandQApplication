package com.group1.pandqapplication.ui.checkout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.group1.pandqapplication.shared.ui.theme.CheckoutPrimary
import kotlinx.coroutines.delay

/**
 * Dialog displaying SePay VietQR code for bank transfer payment
 */
@Composable
fun SepayQRDialog(
    qrUrl: String,
    amount: Long,
    bankAccount: String?,
    accountName: String?,
    content: String?,
    transactionId: String?,
    onDismiss: () -> Unit,
    onCheckStatus: () -> Unit
) {
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current
    var copied by remember { mutableStateOf(false) }
    
    // Auto-check status every 5 seconds
    LaunchedEffect(transactionId) {
        while (true) {
            delay(5000)
            onCheckStatus()
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false
        )
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .wrapContentHeight(),
            shape = RoundedCornerShape(20.dp),
            color = Color.White
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Quét mã QR để thanh toán",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF111827)
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Đóng",
                            tint = Color(0xFF6B7280)
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // QR Code Image
                Box(
                    modifier = Modifier
                        .size(280.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFF9FAFB)),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(qrUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = "VietQR Code",
                        modifier = Modifier.size(260.dp),
                        contentScale = ContentScale.Fit
                    )
                }
                
                Spacer(modifier = Modifier.height(20.dp))
                
                // Amount
                Text(
                    text = "Số tiền: ${formatAmount(amount)}₫",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = CheckoutPrimary
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Bank info
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF3F4F6), RoundedCornerShape(12.dp))
                        .padding(16.dp)
                ) {
                    InfoRow("Ngân hàng", "TPBank")
                    Spacer(modifier = Modifier.height(8.dp))
                    InfoRow("Số tài khoản", bankAccount ?: "")
                    Spacer(modifier = Modifier.height(8.dp))
                    InfoRow("Chủ tài khoản", accountName ?: "")
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Payment content with copy button
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Nội dung CK",
                                fontSize = 12.sp,
                                color = Color(0xFF6B7280)
                            )
                            Text(
                                text = content ?: "",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF111827)
                            )
                        }
                        IconButton(
                            onClick = {
                                content?.let {
                                    clipboardManager.setText(AnnotatedString(it))
                                    copied = true
                                }
                            }
                        ) {
                            Icon(
                                Icons.Default.ContentCopy,
                                contentDescription = "Copy",
                                tint = if (copied) CheckoutPrimary else Color(0xFF6B7280)
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Instructions
                Text(
                    text = "Mở app Ngân hàng và quét mã QR để thanh toán.\nHệ thống sẽ tự động xác nhận khi thanh toán thành công.",
                    fontSize = 13.sp,
                    color = Color(0xFF6B7280),
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Check status button
                OutlinedButton(
                    onClick = onCheckStatus,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Kiểm tra trạng thái")
                }
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color(0xFF6B7280)
        )
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF111827)
        )
    }
}

private fun formatAmount(amount: Long): String {
    return String.format("%,d", amount).replace(",", ".")
}
