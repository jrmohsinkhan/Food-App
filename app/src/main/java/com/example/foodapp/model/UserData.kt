package com.example.foodapp.model

import kotlinx.serialization.Serializable

@Serializable
data class UserData(
    val userId: Int,
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String? = null,
    val phone: String? = null,
    val token: String? = null,
    val profileImage: String? = null,
    val cnic: String? = null,
    val deviceId: String? = null,
    val deviceType: String? = null,
    val fcmToken: String? = null,
    val version: String? = null,
    val isActive: Int? = null,
    val isBlock: Int? = null,
    val createdById: Int? = null,
    val updatedById: Int? = null,
    val createdByName: String? = null,
    val userType: String? = null,
    val organizationId: Int? = null,
    val organizationName: String? = null,
    val districtId: Int? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null,
    val username: String? = null,
    val authorities: List<String>? = null,
    val enabled: Boolean? = null,
    val accountNonLocked: Boolean? = null,
    val credentialsNonExpired: Boolean? = null,
    val accountNonExpired: Boolean? = null
)
