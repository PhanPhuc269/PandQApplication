package com.group1.pandqapplication.ui.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group1.pandqapplication.shared.data.remote.dto.NotificationDto
import com.group1.pandqapplication.shared.data.repository.AuthRepository
import com.group1.pandqapplication.shared.data.repository.NotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class NotificationUiState(
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val notifications: List<NotificationDto> = emptyList(),
    val errorMessage: String? = null,
    val selectedFilter: String = "All", // All, Orders, Promos
    val hasMore: Boolean = true,
    val currentPage: Int = 0
)

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(NotificationUiState())
    val uiState: StateFlow<NotificationUiState> = _uiState.asStateFlow()

    private var currentUserEmail: String? = null

    init {
        loadNotifications()
    }

    fun loadNotifications() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, currentPage = 0, hasMore = true) }

            try {
                val email = authRepository.getCurrentUserEmail()
                if (email.isNullOrEmpty()) {
                    _uiState.update { 
                        it.copy(isLoading = false, errorMessage = "Chưa đăng nhập") 
                    }
                    return@launch
                }

                currentUserEmail = email
                val result = notificationRepository.getNotificationsByEmail(email)

                result.fold(
                    onSuccess = { notifications ->
                        _uiState.update { 
                            it.copy(
                                isLoading = false, 
                                notifications = notifications,
                                hasMore = false // Backend currently returns all, no pagination yet
                            ) 
                        }
                    },
                    onFailure = { error ->
                        _uiState.update { 
                            it.copy(isLoading = false, errorMessage = error.message) 
                        }
                    }
                )
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(isLoading = false, errorMessage = e.message) 
                }
            }
        }
    }

    fun loadMore() {
        val state = _uiState.value
        if (state.isLoading || state.isLoadingMore || !state.hasMore) return
        
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingMore = true) }
            
            // TODO: Implement actual pagination when backend supports it
            // For now, just mark as no more items
            _uiState.update { it.copy(isLoadingMore = false, hasMore = false) }
        }
    }

    fun markAsRead(notificationId: String) {
        viewModelScope.launch {
            val result = notificationRepository.markAsRead(notificationId)
            result.onSuccess {
                _uiState.update { state ->
                    state.copy(
                        notifications = state.notifications.map { notification ->
                            if (notification.id == notificationId) {
                                notification.copy(isRead = true)
                            } else {
                                notification
                            }
                        }
                    )
                }
            }
        }
    }

    fun markAllAsRead() {
        viewModelScope.launch {
            _uiState.value.notifications
                .filter { !it.isRead }
                .forEach { notification ->
                    notificationRepository.markAsRead(notification.id)
                }
            
            _uiState.update { state ->
                state.copy(
                    notifications = state.notifications.map { it.copy(isRead = true) }
                )
            }
        }
    }

    fun setFilter(filter: String) {
        _uiState.update { it.copy(selectedFilter = filter) }
    }

    fun getFilteredNotifications(): List<NotificationDto> {
        val state = _uiState.value
        return when (state.selectedFilter) {
            "Orders" -> state.notifications.filter { it.type == "ORDER_UPDATE" }
            "Promos" -> state.notifications.filter { it.type == "PROMOTION" }
            else -> state.notifications
        }
    }

    fun deleteNotification(notificationId: String) {
        viewModelScope.launch {
            // Remove from local state immediately for instant UI feedback
            _uiState.update { state ->
                state.copy(
                    notifications = state.notifications.filter { it.id != notificationId }
                )
            }
            // TODO: Call backend delete API when available
            // notificationRepository.deleteNotification(notificationId)
        }
    }

    fun refresh() {
        loadNotifications()
    }
}

