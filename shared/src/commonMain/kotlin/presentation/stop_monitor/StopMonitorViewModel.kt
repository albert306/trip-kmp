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
            StopMonitorEvent.ToggleExpandedStopInfo -> {
                _state.value = _state.value.copy(
                    isStopInfoCardExpanded = !_state.value.isStopInfoCardExpanded
                )
            }

            StopMonitorEvent.Close -> onCloseClicked()

            StopMonitorEvent.UpdateDepartures -> updateDepartures(true)

            StopMonitorEvent.IncreaseDepartureCount -> {
                _state.value = _state.value.copy(
                    queriedDepartureCount = state.value.queriedDepartureCount + 10
                )
                updateDepartures(false)
            }
        }
    }


    private fun updateDepartures(showRefreshingIndicator: Boolean = true) {
        coroutineScope.launch {
            if (showRefreshingIndicator)
                _state.value = _state.value.copy(isRefreshing = true)

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
            _state.value = _state.value.copy(
                departures = departures,
                maxDepartureCount = maxDepartureCount
            )

            if (showRefreshingIndicator) {
                delay(300)
                _state.value = _state.value.copy(isRefreshing = false)
            }
        }
    }

//    fun toggleVisibilityDetailedStopSchedule(departureIndex: Int) {
//        coroutineScope.launch {
//            _departures.update { currentDepartures ->
//                when (currentDepartures[departureIndex].isShowingDetailedStopSchedule) {
//                    true -> {
//                        currentDepartures[departureIndex].isShowingDetailedStopSchedule = false
//                    }
//                    false -> {
//                        currentDepartures[departureIndex].isShowingDetailedStopSchedule = true
//                        val detailedStopScheduleResource = useCases.getDetailedStopSchedule(
//                            departure = currentDepartures[departureIndex],
//                            stopId = stop.id
//                        )
//                        when (detailedStopScheduleResource) {
//                            is Result.Error -> {
//                                println("TOAST: " + detailedStopScheduleResource.message)
//                                currentDepartures[departureIndex].detailedStopSchedule = null
//                            }
//
//                            is Result.Success -> {
//                                currentDepartures[departureIndex].detailedStopSchedule = detailedStopScheduleResource.data!!.filter { it.shedulePosition == "Next" }
//                            }
//                        }
//                    }
//                }
//                currentDepartures
//            }
//        }
//    }
}