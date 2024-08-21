package presentation.home_screen

import domain.models.Stop
import domain.models.StopListSource
import domain.use_case.UseCases
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import util.CoroutineViewModel
import util.Result
import domain.models.PickableDateTime
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.datetime.Clock

@OptIn(FlowPreview::class)
class HomeScreenViewModel(
    private val onStopClicked: (Stop, Instant) -> Unit,
) : CoroutineViewModel(), KoinComponent {

    private val useCases: UseCases by inject()

    private val _state = MutableStateFlow(HomeScreenState())
    val state = _state.asStateFlow()

    private val _sideEffect = Channel<HomeScreenSideEffect>(Channel.BUFFERED)
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

    fun onEvent(event: HomeScreenEvent) {
        when (event) {

            is HomeScreenEvent.Search -> {
                _state.value = state.value.copy(searchText = event.text)
            }

            is HomeScreenEvent.StartStopMonitor -> {
                val stop = event.stop ?: state.value.stopList.firstOrNull()
                if (stop == null) { return } //TODO(give user feedback that no such stop was found)


                val queriedTime = if (state.value.selectedDateTime.dateTimeIsValid()) {
                    state.value.selectedDateTime.toInstant()
                } else {
                    coroutineScope.launch {
                        _sideEffect.send(HomeScreenSideEffect.ShowCorrectedDateTimeMsg)
                    }
                    Clock.System.now()
                }

                onStopClicked(stop, queriedTime)
            }

            is HomeScreenEvent.ToggleFavorite -> toggleFavoriteStop(event.stop)

            is HomeScreenEvent.ReorderFavoriteStop -> reorderFavoriteStop(event.stopId, event.from, event.to)

            is HomeScreenEvent.ChangeSelectedDate -> {
                _state.value = state.value.copy(
                    selectedDateTime = state.value.selectedDateTime.copy(date = event.date)
                )
            }

            is HomeScreenEvent.ChangeSelectedTime -> {
                _state.value = state.value.copy(
                    selectedDateTime = state.value.selectedDateTime.copy(time = event.time)
                )
            }

            is HomeScreenEvent.ResetSelectedDateTime -> {
                _state.value = _state.value.copy(
                    selectedDateTime = PickableDateTime()
                )
            }
        }
    }


    private fun toggleFavoriteStop(stop: Stop) {
        coroutineScope.launch {
            useCases.toggleFavoriteStopUseCase(stop)

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
        val resultList = when (val recommendedStopsResult = useCases.findStopByQueryUseCase(query)) {
            is Result.Error -> {
                _sideEffect.send(HomeScreenSideEffect.ShowError(recommendedStopsResult.error))
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
        val resultList = when (val favoriteStopsResult = useCases.getFavoriteStopsUseCase()) {
            is Result.Error -> {
                _sideEffect.send(HomeScreenSideEffect.ShowError(favoriteStopsResult.error))
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