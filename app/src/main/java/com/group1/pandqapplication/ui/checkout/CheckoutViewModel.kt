package com.group1.pandqapplication.ui.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group1.pandqapplication.shared.data.remote.AppApiService
import com.group1.pandqapplication.shared.data.remote.dto.PaymentDetailsDto
import com.group1.pandqapplication.shared.data.remote.dto.SepayCreateQRRequest
import com.group1.pandqapplication.shared.data.remote.dto.ZaloPayCreateOrderRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CheckoutUiState(
    val selectedPaymentMethod: PaymentMethod = PaymentMethod.ZALOPAY,
    val isProcessingPayment: Boolean = false,
    val paymentSuccess: Boolean = false,
    val paymentError: String? = null,
    val zpTransToken: String? = null,
    val appTransId: String? = null,
    // SePay state
    val sepayQrUrl: String? = null,
    val sepayTransactionId: String? = null,
    val sepayBankAccount: String? = null,
    val sepayAccountName: String? = null,
    val sepayContent: String? = null,
    // Payment details from API
    val paymentDetails: PaymentDetailsDto? = null,
    val isLoadingPaymentDetails: Boolean = false
)

sealed class PaymentResult {
    object Success : PaymentResult()
    object Cancelled : PaymentResult()
    data class Error(val message: String) : PaymentResult()
    object ZaloPayNotInstalled : PaymentResult()
}

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val apiService: AppApiService
) : ViewModel() {

    private val _uiState = MutableStateFlow(CheckoutUiState())
    val uiState: StateFlow<CheckoutUiState> = _uiState.asStateFlow()

    fun selectPaymentMethod(method: PaymentMethod) {
        _uiState.update { it.copy(selectedPaymentMethod = method) }
    }

    /**
     * Load payment details from API for checkout
     * This fetches user info, shipping address, items, and amounts
     */
    fun loadPaymentDetails(orderId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingPaymentDetails = true, paymentError = null) }
            
            try {
                val paymentDetails = apiService.getPaymentDetails(orderId)
                
                // Payment details loaded successfully
                _uiState.update { 
                    it.copy(
                        isLoadingPaymentDetails = false,
                        paymentDetails = paymentDetails
                    ) 
                }
                
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isLoadingPaymentDetails = false, 
                        paymentError = "Lỗi tải thông tin đơn hàng: ${e.message}"
                    ) 
                }
            }
        }
    }

    fun initiatePayment(orderId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isProcessingPayment = true, paymentError = null) }
            
            when (_uiState.value.selectedPaymentMethod) {
                PaymentMethod.ZALOPAY -> {
                    initiateZaloPayPayment(orderId)
                }
                PaymentMethod.SEPAY -> {
                    initiateSepayPayment(orderId)
                }
            }
        }
    }

    private suspend fun initiateZaloPayPayment(orderId: String) {
        try {
            // Use actual amount from payment details
            val finalAmount = _uiState.value.paymentDetails?.finalAmount ?: 0L
            
            val request = ZaloPayCreateOrderRequest(
                amount = finalAmount,
                description = "Thanh toán đơn hàng",
                userId = _uiState.value.paymentDetails?.userId,
                orderId = orderId
            )
            
            val response = apiService.createZaloPayOrder(request)
            
            if (response.returnCode == 1 && response.zpTransToken != null) {
                _uiState.update { 
                    it.copy(
                        zpTransToken = response.zpTransToken,
                        appTransId = response.appTransId,
                        isProcessingPayment = false
                    ) 
                }
            } else {
                _uiState.update { 
                    it.copy(
                        isProcessingPayment = false, 
                        paymentError = response.returnMessage ?: "Không thể tạo đơn hàng ZaloPay"
                    ) 
                }
            }
            
        } catch (e: Exception) {
            _uiState.update { 
                it.copy(
                    isProcessingPayment = false, 
                    paymentError = "Lỗi kết nối: ${e.message}"
                ) 
            }
        }
    }

    /**
     * Initiate SePay payment using VietQR
     * SePay generates a QR code for bank transfer payment
     */
    private suspend fun initiateSepayPayment(orderId: String) {
        try {
            val finalAmount = _uiState.value.paymentDetails?.finalAmount ?: 0L
            
            val request = SepayCreateQRRequest(
                amount = finalAmount,
                description = "Chuyen khoan ngan hang",
                orderId = orderId
            )
            
            val response = apiService.createSepayQR(request)
            
            if (response.returnCode == 1 && response.qrDataUrl != null) {
                _uiState.update { 
                    it.copy(
                        isProcessingPayment = false,
                        sepayQrUrl = response.qrDataUrl,
                        sepayTransactionId = response.transactionId,
                        sepayBankAccount = response.bankAccount,
                        sepayAccountName = response.accountName,
                        sepayContent = response.content
                    ) 
                }
            } else {
                _uiState.update { 
                    it.copy(
                        isProcessingPayment = false, 
                        paymentError = response.returnMessage ?: "Không thể tạo mã QR"
                    ) 
                }
            }
            
        } catch (e: Exception) {
            _uiState.update { 
                it.copy(
                    isProcessingPayment = false, 
                    paymentError = "Lỗi kết nối SePay: ${e.message}"
                ) 
            }
        }
    }

    fun handlePaymentResult(result: PaymentResult) {
        when (result) {
            is PaymentResult.Success -> {
                _uiState.update { 
                    it.copy(
                        paymentSuccess = true, 
                        isProcessingPayment = false,
                        paymentError = null
                    ) 
                }
            }
            is PaymentResult.Cancelled -> {
                _uiState.update { 
                    it.copy(
                        isProcessingPayment = false,
                        paymentError = "Thanh toán đã bị hủy"
                    ) 
                }
            }
            is PaymentResult.Error -> {
                _uiState.update { 
                    it.copy(
                        isProcessingPayment = false,
                        paymentError = result.message
                    ) 
                }
            }
            is PaymentResult.ZaloPayNotInstalled -> {
                _uiState.update { 
                    it.copy(
                        isProcessingPayment = false,
                        paymentError = "Vui lòng cài đặt ứng dụng ZaloPay để thanh toán"
                    ) 
                }
            }
        }
    }

    fun clearPaymentError() {
        _uiState.update { it.copy(paymentError = null) }
    }

    fun setPaymentError(message: String) {
        _uiState.update { it.copy(paymentError = message) }
    }

    /**
     * Clear zpTransToken after SDK call to prevent LaunchedEffect re-triggering
     * Keeps appTransId for payment status checking
     */
    fun clearZpTransToken() {
        _uiState.update { it.copy(zpTransToken = null) }
    }

    fun resetPaymentState() {
        _uiState.update { 
            it.copy(
                isProcessingPayment = false,
                paymentSuccess = false,
                paymentError = null,
                zpTransToken = null,
                appTransId = null,
                // Clear SePay state
                sepayQrUrl = null,
                sepayTransactionId = null,
                sepayBankAccount = null,
                sepayAccountName = null,
                sepayContent = null
            ) 
        }
    }

    /**
     * Check payment status after returning from ZaloPay app
     */
    fun checkPaymentStatus() {
        val appTransId = _uiState.value.appTransId ?: return
        
        viewModelScope.launch {
            try {
                val response = apiService.getZaloPayStatus(appTransId)
                
                if (response.returnCode == 1) {
                    handlePaymentResult(PaymentResult.Success)
                } else if (response.isProcessing == true) {
                    // Payment still processing, wait
                    _uiState.update { 
                        it.copy(paymentError = "Đang xử lý thanh toán...")
                    }
                } else {
                    handlePaymentResult(PaymentResult.Error(response.returnMessage ?: "Thanh toán thất bại"))
                }
            } catch (e: Exception) {
                // Network error, payment status unknown
                _uiState.update { 
                    it.copy(paymentError = "Không thể kiểm tra trạng thái thanh toán")
                }
            }
        }
    }

    /**
     * Check SePay payment status
     */
    fun checkSepayStatus(transactionId: String) {
        viewModelScope.launch {
            try {
                val response = apiService.getSepayStatus(transactionId)
                
                if (response.returnCode == 1 && response.isPaid == true) {
                    // Payment confirmed!
                    handlePaymentResult(PaymentResult.Success)
                }
                // If not paid yet, do nothing - will retry automatically
            } catch (e: Exception) {
                // Ignore errors during status check
            }
        }
    }
}
