package com.example.foodapp.model

import kotlinx.io.bytestring.unsafe.UnsafeByteStringOperations
import kotlinx.serialization.Serializable

@Serializable
data class SignupRequest(
    val name: String,
    val email: String,
    val password: String,
    val phone: String,
    val userType: Int
)
