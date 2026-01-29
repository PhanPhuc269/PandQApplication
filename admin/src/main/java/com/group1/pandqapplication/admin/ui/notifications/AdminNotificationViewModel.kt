package com.group1.pandqapplication.admin.ui.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group1.pandqapplication.admin.data.remote.AdminApiService
import com.group1.pandqapplication.admin.data.remote.dto.AdminNotificationItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class NotificationUiState {
    object Loading : NotificationUiState()
    data class Success(val notifications: List<AdminNotificationItem>) : NotificationUiState()
    data class Error(val message: String) : NotificationUiState()
}

@HiltViewModel
class AdminNotificationViewModel @Inject constructor(
    private val apiService: AdminApiService
) : ViewModel() {

    private val _uiState = MutableStateFlow<NotificationUiState>(NotificationUiState.Loading)
    val uiState: StateFlow<NotificationUiState> = _uiState.asStateFlow()
    
    // Navigation event for deep linking
    private val _navigationEvent = MutableSharedFlow<String?>()
    val navigationEvent: SharedFlow<String?> = _navigationEvent.asSharedFlow()

    init {
        loadNotifications()
    }

    fun loadNotifications() {
        viewModelScope.launch {
            _uiState.value = NotificationUiState.Loading
            try {
                val response = apiService.getAdminNotifications()
                _uiState.value = NotificationUiState.Success(response.content)
            } catch (e: Exception) {
                _uiState.value = NotificationUiState.Error(e.message ?: "Failed to load notifications")
            }
        }
    }
    
    fun onNotificationClick(notification: AdminNotificationItem) {
        android.util.Log.d("AdminNotification", "Clicked notification: ${notification.id} - targetData: ${notification.targetData}")
        
        // Emit navigation event
        viewModelScope.launch {
            _navigationEvent.emit(notification.targetData)
        }
    }
}
