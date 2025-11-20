package com.v2v.app.data.model

data class CreateVehicleRequest(
    val registrationNumber: String? = null,
    val make: String? = null,
    val model: String? = null,
    val year: Int? = null
)

data class VehicleResponse(
    val id: String,
    val userId: String,
    val registrationNumber: String?,
    val make: String?,
    val model: String?,
    val year: Int?,
    val vehicleUuid: String,
    val createdAt: String,
    val updatedAt: String
)

