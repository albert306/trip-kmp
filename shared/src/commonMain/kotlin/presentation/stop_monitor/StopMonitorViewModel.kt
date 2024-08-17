package presentation.stop_monitor

import domain.models.Departure
import domain.models.Stop
import domain.use_case.UseCases
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
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

    private val _isStopInfoCardExpanded = MutableStateFlow(false)
    val isStopInfoCardExpanded = _isStopInfoCardExpanded.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    private val _departures = MutableStateFlow(listOf<Departure>())
    val departures = _departures.asStateFlow()

    private val _departureCount = MutableStateFlow(30)
    val departureCount = _departureCount.asStateFlow()

    init {
        updateDepartures()
    }

    fun expandStopInfo() {
        _isStopInfoCardExpanded.value = !_isStopInfoCardExpanded.value
    }

    fun close() {
        onCloseClicked()
    }

    fun updateDepartures() {
        coroutineScope.launch {
            _isRefreshing.update { true }
            _departures.update {
                val stopMonitorInfoResource = useCases.getStopMonitorUseCase(
                    stop = stop,
                    time = queriedTime,
                    limit = departureCount.value,
                )
                when (stopMonitorInfoResource) {
                    is Result.Error -> {
                        // TODO: display error
                        emptyList()
                    }
                    is Result.Success -> {
                        stopMonitorInfoResource.data.departures
                    }
                }
            }
            delay(300)
            _isRefreshing.update { false }
        }
    }

    fun increaseDepartureCount() {
        _departureCount.value += 20
        coroutineScope.launch {
            _departures.update {
                val stopMonitorInfoResource = useCases.getStopMonitorUseCase(
                    stop = stop,
                    time = queriedTime,
                    limit = departureCount.value,
                )
                when (stopMonitorInfoResource) {
                    is Result.Error -> {
                        // TODO: display error
                        emptyList()
                    }
                    is Result.Success -> {
                        stopMonitorInfoResource.data.departures
                    }
                }
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