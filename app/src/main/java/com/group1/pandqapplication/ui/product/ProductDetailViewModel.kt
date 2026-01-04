package com.group1.pandqapplication.ui.product

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import com.group1.pandqapplication.data.repository.ProductRepository
import com.group1.pandqapplication.shared.data.repository.AuthRepository
import com.group1.pandqapplication.shared.data.repository.GuestCartRepository
import com.group1.pandqapplication.shared.data.repository.GuestCartRepositoryImpl
import com.group1.pandqapplication.shared.data.remote.ApiService
import com.group1.pandqapplication.shared.data.remote.dto.AddToCartRequest
import com.group1.pandqapplication.shared.data.remote.dto.ProductDetailDto
import com.group1.pandqapplication.shared.data.remote.dto.ReviewDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProductDetailUiState(
    val isLoading: Boolean = true,
    val isLoadingReviews: Boolean = false,
    val isSubmittingReview: Boolean = false,
    val showWriteReviewScreen: Boolean = false, // Controls full screen visibility
    val product: ProductDetailDto? = null,
    val reviews: List<ReviewDto> = emptyList(),
    val quantity: Int = 1,
    val selectedTab: Int = 0,
    val filterByRating: Int? = null, // null = All, 1-5 = specific rating
    val sortBy: String = "newest", // "newest", "highest", "lowest"
    val error: String? = null,
    val reviewSubmitSuccess: Boolean = false,
    val reviewSubmitError: String? = null,
    val addToCartSuccess: Boolean = false,
    val addToCartError: String? = null
)

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val repository: ProductRepository,
    private val apiService: ApiService,
    private val authRepository: AuthRepository,
    savedStateHandle: SavedStateHandle,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductDetailUiState())
    val uiState: StateFlow<ProductDetailUiState> = _uiState.asStateFlow()

    private val productId: String = savedStateHandle.get<String>("productId") ?: ""
    private val guestCartRepository: GuestCartRepository by lazy {
        GuestCartRepositoryImpl(context)
    }

    init {
        if (productId.isNotEmpty()) {
            loadProduct()
            loadReviews()
        }
    }

    fun loadProduct() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            repository.getProductById(productId).onSuccess { product ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    product = product
                )
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = error.message ?: "Failed to load product"
                )
            }
        }
    }

    fun loadReviews() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoadingReviews = true)

            repository.getReviewsByProductId(
                productId = productId,
                filterByRating = _uiState.value.filterByRating,
                sortBy = _uiState.value.sortBy
            ).onSuccess { reviews ->
                _uiState.value = _uiState.value.copy(
                    isLoadingReviews = false,
                    reviews = reviews
                )
            }.onFailure {
                _uiState.value = _uiState.value.copy(
                    isLoadingReviews = false
                )
            }
        }
    }

    fun filterReviews(rating: Int?) {
        _uiState.value = _uiState.value.copy(filterByRating = rating)
        loadReviews()
    }

    fun sortReviews(sortBy: String) {
        _uiState.value = _uiState.value.copy(sortBy = sortBy)
        loadReviews()
    }

    fun submitReview(rating: Int, comment: String, imageUrls: List<String> = emptyList()) {
        if (rating < 1 || rating > 5) {
            _uiState.value = _uiState.value.copy(
                reviewSubmitError = "Please select a rating"
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isSubmittingReview = true,
                reviewSubmitError = null,
                reviewSubmitSuccess = false
            )

            // Get Firebase UID of current logged-in user
            val currentUserId = authRepository.getCurrentFirebaseUid() ?: ""
            
            if (currentUserId.isEmpty()) {
                _uiState.value = _uiState.value.copy(
                    isSubmittingReview = false,
                    reviewSubmitError = "Vui lòng đăng nhập để đánh giá"
                )
                return@launch
            }

            repository.createReview(
                productId = productId,
                userId = currentUserId,
                rating = rating,
                comment = comment.ifBlank { null },
                imageUrls = imageUrls.ifEmpty { null }
            ).onSuccess {
                _uiState.value = _uiState.value.copy(
                    isSubmittingReview = false,
                    reviewSubmitSuccess = true,
                    showWriteReviewScreen = false // Close screen on success
                )
                // Reload reviews and product to update stats
                loadReviews()
                loadProduct()
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isSubmittingReview = false,
                    reviewSubmitError = error.message ?: "Failed to submit review"
                )
            }
        }
    }

    fun toggleWriteReviewScreen(show: Boolean) {
        _uiState.value = _uiState.value.copy(showWriteReviewScreen = show)
        if (!show) clearReviewSubmitState()
    }

    fun clearReviewSubmitState() {
        _uiState.value = _uiState.value.copy(
            reviewSubmitSuccess = false,
            reviewSubmitError = null
        )
    }

    fun selectTab(tabIndex: Int) {
        _uiState.value = _uiState.value.copy(selectedTab = tabIndex)
    }

    fun increaseQuantity() {
        _uiState.value = _uiState.value.copy(quantity = _uiState.value.quantity + 1)
    }

    fun decreaseQuantity() {
        if (_uiState.value.quantity > 1) {
            _uiState.value = _uiState.value.copy(quantity = _uiState.value.quantity - 1)
        }
    }

    fun addToCart(userId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                addToCartError = null,
                addToCartSuccess = false
            )
            try {
                // If user is guest (empty userId)
                if (userId.isEmpty()) {
                    // Add to local guest cart
                    val product = _uiState.value.product
                    if (product != null) {
                        try {
                            guestCartRepository.addToGuestCart(
                                productId = productId,
                                productName = product.name,
                                quantity = _uiState.value.quantity,
                                price = product.price.toDouble(),
                                imageUrl = product.images?.firstOrNull()?.imageUrl
                            )
                            _uiState.value = _uiState.value.copy(
                                addToCartSuccess = true,
                                quantity = 1  // Reset quantity after adding
                            )
                        } catch (e: Exception) {
                            _uiState.value = _uiState.value.copy(
                                addToCartError = "Lỗi thêm vào giỏ hàng: ${e.message}"
                            )
                        }
                    } else {
                        _uiState.value = _uiState.value.copy(
                            addToCartError = "Không thể tải thông tin sản phẩm"
                        )
                    }
                } else {
                    // Add to server cart for logged-in user
                    val request = AddToCartRequest(
                        userId = userId,
                        productId = productId,
                        quantity = _uiState.value.quantity
                    )
                    val response = apiService.addToCart(request)
                    if (response.isSuccessful) {
                        _uiState.value = _uiState.value.copy(
                            addToCartSuccess = true,
                            quantity = 1  // Reset quantity after adding
                        )
                    } else {
                        _uiState.value = _uiState.value.copy(
                            addToCartError = response.message() ?: "Không thể thêm vào giỏ hàng"
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    addToCartError = e.message ?: "Có lỗi xảy ra"
                )
            }
        }
    }

    fun clearAddToCartState() {
        _uiState.value = _uiState.value.copy(
            addToCartSuccess = false,
            addToCartError = null
        )
    }

    fun uploadImage(file: java.io.File, onResult: (Result<String>) -> Unit) {
        viewModelScope.launch {
            val result = repository.uploadImage(file)
            onResult(result)
        }
    }

    fun retry() {
        loadProduct()
        loadReviews()
    }
}





