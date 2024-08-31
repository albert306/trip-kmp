package presentation.stop_monitor

import domain.models.Departure

data class StopMonitorState(
    val isStopInfoCardExpanded: Boolean = false,
    val isRefreshing: Boolean = false,
    val departures: List<Departure> = emptyList(),
    val detailVisibility: Map<Departure, DepartureDetailLevel> = emptyMap(),
    val queriedDepartureCount: Int = 30,
    val maxDepartureCount: Int = Int.MAX_VALUE,
)