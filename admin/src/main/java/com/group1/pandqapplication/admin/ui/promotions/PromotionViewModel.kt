package com.group1.pandqapplication.admin.ui.promotions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group1.pandqapplication.admin.data.remote.dto.*
import com.group1.pandqapplication.admin.data.repository.PromotionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

/**
 * UI State cho màn hình danh sách khuyến mãi
 */
data class PromotionsUiState(
    val isLoading: Boolean = false,
    val promotions: List<PromotionDto> = emptyList(),
    val error: String? = null,
    val selectedFilter: PromotionFilter = PromotionFilter.ALL,
    val searchQuery: String = ""
)

enum class PromotionFilter {
    ALL, ACTIVE, SCHEDULED, EXPIRED
}

/**
 * UI State cho màn hình tạo/sửa khuyến mãi
 */
data class CreatePromotionUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null,
    
    // Edit mode
    val editingPromotionId: String? = null,
    val isEditMode: Boolean = false,
    
    // Form fields
    val name: String = "",
    val code: String = "",
    val description: String = "",
    val type: DiscountType = DiscountType.PERCENTAGE,
    val value: String = "",
    val maxDiscountAmount: String = "",
    val minOrderValue: String = "",
    val startDate: String = "",
    val endDate: String = "",
    val quantityLimit: String = "",
    val isActive: Boolean = true
)

