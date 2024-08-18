package presentation.stop_monitor

import domain.models.Departure

data class StopMonitorState(
    val isStopInfoCardExpanded: Boolean = false,
    val isRefreshing: Boolean = false,
    val departures: List<Departure> = emptyList(),
    val departureCount: Int = 30,
)