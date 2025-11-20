package com.v2v.app.data.model

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class AuthResponse(
    val accessToken: String,
    val user: UserResponse
)

data class UserResponse(
    val id: String,
    val name: String,
    val email: String,
    val role: String,
    val createdAt: String
)

