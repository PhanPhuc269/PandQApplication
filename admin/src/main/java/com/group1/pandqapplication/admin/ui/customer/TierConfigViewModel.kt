package com.group1.pandqapplication.admin.ui.customer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group1.pandqapplication.admin.data.remote.AdminApiService
import com.group1.pandqapplication.admin.data.remote.dto.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TierConfigUiState(
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val configs: List<TierConfigDto> = emptyList(),
    val editingConfigs: Map<CustomerTier, EditableTierConfig> = emptyMap(),
    val error: String? = null,
    val successMessage: String? = null
)

data class EditableTierConfig(
    val tier: CustomerTier,
    val minSpent: String,
    val maxSpent: String,
    val displayName: String,
    val description: String
)

@HiltViewModel
class TierConfigViewModel @Inject constructor(
    private val apiService: AdminApiService
) : ViewModel() {

    private val _uiState = MutableStateFlow(TierConfigUiState())
    val uiState: StateFlow<TierConfigUiState> = _uiState.asStateFlow()

    init {
        loadConfigs()
    }

    fun loadConfigs() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            try {
                val response = apiService.getTierConfigs()
                val editingMap = response.configs.associate { config ->
                    config.tier to EditableTierConfig(
                        tier = config.tier,
                        minSpent = config.minSpent.toLong().toString(),
                        maxSpent = config.maxSpent?.toLong()?.toString() ?: "",
                        displayName = config.displayName ?: "",
                        description = config.description ?: ""
                    )
                }
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        configs = response.configs,
                        editingConfigs = editingMap
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Không thể tải cấu hình"
                    )
                }
            }
        }
    }

    fun updateMinSpent(tier: CustomerTier, value: String) {
        _uiState.update { state ->
            val current = state.editingConfigs[tier] ?: return@update state
            state.copy(
                editingConfigs = state.editingConfigs + (tier to current.copy(minSpent = value))
            )
        }
    }

    fun updateMaxSpent(tier: CustomerTier, value: String) {
        _uiState.update { state ->
            val current = state.editingConfigs[tier] ?: return@update state
            state.copy(
                editingConfigs = state.editingConfigs + (tier to current.copy(maxSpent = value))
            )
        }
    }

    fun saveConfigs() {
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, error = null, successMessage = null) }
            
            try {
                val configs = _uiState.value.editingConfigs.values.map { config ->
                    UpdateTierConfigRequest(
                        tier = config.tier,
                        minSpent = config.minSpent.toDoubleOrNull() ?: 0.0,
                        maxSpent = config.maxSpent.toDoubleOrNull(),
                        displayName = config.displayName.takeIf { it.isNotBlank() },
                        description = config.description.takeIf { it.isNotBlank() }
                    )
                }
                
                apiService.updateTierConfigs(UpdateAllTierConfigsRequest(configs))
                
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        successMessage = "Đã lưu cấu hình thành công!"
                    )
                }
                
                // Reload to get updated data
                loadConfigs()
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        error = e.message ?: "Không thể lưu cấu hình"
                    )
                }
            }
        }
    }

    fun clearMessages() {
        _uiState.update { it.copy(error = null, successMessage = null) }
    }
}
