package presentation.stop_monitor

import util.error.NetworkError

sealed interface StopMonitorSideEffect {
    data class ShowNetworkError(val error: NetworkError) : StopMonitorSideEffect
}