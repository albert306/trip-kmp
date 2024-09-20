package de.awolf.trip.kmp.departures.presentation.departures_entry_screen

import de.awolf.trip.kmp.core.domain.models.Stop
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

sealed class DeparturesEntryScreenEvent {
    data class DeparturesEntry(val text: String) : DeparturesEntryScreenEvent()
    data class StartStopMonitor(val stop: Stop?) : DeparturesEntryScreenEvent()
    data class ToggleFavorite(val stop: Stop) : DeparturesEntryScreenEvent()
    data class ReorderFavoriteStop(val stopId: String, val from: Int, val to: Int) : DeparturesEntryScreenEvent()
    data class ChangeSelectedDate(val date: LocalDate) : DeparturesEntryScreenEvent()
    data class ChangeSelectedTime(val time: LocalTime) : DeparturesEntryScreenEvent()
    data object ResetSelectedDateTime : DeparturesEntryScreenEvent()
}

