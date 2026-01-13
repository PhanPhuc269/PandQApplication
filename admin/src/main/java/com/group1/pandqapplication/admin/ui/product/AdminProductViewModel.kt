package com.group1.pandqapplication.admin.ui.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group1.pandqapplication.admin.data.repository.ProductRepository
import com.group1.pandqapplication.shared.data.remote.dto.CategoryDto
import com.group1.pandqapplication.shared.data.remote.dto.CreateProductRequest

import com.group1.pandqapplication.shared.data.remote.dto.ProductDetailDto
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
    val filteredProducts: List<ProductDto> = emptyList(), // Store filtered list separately
    val categories: List<CategoryDto> = emptyList(),
    val currentProduct: ProductDetailDto? = null,
    val error: String? = null,
    val operationSuccess: Boolean = false,
    val operationError: String? = null,
    val searchQuery: String = "",
    val filterStatus: String = "ALL" // ALL, IN_STOCK, LOW_STOCK, OUT_OF_STOCK
)


@HiltViewModel
class AdminProductViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminProductUiState())
    val uiState: StateFlow<AdminProductUiState> = _uiState.asStateFlow()

    init {
        loadProducts()
        loadCategories()
    }

    private fun updateFilteredList() {
        val currentList = _uiState.value.products
        val query = _uiState.value.searchQuery.trim().lowercase()
        val status = _uiState.value.filterStatus

        val filtered = currentList.filter { product ->
            val matchesSearch = product.name.lowercase().contains(query) // Simple name search
            val matchesStatus = when (status) {
                "ALL" -> true
                "IN_STOCK" -> product.status == "IN_STOCK" || product.status == "ACTIVE" // Handle backend statuses
                "LOW_STOCK" -> product.status == "LOW_STOCK"
                "OUT_OF_STOCK" -> product.status == "OUT_OF_STOCK" || product.status == "INACTIVE"
                else -> true
            }
            matchesSearch && matchesStatus
        }

        _uiState.value = _uiState.value.copy(filteredProducts = filtered)
    }
    
    fun onSearchQueryChanged(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
        updateFilteredList()
    }
    
    fun onFilterStatusChanged(status: String) {
        _uiState.value = _uiState.value.copy(filterStatus = status)
        updateFilteredList()
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
                    updateFilteredList() // Update filtered list after loading
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message ?: "Failed to load products"
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

    fun createProduct(
        categoryId: String,
        name: String,
        description: String,
        price: Double,
        costPrice: Double?,
        thumbnailUrl: String,
        status: String,
        images: List<String>,
        specifications: List<ProductSpecificationDto>,
        stock: String?
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
                specifications = specifications,
                stockQuantity = stock?.toIntOrNull()
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
        specifications: List<ProductSpecificationDto>,
        stock: String? = null
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
                specifications = specifications,
                stockQuantity = stock?.toIntOrNull()
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
                    // Update list locally for instant feedback
                    val updatedList = _uiState.value.products.filter { it.id != id }
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        operationSuccess = true,
                        products = updatedList
                    )
                    updateFilteredList() // Re-apply filters
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        operationError = error.message ?: "Failed to delete product"
                    )
                }
        }
    }


    fun loadCategories() {
        viewModelScope.launch {
            // Don't show global loading for categories, just fetch silently or handle error separately
            try {
                val categories = repository.getCategories()
                _uiState.value = _uiState.value.copy(categories = categories)
            } catch (e: Exception) {
                // Log or handle error if needed
                e.printStackTrace()
            }
        }
    }


    fun uploadImage(file: java.io.File, onResult: (String?) -> Unit) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            // Use CloudinaryHelper directly
            com.group1.pandqapplication.admin.util.CloudinaryHelper.uploadImage(file)
                .onSuccess { url ->
                    _uiState.value = _uiState.value.copy(isLoading = false)
                    onResult(url)
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        operationError = error.message ?: "Failed to upload image"
                    )
                    onResult(null)
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
