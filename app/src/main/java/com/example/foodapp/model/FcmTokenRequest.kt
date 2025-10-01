package com.example.foodapp.model

import kotlinx.serialization.Serializable

@Serializable
data class FcmTokenRequest(
    val fcmToken: String
)