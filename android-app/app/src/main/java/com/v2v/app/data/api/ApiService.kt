package com.v2v.app.data.api

import com.v2v.app.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    // Auth
    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    // Vehicles
    @POST("vehicles")
    suspend fun createVehicle(@Body request: CreateVehicleRequest): Response<VehicleResponse>

    @GET("vehicles/my")
    suspend fun getMyVehicles(): Response<List<VehicleResponse>>

    // Devices
    @POST("devices/register")
    suspend fun registerDevice(@Body request: RegisterDeviceRequest): Response<DeviceResponse>

    @PATCH("devices/{id}/heartbeat")
    suspend fun updateDeviceHeartbeat(@Path("id") deviceId: String): Response<DeviceResponse>

    // Detections
    @POST("detections/unregistered")
    suspend fun createUnregisteredDetection(@Body request: CreateDetectionRequest): Response<DetectionResponse>
}

