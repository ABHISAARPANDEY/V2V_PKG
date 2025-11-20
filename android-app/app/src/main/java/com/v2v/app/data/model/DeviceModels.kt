package com.v2v.app.data.model

data class RegisterDeviceRequest(
    val deviceIdentifier: String,
    val platform: String,
    val vehicleId: String? = null
)

data class DeviceResponse(
    val id: String,
    val userId: String,
    val vehicleId: String?,
    val devicePlatform: String,
    val deviceIdentifier: String,
    val lastSeenAt: String?,
    val createdAt: String,
    val updatedAt: String
)

