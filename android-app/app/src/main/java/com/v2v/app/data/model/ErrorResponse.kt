package com.v2v.app.data.model

data class ErrorResponse(
    val message: String? = null,
    val error: String? = null,
    val statusCode: Int? = null
)

