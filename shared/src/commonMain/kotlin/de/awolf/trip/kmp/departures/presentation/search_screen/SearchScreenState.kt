package de.awolf.trip.kmp.departures.presentation.search_screen

import de.awolf.trip.kmp.core.domain.models.PickableDateTime
import de.awolf.trip.kmp.core.domain.models.Stop
import de.awolf.trip.kmp.core.domain.models.StopListSource

data class SearchScreenState(
    val searchText: String = "",
    val selectedDateTime: PickableDateTime = PickableDateTime(),
    val stopList: List<Stop> = emptyList(),
    val stopListSource: StopListSource = StopListSource.NONE,
)