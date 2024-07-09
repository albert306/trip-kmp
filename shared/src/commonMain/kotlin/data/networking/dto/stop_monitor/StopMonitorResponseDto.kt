package data.networking.dto.stop_monitor

import data.networking.dto.ResponseStatusDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StopMonitorResponseDto(
    @SerialName("Status") val responseStatusDto: ResponseStatusDto,
    @SerialName("Name") val name: String,
    @SerialName("Place") val region: String,
    @SerialName("ExpirationTime") val expirationTime: String? = null,
    @SerialName("Departures") val departures: List<DepartureDto> = emptyList(),

    )