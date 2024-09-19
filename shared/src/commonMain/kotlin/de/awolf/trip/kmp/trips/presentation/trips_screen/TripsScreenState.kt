package de.awolf.trip.kmp.trips.presentation.trips_screen

import de.awolf.trip.kmp.trips.domain.models.Route
import de.awolf.trip.kmp.trips.domain.models.TripQuery

data class TripsScreenState(
    val tripQuery: TripQuery,
    val isRefreshing: Boolean = false,
    val routes: List<Route> = emptyList(),
)