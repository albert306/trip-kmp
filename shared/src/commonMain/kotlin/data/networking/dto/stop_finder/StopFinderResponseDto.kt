package data.networking.dto.stop_finder

import data.networking.dto.ResponseStatusDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StopFinderResponseDto(
    @SerialName("Status") val responseStatusDto: ResponseStatusDto,
    @SerialName("PointStatus") val pointStatus: String,
    @SerialName("Points") val stops: List<String> = emptyList(),
    @SerialName("ExpirationTime") val expirationTime: String? = null,
)
