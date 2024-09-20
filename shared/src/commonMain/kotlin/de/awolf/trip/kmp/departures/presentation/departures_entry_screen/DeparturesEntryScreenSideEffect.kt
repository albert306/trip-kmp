package de.awolf.trip.kmp.departures.presentation.departures_entry_screen

import de.awolf.trip.kmp.core.util.error.Error

sealed interface DeparturesEntryScreenSideEffect {
    data object ShowNoStopFoundMsg: DeparturesEntryScreenSideEffect
    data object ShowInvalidDateTimeMsg : DeparturesEntryScreenSideEffect
    data class ShowError(val error: Error) : DeparturesEntryScreenSideEffect
}