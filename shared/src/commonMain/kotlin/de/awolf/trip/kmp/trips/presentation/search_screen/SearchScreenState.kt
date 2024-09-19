package de.awolf.trip.kmp.trips.presentation.search_screen

import de.awolf.trip.kmp.core.domain.models.Stop
import de.awolf.trip.kmp.trips.domain.models.Trip
import de.awolf.trip.kmp.trips.domain.models.TripQuery

data class SearchScreenState(
    val originText: String = "",
    val destinationText: String = "",
    val viaText: String = "",
    val tripQuery: TripQuery = TripQuery(
        origin = "",
        destination = ""
    ),
    val searchResultList: List<Stop> = emptyList(),
    val favoriteStops: List<Stop> = emptyList(),
    val favoriteTrips: List<Trip> = emptyList()
)