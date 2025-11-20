package com.v2v.app.ble

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.AdvertiseCallback
import android.bluetooth.le.AdvertiseData
import android.bluetooth.le.AdvertiseSettings
import android.bluetooth.le.BluetoothLeAdvertiser
import android.content.Context
import android.util.Log
import java.util.UUID

class BleAdvertiser(private val context: Context) {
    private val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    private val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter
    private var advertiser: BluetoothLeAdvertiser? = null
    private var isAdvertising = false

    // Custom service UUID for V2V app
    private val V2V_SERVICE_UUID = UUID.fromString("0000FEAA-0000-1000-8000-00805F9B34FB")

    companion object {
        private const val TAG = "BleAdvertiser"
    }

    fun startAdvertising(vehicleUuid: String, callback: (Boolean, String?) -> Unit) {
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled) {
            callback(false, "Bluetooth is not enabled")
            return
        }

        advertiser = bluetoothAdapter.bluetoothLeAdvertiser
        if (advertiser == null) {
            callback(false, "BLE advertising not supported on this device")
            return
        }

        if (isAdvertising) {
            callback(false, "Already advertising")
            return
        }

        // Convert vehicle UUID to bytes (first 16 bytes of UUID)
        val uuidBytes = vehicleUuid.replace("-", "").chunked(2).map { it.toInt(16).toByte() }.toByteArray()
        // Take first 16 bytes or pad if needed
        val serviceData = uuidBytes.take(16).toByteArray()

        val advertiseSettings = AdvertiseSettings.Builder()
            .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_BALANCED)
            .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_MEDIUM)
            .setConnectable(false)
            .setTimeout(0) // Advertise indefinitely
            .build()

        val advertiseData = AdvertiseData.Builder()
            .setIncludeDeviceName(false)
            .addServiceUuid(android.os.ParcelUuid(V2V_SERVICE_UUID))
            .addServiceData(android.os.ParcelUuid(V2V_SERVICE_UUID), serviceData)
            .build()

        val advertiseCallback = object : AdvertiseCallback() {
            override fun onStartSuccess(settingsInEffect: AdvertiseSettings) {
                super.onStartSuccess(settingsInEffect)
                isAdvertising = true
                Log.d(TAG, "BLE advertising started successfully")
                callback(true, null)
            }

            override fun onStartFailure(errorCode: Int) {
                super.onStartFailure(errorCode)
                isAdvertising = false
                val errorMessage = when (errorCode) {
                    ADVERTISE_FAILED_DATA_TOO_LARGE -> "Advertisement data too large"
                    ADVERTISE_FAILED_TOO_MANY_ADVERTISERS -> "Too many advertisers"
                    ADVERTISE_FAILED_ALREADY_STARTED -> "Already started"
                    ADVERTISE_FAILED_INTERNAL_ERROR -> "Internal error"
                    ADVERTISE_FAILED_FEATURE_UNSUPPORTED -> "Feature unsupported"
                    else -> "Unknown error: $errorCode"
                }
                Log.e(TAG, "BLE advertising failed: $errorMessage")
                callback(false, errorMessage)
            }
        }

        try {
            advertiser?.startAdvertising(advertiseSettings, advertiseData, advertiseCallback)
        } catch (e: Exception) {
            Log.e(TAG, "Exception starting advertising", e)
            callback(false, e.message)
        }
    }

    fun stopAdvertising() {
        if (isAdvertising && advertiser != null) {
            try {
                advertiser?.stopAdvertising(object : AdvertiseCallback() {})
                isAdvertising = false
                Log.d(TAG, "BLE advertising stopped")
            } catch (e: Exception) {
                Log.e(TAG, "Exception stopping advertising", e)
            }
        }
    }

    fun isAdvertising(): Boolean = isAdvertising
}

