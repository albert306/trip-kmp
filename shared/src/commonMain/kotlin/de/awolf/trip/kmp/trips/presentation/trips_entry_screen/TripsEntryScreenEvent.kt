package de.awolf.trip.kmp.trips.presentation.trips_entry_screen

import de.awolf.trip.kmp.core.domain.models.Stop
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlin.time.Duration

sealed class TripsEntryScreenEvent {
    data class FocusChange(val field: SearchField) : TripsEntryScreenEvent()

    data class TextChange(val text: String, val field: SearchField? = null) : TripsEntryScreenEvent()

    data class SetStop(val stop: Stop, val field: SearchField? = null) : TripsEntryScreenEvent()

    data object ToggleShowVia : TripsEntryScreenEvent()
    data object SwapOriginAndDestination : TripsEntryScreenEvent()

    data class ChangeSelectedDate(val date: LocalDate) : TripsEntryScreenEvent()
    data class ChangeSelectedTime(val time: LocalTime) : TripsEntryScreenEvent()
    data object ResetSelectedDateTime : TripsEntryScreenEvent()
    data object ToggleIsArrival : TripsEntryScreenEvent()
    data class ChangeDurationOfStay(val duration: Duration) : TripsEntryScreenEvent()

    data object Submit : TripsEntryScreenEvent()

    data class ToggleFavoriteStop(val stop: Stop) : TripsEntryScreenEvent()
    data class ReorderFavoriteStop(val stopId: String, val from: Int, val to: Int) : TripsEntryScreenEvent()
}

