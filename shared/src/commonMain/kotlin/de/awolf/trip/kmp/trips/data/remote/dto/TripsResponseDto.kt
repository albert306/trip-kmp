package de.awolf.trip.kmp.trips.data.remote.dto

import de.awolf.trip.kmp.core.data.remote.dto.ResponseStatusDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TripsResponseDto(
    @SerialName("SessionId") val sessionId: String? = null,
    @SerialName("Status") val responseStatusDto: ResponseStatusDto,
    @SerialName("Routes") val routes: List<RouteDto>
)
