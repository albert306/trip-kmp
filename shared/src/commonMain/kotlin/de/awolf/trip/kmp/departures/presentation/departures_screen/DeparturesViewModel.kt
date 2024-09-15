package de.awolf.trip.kmp.departures.presentation.departures_screen

import de.awolf.trip.kmp.departures.domain.models.Departure
import de.awolf.trip.kmp.core.domain.models.Stop
import de.awolf.trip.kmp.departures.domain.models.StopScheduleItem
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import de.awolf.trip.kmp.core.util.CoroutineViewModel
import de.awolf.trip.kmp.core.util.Result
import de.awolf.trip.kmp.departures.domain.use_cases.DeparturesUseCases

class DeparturesViewModel(
    val stop: Stop,
    val queriedTime: Instant,
    private val onCloseClicked: () -> Unit,
) : CoroutineViewModel(), KoinComponent {

    private val useCases: DeparturesUseCases by inject()

    private val _state = MutableStateFlow(DeparturesScreenState())
    val state = _state.asStateFlow()

    private val _sideEffect = Channel<DeparturesScreenSideEffect>(Channel.BUFFERED)
    val sideEffect = _sideEffect.receiveAsFlow()

    init {
        updateDepartures(true)
    }

    fun onEvent(event: DeparturesScreenEvent) {
        when (event) {
            is DeparturesScreenEvent.ToggleExpandedStopInfo -> {
                _state.value = state.value.copy(
                    isStopInfoCardExpanded = !state.value.isStopInfoCardExpanded
                )
            }

            is DeparturesScreenEvent.Close -> onCloseClicked()

            is DeparturesScreenEvent.UpdateDeparturesScreen -> updateDepartures(true)

            is DeparturesScreenEvent.IncreaseDepartureCount -> {
                _state.value = state.value.copy(
                    queriedDepartureCount = state.value.queriedDepartureCount + 10
                )
                updateDepartures(false)
            }

            is DeparturesScreenEvent.DepartureDetails -> showDepartureDetails(event.departure, event.detailLevel)
        }
    }


    private fun updateDepartures(showRefreshingIndicator: Boolean = true) {
        coroutineScope.launch {
            if (showRefreshingIndicator)
                _state.value = state.value.copy(isRefreshing = true)

            val departures: List<Departure>
            val maxDepartureCount: Int?

            val stopMonitorInfoResource = useCases.fetchDepartures(
                stop = stop,
                time = queriedTime,
                limit = state.value.queriedDepartureCount,
            )
            when (stopMonitorInfoResource) {
                is Result.Error -> {
                    _sideEffect.send(
                        DeparturesScreenSideEffect.ShowNetworkError(
                            stopMonitorInfoResource.error
                        )
                    )
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
            val initialDetailLevel = if (detailLevel == DepartureDetailLevel.NONE)
                DepartureDetailLevel.NONE
            else
                DepartureDetailLevel.PLATFORM

            _state.value = state.value.copy(
                detailVisibility = state.value.detailVisibility + (departure to initialDetailLevel)
            )

            if (detailLevel < DepartureDetailLevel.STOP_SCHEDULE)
                return@launch

            if (departure.stopSchedule != null) {
                _state.value = state.value.copy(
                    detailVisibility = state.value.detailVisibility + (departure to detailLevel)
                )
                return@launch
            }

            fetchStopSchedule(departure)?.let { stopSchedule ->
                val currentStop = stopSchedule.firstOrNull {
                    it.schedulePosition == StopScheduleItem.SchedulePosition.CURRENT
                }

                val updatedDepartures = state.value.departures.map {
                    if (it == departure) {
                        it.copy(
                            realTime = currentStop?.realTime ?: it.realTime,
                            stopSchedule = stopSchedule
                        )
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
            val stopScheduleResource = useCases.fetchStopSchedule(
                departure = departure,
                stopId = stop.id
            )
            when (stopScheduleResource) {
                is Result.Error -> {
                    _sideEffect.send(
                        DeparturesScreenSideEffect.ShowNetworkError(
                            stopScheduleResource.error
                        )
                    )
                    null
                }

                is Result.Success -> {
                    stopScheduleResource.data
                }
            }
        }
    }
}