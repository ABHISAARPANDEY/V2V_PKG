package com.v2v.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.v2v.app.data.model.VehicleResponse
import com.v2v.app.data.repository.VehicleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class VehicleUiState(
    val isLoading: Boolean = false,
    val vehicles: List<VehicleResponse> = emptyList(),
    val currentVehicle: VehicleResponse? = null,
    val error: String? = null
)

class VehicleViewModel : ViewModel() {
    private val vehicleRepository = VehicleRepository()
    private val _uiState = MutableStateFlow(VehicleUiState())
    val uiState: StateFlow<VehicleUiState> = _uiState.asStateFlow()

    init {
        loadVehicles()
    }

    fun loadVehicles() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val result = vehicleRepository.getMyVehicles()
            result.onSuccess { vehicles ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    vehicles = vehicles,
                    currentVehicle = vehicles.firstOrNull(),
                    error = null
                )
            }.onFailure { exception ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = exception.message ?: "Failed to load vehicles"
                )
            }
        }
    }

    fun createVehicle(
        registrationNumber: String? = null,
        make: String? = null,
        model: String? = null,
        year: Int? = null
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val result = vehicleRepository.createVehicle(registrationNumber, make, model, year)
            result.onSuccess { vehicle ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    currentVehicle = vehicle,
                    vehicles = listOf(vehicle),
                    error = null
                )
            }.onFailure { exception ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = exception.message ?: "Failed to create vehicle"
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

