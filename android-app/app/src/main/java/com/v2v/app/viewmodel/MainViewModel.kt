package com.v2v.app.viewmodel

import android.app.Application
import android.content.Context
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.*
import com.v2v.app.ble.*
import com.v2v.app.data.api.TokenManager
import com.v2v.app.data.model.VehicleResponse
import com.v2v.app.data.repository.DeviceRepository
import com.v2v.app.data.repository.DetectionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

data class MainUiState(
    val isAdvertising: Boolean = false,
    val isScanning: Boolean = false,
    val currentVehicle: VehicleResponse? = null,
    val deviceId: String? = null,
    val unregisteredDetections: List<UnregisteredDetection> = emptyList(),
    val error: String? = null,
    val location: Location? = null
)

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val bleAdvertiser = BleAdvertiser(application)
    private val bleScanner = BleScanner(application)
    private val detectionManager = DetectionManager()
    private val deviceRepository = DeviceRepository()
    private val detectionRepository = DetectionRepository()

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(application)
    private var locationCallback: LocationCallback? = null
    private val reportedDetections = mutableSetOf<String>()

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        TokenManager.init(application)
        viewModelScope.launch {
            detectionManager.unregisteredDetections.collect { detections ->
                _uiState.value = _uiState.value.copy(unregisteredDetections = detections)
            }
        }
    }

    fun initializeDevice(vehicle: VehicleResponse) {
        viewModelScope.launch {
            // Generate device identifier
            val deviceId = UUID.randomUUID().toString()
            _uiState.value = _uiState.value.copy(
                currentVehicle = vehicle,
                deviceId = deviceId
            )

            // Register device with backend
            val result = deviceRepository.registerDevice(
                deviceIdentifier = deviceId,
                platform = "ANDROID",
                vehicleId = vehicle.id
            )

            result.onSuccess { device ->
                _uiState.value = _uiState.value.copy(deviceId = device.id)
                startServices()
            }.onFailure { exception ->
                _uiState.value = _uiState.value.copy(
                    error = exception.message ?: "Failed to register device"
                )
            }
        }
    }

    private fun startServices() {
        val vehicle = _uiState.value.currentVehicle ?: return
        val vehicleUuid = vehicle.vehicleUuid

        // Start BLE advertising
        bleAdvertiser.startAdvertising(vehicleUuid) { success, error ->
            _uiState.value = _uiState.value.copy(
                isAdvertising = success,
                error = error
            )
        }

        // Set registered UUIDs for scanner
        bleScanner.setRegisteredVehicleUuids(setOf(vehicleUuid))

        // Start BLE scanning
        bleScanner.startScanning(
            onDeviceFound = { device ->
                handleScannedDevice(device)
            },
            onError = { error ->
                _uiState.value = _uiState.value.copy(error = error)
            }
        )

        _uiState.value = _uiState.value.copy(isScanning = true)

        // Start location updates
        startLocationUpdates()
    }

    private fun handleScannedDevice(device: ScannedDevice) {
        val registeredUuids = setOf(_uiState.value.currentVehicle?.vehicleUuid ?: "")
        detectionManager.processScannedDevice(device, registeredUuids)

        // Check for confirmed detections and report to backend
        viewModelScope.launch {
            val confirmed = detectionManager.getConfirmedDetections()
            confirmed.forEach { detection ->
                if (!reportedDetections.contains(detection.identifier)) {
                    reportDetection(detection)
                }
            }
        }
    }

    private fun reportDetection(detection: UnregisteredDetection) {
        viewModelScope.launch {
            val vehicle = _uiState.value.currentVehicle ?: return@launch
            val deviceId = _uiState.value.deviceId ?: return@launch
            val location = _uiState.value.location

            val result = detectionRepository.createUnregisteredDetection(
                detectedByVehicleId = vehicle.id,
                detectedByDeviceId = deviceId,
                unregisteredIdentifier = detection.identifier,
                rssi = detection.rssi,
                latitude = location?.latitude,
                longitude = location?.longitude
            )

            result.onSuccess {
                reportedDetections.add(detection.identifier)
            }.onFailure { exception ->
                _uiState.value = _uiState.value.copy(
                    error = exception.message ?: "Failed to report detection"
                )
            }
        }
    }

    private fun startLocationUpdates() {
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
            .setIntervalMillis(30000)
            .build()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.lastLocation?.let { location ->
                    _uiState.value = _uiState.value.copy(location = location)
                }
            }
        }

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback!!,
            getApplication<Application>().mainLooper
        )
    }

    fun stopServices() {
        bleAdvertiser.stopAdvertising()
        bleScanner.stopScanning()
        locationCallback?.let {
            fusedLocationClient.removeLocationUpdates(it)
        }
        _uiState.value = _uiState.value.copy(
            isAdvertising = false,
            isScanning = false
        )
    }

    override fun onCleared() {
        super.onCleared()
        stopServices()
    }
}

