package com.group1.pandqapplication.ui.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group1.pandqapplication.shared.data.remote.dto.NotificationDto
import com.group1.pandqapplication.shared.data.repository.AuthRepository
import com.group1.pandqapplication.shared.data.repository.NotificationRepository
import com.group1.pandqapplication.shared.data.repository.UserRepository
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
    val selectedFilter: String = "All", // All, Orders, Promos, System
    val hasMore: Boolean = true,
    val currentPage: Int = 0,
    val preferences: com.group1.pandqapplication.shared.data.remote.dto.NotificationPreferenceResponse? = null
)

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository,
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(NotificationUiState())
    val uiState: StateFlow<NotificationUiState> = _uiState.asStateFlow()

    private var currentUserEmail: String? = null
    private var currentUserId: String? = null

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val email = authRepository.getCurrentUserEmail()
                if (email.isNullOrEmpty()) {
                    _uiState.update { it.copy(isLoading = false, errorMessage = "Chưa đăng nhập") }
                    return@launch
                }
                currentUserEmail = email

                // Fetch User ID
                val userResult = userRepository.getUserByEmail(email)
                userResult.onSuccess { user ->
                    currentUserId = user.id
                    loadPreferences(user.id)
                }.onFailure {
                    // Log or handle error getting user ID
                }

                // Fetch Notifications
                // We use getNotificationsByEmail for convenience as it handles finding user by email on backend too
                val notifResult = notificationRepository.getNotificationsByEmail(email)
                
                notifResult.fold(
                    onSuccess = { notifications ->
                        _uiState.update { 
                            it.copy(
                                isLoading = false, 
                                notifications = notifications,
                                hasMore = false
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
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
            }
        }
    }

    fun loadPreferences(userId: String) {
        viewModelScope.launch {
            val result = notificationRepository.getPreferences(userId)
            result.onSuccess { prefs ->
                _uiState.update { it.copy(preferences = prefs) }
            }
        }
    }

    fun updatePreference(request: com.group1.pandqapplication.shared.data.remote.dto.NotificationPreferenceRequest) {
        val userId = currentUserId ?: return
        viewModelScope.launch {
            // Optimistic update locally? 
            // Better to wait for server response or just fire and forget but reload.
            val result = notificationRepository.updatePreferences(userId, request)
            if (result.isSuccess) {
                loadPreferences(userId)
            }
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
        // If we wanted to fetch from server based on filter, we would call loadNotifications(type) here.
        // But for now client side filtering is sufficient and faster.
    }

    fun getFilteredNotifications(): List<NotificationDto> {
        val state = _uiState.value
        return when (state.selectedFilter) {
            "Orders" -> state.notifications.filter { it.type == "ORDER_UPDATE" || it.type == "PAYMENT_SUCCESS" }
            "Promos" -> state.notifications.filter { it.type == "PROMOTION" }
            "Chats" -> state.notifications.filter { it.type == "CHAT_MESSAGE" }
            else -> state.notifications
        }
    }
    
    fun refresh() {
        loadData()
    }
    
    fun deleteNotification(notificationId: String) {
         viewModelScope.launch {
            _uiState.update { state ->
                state.copy(
                    notifications = state.notifications.filter { it.id != notificationId }
                )
            }
        }
    }
}
