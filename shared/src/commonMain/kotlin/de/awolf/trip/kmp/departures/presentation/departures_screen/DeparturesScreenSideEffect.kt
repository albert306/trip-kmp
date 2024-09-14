package de.awolf.trip.kmp.departures.presentation.departures_screen

import de.awolf.trip.kmp.core.util.error.NetworkError

sealed interface DeparturesScreenSideEffect {
    data class ShowNetworkError(val error: NetworkError) : DeparturesScreenSideEffect
}