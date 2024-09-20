package de.awolf.trip.kmp.trips.presentation.trips_entry_screen

import de.awolf.trip.kmp.core.domain.models.Stop
import de.awolf.trip.kmp.trips.domain.models.TripQuery

data class TripsEntryScreenState(
    val originText: String = "",
    val destinationText: String = "",
    val viaText: String = "",
    val tripQuery: TripQuery = TripQuery(
        origin = "",
        destination = ""
    ),
    val searchResultList: List<Stop> = emptyList(),
    val favoriteStops: List<Stop> = emptyList(),
)