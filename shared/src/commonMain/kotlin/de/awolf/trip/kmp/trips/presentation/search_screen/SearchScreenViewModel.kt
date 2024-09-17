package de.awolf.trip.kmp.trips.presentation.search_screen

import de.awolf.trip.kmp.core.domain.models.PickableDateTime
import de.awolf.trip.kmp.core.domain.use_cases.CoreUseCases
import de.awolf.trip.kmp.core.util.CoroutineViewModel
import de.awolf.trip.kmp.core.util.Result
import de.awolf.trip.kmp.trips.domain.models.Trip
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@OptIn(FlowPreview::class)
class SearchScreenViewModel(
    private val onSubmitClicked: (Trip, PickableDateTime, Boolean) -> Unit,
) : CoroutineViewModel(), KoinComponent {

    private val useCases: CoreUseCases by inject()

    private val _state = MutableStateFlow(SearchScreenState())
    val state = _state.asStateFlow()

    private val _sideEffect = Channel<SearchScreenSideEffect>(Channel.BUFFERED)
    val sideEffect = _sideEffect.receiveAsFlow()

    init {
        state
            .map { it.originText }
            .distinctUntilChanged()
            .debounce(100L)
            .onEach { text ->
                if (text.length < 3) {
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

        state
            .map { it.destinationText }
            .distinctUntilChanged()
            .debounce(100L)
            .onEach { text ->
                if (text.length < 3) {
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
                    _state.value = state.value.copy(originText = event.text)
                }

                is SearchScreenEvent.DestinationTextChange -> {
                    _state.value = state.value.copy(destinationText = event.text)
                }

                is SearchScreenEvent.SetAsOrigin -> {
                    _state.value = state.value.copy(origin = event.stop)
                }

                is SearchScreenEvent.SetAsDestination -> {
                    _state.value = state.value.copy(destination = event.stop)
                }

                is SearchScreenEvent.ChangeSelectedDate -> {
                    _state.value = state.value.copy(
                        selectedDateTime = state.value.selectedDateTime.copy(date = event.date)
                    )
                }

                is SearchScreenEvent.ChangeSelectedTime -> {
                    _state.value = state.value.copy(
                        selectedDateTime = state.value.selectedDateTime.copy(time = event.time)
                    )
                }

                is SearchScreenEvent.ResetSelectedDateTime -> {
                    _state.value = state.value.copy(
                        selectedDateTime = PickableDateTime()
                    )
                }

                is SearchScreenEvent.ToggleIsArrival -> {
                    _state.value = state.value.copy(
                        isArrival = !state.value.isArrival
                    )
                }

                is SearchScreenEvent.Submit -> submit()

                is SearchScreenEvent.ToggleFavoriteStop -> {
                    useCases.toggleFavoriteStop(event.stop)
                    setFavoriteStops()
                }

                is SearchScreenEvent.ToggleFavoriteTrip -> TODO()

                is SearchScreenEvent.ReorderFavoriteStop -> {
                    useCases.reorderFavoriteStops(event.stopId, event.from.toLong(), event.to.toLong())
                    setFavoriteStops()
                }

                is SearchScreenEvent.ReorderFavoriteTrip -> TODO()
            }
        }
    }

    private suspend fun submit() {
        val origin = state.value.origin
        if (origin == null) {
            _sideEffect.send(SearchScreenSideEffect.ShowNoOriginSelectedMsg)
            return
        }
        val destination = state.value.destination
        if (destination == null) {
            _sideEffect.send(SearchScreenSideEffect.ShowNoDestinationSelectedMsg)
            return
        }
        val selectedDateTime = state.value.selectedDateTime
        if (!selectedDateTime.dateTimeIsValid()) {
            _sideEffect.send(SearchScreenSideEffect.ShowInvalidDateTimeMsg)
            return
        }

        onSubmitClicked(
            Trip(origin, destination),
            selectedDateTime,
            state.value.isArrival
        )
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