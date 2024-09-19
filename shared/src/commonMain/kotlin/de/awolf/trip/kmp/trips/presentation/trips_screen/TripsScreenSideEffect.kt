package de.awolf.trip.kmp.trips.presentation.trips_screen

import de.awolf.trip.kmp.core.util.error.Error

sealed interface TripsScreenSideEffect {
    data class ShowError(val error: Error) : TripsScreenSideEffect
}