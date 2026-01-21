package com.group1.pandqapplication.ui.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group1.pandqapplication.shared.data.remote.AppApiService
import com.group1.pandqapplication.shared.data.remote.dto.PaymentDetailsDto
import com.group1.pandqapplication.shared.data.remote.dto.PromotionDto
import com.group1.pandqapplication.shared.data.remote.dto.SepayCreateQRRequest
import com.group1.pandqapplication.shared.data.remote.dto.ValidatePromotionRequest
import com.group1.pandqapplication.shared.data.remote.dto.ZaloPayCreateOrderRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
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
    val isLoadingPaymentDetails: Boolean = false,
    // Promo code state
    val promoCode: String = "",
    val isValidatingPromo: Boolean = false,
    val promotionValid: Boolean? = null,
    val promotionMessage: String? = null,
    val discountAmount: BigDecimal = BigDecimal.ZERO,
    // Voucher selection state - support 2 vouchers (shipping + discount)
    val availableVouchers: List<PromotionDto> = emptyList(),
    val isLoadingVouchers: Boolean = false,
    val selectedShippingVoucher: PromotionDto? = null,
    val selectedDiscountVoucher: PromotionDto? = null,
    val shippingDiscount: BigDecimal = BigDecimal.ZERO,
    val productDiscount: BigDecimal = BigDecimal.ZERO,
    val showVoucherSelection: Boolean = false
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
                } else if (response.returnCode == 0) {
                    // Transaction not found or error
                    _uiState.update { 
                        it.copy(paymentError = response.returnMessage ?: "Không tìm thấy giao dịch") 
                    }
                } else {
                    // Not paid yet - show feedback to user
                    _uiState.update { 
                        it.copy(paymentError = "Chưa nhận được thanh toán. Vui lòng quét mã QR để thanh toán.") 
                    }
                }
            } catch (e: Exception) {
                // Network error during status check
                _uiState.update { 
                    it.copy(paymentError = "Lỗi kiểm tra: ${e.message}") 
                }
            }
        }
    }

    // ==================== Promo Code ====================

    fun updatePromoCode(code: String) {
        _uiState.update { 
            it.copy(
                promoCode = code,
                promotionValid = null,
                promotionMessage = null
            )
        }
    }

    fun validatePromoCode() {
        val promoCode = _uiState.value.promoCode
        if (promoCode.isBlank()) {
            _uiState.update { 
                it.copy(
                    promotionValid = false,
                    promotionMessage = "Vui lòng nhập mã giảm giá"
                )
            }
            return
        }

        val orderTotal = _uiState.value.paymentDetails?.finalAmount?.let { BigDecimal(it) }

        viewModelScope.launch {
            _uiState.update { it.copy(isValidatingPromo = true, promotionMessage = null) }

            try {
                val request = ValidatePromotionRequest(
                    promoCode = promoCode,
                    orderTotal = orderTotal
                )

                val response = apiService.validatePromotion(request)

                if (response.valid) {
                    _uiState.update {
                        it.copy(
                            isValidatingPromo = false,
                            promotionValid = true,
                            promotionMessage = response.message ?: "Áp dụng mã thành công!",
                            discountAmount = response.discountAmount ?: BigDecimal.ZERO
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            isValidatingPromo = false,
                            promotionValid = false,
                            promotionMessage = response.message ?: "Mã không hợp lệ",
                            discountAmount = BigDecimal.ZERO
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isValidatingPromo = false,
                        promotionValid = false,
                        promotionMessage = "Lỗi kiểm tra mã: ${e.message}",
                        discountAmount = BigDecimal.ZERO
                    )
                }
            }
        }
    }

    fun clearPromoCode() {
        _uiState.update {
            it.copy(
                promoCode = "",
                promotionValid = null,
                promotionMessage = null,
                discountAmount = BigDecimal.ZERO,
                shippingDiscount = BigDecimal.ZERO,
                productDiscount = BigDecimal.ZERO,
                selectedShippingVoucher = null,
                selectedDiscountVoucher = null
            )
        }
    }

    // ==================== Voucher Selection ====================

    fun loadAllVouchers() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingVouchers = true) }
            val userId = _uiState.value.paymentDetails?.userId ?: ""
            
            if (userId.isEmpty()) {
                // Guest user or no user info loaded yet
                _uiState.update { it.copy(isLoadingVouchers = false) }
                return@launch
            }
            
            try {
                // Use getMyVouchers to show only claimed vouchers in user's wallet
                val response = apiService.getMyVouchers(userId)
                
                // Map VoucherResponseDto to PromotionDto for compatibility with UI
                val vouchers = response.vouchers
                    .filter { !it.isUsed } // Only show unused vouchers
                    .map { v ->
                        PromotionDto(
                            id = v.id,
                            code = v.code,
                            name = v.name,
                            type = v.discountType ?: "FIXED_AMOUNT",
                            value = v.value,
                            maxDiscountAmount = v.maxDiscountAmount,
                            minOrderValue = v.minOrderValue,
                            startDate = v.startDate,
                            endDate = v.endDate,
                            quantityLimit = v.quantityLimit,
                            usageCount = v.usageCount,
                            status = "ACTIVE" // Assuming vouchers in wallet are active/valid for display
                        )
                    }

                _uiState.update {
                    it.copy(
                        availableVouchers = vouchers,
                        isLoadingVouchers = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoadingVouchers = false,
                        paymentError = "Không thể tải danh sách voucher: ${e.message}"
                    )
                }
            }
        }
    }

    fun toggleVoucherSelection(show: Boolean) {
        _uiState.update { it.copy(showVoucherSelection = show) }
        // Always reload to get latest status (used/unused)
        if (show) {
            loadAllVouchers()
        }
    }

    /**
     * Toggle chọn/bỏ chọn voucher theo loại (shipping hoặc discount)
     * Cho phép chọn 1 voucher mỗi loại
     */
    fun toggleVoucherInSelection(voucher: PromotionDto) {
        val isShipping = voucher.type?.uppercase() == "FREE_SHIPPING"
        
        _uiState.update { currentState ->
            if (isShipping) {
                // Toggle shipping voucher
                val newSelected = if (currentState.selectedShippingVoucher?.id == voucher.id) null else voucher
                currentState.copy(selectedShippingVoucher = newSelected)
            } else {
                // Toggle discount voucher
                val newSelected = if (currentState.selectedDiscountVoucher?.id == voucher.id) null else voucher
                currentState.copy(selectedDiscountVoucher = newSelected)
            }
        }
    }

    /**
     * Xác nhận chọn voucher và áp dụng discount
     * Now calls the backend to actually save the promotion to the order
     */
    fun confirmVoucherSelection(orderId: String) {
        val shippingVoucher = _uiState.value.selectedShippingVoucher
        val discountVoucher = _uiState.value.selectedDiscountVoucher
        val userId = _uiState.value.paymentDetails?.userId ?: ""
        val orderTotal = _uiState.value.paymentDetails?.finalAmount?.let { BigDecimal(it) } ?: BigDecimal.ZERO

        viewModelScope.launch {
            _uiState.update { it.copy(isValidatingPromo = true) }

            var shippingDiscount = BigDecimal.ZERO
            var productDiscount = BigDecimal.ZERO
            var appliedCode = ""
            var isValid = false
            var message = ""

            try {
                // Apply shipping voucher to order (only one voucher can be applied at a time)
                // If user selected discount voucher, prioritize that
                val selectedVoucher = discountVoucher ?: shippingVoucher
                
                if (selectedVoucher != null && userId.isNotEmpty()) {
                    // Call API to apply promotion to order
                    val applyRequest = com.group1.pandqapplication.shared.data.remote.dto.ApplyPromotionRequest(
                        userId = userId,
                        promotionId = selectedVoucher.id
                    )
                    
                    val updatedOrder = apiService.applyPromotion(orderId, applyRequest)
                    
                    // Update local state with the result
                    val discount = updatedOrder.discountAmount?.let { BigDecimal(it.toString()) } ?: BigDecimal.ZERO
                    appliedCode = selectedVoucher.code
                    isValid = true
                    message = "Áp dụng $appliedCode thành công!"
                    
                    if (selectedVoucher == shippingVoucher) {
                        shippingDiscount = discount
                    } else {
                        productDiscount = discount
                    }
                    
                    // Reload payment details to get updated amounts
                    loadPaymentDetails(orderId)
                }

                val totalDiscount = shippingDiscount.add(productDiscount)
                
                if (!isValid && (shippingVoucher != null || discountVoucher != null)) {
                    message = "Voucher không hợp lệ"
                }

                _uiState.update {
                    it.copy(
                        isValidatingPromo = false,
                        promoCode = appliedCode,
                        promotionValid = if (appliedCode.isEmpty()) null else isValid,
                        promotionMessage = if (message.isEmpty()) null else message,
                        shippingDiscount = shippingDiscount,
                        productDiscount = productDiscount,
                        discountAmount = totalDiscount,
                        showVoucherSelection = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isValidatingPromo = false,
                        paymentError = "Lỗi áp dụng voucher: ${e.message}"
                    )
                }
            }
        }
    }

    fun isVoucherEligible(voucher: PromotionDto): Boolean {
        // Check if voucher is ACTIVE
        return voucher.status?.uppercase() == "ACTIVE"
    }

    fun isVoucherSelected(voucher: PromotionDto): Boolean {
        val state = _uiState.value
        return state.selectedShippingVoucher?.id == voucher.id || 
               state.selectedDiscountVoucher?.id == voucher.id
    }
}
