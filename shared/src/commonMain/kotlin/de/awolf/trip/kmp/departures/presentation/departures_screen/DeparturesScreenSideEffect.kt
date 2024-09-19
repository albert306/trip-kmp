package de.awolf.trip.kmp.departures.presentation.departures_screen

import de.awolf.trip.kmp.core.util.error.Error

sealed interface DeparturesScreenSideEffect {
    data class ShowError(val error: Error) : DeparturesScreenSideEffect
}