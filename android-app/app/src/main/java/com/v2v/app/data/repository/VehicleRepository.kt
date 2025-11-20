package com.v2v.app.data.repository

import com.v2v.app.data.api.ApiClient
import com.v2v.app.data.model.*

class VehicleRepository {
    private val apiService = ApiClient.apiService

    suspend fun createVehicle(
        registrationNumber: String? = null,
        make: String? = null,
        model: String? = null,
        year: Int? = null
    ): Result<VehicleResponse> {
        return try {
            val response = apiService.createVehicle(
                CreateVehicleRequest(registrationNumber, make, model, year)
            )
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception(response.message() ?: "Failed to create vehicle"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getMyVehicles(): Result<List<VehicleResponse>> {
        return try {
            val response = apiService.getMyVehicles()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception(response.message() ?: "Failed to fetch vehicles"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

