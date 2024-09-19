package de.awolf.trip.kmp.trips.presentation.search_screen

import de.awolf.trip.kmp.core.util.error.Error

sealed interface SearchScreenSideEffect {
    data object ShowNoOriginSelectedMsg : SearchScreenSideEffect
    data object ShowNoDestinationSelectedMsg : SearchScreenSideEffect
    data object ShowInvalidDateTimeMsg : SearchScreenSideEffect
    data class ShowError(val error: Error) : SearchScreenSideEffect
}