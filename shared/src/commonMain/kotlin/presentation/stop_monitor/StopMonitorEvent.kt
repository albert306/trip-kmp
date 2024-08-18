package presentation.stop_monitor

sealed class StopMonitorEvent {
    data object ToggleExpandedStopInfo : StopMonitorEvent()
    data object Close : StopMonitorEvent()
    data object UpdateDepartures : StopMonitorEvent()
    data object IncreaseDepartureCount : StopMonitorEvent()
}