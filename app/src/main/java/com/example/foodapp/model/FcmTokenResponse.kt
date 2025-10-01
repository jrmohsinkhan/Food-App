package com.example.foodapp.model

import kotlinx.serialization.Serializable

@Serializable
data class FcmTokenResponse(
    val success: Boolean,
    val message: String? = null
)