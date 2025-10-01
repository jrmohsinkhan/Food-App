package com.example.foodapp.model

import kotlinx.serialization.Serializable

@Serializable
data class Venue(
    val venueID: Int,
    val venueName: String,
    val venueSports: List<VenueSport> = emptyList(),
    val address: String,
    val city: String,
    val contactNumber: String,
    val imageURL: String,
    val createdBy: VenueCreator,
    val createdAt: String,
    val verified: Boolean
)

@Serializable
data class VenueSport(
    val venueSportID: Int,
    val sport: Sport
)

@Serializable
data class Sport(
    val sportID: Int,
    val sportName: String
)

@Serializable
data class VenueCreator(
    val userID: Int,
    val name: String,
    val email: String,
    val phone: String,
    val userRole: VenueUserRole,
    val createdAt: String,
    val passwordHash: String
)

@Serializable
data class VenueUserRole(
    val roleID: Int,
    val roleName: String
)
