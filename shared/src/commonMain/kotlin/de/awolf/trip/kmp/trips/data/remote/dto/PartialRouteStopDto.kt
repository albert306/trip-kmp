package de.awolf.trip.kmp.trips.data.remote.dto

import de.awolf.trip.kmp.core.data.remote.dto.PlatformDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PartialRouteStopDto(
    @SerialName("ArrivalTime") val arrivalScheduledTime: String,
    @SerialName("DepartureTime") val departureScheduledTime: String,
    @SerialName("ArrivalRealTime") val arrivalRealTime: String? = null,
    @SerialName("DepartureRealTime") val departureRealTime: String? = null,
    @SerialName("Place") val stopRegion: String,
    @SerialName("Name") val stopName: String,
    @SerialName("Type") val type: String = "Stop",
    @SerialName("DataId") val stopId: String,
    @SerialName("DhId") val dhId: String? = null,
    @SerialName("Platform") val platform: PlatformDto? = null,
    @SerialName("Latitude") val latitude: Int? = null,
    @SerialName("Longitude") val longitude: Int? = null,
    @SerialName("DepartureState") val departureState: String = "InTime",
    @SerialName("ArrivalState") val arrivalState: String = "InTime",
    @SerialName("CancelReasons") val cancelReasons: List<String> = listOf(),
    @SerialName("ParkAndRail") val parkAndRail: List<String> = listOf(),
    @SerialName("Occupancy") val occupancy: String? = null
)
