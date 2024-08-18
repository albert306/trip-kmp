package presentation.home_screen

import domain.models.Stop
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

sealed class HomeScreenEvent {
    data class Search(val text: String) : HomeScreenEvent()
    data class StartStopMonitor(val stop: Stop?) : HomeScreenEvent()
    data class ToggleFavorite(val stop: Stop) : HomeScreenEvent()
    data class ReorderFavoriteStop(val stopId: String, val from: Int, val to: Int) : HomeScreenEvent()
    data class ChangeSelectedDate(val date: LocalDate) : HomeScreenEvent()
    data class ChangeSelectedTime(val time: LocalTime) : HomeScreenEvent()
    data object ResetSelectedDateTime : HomeScreenEvent()
}

