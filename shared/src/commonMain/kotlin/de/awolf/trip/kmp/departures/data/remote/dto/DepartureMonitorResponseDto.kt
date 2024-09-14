package de.awolf.trip.kmp.departures.data.remote.dto

import de.awolf.trip.kmp.core.data.remote.dto.ResponseStatusDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DepartureMonitorResponseDto(
    @SerialName("Status") val responseStatusDto: ResponseStatusDto,
    @SerialName("Name") val name: String,
    @SerialName("Place") val region: String,
    @SerialName("ExpirationTime") val expirationTime: String? = null,
    @SerialName("Departures") val departures: List<DepartureDto> = emptyList(),

    )