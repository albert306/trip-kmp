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
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import util.CoroutineViewModel
import util.Result
import domain.models.PickableDateTime
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

@OptIn(FlowPreview::class)
class HomeScreenViewModel(
    val onStopClicked: (Stop, Instant) -> Unit,
) : CoroutineViewModel(), KoinComponent {

    private val useCases: UseCases by inject()

    private val _state = MutableStateFlow(HomeScreenState())
    val state = _state.asStateFlow()


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


    fun onSearchTextChange(text: String) {
        _state.value = state.value.copy(searchText = text)
    }

    fun startStopMonitor(stop: Stop?) {
        if (stop == null) { return } //TODO(give user feedback that no such stop was found)

        onStopClicked(stop, state.value.selectedDateTime.toInstant())
    }

    fun toggleFavoriteStop(stop: Stop) {
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

    fun changeSelectedDate(date: LocalDate) {
        _state.value = state.value.copy(
            selectedDateTime = state.value.selectedDateTime.copy(date = date)
        )
    }

    fun changeSelectedTime(time: LocalTime) {
        _state.value = state.value.copy(
            selectedDateTime = state.value.selectedDateTime.copy(time = time)
        )
    }

    fun selectedDateTimeIsValid(): Boolean {
        if (_state.value.selectedDateTime.dateTimeIsValid()) {
            return true
        } else {
            resetDateTime()
            return false
        }
    }

    fun resetDateTime() {
        _state.value = _state.value.copy(
            selectedDateTime = PickableDateTime()
        )
    }


    fun reorderFavoriteStop(stopId: String, from: Int, to: Int) {
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
                // TODO: display error
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
                // TODO: display error
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