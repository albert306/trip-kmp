package presentation.stop_monitor

import domain.models.Departure

sealed class StopMonitorEvent {
    data object ToggleExpandedStopInfo : StopMonitorEvent()
    data object Close : StopMonitorEvent()
    data object UpdateDepartures : StopMonitorEvent()
    data object IncreaseDepartureCount : StopMonitorEvent()
    data class ToggleStopSchedule(val departure: Departure) : StopMonitorEvent()
}