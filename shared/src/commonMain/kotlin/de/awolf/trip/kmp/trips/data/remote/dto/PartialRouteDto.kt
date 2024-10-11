package de.awolf.trip.kmp.trips.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PartialRouteDto(
    @SerialName("PartialRouteId") val partialRouteId: Int? = null,
    @SerialName("Duration") val duration: Int = 0,
    @SerialName("Mot") val mot: MotDto,
    @SerialName("MapDataIndex") val mapDataIndex: Int? = null,
    @SerialName("Shift") val shift: String,
    @SerialName("RegularStops") val regularStops: List<PartialRouteStopDto> = listOf(),
    @SerialName("ChangeoverEndangered") val changeoverEndangered: Boolean = false,
    @SerialName("NextDepartureTimes") val nextDepartureTimes: List<String> = listOf(),
    @SerialName("PreviousDepartureTimes") val previousDepartureTimes: List<String> = listOf()
)
