package com.group1.pandqapplication.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group1.pandqapplication.data.repository.HomeRepository
import com.group1.pandqapplication.shared.data.remote.dto.CategoryDto
import com.group1.pandqapplication.shared.data.remote.dto.ProductDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val isLoading: Boolean = true,
    val isLoadingMore: Boolean = false,
    val categories: List<CategoryDto> = emptyList(),
    val products: List<ProductDto> = emptyList(),
    val selectedCategoryId: String? = null,
    val selectedCategoryName: String? = null,
    val currentPage: Int = 0,
    val hasMore: Boolean = true,
    val error: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: HomeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    companion object {
        private const val PAGE_SIZE = 10
    }

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            // Load categories
            val categoriesResult = repository.getCategories()
            val categories = categoriesResult.getOrNull() ?: emptyList()

            // Load first page of products
            val productsResult = repository.searchProducts(
                page = 0,
                size = PAGE_SIZE,
                categoryId = _uiState.value.selectedCategoryId
            )

            val products = productsResult.getOrNull()?.data ?: emptyList()
            val hasMore = productsResult.getOrNull()?.meta?.pagination?.hasMore ?: false

            val error = when {
                categoriesResult.isFailure -> categoriesResult.exceptionOrNull()?.message
                productsResult.isFailure -> productsResult.exceptionOrNull()?.message
                else -> null
            }

            _uiState.value = _uiState.value.copy(
                isLoading = false,
                categories = categories,
                products = products,
                currentPage = 0,
                hasMore = hasMore,
                error = error
            )
        }
    }

    fun loadMoreProducts() {
        val state = _uiState.value
        
        // Don't load if already loading or no more data
        if (state.isLoadingMore || !state.hasMore) return

        viewModelScope.launch {
            _uiState.value = state.copy(isLoadingMore = true)

            val nextPage = state.currentPage + 1
            val result = repository.searchProducts(
                page = nextPage,
                size = PAGE_SIZE,
                categoryId = state.selectedCategoryId
            )

            result.onSuccess { response ->
                _uiState.value = _uiState.value.copy(
                    isLoadingMore = false,
                    products = state.products + response.data,
                    currentPage = nextPage,
                    hasMore = response.meta?.pagination?.hasMore ?: false
                )
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isLoadingMore = false,
                    error = error.message
                )
            }
        }
    }

    fun selectCategory(categoryId: String, categoryName: String) {
        _uiState.value = _uiState.value.copy(
            selectedCategoryId = categoryId,
            selectedCategoryName = categoryName,
            products = emptyList(),
            currentPage = 0,
            hasMore = true
        )
        // Reload products with category filter
        loadProductsForCategory(categoryId)
    }

    private fun loadProductsForCategory(categoryId: String?) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            val result = repository.searchProducts(
                page = 0,
                size = PAGE_SIZE,
                categoryId = categoryId
            )

            result.onSuccess { response ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    products = response.data,
                    currentPage = 0,
                    hasMore = response.meta?.pagination?.hasMore ?: false
                )
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = error.message
                )
            }
        }
    }

    fun clearCategoryFilter() {
        _uiState.value = _uiState.value.copy(
            selectedCategoryId = null,
            selectedCategoryName = null,
            products = emptyList(),
            currentPage = 0,
            hasMore = true
        )
        // Reload all products
        loadProductsForCategory(null)
    }

    fun refresh() {
        _uiState.value = _uiState.value.copy(
            products = emptyList(),
            currentPage = 0,
            hasMore = true
        )
        loadData()
    }
}
