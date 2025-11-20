package com.v2v.app.data.repository

import com.google.gson.Gson
import com.v2v.app.data.api.ApiClient
import com.v2v.app.data.api.TokenManager
import com.v2v.app.data.model.*

class AuthRepository {
    private val apiService = ApiClient.apiService
    private val gson = Gson()

    suspend fun register(name: String, email: String, password: String): Result<AuthResponse> {
        return try {
            val response = apiService.register(RegisterRequest(name, email, password))
            if (response.isSuccessful && response.body() != null) {
                val authResponse = response.body()!!
                TokenManager.saveToken(authResponse.accessToken)
                Result.success(authResponse)
            } else {
                // Parse error message from response body
                val errorBody = response.errorBody()?.string()
                val errorMessage = parseErrorMessage(errorBody, response.code(), response.message())
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network error: ${e.message ?: "Unable to connect to server"}"))
        }
    }

    suspend fun login(email: String, password: String): Result<AuthResponse> {
        return try {
            val response = apiService.login(LoginRequest(email, password))
            if (response.isSuccessful && response.body() != null) {
                val authResponse = response.body()!!
                TokenManager.saveToken(authResponse.accessToken)
                Result.success(authResponse)
            } else {
                // Parse error message from response body
                val errorBody = response.errorBody()?.string()
                val errorMessage = parseErrorMessage(errorBody, response.code(), response.message())
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network error: ${e.message ?: "Unable to connect to server"}"))
        }
    }

    fun logout() {
        TokenManager.clearToken()
    }

    fun isLoggedIn(): Boolean {
        return TokenManager.getToken() != null
    }

    private fun parseErrorMessage(errorBody: String?, code: Int, message: String): String {
        if (errorBody == null || errorBody.isEmpty()) {
            return when (code) {
                400 -> "Bad Request: Please check your input (name, email, password)"
                401 -> "Unauthorized: Invalid credentials"
                404 -> "Not Found: Server endpoint not found"
                500 -> "Server Error: Please try again later"
                else -> "Request failed: $code $message"
            }
        }

        return try {
            // Try to parse as JSON error response
            val errorResponse = gson.fromJson(errorBody, ErrorResponse::class.java)
            val errorMsg = errorResponse.message ?: errorResponse.error
            
            // If it's a validation error array, extract all messages
            if (errorMsg != null) {
                errorMsg
            } else {
                // Try to extract validation errors from array format
                val validationMatch = Regex("\"message\"\\s*:\\s*\\[([^\\]]+)\\]").find(errorBody)
                if (validationMatch != null) {
                    val messages = Regex("\"([^\"]+)\"").findAll(validationMatch.groupValues[1])
                        .map { it.groupValues[1] }
                        .joinToString(", ")
                    if (messages.isNotEmpty()) messages else errorBody
                } else {
                    // Try simple message extraction
                    val simpleMatch = Regex("\"message\"\\s*:\\s*\"([^\"]+)\"").find(errorBody)
                    simpleMatch?.groupValues?.get(1) ?: errorBody
                }
            }
        } catch (e: Exception) {
            // If JSON parsing fails, try to extract message from raw body
            try {
                // Try to find validation error messages
                val validationPattern = Regex("(?:message|error)\"\\s*:\\s*\"([^\"]+)\"")
                val matches = validationPattern.findAll(errorBody)
                val messages = matches.map { it.groupValues[1] }.toList()
                
                if (messages.isNotEmpty()) {
                    messages.joinToString(", ")
                } else {
                    // Fallback: return first 200 chars of error body
                    errorBody.take(200)
                }
            } catch (e2: Exception) {
                errorBody.take(200)
            }
        }
    }
}

