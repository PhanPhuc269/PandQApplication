package com.group1.pandqapplication.admin.ui.notification

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group1.pandqapplication.admin.data.remote.dto.CreateNotificationTemplateRequest
import com.group1.pandqapplication.admin.data.remote.dto.NotificationTemplateDto
import com.group1.pandqapplication.admin.data.remote.AdminApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class NotificationTemplateUiState(
    val isLoading: Boolean = false,
    val templates: List<NotificationTemplateDto> = emptyList(),
    val error: String? = null,
    val successMessage: String? = null
)

@HiltViewModel
class NotificationTemplateViewModel @Inject constructor(
    private val adminApiService: AdminApiService
) : ViewModel() {

    private val _uiState = MutableStateFlow(NotificationTemplateUiState())
    val uiState: StateFlow<NotificationTemplateUiState> = _uiState.asStateFlow()

    init {
        loadTemplates()
    }

    fun loadTemplates() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val templates = adminApiService.getNotificationTemplates()
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    templates = templates
                )
            } catch (e: Exception) {
                Log.e("NotificationTemplateVM", "Failed to load templates", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Không thể tải danh sách: ${e.message}"
                )
            }
        }
    }

    fun toggleActive(templateId: String) {
        viewModelScope.launch {
            try {
                val updated = adminApiService.toggleNotificationTemplate(templateId)
                _uiState.value = _uiState.value.copy(
                    templates = _uiState.value.templates.map { 
                        if (it.id == templateId) updated else it 
                    },
                    successMessage = if (updated.isActive) "Đã kích hoạt" else "Đã tắt"
                )
            } catch (e: Exception) {
                Log.e("NotificationTemplateVM", "Failed to toggle", e)
                _uiState.value = _uiState.value.copy(error = "Lỗi: ${e.message}")
            }
        }
    }

    fun sendNotification(templateId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val updated = adminApiService.sendNotificationTemplate(templateId)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    templates = _uiState.value.templates.map { 
                        if (it.id == templateId) updated else it 
                    },
                    successMessage = "Đã gửi thông báo đến tất cả users!"
                )
            } catch (e: Exception) {
                Log.e("NotificationTemplateVM", "Failed to send", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Lỗi gửi: ${e.message}"
                )
            }
        }
    }

    fun createTemplate(title: String, body: String, type: String = "SYSTEM") {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val request = CreateNotificationTemplateRequest(
                    title = title,
                    body = body,
                    type = type
                )
                adminApiService.createNotificationTemplate(request)
                loadTemplates()
                _uiState.value = _uiState.value.copy(successMessage = "Đã tạo mẫu thông báo!")
            } catch (e: Exception) {
                Log.e("NotificationTemplateVM", "Failed to create", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Lỗi tạo: ${e.message}"
                )
            }
        }
    }

    fun deleteTemplate(templateId: String) {
        viewModelScope.launch {
            try {
                adminApiService.deleteNotificationTemplate(templateId)
                _uiState.value = _uiState.value.copy(
                    templates = _uiState.value.templates.filter { it.id != templateId },
                    successMessage = "Đã xóa!"
                )
            } catch (e: Exception) {
                Log.e("NotificationTemplateVM", "Failed to delete", e)
                _uiState.value = _uiState.value.copy(error = "Lỗi xóa: ${e.message}")
            }
        }
    }

    fun clearMessage() {
        _uiState.value = _uiState.value.copy(successMessage = null, error = null)
    }
}
