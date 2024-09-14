package de.awolf.trip.kmp.search.presentation

import de.awolf.trip.kmp.core.util.error.DatabaseError
import de.awolf.trip.kmp.core.util.error.NetworkError

sealed interface SearchScreenSideEffect {
    data object ShowNoStopFoundMsg: SearchScreenSideEffect
    data object ShowInvalidDateTimeMsg : SearchScreenSideEffect
    data class ShowDatabaseError(val error: DatabaseError) : SearchScreenSideEffect
    data class ShowNetworkError(val error: NetworkError) : SearchScreenSideEffect
}