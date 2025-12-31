package com.group1.pandqapplication.ui.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group1.pandqapplication.data.repository.ProductRepository
import com.group1.pandqapplication.shared.data.remote.dto.ProductDetailDto
import com.group1.pandqapplication.shared.data.remote.dto.CreateProductRequest
import com.group1.pandqapplication.shared.data.remote.dto.ProductDto

import com.group1.pandqapplication.shared.data.remote.dto.ProductSpecificationDto
import com.group1.pandqapplication.shared.data.remote.dto.UpdateProductRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AdminProductUiState(
    val isLoading: Boolean = false,
    val products: List<ProductDto> = emptyList(),
    val currentProduct: ProductDetailDto? = null,
    val error: String? = null,
    val operationSuccess: Boolean = false,

    val operationError: String? = null
)

@HiltViewModel
class AdminProductViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminProductUiState())
    val uiState: StateFlow<AdminProductUiState> = _uiState.asStateFlow()

    init {
        loadProducts()
    }

    fun loadProducts() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            repository.getAllProducts()
                .onSuccess { products ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        products = products
                    )
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message ?: "Failed to load products"
                    )
                }
        }
    }

    fun createProduct(
        categoryId: String,
        name: String,
        description: String,
        price: Double,
        costPrice: Double?,
        thumbnailUrl: String,
        status: String,
        images: List<String>,
        specifications: List<ProductSpecificationDto>
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, operationSuccess = false, operationError = null)
            
            val request = CreateProductRequest(
                categoryId = categoryId,
                name = name,
                description = description,
                price = price,
                costPrice = costPrice,
                thumbnailUrl = thumbnailUrl,
                status = status,
                images = images,
                specifications = specifications
            )

            repository.createProduct(request)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        operationSuccess = true
                    )
                    loadProducts() // Reload list
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        operationError = error.message ?: "Failed to create product"
                    )
                }
        }
    }

    fun updateProduct(
        id: String,
        categoryId: String,
        name: String,
        description: String,
        price: Double,
        costPrice: Double?,
        thumbnailUrl: String,
        status: String,
        images: List<String>,
        specifications: List<ProductSpecificationDto>
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, operationSuccess = false, operationError = null)

            val request = UpdateProductRequest(
                categoryId = categoryId,
                name = name,
                description = description,
                price = price,
                costPrice = costPrice,
                thumbnailUrl = thumbnailUrl,
                status = status,
                images = images,
                specifications = specifications
            )

            repository.updateProduct(id, request)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        operationSuccess = true
                    )
                    loadProducts()
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        operationError = error.message ?: "Failed to update product"
                    )
                }
        }
    }

    fun deleteProduct(id: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, operationSuccess = false, operationError = null)
            
            repository.deleteProduct(id)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        operationSuccess = true
                    )
                    loadProducts()
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        operationError = error.message ?: "Failed to delete product"
                    )
                }
        }
    }

    fun getProduct(id: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null, currentProduct = null)
            repository.getProductById(id)
                .onSuccess { product ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        currentProduct = product
                    )
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message ?: "Failed to load product details"
                    )
                }
        }
    }

    fun clearOperationState() {

        _uiState.value = _uiState.value.copy(
            operationSuccess = false,
            operationError = null
        )
    }
}
