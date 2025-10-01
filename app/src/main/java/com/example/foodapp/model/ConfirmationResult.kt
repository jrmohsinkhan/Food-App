package com.example.foodapp.model

sealed class ConfirmationResult {
    // The successful API returns a simple string message.
    data class Success(val message: String) : ConfirmationResult()
    data class Error(val message: String) : ConfirmationResult() // For API-level errors (e.g., 400 No tokens)
    data class NetworkError(val message: String) : ConfirmationResult() // For connection/server errors
}
