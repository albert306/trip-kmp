package de.awolf.trip.kmp.trips.presentation.search_screen

import de.awolf.trip.kmp.core.domain.models.Stop
import de.awolf.trip.kmp.trips.domain.models.Trip
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

sealed class SearchScreenEvent {
    data class OriginTextChange(val text: String) : SearchScreenEvent()
    data class DestinationTextChange(val text: String) : SearchScreenEvent()
    data class SetAsOrigin(val stop: Stop) : SearchScreenEvent()
    data class SetAsDestination(val stop: Stop) : SearchScreenEvent()
    data class ChangeSelectedDate(val date: LocalDate) : SearchScreenEvent()
    data class ChangeSelectedTime(val time: LocalTime) : SearchScreenEvent()
    data object ResetSelectedDateTime : SearchScreenEvent()
    data object ToggleIsArrival : SearchScreenEvent()
    data object Submit : SearchScreenEvent()
    data class ToggleFavoriteStop(val stop: Stop) : SearchScreenEvent()
    data class ToggleFavoriteTrip(val trip: Trip) : SearchScreenEvent()
    data class ReorderFavoriteStop(val stopId: String, val from: Int, val to: Int) : SearchScreenEvent()
    data class ReorderFavoriteTrip(val trip: Trip, val from: Int, val to: Int) : SearchScreenEvent()
}

