package de.awolf.trip.kmp.trips.presentation.search_screen

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
class SearchScreenViewModel(
    private val onSubmitClicked: (TripQuery) -> Unit,
) : CoroutineViewModel(), KoinComponent {

    private val useCases: CoreUseCases by inject()

    private val _state = MutableStateFlow(SearchScreenState())
    val state = _state.asStateFlow()

    private val _sideEffect = Channel<SearchScreenSideEffect>(Channel.BUFFERED)
    val sideEffect = _sideEffect.receiveAsFlow()

    private val searchText = MutableStateFlow("")

    init {
        searchText
            .debounce(100L)
            .onEach {
                if (it.length < 3) {
                    setFavoriteStops()
                }
            }
            .filter { it.length >= 3 } // Vvo api only returns results for 3 or more characters
            .onEach { text ->
                setStopsByQuery(text)
            }
            .launchIn(
                coroutineScope
            )
    }

    fun onEvent(event: SearchScreenEvent) {
        coroutineScope.launch {
            when (event) {
                is SearchScreenEvent.OriginTextChange -> {
                    searchText.value = event.text
                }

                is SearchScreenEvent.ViaTextChange -> {
                    searchText.value = event.text
                }

                is SearchScreenEvent.DestinationTextChange -> {
                    searchText.value = event.text
                }

                is SearchScreenEvent.SetAsOrigin -> {
                    _state.value = state.value.copy(
                        tripQuery = state.value.tripQuery.copy(origin = event.stop.id),
                    )
                }

                is SearchScreenEvent.SetAsVia ->  {
                    _state.value = state.value.copy(
                        tripQuery = state.value.tripQuery.copy(via = event.stop.id),
                    )
                }

                is SearchScreenEvent.SetAsDestination -> {
                    _state.value = state.value.copy(
                        tripQuery = state.value.tripQuery.copy(destination = event.stop.id),
                    )
                }

                is SearchScreenEvent.ChangeSelectedDate -> {
                    _state.value = state.value.copy(
                        tripQuery = state.value.tripQuery.copy(
                            time = state.value.tripQuery.time.copy(date = event.date)
                        ),
                    )
                }

                is SearchScreenEvent.ChangeSelectedTime -> {
                    _state.value = state.value.copy(
                        tripQuery = state.value.tripQuery.copy(
                            time = state.value.tripQuery.time.copy(time = event.time)
                        ),
                    )
                }

                is SearchScreenEvent.ResetSelectedDateTime -> {
                    _state.value = state.value.copy(
                        tripQuery = state.value.tripQuery.copy(
                            time = PickableDateTime()
                        ),
                    )
                }

                is SearchScreenEvent.ToggleIsArrival -> {
                    _state.value = state.value.copy(
                        tripQuery = state.value.tripQuery.copy(
                            isArrivalTime = !state.value.tripQuery.isArrivalTime
                        ),
                    )
                }

                is SearchScreenEvent.ChangeDurationOfStay -> {
                    _state.value = state.value.copy(
                        tripQuery = state.value.tripQuery.copy(
                            stayDuration = event.duration
                        ),
                    )
                }

                is SearchScreenEvent.Submit -> submit()

                is SearchScreenEvent.ToggleFavoriteStop -> {
                    useCases.toggleFavoriteStop(event.stop)
                    setFavoriteStops()
                }

                is SearchScreenEvent.ReorderFavoriteStop -> {
                    useCases.reorderFavoriteStops(event.stopId, event.from.toLong(), event.to.toLong())
                    setFavoriteStops()
                }
            }
        }
    }

    private suspend fun submit() {
        if (state.value.tripQuery.origin.isEmpty()) {
            _sideEffect.send(SearchScreenSideEffect.ShowNoOriginSelectedMsg)
            return
        }
        if (state.value.tripQuery.destination.isEmpty()) {
            _sideEffect.send(SearchScreenSideEffect.ShowNoDestinationSelectedMsg)
            return
        }
        if (!state.value.tripQuery.time.dateTimeIsValid()) {
            _sideEffect.send(SearchScreenSideEffect.ShowInvalidDateTimeMsg)
            return
        }

        onSubmitClicked(state.value.tripQuery)
    }

    private suspend fun setStopsByQuery(query: String) {
        val resultList = when (val recommendedStopsResult = useCases.findStopByQuery(query)) {
            is Result.Error -> {
                _sideEffect.send(SearchScreenSideEffect.ShowNetworkError(recommendedStopsResult.error))
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
                _sideEffect.send(SearchScreenSideEffect.ShowDatabaseError(favoriteStopsResult.error))
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