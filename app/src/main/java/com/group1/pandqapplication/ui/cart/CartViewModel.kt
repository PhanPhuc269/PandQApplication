package com.group1.pandqapplication.ui.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group1.pandqapplication.shared.data.remote.ApiService
import com.group1.pandqapplication.shared.data.remote.dto.AddToCartRequest
import com.group1.pandqapplication.shared.data.remote.dto.OrderDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CartUiState(
    val cart: OrderDto? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false
)

@HiltViewModel
class CartViewModel @Inject constructor(
    private val apiService: ApiService
) : ViewModel() {

    private val _uiState = MutableStateFlow(CartUiState())
    val uiState: StateFlow<CartUiState> = _uiState.asStateFlow()

    fun loadCart(userId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val response = apiService.getCart(userId)
                if (response.isSuccessful && response.body() != null) {
                    _uiState.value = _uiState.value.copy(
                        cart = response.body(),
                        isLoading = false,
                        error = null
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = response.message() ?: "Lỗi khi tải giỏ hàng"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Có lỗi xảy ra"
                )
            }
        }
    }

    fun addToCart(userId: String, productId: String, quantity: Int) {
        viewModelScope.launch {
            try {
                val request = AddToCartRequest(
                    userId = userId,
                    productId = productId,
                    quantity = quantity
                )
                val response = apiService.addToCart(request)
                if (response.isSuccessful && response.body() != null) {
                    _uiState.value = _uiState.value.copy(
                        cart = response.body(),
                        error = null,
                        success = true
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        error = response.message() ?: "Không thể thêm vào giỏ hàng",
                        success = false
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Có lỗi xảy ra",
                    success = false
                )
            }
        }
    }

    fun decreaseQuantity(userId: String, productId: String) {
        viewModelScope.launch {
            try {
                val request = AddToCartRequest(
                    userId = userId,
                    productId = productId,
                    quantity = 1
                )
                val response = apiService.decreaseQuantity(request)
                if (response.isSuccessful && response.body() != null) {
                    _uiState.value = _uiState.value.copy(
                        cart = response.body(),
                        error = null,
                        success = true
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        error = response.message() ?: "Không thể giảm số lượng",
                        success = false
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Có lỗi xảy ra",
                    success = false
                )
            }
        }
    }

    fun removeFromCart(userId: String, productId: String) {
        viewModelScope.launch {
            try {
                val response = apiService.removeFromCart(userId, productId)
                if (response.isSuccessful && response.body() != null) {
                    _uiState.value = _uiState.value.copy(
                        cart = response.body(),
                        error = null,
                        success = true
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        error = response.message() ?: "Không thể xóa khỏi giỏ hàng",
                        success = false
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Có lỗi xảy ra",
                    success = false
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun clearSuccess() {
        _uiState.value = _uiState.value.copy(success = false)
    }
}
