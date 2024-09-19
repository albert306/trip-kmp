package de.awolf.trip.kmp.trips.data.remote.dto

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TripQueryDto(
    @SerialName("origin") val origin: String,
    @SerialName("via") val via: String? = null,
    @SerialName("durationOfStay") val stayDuration: String? = null,
    @SerialName("destination") val destination: String,
    @SerialName("time") val time: Instant,
    @SerialName("isArrivalTime") val isArrivalTime: Boolean,
    @SerialName("shortTermChanges") val shorttermchanges: Boolean,
    @SerialName("standardSettings") val settings: SettingsDto? = null,
) {
    @Serializable
    data class SettingsDto(
        @SerialName("footpathToStop") val footpathToStop: Int,
        @SerialName("walkingSpeed") val walkingSpeed: String,
        @SerialName("maxChanges") val maxChanges: Int,
        @SerialName("mot") val modeOfTransport: List<String>,
        @SerialName("includeAlternativeStops") val includeAlternativeStops: Boolean,
    )
}
