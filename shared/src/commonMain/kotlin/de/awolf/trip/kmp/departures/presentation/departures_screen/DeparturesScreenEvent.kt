package de.awolf.trip.kmp.departures.presentation.departures_screen

import de.awolf.trip.kmp.departures.domain.models.Departure

sealed class DeparturesScreenEvent {
    data object ToggleExpandedStopInfo : DeparturesScreenEvent()
    data object Close : DeparturesScreenEvent()
    data object UpdateDeparturesScreen : DeparturesScreenEvent()
    data object IncreaseDepartureCount : DeparturesScreenEvent()
    data class DepartureDetails(val departure: Departure, val detailLevel: DepartureDetailLevel) :
        DeparturesScreenEvent()
}