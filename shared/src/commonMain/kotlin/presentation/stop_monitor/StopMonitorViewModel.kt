package presentation.stop_monitor

import domain.models.Departure
import domain.models.Stop
import domain.use_case.UseCases
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import util.CoroutineViewModel
import util.Result

class StopMonitorViewModel(
    val stop: Stop,
    val queriedTime: Instant,
    private val onCloseClicked: () -> Unit,
) : CoroutineViewModel(), KoinComponent {

    private val useCases: UseCases by inject()

    private val _state = MutableStateFlow(StopMonitorState())
    val state = _state.asStateFlow()

    private val _sideEffect = Channel<StopMonitorSideEffect>(Channel.BUFFERED)
    val sideEffect = _sideEffect.receiveAsFlow()

    init {
        updateDepartures(true)
    }

    fun onEvent(event: StopMonitorEvent) {
        when (event) {
            is StopMonitorEvent.ToggleExpandedStopInfo -> {
                _state.value = state.value.copy(
                    isStopInfoCardExpanded = !state.value.isStopInfoCardExpanded
                )
            }

            is StopMonitorEvent.Close -> onCloseClicked()

            is StopMonitorEvent.UpdateDepartures -> updateDepartures(true)

            is StopMonitorEvent.IncreaseDepartureCount -> {
                _state.value = state.value.copy(
                    queriedDepartureCount = state.value.queriedDepartureCount + 10
                )
                updateDepartures(false)
            }

            is StopMonitorEvent.ToggleStopSchedule -> {
                val isVisible = state.value.detailVisibility[event.departure] ?: false
                if (isVisible) {
                    _state.value = state.value.copy(
                        detailVisibility = state.value.detailVisibility + (event.departure to false)
                    )
                } else {
                    showStopSchedule(event.departure)
                }
            }
        }
    }


    private fun updateDepartures(showRefreshingIndicator: Boolean = true) {
        coroutineScope.launch {
            if (showRefreshingIndicator)
                _state.value = state.value.copy(isRefreshing = true)

            val departures: List<Departure>
            val maxDepartureCount: Int?

            val stopMonitorInfoResource = useCases.getStopMonitorUseCase(
                stop = stop,
                time = queriedTime,
                limit = state.value.queriedDepartureCount,
            )
            when (stopMonitorInfoResource) {
                is Result.Error -> {
                    _sideEffect.send(StopMonitorSideEffect.ShowNetworkError(stopMonitorInfoResource.error))
                    departures = emptyList()
                    maxDepartureCount = 0
                }
                is Result.Success -> {
                    departures = stopMonitorInfoResource.data.departures
                    maxDepartureCount = stopMonitorInfoResource.data.maxDepartureCount
                }
            }
            _state.value = state.value.copy(
                departures = departures,
                maxDepartureCount = maxDepartureCount
            )

            if (showRefreshingIndicator) {
                delay(300)
                _state.value = state.value.copy(isRefreshing = false)
            }
        }
    }

    private fun showStopSchedule(departure: Departure) {

        coroutineScope.launch {
            val stopScheduleResource = useCases.getStopSchedule(
                departure = departure,
                stopId = stop.id
            )
            val updatedDepartures = when (stopScheduleResource) {
                is Result.Error -> {
                    _sideEffect.send(StopMonitorSideEffect.ShowNetworkError(stopScheduleResource.error))

                    state.value.departures
                }

                is Result.Success -> {
                    state.value.departures.map {
                        if (it == departure) {
                            it.copy(
                                stopSchedule = stopScheduleResource.data
                            )
                        } else {
                            it
                        }
                    }
                }

            }
            _state.value = state.value.copy(
                departures = updatedDepartures,
                detailVisibility = state.value.detailVisibility + (departure to true)
            )
        }
    }
}