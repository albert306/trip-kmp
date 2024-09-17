package de.awolf.trip.kmp.trips.presentation.search_screen

import de.awolf.trip.kmp.core.domain.models.PickableDateTime
import de.awolf.trip.kmp.core.domain.models.Stop
import de.awolf.trip.kmp.trips.domain.models.Trip

data class SearchScreenState(
    val originText: String = "",
    val destinationText: String = "",
    val origin: Stop? = null,
    val destination: Stop? = null,
    val selectedDateTime: PickableDateTime = PickableDateTime(),
    val isArrival: Boolean = false,
    val searchResultList: List<Stop> = emptyList(),
    val favoriteStops: List<Stop> = emptyList(),
    val favoriteTrips: List<Trip> = emptyList()
)