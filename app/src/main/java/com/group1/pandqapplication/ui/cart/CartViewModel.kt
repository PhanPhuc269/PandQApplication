package com.group1.pandqapplication.ui.cart

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.qualifiers.ApplicationContext
import com.group1.pandqapplication.shared.data.remote.ApiService
import com.group1.pandqapplication.shared.data.remote.dto.AddToCartRequest
import com.group1.pandqapplication.shared.data.remote.dto.OrderDto
import com.group1.pandqapplication.shared.data.remote.dto.OrderItemDto
import com.group1.pandqapplication.shared.data.repository.GuestCartRepository
import com.group1.pandqapplication.shared.data.repository.GuestCartRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

data class CartUiState(
    val cart: OrderDto? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false
)

@HiltViewModel
class CartViewModel @Inject constructor(
    private val apiService: ApiService,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(CartUiState())
    val uiState: StateFlow<CartUiState> = _uiState.asStateFlow()

    private val guestCartRepository: GuestCartRepository by lazy {
        GuestCartRepositoryImpl(context)
    }

    fun loadCart(userId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                // If guest (empty userId), load guest cart
                if (userId.isEmpty()) {
                    loadGuestCart()
                } else {
                    // Load user cart from server
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
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Có lỗi xảy ra"
                )
            }
        }
    }

    private suspend fun loadGuestCart() {
        try {
            // Convert guest cart items to OrderDto format
            guestCartRepository.getGuestCart().collect { guestCart ->
                if (guestCart.items.isNotEmpty()) {
                    // Create OrderDto from guest cart
                    val items = guestCart.items.map { item ->
                        OrderItemDto(
                            productId = item.productId,
                            productName = item.productName,
                            quantity = item.quantity,
                            price = item.price,
                            totalPrice = item.price * item.quantity,
                            imageUrl = item.imageUrl
                        )
                    }

                    val totalAmount = items.sumOf { it.totalPrice }

                    val guestOrderDto = OrderDto(
                        id = "guest_cart",
                        userId = "",
                        totalAmount = totalAmount,
                        shippingFee = 0.0,
                        discountAmount = 0.0,
                        finalAmount = totalAmount,
                        paymentMethod = null,
                        status = "PENDING",
                        shippingAddress = null,
                        createdAt = null,
                        items = items
                    )

                    _uiState.value = _uiState.value.copy(
                        cart = guestOrderDto,
                        isLoading = false,
                        error = null
                    )
                } else {
                    // Empty guest cart
                    _uiState.value = _uiState.value.copy(
                        cart = OrderDto(
                            id = "guest_cart",
                            userId = "",
                            totalAmount = 0.0,
                            shippingFee = 0.0,
                            discountAmount = 0.0,
                            finalAmount = 0.0,
                            paymentMethod = null,
                            status = "PENDING",
                            shippingAddress = null,
                            createdAt = null,
                            items = emptyList()
                        ),
                        isLoading = false,
                        error = null
                    )
                }
            }
        } catch (e: Exception) {
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                error = "Lỗi khi tải giỏ hàng: ${e.message}"
            )
        }
    }

    fun addToCart(userId: String, productId: String, quantity: Int) {
        viewModelScope.launch {
            try {
                // If guest, increase quantity in guest cart
                if (userId.isEmpty()) {
                    val currentCart = _uiState.value.cart
                    if (currentCart != null) {
                        // Find current item and increase quantity
                        val currentItem = currentCart.items.find { it.productId == productId }
                        if (currentItem != null) {
                            val newQuantity = currentItem.quantity + quantity
                            guestCartRepository.updateGuestCartItemQuantity(productId, newQuantity)
                            
                            // Update UI without reloading entire cart
                            val updatedItems = currentCart.items.map { item ->
                                if (item.productId == productId) {
                                    item.copy(quantity = newQuantity, totalPrice = item.price * newQuantity)
                                } else {
                                    item
                                }
                            }
                            val newTotalAmount = updatedItems.sumOf { it.totalPrice }
                            val updatedCart = currentCart.copy(
                                items = updatedItems,
                                totalAmount = newTotalAmount,
                                finalAmount = newTotalAmount
                            )
                            _uiState.value = _uiState.value.copy(
                                cart = updatedCart,
                                error = null,
                                success = true
                            )
                        }
                    }
                } else {
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
                // If guest, decrease in guest cart
                if (userId.isEmpty()) {
                    val currentCart = _uiState.value.cart
                    if (currentCart != null) {
                        // Find current item and decrease quantity
                        val currentItem = currentCart.items.find { it.productId == productId }
                        if (currentItem != null && currentItem.quantity > 1) {
                            // Just decrease by 1
                            val newQuantity = currentItem.quantity - 1
                            guestCartRepository.updateGuestCartItemQuantity(productId, newQuantity)
                            
                            // Update UI without reloading entire cart
                            val updatedItems = currentCart.items.map { item ->
                                if (item.productId == productId) {
                                    item.copy(quantity = newQuantity, totalPrice = item.price * newQuantity)
                                } else {
                                    item
                                }
                            }
                            val newTotalAmount = updatedItems.sumOf { it.totalPrice }
                            val updatedCart = currentCart.copy(
                                items = updatedItems,
                                totalAmount = newTotalAmount,
                                finalAmount = newTotalAmount
                            )
                            _uiState.value = _uiState.value.copy(
                                cart = updatedCart,
                                error = null,
                                success = true
                            )
                        } else if (currentItem != null && currentItem.quantity == 1) {
                            // Remove item if quantity is 1
                            guestCartRepository.removeFromGuestCart(productId)
                            
                            // Update UI without reloading
                            val updatedItems = currentCart.items.filter { it.productId != productId }
                            val newTotalAmount = updatedItems.sumOf { it.totalPrice }
                            val updatedCart = currentCart.copy(
                                items = updatedItems,
                                totalAmount = newTotalAmount,
                                finalAmount = newTotalAmount
                            )
                            _uiState.value = _uiState.value.copy(
                                cart = updatedCart,
                                error = null,
                                success = true
                            )
                        }
                    }
                } else {
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
                // If guest, remove from guest cart
                if (userId.isEmpty()) {
                    guestCartRepository.removeFromGuestCart(productId)
                    loadCart("")
                } else {
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
