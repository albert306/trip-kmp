package de.awolf.trip.kmp.departures.presentation.search_screen

import de.awolf.trip.kmp.core.util.error.Error

sealed interface SearchScreenSideEffect {
    data object ShowNoStopFoundMsg: SearchScreenSideEffect
    data object ShowInvalidDateTimeMsg : SearchScreenSideEffect
    data class ShowError(val error: Error) : SearchScreenSideEffect
}