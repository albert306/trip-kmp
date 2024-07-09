package de.awolf.trip_compose.data.remote.dto.stop_monitor

import data.networking.dto.ResponseStatusDto
import data.networking.dto.stop_monitor.StopScheduleItemDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StopScheduleResponseDto(
    @SerialName("Status") val responseStatusDto: ResponseStatusDto,
    @SerialName("ExpirationTime") val expirationTime: String? = null,
    @SerialName("Stops") val scheduledStops: List<StopScheduleItemDto>
)