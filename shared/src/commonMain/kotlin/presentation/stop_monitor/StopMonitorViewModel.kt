package presentation.stop_monitor

import domain.models.Departure
import domain.models.Stop
import domain.models.StopScheduleItem
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

            is StopMonitorEvent.DepartureDetails -> showDepartureDetails(event.departure, event.detailLevel)
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

    private fun showDepartureDetails(departure: Departure, detailLevel: DepartureDetailLevel) {
        coroutineScope.launch {
            _state.value = state.value.copy(
                detailVisibility = state.value.detailVisibility +
                        (departure to DepartureDetailLevel.PLATFORM)
            )

            if (detailLevel < DepartureDetailLevel.STOP_SCHEDULE)
                return@launch

            if (departure.stopSchedule != null) {
                _state.value = state.value.copy(
                    detailVisibility = state.value.detailVisibility + (departure to detailLevel)
                )
                return@launch
            }

            fetchStopSchedule(departure).let { stopSchedule ->
                val updatedDepartures = state.value.departures.map {
                    if (it == departure) {
                        it.copy(stopSchedule = stopSchedule)
                    } else {
                        it
                    }
                }
                _state.value = state.value.copy(
                    departures = updatedDepartures,
                    detailVisibility = state.value.detailVisibility + (departure to detailLevel)
                )
            }
        }
    }

    private suspend fun fetchStopSchedule(
        departure: Departure,
    ): List<StopScheduleItem>? {

        return if (departure.stopSchedule != null) {
            departure.stopSchedule
        } else {
            val stopScheduleResource = useCases.getStopSchedule(
                departure = departure,
                stopId = stop.id
            )
            when (stopScheduleResource) {
                is Result.Error -> {
                    _sideEffect.send(StopMonitorSideEffect.ShowNetworkError(stopScheduleResource.error))
                    null
                }

                is Result.Success -> {
                    stopScheduleResource.data
                }
            }
        }
    }
}