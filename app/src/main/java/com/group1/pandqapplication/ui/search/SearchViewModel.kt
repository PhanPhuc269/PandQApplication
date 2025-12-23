package com.group1.pandqapplication.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group1.pandqapplication.shared.data.remote.dto.ProductSearchDto
import com.group1.pandqapplication.shared.data.repository.CategoryItem
import com.group1.pandqapplication.shared.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale
import javax.inject.Inject

data class SearchUiState(
    val searchQuery: String = "",
    val products: List<SearchProduct> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val errorMessage: String? = null,
    val showFilterSheet: Boolean = false,
    val activeFilters: List<String> = emptyList(),
    val favoriteProductNames: Set<String> = emptySet(),
    val cartProductNames: Set<String> = emptySet(),
    // Categories
    val categories: List<CategoryItem> = emptyList(),
    val selectedCategoryId: String? = null,
    // Filter sheet state
    val priceRange: ClosedFloatingPointRange<Float> = 0f..50000000f,
    val selectedRatingOption: Int = 3, // 1 = 4.0+, 2 = 3.0+, 3 = Any
    val inStockOnly: Boolean = false,
    // Pagination
    val currentPage: Int = 0,
    val totalPages: Int = 0,
    val totalResults: Long = 0,
    val hasMore: Boolean = true,
    // Sort
    val sortBy: String = "newest"
)

