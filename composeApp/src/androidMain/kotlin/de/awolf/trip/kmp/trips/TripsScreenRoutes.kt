package de.awolf.trip.kmp.trips

import de.awolf.trip.kmp.trips.domain.models.TripQuery
import kotlinx.serialization.Serializable

@Serializable
object TripsEntryScreenRoute

@Serializable
data class TripsScreenRoute(
    val tripQuery: TripQuery
)