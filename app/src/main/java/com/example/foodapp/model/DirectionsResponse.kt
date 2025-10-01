package com.example.foodapp.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DirectionsResponse(
    val routes: List<Route> = emptyList(),
    val status: String? = null
)

@Serializable
data class Route(
    val legs: List<Leg> = emptyList(),
    @SerialName("overview_polyline") val overviewPolyline: OverviewPolyline? = null
)

@Serializable
data class Leg(
    val distance: Distance? = null,
    val duration: Duration? = null,
    @SerialName("start_address") val startAddress: String? = null,
    @SerialName("end_address") val endAddress: String? = null,
    @SerialName("start_location") val startLocation: Location? = null,
    @SerialName("end_location") val endLocation: Location? = null,
    val steps: List<Step> = emptyList()
)

@Serializable
data class Step(
    val distance: Distance? = null,
    val duration: Duration? = null,
    @SerialName("start_location") val startLocation: Location? = null,
    @SerialName("end_location") val endLocation: Location? = null,
    val html_instructions: String? = null,
    val polyline: Polyline? = null,
    val travel_mode: String? = null
)

@Serializable
data class Distance(
    val text: String? = null,
    val value: Int? = null
)

@Serializable
data class Duration(
    val text: String? = null,
    val value: Int? = null
)

@Serializable
data class Location(
    val lat: Double? = null,
    val lng: Double? = null
)

@Serializable
data class Polyline(
    val points: String? = null
)

@Serializable
data class OverviewPolyline(
    val points: String? = null
)
