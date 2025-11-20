package com.v2v.app.data.repository

import com.v2v.app.data.api.ApiClient
import com.v2v.app.data.model.*
import java.text.SimpleDateFormat
import java.util.*

class DetectionRepository {
    private val apiService = ApiClient.apiService

    suspend fun createUnregisteredDetection(
        detectedByVehicleId: String,
        detectedByDeviceId: String,
        unregisteredIdentifier: String,
        rssi: Int? = null,
        latitude: Double? = null,
        longitude: Double? = null
    ): Result<DetectionResponse> {
        return try {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
            dateFormat.timeZone = TimeZone.getTimeZone("UTC")
            val detectedAt = dateFormat.format(Date())

            val response = apiService.createUnregisteredDetection(
                CreateDetectionRequest(
                    detectedByVehicleId,
                    detectedByDeviceId,
                    unregisteredIdentifier,
                    rssi,
                    latitude,
                    longitude,
                    detectedAt
                )
            )
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception(response.message() ?: "Failed to create detection"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

