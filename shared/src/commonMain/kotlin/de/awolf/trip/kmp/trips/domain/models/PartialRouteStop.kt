package de.awolf.trip.kmp.trips.domain.models

import de.awolf.trip.kmp.core.domain.models.DepartureState
import de.awolf.trip.kmp.core.domain.models.Platform
import kotlinx.datetime.Instant

data class PartialRouteStop(
    val arrivalScheduledTime: Instant,
    val departureScheduledTime: Instant,
    val arrivalRealTime: Instant,
    val departureRealTime: Instant,
    val stopRegion: String,
    val stopName: String,
    val type: String,
    val stopId: String,
    val dhId: String?,
    val platform: Platform?,
    val latitude: Int?,
    val longitude: Int?,
    val departureState: DepartureState,
    val arrivalState: DepartureState,
    val cancelReasons: List<String>,
    val parkAndRail: List<String>,
    val occupancy: String?
)