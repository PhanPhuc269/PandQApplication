package com.group1.pandqapplication.ui.order

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group1.pandqapplication.shared.data.remote.dto.OrderDto
import com.group1.pandqapplication.shared.data.repository.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class OrderSuccessUiState(
    val isLoading: Boolean = true,
    val orderId: String = "",
    val totalAmount: Double = 0.0,
    val error: String? = null
)

@HiltViewModel
class OrderSuccessViewModel @Inject constructor(
    private val orderRepository: OrderRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(OrderSuccessUiState())
    val uiState: StateFlow<OrderSuccessUiState> = _uiState.asStateFlow()

    init {
        val orderId = savedStateHandle.get<String>("orderId") ?: ""
        if (orderId.isNotEmpty()) {
            loadOrderDetails(orderId)
        }
    }

    private fun loadOrderDetails(orderId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            orderRepository.getOrderById(orderId)
                .onSuccess { order ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        orderId = order.id,
                        totalAmount = order.finalAmount,
                        error = null
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        orderId = orderId,
                        error = exception.message
                    )
                }
        }
    }
}
