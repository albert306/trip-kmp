package de.awolf.trip.kmp.trips.presentation.trips_entry_screen

import de.awolf.trip.kmp.core.domain.models.PickableDateTime
import de.awolf.trip.kmp.core.domain.use_cases.CoreUseCases
import de.awolf.trip.kmp.core.util.CoroutineViewModel
import de.awolf.trip.kmp.core.util.Result
import de.awolf.trip.kmp.trips.domain.models.TripQuery
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@OptIn(FlowPreview::class)
class TripsEntryScreenViewModel(
    private val onSubmitClicked: (TripQuery) -> Unit,
) : CoroutineViewModel(), KoinComponent {

    private val useCases: CoreUseCases by inject()

    private val _state = MutableStateFlow(TripsEntryScreenState())
    val state = _state.asStateFlow()

    private val _sideEffect = Channel<TripsEntryScreenSideEffect>(Channel.BUFFERED)
    val sideEffect = _sideEffect.receiveAsFlow()

    private val searchText = MutableStateFlow("")

    init {
        searchText
            .onEach {
                if (it.length < 3) {
                    setFavoriteStops()
                }
            }
            .filter { it.length >= 3 } // Vvo api only returns results for 3 or more characters
            .debounce(100L)
            .onEach { text ->
                setStopsByQuery(text)
            }
            .launchIn(
                coroutineScope
            )
    }

    fun onEvent(event: TripsEntryScreenEvent) {
        coroutineScope.launch {
            when (event) {
                is TripsEntryScreenEvent.FocusChange -> {
                    _state.value = state.value.copy(focusedField = event.field)
                    searchText.value = when (event.field) {
                        SearchField.ORIGIN -> state.value.originText
                        SearchField.VIA -> state.value.viaText
                        SearchField.DESTINATION -> state.value.destinationText
                        SearchField.NONE -> return@launch
                    }
                }

                is TripsEntryScreenEvent.TextChange -> {
                    val newTripQuery: TripQuery
                    when (event.field ?: state.value.focusedField) {
                        SearchField.ORIGIN -> {
                            _state.value = state.value.copy(originText = event.text)
                            newTripQuery = state.value.tripQuery.copy(origin = null)
                        }
                        SearchField.VIA -> {
                            _state.value = state.value.copy(viaText = event.text)
                            newTripQuery = state.value.tripQuery.copy(via = null)
                        }
                        SearchField.DESTINATION -> {
                            _state.value = state.value.copy(destinationText = event.text)
                            newTripQuery = state.value.tripQuery.copy(destination = null)
                        }
                        SearchField.NONE -> return@launch
                    }
                    _state.value = state.value.copy(tripQuery = newTripQuery)
                    searchText.value = event.text
                }

                is TripsEntryScreenEvent.SetStop -> {
                    val newQuery = when (event.field ?: state.value.focusedField) {
                        SearchField.ORIGIN -> state.value.tripQuery.copy(origin = event.stop)
                        SearchField.VIA -> state.value.tripQuery.copy(via = event.stop)
                        SearchField.DESTINATION -> state.value.tripQuery.copy(destination = event.stop)
                        SearchField.NONE -> return@launch
                    }
                    _state.value = state.value.copy(tripQuery = newQuery)
                }

                is TripsEntryScreenEvent.SwapOriginAndDestination -> {
                    val newQuery = state.value.tripQuery.copy(
                        origin = state.value.tripQuery.destination,
                        destination = state.value.tripQuery.origin
                    )
                    _state.value = state.value.copy(
                        originText = state.value.destinationText,
                        destinationText = state.value.originText,
                        tripQuery = newQuery
                    )
                }

                is TripsEntryScreenEvent.ToggleShowVia -> {
                    _state.value = state.value.copy(showVia = !state.value.showVia)
                }

                is TripsEntryScreenEvent.ChangeSelectedDate -> {
                    _state.value = state.value.copy(
                        tripQuery = state.value.tripQuery.copy(
                            time = state.value.tripQuery.time.copy(date = event.date)
                        ),
                    )
                }

                is TripsEntryScreenEvent.ChangeSelectedTime -> {
                    _state.value = state.value.copy(
                        tripQuery = state.value.tripQuery.copy(
                            time = state.value.tripQuery.time.copy(time = event.time)
                        ),
                    )
                }

                is TripsEntryScreenEvent.ResetSelectedDateTime -> {
                    _state.value = state.value.copy(
                        tripQuery = state.value.tripQuery.copy(
                            time = PickableDateTime()
                        ),
                    )
                }

                is TripsEntryScreenEvent.ToggleIsArrival -> {
                    _state.value = state.value.copy(
                        tripQuery = state.value.tripQuery.copy(
                            isArrivalTime = !state.value.tripQuery.isArrivalTime
                        ),
                    )
                }

                is TripsEntryScreenEvent.ChangeDurationOfStay -> {
                    _state.value = state.value.copy(
                        tripQuery = state.value.tripQuery.copy(
                            stayDuration = event.duration
                        ),
                    )
                }

                is TripsEntryScreenEvent.Submit -> submit()

                is TripsEntryScreenEvent.ToggleFavoriteStop -> {
                    useCases.toggleFavoriteStop(event.stop)
                    setFavoriteStops()
                }

                is TripsEntryScreenEvent.ReorderFavoriteStop -> {
                    useCases.reorderFavoriteStops(event.stopId, event.from.toLong(), event.to.toLong())
                    setFavoriteStops()
                }
            }
        }
    }

    private suspend fun submit() {
        if (state.value.tripQuery.origin == null) {
            _sideEffect.send(TripsEntryScreenSideEffect.ShowNoOriginSelectedMsg)
            return
        }
        if (state.value.tripQuery.destination == null) {
            _sideEffect.send(TripsEntryScreenSideEffect.ShowNoDestinationSelectedMsg)
            return
        }
        if (!state.value.tripQuery.time.dateTimeIsValid()) {
            _sideEffect.send(TripsEntryScreenSideEffect.ShowInvalidDateTimeMsg)
            return
        }
        if (!state.value.showVia) {
            onSubmitClicked(state.value.tripQuery.copy(via = null))
        } else {
            onSubmitClicked(state.value.tripQuery)
        }
    }

    private suspend fun setStopsByQuery(query: String) {
        val resultList = when (val recommendedStopsResult = useCases.findStopByQuery(query)) {
            is Result.Error -> {
                _sideEffect.send(TripsEntryScreenSideEffect.ShowError(recommendedStopsResult.error))
                emptyList()
            }

            is Result.Success -> {
                recommendedStopsResult.data.stops
            }
        }
        _state.value = state.value.copy(
            searchResultList = resultList,
        )
    }

    private suspend fun setFavoriteStops() {
        val resultList = when (val favoriteStopsResult = useCases.getFavoriteStops()) {
            is Result.Error -> {
                _sideEffect.send(TripsEntryScreenSideEffect.ShowError(favoriteStopsResult.error))
                emptyList()
            }
            is Result.Success -> {
                favoriteStopsResult.data
            }
        }
        _state.value = state.value.copy(
            searchResultList = resultList,
        )
    }
}