package com.v2v.app.data.model

data class CreateDetectionRequest(
    val detectedByVehicleId: String,
    val detectedByDeviceId: String,
    val unregisteredIdentifier: String,
    val rssi: Int? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val detectedAt: String? = null
)

data class DetectionResponse(
    val id: String,
    val detectedByVehicleId: String,
    val detectedByDeviceId: String,
    val unregisteredIdentifier: String,
    val rssi: Int?,
    val latitude: Double?,
    val longitude: Double?,
    val detectedAt: String,
    val status: String,
    val createdAt: String
)

