package com.example.foodapp.model

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val token: String,
    val role: String,
    val name: String
)

@Serializable
data class ErrorResponse(
    val message: String
)

sealed class LoginResult {
    data class Success(val data: LoginResponse) : LoginResult()
    data class Error(val error: String) : LoginResult()
    data class NetworkError(val message: String) : LoginResult() // For non-HTTP, I/O errors
}
