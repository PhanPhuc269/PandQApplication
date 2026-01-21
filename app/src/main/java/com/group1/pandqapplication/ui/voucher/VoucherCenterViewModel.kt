package com.group1.pandqapplication.ui.voucher

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group1.pandqapplication.shared.data.remote.AppApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// Local data class for voucher items
data class VoucherItem(
    val id: String,
    val code: String,
    val name: String,
    val description: String?,
    val type: String, // FREE_SHIPPING, PERCENTAGE, FIXED_AMOUNT
    val value: Double,
    val maxDiscountAmount: Double? = null,
    val minOrderValue: Double? = null,
    val endDate: String? = null,
    val isClaimed: Boolean = false
)

data class VoucherCenterUiState(
    val isLoading: Boolean = false,
    val vouchers: List<VoucherItem> = emptyList(),
    val error: String? = null,
    val claimingVoucherId: String? = null,
    val userId: String = ""
)

@HiltViewModel
class VoucherCenterViewModel @Inject constructor(
    private val apiService: AppApiService
) : ViewModel() {

    private val _uiState = MutableStateFlow(VoucherCenterUiState())
    val uiState: StateFlow<VoucherCenterUiState> = _uiState.asStateFlow()

    init {
        loadVouchers()
    }

    fun setUserId(id: String) {
        if (_uiState.value.userId != id) {
            _uiState.update { it.copy(userId = id) }
            loadVouchers() // Reload when user ID changes
        }
    }

    fun loadVouchers() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            val userId = _uiState.value.userId
            
            try {
                // Call backend API to get available vouchers with user context
                val response = apiService.getAvailableVouchers(if (userId.isNotEmpty()) userId else null)
                
                val vouchers = response.vouchers.map { promo ->
                    VoucherItem(
                        id = promo.id,
                        code = promo.code,
                        name = promo.name,
                        description = promo.description ?: buildDescriptionFromDto(promo),
                        type = promo.discountType ?: "FIXED_AMOUNT",
                        value = promo.value?.toDouble() ?: 0.0,
                        maxDiscountAmount = promo.maxDiscountAmount?.toDouble(),
                        minOrderValue = promo.minOrderValue?.toDouble(),
                        endDate = promo.endDate?.take(10), // Format: yyyy-MM-dd
                        isClaimed = promo.isClaimed
                    )
                }
                
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        vouchers = vouchers,
                        error = null
                    ) 
                }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Không thể tải voucher"
                    ) 
                }
            }
        }
    }

    private fun buildDescriptionFromDto(promo: com.group1.pandqapplication.shared.data.remote.dto.VoucherResponseDto): String {
        val parts = mutableListOf<String>()
        promo.minOrderValue?.let { 
            parts.add("Đơn tối thiểu ${formatCurrency(it.toDouble())}")
        }
        promo.maxDiscountAmount?.let {
            parts.add("Giảm tối đa ${formatCurrency(it.toDouble())}")
        }
        return parts.joinToString(" - ").ifEmpty { "Áp dụng cho mọi đơn hàng" }
    }

    private fun formatCurrency(value: Double): String {
        return "₫${String.format("%,.0f", value)}"
    }

    fun claimVoucher(voucherId: String) {
        viewModelScope.launch {
            val userId = _uiState.value.userId
            if (userId.isEmpty()) {
                _uiState.update { it.copy(error = "Vui lòng đăng nhập để lưu voucher") }
                return@launch
            }
            
            _uiState.update { it.copy(claimingVoucherId = voucherId) }
            
            try {
                val response = apiService.claimVoucher(
                    userId = userId,
                    request = com.group1.pandqapplication.shared.data.remote.dto.ClaimVoucherRequest(voucherId)
                )
                
                if (response.success) {
                    _uiState.update { state ->
                        state.copy(
                            claimingVoucherId = null,
                            vouchers = state.vouchers.map { voucher ->
                                if (voucher.id == voucherId) {
                                    voucher.copy(isClaimed = true)
                                } else voucher
                            }
                        )
                    }
                } else {
                     _uiState.update { 
                        it.copy(
                            claimingVoucherId = null,
                            error = response.message
                        ) 
                    }
                }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        claimingVoucherId = null,
                        error = e.message ?: "Không thể lưu voucher"
                    ) 
                }
            }
        }
    }
    
    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
