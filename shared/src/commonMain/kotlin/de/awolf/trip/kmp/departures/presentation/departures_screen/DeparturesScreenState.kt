package de.awolf.trip.kmp.departures.presentation.departures_screen

import de.awolf.trip.kmp.departures.domain.models.Departure

data class DeparturesScreenState(
    val isStopInfoCardExpanded: Boolean = false,
    val isRefreshing: Boolean = false,
    val departures: List<Departure> = emptyList(),
    val detailVisibility: Map<Departure, DepartureDetailLevel> = emptyMap(),
    val queriedDepartureCount: Int = 30,
    val maxDepartureCount: Int = Int.MAX_VALUE,
)