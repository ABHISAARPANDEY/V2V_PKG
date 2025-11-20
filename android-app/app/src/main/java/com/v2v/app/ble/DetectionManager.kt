package com.v2v.app.ble

import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.concurrent.ConcurrentHashMap

data class UnregisteredDetection(
    val identifier: String,
    val address: String,
    val rssi: Int,
    val timestamp: Long,
    val count: Int = 1
)

class DetectionManager {
    private val detections = ConcurrentHashMap<String, UnregisteredDetection>()
    private val _unregisteredDetections = MutableStateFlow<List<UnregisteredDetection>>(emptyList())
    val unregisteredDetections: StateFlow<List<UnregisteredDetection>> = _unregisteredDetections.asStateFlow()

    companion object {
        private const val TAG = "DetectionManager"
        private const val CONFIRMATION_THRESHOLD = 3 // Number of detections needed
        private const val CONFIRMATION_WINDOW_MS = 3000L // 3 seconds window
    }

    fun processScannedDevice(device: ScannedDevice, registeredUuids: Set<String>) {
        // If device has a vehicle UUID and it's registered, skip
        if (device.vehicleUuid != null && device.isRegistered) {
            return
        }

        // Use device address or vehicle UUID as identifier
        val identifier = device.vehicleUuid ?: device.address
        val currentTime = System.currentTimeMillis()

        synchronized(detections) {
            val existing = detections[identifier]

            if (existing != null) {
                // Check if within confirmation window
                if (currentTime - existing.timestamp < CONFIRMATION_WINDOW_MS) {
                    // Increment count
                    val updated = existing.copy(
                        count = existing.count + 1,
                        rssi = device.rssi, // Update with latest RSSI
                        timestamp = currentTime
                    )
                    detections[identifier] = updated

                    // If threshold reached, mark as confirmed
                    if (updated.count >= CONFIRMATION_THRESHOLD) {
                        Log.d(TAG, "Confirmed unregistered detection: $identifier")
                        updateDetectionsList()
                    }
                } else {
                    // Outside window, reset
                    detections[identifier] = UnregisteredDetection(
                        identifier = identifier,
                        address = device.address,
                        rssi = device.rssi,
                        timestamp = currentTime,
                        count = 1
                    )
                }
            } else {
                // New detection
                detections[identifier] = UnregisteredDetection(
                    identifier = identifier,
                    address = device.address,
                    rssi = device.rssi,
                    timestamp = currentTime,
                    count = 1
                )
            }
        }

        updateDetectionsList()
    }

    private fun updateDetectionsList() {
        val confirmed = detections.values.filter { it.count >= CONFIRMATION_THRESHOLD }
            .sortedByDescending { it.timestamp }
        _unregisteredDetections.value = confirmed
    }

    fun clearDetections() {
        detections.clear()
        _unregisteredDetections.value = emptyList()
    }

    fun getConfirmedDetections(): List<UnregisteredDetection> {
        return detections.values.filter { it.count >= CONFIRMATION_THRESHOLD }
            .sortedByDescending { it.timestamp }
    }
}

