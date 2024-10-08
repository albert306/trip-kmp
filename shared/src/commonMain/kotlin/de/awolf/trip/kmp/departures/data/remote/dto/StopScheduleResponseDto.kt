package de.awolf.trip.kmp.departures.data.remote.dto

import de.awolf.trip.kmp.core.data.remote.dto.ResponseStatusDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StopScheduleResponseDto(
    @SerialName("Status") val responseStatusDto: ResponseStatusDto,
    @SerialName("ExpirationTime") val expirationTime: String? = null,
    @SerialName("Stops") val scheduledStops: List<StopScheduleItemDto>
)