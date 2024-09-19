package de.awolf.trip.kmp.trips.presentation.trips_screen

import de.awolf.trip.kmp.trips.domain.models.Route

sealed class TripsScreenEvent {
    data object Close : TripsScreenEvent()
    data object UpdateTripsScreen : TripsScreenEvent()
    data class RouteDetails(val route: Route) : TripsScreenEvent()
}