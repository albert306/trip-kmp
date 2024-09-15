package de.awolf.trip.kmp.departures.presentation.search_screen

import de.awolf.trip.kmp.core.domain.models.Stop
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

sealed class SearchScreenEvent {
    data class Search(val text: String) : SearchScreenEvent()
    data class StartStopMonitor(val stop: Stop?) : SearchScreenEvent()
    data class ToggleFavorite(val stop: Stop) : SearchScreenEvent()
    data class ReorderFavoriteStop(val stopId: String, val from: Int, val to: Int) : SearchScreenEvent()
    data class ChangeSelectedDate(val date: LocalDate) : SearchScreenEvent()
    data class ChangeSelectedTime(val time: LocalTime) : SearchScreenEvent()
    data object ResetSelectedDateTime : SearchScreenEvent()
}

