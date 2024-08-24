package presentation.home_screen

import util.error.DatabaseError
import util.error.NetworkError

sealed interface HomeScreenSideEffect {
    data object ShowNoStopFoundMsg: HomeScreenSideEffect
    data object ShowInvalidDateTimeMsg : HomeScreenSideEffect
    data class ShowDatabaseError(val error: DatabaseError) : HomeScreenSideEffect
    data class ShowNetworkError(val error: NetworkError) : HomeScreenSideEffect
}