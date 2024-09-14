package de.awolf.trip.kmp.search.presentation

import de.awolf.trip.kmp.core.domain.models.PickableDateTime
import de.awolf.trip.kmp.core.domain.models.Stop
import de.awolf.trip.kmp.search.domain.models.StopListSource

data class SearchScreenState(
    val searchText: String = "",
    val selectedDateTime: PickableDateTime = PickableDateTime(),
    val stopList: List<Stop> = emptyList(),
    val stopListSource: StopListSource = StopListSource.NONE,
)