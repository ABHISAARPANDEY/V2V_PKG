package com.v2v.app.data.repository

import com.v2v.app.data.api.ApiClient
import com.v2v.app.data.model.*

class DeviceRepository {
    private val apiService = ApiClient.apiService

    suspend fun registerDevice(
        deviceIdentifier: String,
        platform: String,
        vehicleId: String? = null
    ): Result<DeviceResponse> {
        return try {
            val response = apiService.registerDevice(
                RegisterDeviceRequest(deviceIdentifier, platform, vehicleId)
            )
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception(response.message() ?: "Failed to register device"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateHeartbeat(deviceId: String): Result<DeviceResponse> {
        return try {
            val response = apiService.updateDeviceHeartbeat(deviceId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception(response.message() ?: "Failed to update heartbeat"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

