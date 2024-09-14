package de.awolf.trip.kmp.search.data.remote.dto

import de.awolf.trip.kmp.core.data.remote.dto.ResponseStatusDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StopSearchResponseDto(
    @SerialName("Status") val responseStatusDto: ResponseStatusDto,
    @SerialName("PointStatus") val pointStatus: String,
    @SerialName("Points") val stops: List<String> = emptyList(),
    @SerialName("ExpirationTime") val expirationTime: String? = null,
)
