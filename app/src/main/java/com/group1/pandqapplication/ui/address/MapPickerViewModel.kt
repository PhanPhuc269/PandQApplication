package com.group1.pandqapplication.ui.address

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group1.pandqapplication.shared.data.remote.LocationIQService
import com.group1.pandqapplication.shared.util.LocationIQConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MapPickerUiState(
    val isLoading: Boolean = false,
    val selectedLat: Double? = null,
    val selectedLon: Double? = null,
    val selectedAddress: String = "",
    val searchQuery: String = "",
    val searchResults: List<SearchResult> = emptyList(),
    val isSearching: Boolean = false,
    val error: String? = null
)

data class SearchResult(
    val displayName: String,
    val lat: Double,
    val lon: Double
)

@HiltViewModel
class MapPickerViewModel @Inject constructor(
    private val locationIQService: LocationIQService
) : ViewModel() {

    private val _uiState = MutableStateFlow(MapPickerUiState())
    val uiState: StateFlow<MapPickerUiState> = _uiState.asStateFlow()

    fun updateSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun searchLocation(query: String) {
        if (query.isBlank()) {
            _uiState.update { it.copy(searchResults = emptyList()) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSearching = true, error = null) }
            
            try {
                val results = locationIQService.searchLocation(
                    apiKey = LocationIQConstants.API_KEY,
                    query = query
                )
                
                val searchResults = results.map { result ->
                    SearchResult(
                        displayName = result.displayName ?: "",
                        lat = result.lat?.toDoubleOrNull() ?: 0.0,
                        lon = result.lon?.toDoubleOrNull() ?: 0.0
                    )
                }
                
                _uiState.update {
                    it.copy(
                        isSearching = false,
                        searchResults = searchResults
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isSearching = false,
                        error = "Không thể tìm kiếm địa điểm: ${e.message}"
                    )
                }
            }
        }
    }

    fun selectLocation(lat: Double, lon: Double) {
        _uiState.update {
            it.copy(
                selectedLat = lat,
                selectedLon = lon,
                isLoading = true,
                error = null
            )
        }
        
        // Reverse geocode to get address
        viewModelScope.launch {
            try {
                val response = locationIQService.reverseGeocode(
                    apiKey = LocationIQConstants.API_KEY,
                    lat = lat,
                    lon = lon
                )
                
                val address = response.displayName ?: "Không xác định được địa chỉ"
                
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        selectedAddress = address
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Không thể lấy địa chỉ: ${e.message}"
                    )
                }
            }
        }
    }

    fun clearSearch() {
        _uiState.update {
            it.copy(
                searchQuery = "",
                searchResults = emptyList()
            )
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
