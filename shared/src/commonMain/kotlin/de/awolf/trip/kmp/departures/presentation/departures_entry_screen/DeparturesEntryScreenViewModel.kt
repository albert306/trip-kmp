package de.awolf.trip.kmp.departures.presentation.departures_entry_screen

import de.awolf.trip.kmp.core.domain.models.Stop
import de.awolf.trip.kmp.core.domain.models.StopListSource
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import de.awolf.trip.kmp.core.util.CoroutineViewModel
import de.awolf.trip.kmp.core.util.Result
import de.awolf.trip.kmp.core.domain.models.PickableDateTime
import de.awolf.trip.kmp.core.domain.use_cases.CoreUseCases
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow

@OptIn(FlowPreview::class)
class DeparturesEntryScreenViewModel(
    private val onStopClicked: (Stop, PickableDateTime) -> Unit,
) : CoroutineViewModel(), KoinComponent {

    private val useCases: CoreUseCases by inject()

    private val _state = MutableStateFlow(DeparturesEntryScreenState())
    val state = _state.asStateFlow()

    private val _sideEffect = Channel<DeparturesEntryScreenSideEffect>(Channel.BUFFERED)
    val sideEffect = _sideEffect.receiveAsFlow()

    init {
        state
            .map { it.searchText }
            .distinctUntilChanged()
            .debounce(100L)
            .onEach { text ->
                if (text.length < 3 && state.value.stopListSource != StopListSource.FAVORITES) {
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

    fun onEvent(event: DeparturesEntryScreenEvent) {
        when (event) {

            is DeparturesEntryScreenEvent.DeparturesEntry -> {
                _state.value = state.value.copy(searchText = event.text)
            }

            is DeparturesEntryScreenEvent.StartStopMonitor -> {
                val stop = event.stop ?: state.value.stopList.firstOrNull()
                if (stop == null) {
                    coroutineScope.launch {
                        _sideEffect.send(DeparturesEntryScreenSideEffect.ShowNoStopFoundMsg)
                    }
                    return
                }

                if (!state.value.selectedDateTime.dateTimeIsValid()) {
                    coroutineScope.launch {
                        _sideEffect.send(DeparturesEntryScreenSideEffect.ShowInvalidDateTimeMsg)
                    }
                    return
                }

                onStopClicked(stop, state.value.selectedDateTime)
            }

            is DeparturesEntryScreenEvent.ToggleFavorite -> toggleFavoriteStop(event.stop)

            is DeparturesEntryScreenEvent.ReorderFavoriteStop -> reorderFavoriteStop(event.stopId, event.from, event.to)

            is DeparturesEntryScreenEvent.ChangeSelectedDate -> {
                _state.value = state.value.copy(
                    selectedDateTime = state.value.selectedDateTime.copy(date = event.date)
                )
            }

            is DeparturesEntryScreenEvent.ChangeSelectedTime -> {
                _state.value = state.value.copy(
                    selectedDateTime = state.value.selectedDateTime.copy(time = event.time)
                )
            }

            is DeparturesEntryScreenEvent.ResetSelectedDateTime -> {
                _state.value = _state.value.copy(
                    selectedDateTime = PickableDateTime()
                )
            }
        }
    }


    private fun toggleFavoriteStop(stop: Stop) {
        coroutineScope.launch {
            useCases.toggleFavoriteStop(stop)

            val updatedStopList = if (stop.isFavorite && state.value.stopListSource == StopListSource.FAVORITES) {
                state.value.stopList.filter { it.id != stop.id }
            } else {
                state.value.stopList.map {
                    if (it.id == stop.id) {
                        it.copy(isFavorite = !it.isFavorite)
                    } else {
                        it
                    }
                }
            }

            _state.value = state.value.copy(
                stopList = updatedStopList
            )
        }
    }

    private fun reorderFavoriteStop(stopId: String, from: Int, to: Int) {
        coroutineScope.launch {
            useCases.reorderFavoriteStops(stopId, from.toLong(), to.toLong())

            val mutList = state.value.stopList.toMutableList()
            mutList.apply {
                add(to, removeAt(from))
            }

            _state.value = state.value.copy(stopList = mutList)
        }
    }

    private suspend fun setStopsByQuery(query: String) {
        val resultList = when (val recommendedStopsResult = useCases.findStopByQuery(query)) {
            is Result.Error -> {
                _sideEffect.send(DeparturesEntryScreenSideEffect.ShowError(recommendedStopsResult.error))
                emptyList()
            }

            is Result.Success -> {
                recommendedStopsResult.data.stops
            }
        }
        _state.value = state.value.copy(
            stopList = resultList,
            stopListSource = StopListSource.SEARCH
        )
    }

    private suspend fun setFavoriteStops() {
        val resultList = when (val favoriteStopsResult = useCases.getFavoriteStops()) {
            is Result.Error -> {
                _sideEffect.send(DeparturesEntryScreenSideEffect.ShowError(favoriteStopsResult.error))
                emptyList()
            }
            is Result.Success -> {
                favoriteStopsResult.data
            }
        }
        _state.value = state.value.copy(
            stopList = resultList,
            stopListSource = StopListSource.FAVORITES
        )
    }
}