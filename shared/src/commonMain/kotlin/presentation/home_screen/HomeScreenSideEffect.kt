package presentation.home_screen

import util.error.Error

sealed class HomeScreenSideEffect {
    data object ShowCorrectedDateTimeMsg : HomeScreenSideEffect()
    data class ShowError(val error: Error) : HomeScreenSideEffect()
}