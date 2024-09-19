package de.awolf.trip.kmp.trips.presentation.trips_screen

import de.awolf.trip.kmp.core.util.error.NetworkError

sealed interface TripsScreenSideEffect {
    data class ShowNetworkError(val error: NetworkError) : TripsScreenSideEffect
}