@HiltViewModel
class PromotionViewModel @Inject constructor(
    private val repository: PromotionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PromotionsUiState())
    val uiState: StateFlow<PromotionsUiState> = _uiState.asStateFlow()

    private val _createState = MutableStateFlow(CreatePromotionUiState())
    val createState: StateFlow<CreatePromotionUiState> = _createState.asStateFlow()

    init {
        loadPromotions()
    }

    /**
     * Load danh sách khuyến mãi từ API
     */
    fun loadPromotions() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            repository.getPromotions().collect { result ->
                result.fold(
                    onSuccess = { promotions ->
                        _uiState.update { 
                            it.copy(
                                isLoading = false, 
                                promotions = promotions,
                                error = null
                            ) 
                        }
                    },
                    onFailure = { error ->
                        _uiState.update { 
                            it.copy(
                                isLoading = false, 
                                error = error.message ?: "Lỗi không xác định"
                            ) 
                        }
                    }
                )
            }
        }
    }

    /**
     * Lọc khuyến mãi theo trạng thái
     */
    fun setFilter(filter: PromotionFilter) {
        _uiState.update { it.copy(selectedFilter = filter) }
    }

    /**
     * Tìm kiếm khuyến mãi
     */
    fun setSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    /**
     * Lấy danh sách đã lọc
     */
    fun getFilteredPromotions(): List<PromotionDto> {
        val state = _uiState.value
        var filtered = state.promotions

        // Lọc theo trạng thái
        filtered = when (state.selectedFilter) {
            PromotionFilter.ALL -> filtered
            PromotionFilter.ACTIVE -> filtered.filter { 
                // Status phải là ACTIVE và không phải scheduled (startDate không trong tương lai)
                it.status == com.group1.pandqapplication.admin.data.remote.dto.PromotionStatus.ACTIVE &&
                    (it.startDate == null || !isScheduled(it.startDate))
            }
            PromotionFilter.SCHEDULED -> filtered.filter { 
                // Scheduled = startDate trong tương lai (bất kể status)
                it.startDate != null && isScheduled(it.startDate) 
            }
            PromotionFilter.EXPIRED -> filtered.filter { 
                // Expired = endDate đã qua hoặc status INACTIVE
                (it.endDate != null && isExpired(it.endDate)) ||
                    it.status == com.group1.pandqapplication.admin.data.remote.dto.PromotionStatus.INACTIVE
            }
        }

        // Lọc theo search query
        if (state.searchQuery.isNotBlank()) {
            val query = state.searchQuery.lowercase()
            filtered = filtered.filter {
                it.name.lowercase().contains(query) ||
                it.code.lowercase().contains(query)
            }
        }

        return filtered
    }

    private fun isExpired(endDate: String): Boolean {
        return try {
            val date = LocalDateTime.parse(endDate)
            date.isBefore(LocalDateTime.now())
        } catch (e: Exception) {
            false
        }
    }

    private fun isScheduled(startDate: String): Boolean {
        return try {
            val date = LocalDateTime.parse(startDate)
            date.isAfter(LocalDateTime.now())
        } catch (e: Exception) {
            false
        }
    }

    // ==================== Create Promotion ====================

    fun updateName(name: String) {
        _createState.update { it.copy(name = name) }
    }

    fun updateCode(code: String) {
        _createState.update { it.copy(code = code.uppercase()) }
    }

    fun updateDescription(description: String) {
        _createState.update { it.copy(description = description) }
    }

    fun updateType(type: DiscountType) {
        _createState.update { it.copy(type = type) }
    }

    fun updateValue(value: String) {
        _createState.update { it.copy(value = value) }
    }

    fun updateMaxDiscountAmount(amount: String) {
        _createState.update { it.copy(maxDiscountAmount = amount) }
    }

    fun updateMinOrderValue(value: String) {
        _createState.update { it.copy(minOrderValue = value) }
    }

    fun updateStartDate(date: String) {
        _createState.update { it.copy(startDate = date) }
    }

    fun updateEndDate(date: String) {
        _createState.update { it.copy(endDate = date) }
    }

    fun updateQuantityLimit(limit: String) {
        _createState.update { it.copy(quantityLimit = limit) }
    }

    fun updateIsActive(isActive: Boolean) {
        _createState.update { it.copy(isActive = isActive) }
    }

    /**
     * Tạo khuyến mãi mới
     */
    fun createPromotion() {
        val state = _createState.value
        
        // Validate
        if (state.name.isBlank() || state.code.isBlank()) {
            _createState.update { it.copy(error = "Vui lòng nhập tên và mã khuyến mãi") }
            return
        }

        viewModelScope.launch {
            _createState.update { it.copy(isLoading = true, error = null) }

            val request = CreatePromotionRequest(
                code = state.code,
                name = state.name,
                description = state.description.ifBlank { null },
                type = state.type,
                value = state.value.toBigDecimalOrNull(),
                maxDiscountAmount = state.maxDiscountAmount.toBigDecimalOrNull(),
                minOrderValue = state.minOrderValue.toBigDecimalOrNull(),
                startDate = state.startDate.ifBlank { null },
                endDate = state.endDate.ifBlank { null },
                quantityLimit = state.quantityLimit.toIntOrNull()
            )

            repository.createPromotion(request).collect { result ->
                result.fold(
                    onSuccess = {
                        // Refresh list first, wait a moment for it to complete
                        loadPromotions()
                        kotlinx.coroutines.delay(300) // Wait for list to refresh
                        _createState.update { it.copy(isLoading = false, isSuccess = true) }
                    },
                    onFailure = { error ->
                        _createState.update { 
                            it.copy(
                                isLoading = false, 
                                error = error.message ?: "Không thể tạo khuyến mãi"
                            ) 
                        }
                    }
                )
            }
        }
    }

    /**
     * Xóa khuyến mãi
     */
    fun deletePromotion(id: String) {
        viewModelScope.launch {
            repository.deletePromotion(id).collect { result ->
                result.fold(
                    onSuccess = { loadPromotions() },
                    onFailure = { error ->
                        _uiState.update { 
                            it.copy(error = error.message ?: "Không thể xóa khuyến mãi") 
                        }
                    }
                )
            }
        }
    }

    /**
     * Load promotion để edit
     */
    fun loadPromotionById(id: String) {
        viewModelScope.launch {
            _createState.update { it.copy(isLoading = true, error = null) }
            
            repository.getPromotionById(id).collect { result ->
                result.fold(
                    onSuccess = { promotion ->
                        _createState.update {
                            it.copy(
                                isLoading = false,
                                isEditMode = true,
                                editingPromotionId = promotion.id,
                                name = promotion.name,
                                code = promotion.code,
                                description = promotion.description ?: "",
                                type = promotion.type,
                                value = promotion.value?.toString() ?: "",
                                maxDiscountAmount = promotion.maxDiscountAmount?.toString() ?: "",
                                minOrderValue = promotion.minOrderValue?.toString() ?: "",
                                startDate = promotion.startDate ?: "",
                                endDate = promotion.endDate ?: "",
                                quantityLimit = promotion.quantityLimit?.toString() ?: "",
                                isActive = promotion.status == com.group1.pandqapplication.admin.data.remote.dto.PromotionStatus.ACTIVE
                            )
                        }
                    },
                    onFailure = { error ->
                        _createState.update {
                            it.copy(
                                isLoading = false,
                                error = error.message ?: "Không thể load khuyến mãi"
                            )
                        }
                    }
                )
            }
        }
    }

    /**
     * Cập nhật khuyến mãi
     */
    fun updatePromotion() {
        val state = _createState.value
        val id = state.editingPromotionId ?: return
        
        // Validate
        if (state.name.isBlank()) {
            _createState.update { it.copy(error = "Vui lòng nhập tên khuyến mãi") }
            return
        }

        viewModelScope.launch {
            _createState.update { it.copy(isLoading = true, error = null) }

            val request = UpdatePromotionRequest(
                name = state.name,
                description = state.description.ifBlank { null },
                value = state.value.toBigDecimalOrNull(),
                maxDiscountAmount = state.maxDiscountAmount.toBigDecimalOrNull(),
                minOrderValue = state.minOrderValue.toBigDecimalOrNull(),
                startDate = state.startDate.ifBlank { null },
                endDate = state.endDate.ifBlank { null },
                quantityLimit = state.quantityLimit.toIntOrNull(),
                status = if (state.isActive) com.group1.pandqapplication.admin.data.remote.dto.PromotionStatus.ACTIVE else com.group1.pandqapplication.admin.data.remote.dto.PromotionStatus.INACTIVE
            )

            repository.updatePromotion(id, request).collect { result ->
                result.fold(
                    onSuccess = {
                        loadPromotions()
                        kotlinx.coroutines.delay(300)
                        _createState.update { it.copy(isLoading = false, isSuccess = true) }
                    },
                    onFailure = { error ->
                        _createState.update {
                            it.copy(
                                isLoading = false,
                                error = error.message ?: "Không thể cập nhật khuyến mãi"
                            )
                        }
                    }
                )
            }
        }
    }

    /**
     * Save promotion - gọi create hoặc update tùy mode
     */
    fun savePromotion() {
        if (_createState.value.isEditMode) {
            updatePromotion()
        } else {
            createPromotion()
        }
    }

    /**
     * Reset form tạo khuyến mãi
     */
    fun resetCreateState() {
        _createState.value = CreatePromotionUiState()
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
        _createState.update { it.copy(error = null) }
    }
}