@OptIn(FlowPreview::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private val _searchQueryFlow = MutableStateFlow("")

    init {
        // Load categories
        loadCategories()
        
        // Debounce search query changes
        _searchQueryFlow
            .debounce(500)
            .distinctUntilChanged()
            .onEach { query -> performSearch() }
            .launchIn(viewModelScope)
        
        // Initial load
        performSearch()
    }
    
    private fun loadCategories() {
        viewModelScope.launch {
            try {
                val categories = productRepository.getCategories()
                _uiState.update { it.copy(categories = categories) }
            } catch (e: Exception) {
                // Categories failed to load, continue without them
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        _searchQueryFlow.value = query
    }

    fun onClearSearch() {
        _uiState.update { it.copy(searchQuery = "") }
        _searchQueryFlow.value = ""
    }

    fun onShowFilterSheet() {
        _uiState.update { it.copy(showFilterSheet = true) }
    }

    fun onDismissFilterSheet() {
        _uiState.update { it.copy(showFilterSheet = false) }
    }

    fun onRemoveFilter(index: Int) {
        val state = _uiState.value
        val filterToRemove = state.activeFilters.getOrNull(index) ?: return
        
        // Check what type of filter is being removed and update state accordingly
        val selectedCategory = state.categories.find { it.name == filterToRemove }
        
        _uiState.update { currentState ->
            var newState = currentState.copy(
                activeFilters = currentState.activeFilters.toMutableList().also { it.removeAt(index) }
            )
            
            // If removing a category filter
            if (selectedCategory != null && currentState.selectedCategoryId == selectedCategory.id) {
                newState = newState.copy(selectedCategoryId = null)
            }
            
            // If removing price filter
            if (filterToRemove.endsWith("đ")) {
                newState = newState.copy(priceRange = 0f..50000000f)
            }
            
            // If removing rating filter
            if (filterToRemove == "4 Stars+" || filterToRemove == "3 Stars+") {
                newState = newState.copy(selectedRatingOption = 3)
            }
            
            // If removing in stock filter
            if (filterToRemove == "Còn hàng") {
                newState = newState.copy(inStockOnly = false)
            }
            
            newState
        }
        performSearch()
    }

    fun onClearAllFilters() {
        _uiState.update { 
            it.copy(
                activeFilters = emptyList(),
                priceRange = 0f..50000000f,
                selectedCategoryId = null,
                selectedRatingOption = 3,
                inStockOnly = false
            ) 
        }
        performSearch()
    }

    fun onToggleFavorite(productName: String) {
        _uiState.update { state ->
            val newFavorites = if (state.favoriteProductNames.contains(productName)) {
                state.favoriteProductNames - productName
            } else {
                state.favoriteProductNames + productName
            }
            state.copy(favoriteProductNames = newFavorites)
        }
    }

    fun isFavorite(productName: String): Boolean {
        return _uiState.value.favoriteProductNames.contains(productName)
    }

    fun onToggleCart(productName: String) {
        _uiState.update { state ->
            val newCart = if (state.cartProductNames.contains(productName)) {
                state.cartProductNames - productName
            } else {
                state.cartProductNames + productName
            }
            state.copy(cartProductNames = newCart)
        }
    }

    fun isInCart(productName: String): Boolean {
        return _uiState.value.cartProductNames.contains(productName)
    }

    fun onApplyFilters() {
        _uiState.update { it.copy(showFilterSheet = false) }
        updateActiveFilters()
        performSearch()
    }

    fun onResetFilters() {
        _uiState.update {
            it.copy(
                priceRange = 0f..50000000f,
                selectedCategoryId = null,
                selectedRatingOption = 3,
                inStockOnly = false
            )
        }
    }

    fun onPriceRangeChange(range: ClosedFloatingPointRange<Float>) {
        _uiState.update { it.copy(priceRange = range) }
    }

    fun onCategorySelect(categoryId: String?) {
        _uiState.update { state ->
            val newCategoryId = if (state.selectedCategoryId == categoryId) null else categoryId
            state.copy(selectedCategoryId = newCategoryId)
        }
    }

    fun onRatingOptionChange(option: Int) {
        _uiState.update { it.copy(selectedRatingOption = option) }
    }

    fun onInStockToggle(inStock: Boolean) {
        _uiState.update { it.copy(inStockOnly = inStock) }
    }

    fun onSortChange(sortBy: String) {
        _uiState.update { it.copy(sortBy = sortBy) }
        performSearch()
    }

    private fun updateActiveFilters() {
        val filters = mutableListOf<String>()
        val state = _uiState.value

        // Category filter
        state.selectedCategoryId?.let { categoryId ->
            state.categories.find { it.id == categoryId }?.let { category ->
                filters.add(category.name)
            }
        }

        // Price range filter
        if (state.priceRange.start > 0f || state.priceRange.endInclusive < 50000000f) {
            val formatter = NumberFormat.getInstance(Locale("vi", "VN"))
            filters.add("${formatter.format(state.priceRange.start.toLong())}đ - ${formatter.format(state.priceRange.endInclusive.toLong())}đ")
        }

        // Rating filter
        when (state.selectedRatingOption) {
            1 -> filters.add("4 Stars+")
            2 -> filters.add("3 Stars+")
        }

        // In stock filter
        if (state.inStockOnly) {
            filters.add("Còn hàng")
        }

        _uiState.update { it.copy(activeFilters = filters) }
    }

    private fun performSearch(resetPagination: Boolean = true) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val state = _uiState.value
            val minRating = when (state.selectedRatingOption) {
                1 -> 4.0
                2 -> 3.0
                else -> null
            }

            val result = productRepository.searchProducts(
                query = state.searchQuery.takeIf { it.isNotBlank() },
                categoryId = state.selectedCategoryId,
                minPrice = if (state.priceRange.start > 0f) state.priceRange.start.toDouble() else null,
                maxPrice = if (state.priceRange.endInclusive < 5000f) state.priceRange.endInclusive.toDouble() else null,
                minRating = minRating,
                inStockOnly = state.inStockOnly,
                sortBy = state.sortBy,
                page = if (resetPagination) 0 else state.currentPage,
                size = PAGE_SIZE
            )

            result.fold(
                onSuccess = { response ->
                    val products = response.data.map { it.toSearchProduct() }
                    val pagination = response.meta?.pagination
                    val hasMore = (pagination?.page ?: 0) < (pagination?.totalPages ?: 1) - 1
                    _uiState.update { 
                        it.copy(
                            isLoading = false,
                            products = products,
                            currentPage = 0,
                            totalResults = pagination?.total ?: 0L,
                            totalPages = pagination?.totalPages ?: 0,
                            hasMore = hasMore,
                            errorMessage = null
                        ) 
                    }
                },
                onFailure = { error ->
                    _uiState.update { 
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message ?: "Đã xảy ra lỗi khi tìm kiếm"
                        ) 
                    }
                }
            )
        }
    }

    fun loadMoreProducts() {
        val state = _uiState.value
        
        // Don't load if already loading or no more data
        if (state.isLoading || state.isLoadingMore || !state.hasMore) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingMore = true) }

            val minRating = when (state.selectedRatingOption) {
                1 -> 4.0
                2 -> 3.0
                else -> null
            }

            val nextPage = state.currentPage + 1
            val result = productRepository.searchProducts(
                query = state.searchQuery.takeIf { it.isNotBlank() },
                categoryId = state.selectedCategoryId,
                minPrice = if (state.priceRange.start > 0f) state.priceRange.start.toDouble() else null,
                maxPrice = if (state.priceRange.endInclusive < 5000f) state.priceRange.endInclusive.toDouble() else null,
                minRating = minRating,
                inStockOnly = state.inStockOnly,
                sortBy = state.sortBy,
                page = nextPage,
                size = PAGE_SIZE
            )

            result.fold(
                onSuccess = { response ->
                    val newProducts = response.data.map { it.toSearchProduct() }
                    val pagination = response.meta?.pagination
                    val hasMore = (pagination?.page ?: 0) < (pagination?.totalPages ?: 1) - 1
                    _uiState.update { 
                        it.copy(
                            isLoadingMore = false,
                            products = state.products + newProducts,
                            currentPage = nextPage,
                            hasMore = hasMore
                        ) 
                    }
                },
                onFailure = { error ->
                    _uiState.update { 
                        it.copy(
                            isLoadingMore = false,
                            errorMessage = error.message
                        ) 
                    }
                }
            )
        }
    }

    companion object {
        private const val PAGE_SIZE = 20
    }

    private fun ProductSearchDto.toSearchProduct(): SearchProduct {
        val formatter = NumberFormat.getInstance(Locale("vi", "VN"))
        return SearchProduct(
            name = name,
            price = "${formatter.format(price?.toLong() ?: 0)}đ",
            rating = averageRating ?: 0.0,
            reviews = "(${reviewCount ?: 0})",
            imageUrl = thumbnailUrl ?: "",
            isBestSeller = isBestSeller ?: false,
            isLowStock = (stockQuantity ?: 0) < 10
        )
    }
}
