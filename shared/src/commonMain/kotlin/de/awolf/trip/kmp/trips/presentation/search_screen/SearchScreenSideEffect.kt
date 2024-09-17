package de.awolf.trip.kmp.trips.presentation.search_screen

import de.awolf.trip.kmp.core.util.error.DatabaseError
import de.awolf.trip.kmp.core.util.error.NetworkError

sealed interface SearchScreenSideEffect {
    data object ShowNoOriginSelectedMsg : SearchScreenSideEffect
    data object ShowNoDestinationSelectedMsg : SearchScreenSideEffect
    data object ShowInvalidDateTimeMsg : SearchScreenSideEffect
    data class ShowDatabaseError(val error: DatabaseError) : SearchScreenSideEffect
    data class ShowNetworkError(val error: NetworkError) : SearchScreenSideEffect
}