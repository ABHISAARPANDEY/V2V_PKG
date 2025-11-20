package com.v2v.app.ble

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.os.ParcelUuid
import android.util.Log
import java.util.UUID

data class ScannedDevice(
    val address: String,
    val name: String?,
    val rssi: Int,
    val vehicleUuid: String?,
    val isRegistered: Boolean = false
)

class BleScanner(private val context: Context) {
    private val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    private val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter
    private var isScanning = false

    private val V2V_SERVICE_UUID = UUID.fromString("0000FEAA-0000-1000-8000-00805F9B34FB")
    private val V2V_SERVICE_PARCEL_UUID = ParcelUuid(V2V_SERVICE_UUID)

    private var scanCallback: ScanCallback? = null
    private var onDeviceFound: ((ScannedDevice) -> Unit)? = null
    private var registeredVehicleUuids: Set<String> = emptySet()

    companion object {
        private const val TAG = "BleScanner"
    }

    fun setRegisteredVehicleUuids(uuids: Set<String>) {
        registeredVehicleUuids = uuids
    }

    fun startScanning(
        onDeviceFound: (ScannedDevice) -> Unit,
        onError: (String) -> Unit
    ) {
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled) {
            onError("Bluetooth is not enabled")
            return
        }

        if (isScanning) {
            onError("Already scanning")
            return
        }

        this.onDeviceFound = onDeviceFound

        // Scan for ALL BLE devices (not just V2V service UUID)
        // This allows detection of unregistered devices that don't have the app
        // We'll filter in handleScanResult to identify which are registered vs unregistered
        val scanSettings = ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_BALANCED)
            .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES)
            .build()

        scanCallback = object : ScanCallback() {
            override fun onScanResult(callbackType: Int, result: ScanResult) {
                super.onScanResult(callbackType, result)
                handleScanResult(result)
            }

            override fun onBatchScanResults(results: MutableList<ScanResult>) {
                super.onBatchScanResults(results)
                results.forEach { handleScanResult(it) }
            }

            override fun onScanFailed(errorCode: Int) {
                super.onScanFailed(errorCode)
                val errorMessage = when (errorCode) {
                    SCAN_FAILED_ALREADY_STARTED -> "Scan already started"
                    SCAN_FAILED_APPLICATION_REGISTRATION_FAILED -> "Application registration failed"
                    SCAN_FAILED_INTERNAL_ERROR -> "Internal error"
                    SCAN_FAILED_FEATURE_UNSUPPORTED -> "Feature unsupported"
                    SCAN_FAILED_OUT_OF_HARDWARE_RESOURCES -> "Out of hardware resources"
                    else -> "Unknown error: $errorCode"
                }
                Log.e(TAG, "Scan failed: $errorMessage")
                isScanning = false
                onError(errorMessage)
            }
        }

        try {
            // Start scanning without filter to detect ALL BLE devices
            bluetoothAdapter.bluetoothLeScanner.startScan(
                null, // No filter - scan all devices
                scanSettings,
                scanCallback
            )
            isScanning = true
            Log.d(TAG, "BLE scanning started (scanning all devices)")
        } catch (e: Exception) {
            Log.e(TAG, "Exception starting scan", e)
            isScanning = false
            onError(e.message ?: "Failed to start scanning")
        }
    }

    private fun handleScanResult(result: ScanResult) {
        val device = result.device
        val scanRecord = result.scanRecord
        val rssi = result.rssi

        // Extract vehicle UUID from service data (if device has V2V app)
        var vehicleUuid: String? = null
        var isRegistered = false
        var hasV2VService = false

        // Check if device has V2V service UUID
        scanRecord?.serviceUuids?.forEach { uuid ->
            if (uuid.uuid == V2V_SERVICE_UUID) {
                hasV2VService = true
            }
        }

        // Extract vehicle UUID from service data if V2V service exists
        scanRecord?.serviceData?.forEach { (uuid, data) ->
            if (uuid == V2V_SERVICE_PARCEL_UUID && data != null && data.size >= 16) {
                // Convert bytes back to UUID string
                val uuidString = data.take(16).joinToString("") { "%02x".format(it) }
                vehicleUuid = formatUuidString(uuidString)
                isRegistered = registeredVehicleUuids.contains(vehicleUuid)
            }
        }

        // Determine if this is an unregistered device:
        // - If it has V2V service but UUID is not in registered list → unregistered V2V device
        // - If it doesn't have V2V service at all → unregistered (regular BLE device)
        val isUnregistered = if (hasV2VService) {
            // Has V2V service but UUID not registered
            vehicleUuid != null && !isRegistered
        } else {
            // No V2V service - treat as unregistered
            true
        }

        // Only report if it's unregistered (skip registered vehicles)
        if (isUnregistered) {
            // Use device address as identifier for unregistered devices
            val identifier = device.address

            val scannedDevice = ScannedDevice(
                address = device.address,
                name = scanRecord?.deviceName ?: device.name ?: "Unknown Device",
                rssi = rssi,
                vehicleUuid = vehicleUuid,
                isRegistered = false // Always false for unregistered
            )

            onDeviceFound?.invoke(scannedDevice)
        }
        // If device is registered (has V2V service and UUID is in registered list), skip it
    }

    private fun formatUuidString(hexString: String): String {
        // Format as UUID: xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx
        return if (hexString.length >= 32) {
            "${hexString.substring(0, 8)}-${hexString.substring(8, 12)}-${hexString.substring(12, 16)}-${hexString.substring(16, 20)}-${hexString.substring(20, 32)}"
        } else {
            hexString
        }
    }

    fun stopScanning() {
        if (isScanning && bluetoothAdapter != null) {
            try {
                scanCallback?.let {
                    bluetoothAdapter.bluetoothLeScanner.stopScan(it)
                }
                isScanning = false
                scanCallback = null
                onDeviceFound = null
                Log.d(TAG, "BLE scanning stopped")
            } catch (e: Exception) {
                Log.e(TAG, "Exception stopping scan", e)
            }
        }
    }

    fun isScanning(): Boolean = isScanning
}

