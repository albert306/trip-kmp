package viewmodel

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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import util.CoroutineViewModel
import util.Result
import viewmodel.helper.PickableDateTime
import viewmodel.helper.truncateTo

@OptIn(FlowPreview::class)
class HomeScreenViewModel(
    val onStopClicked: (Stop, Instant) -> Unit,
) : CoroutineViewModel(), KoinComponent {

    private val useCases: UseCases by inject()

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _selectedDateTime: MutableStateFlow<PickableDateTime> = MutableStateFlow(PickableDateTime())
    val selectedDateTime = _selectedDateTime.asStateFlow()

    private val _stopList = MutableStateFlow(listOf<Stop>())
    val stopList = _stopList.asStateFlow()

    private val _stopListSource = MutableStateFlow(StopListSource.NONE)
    val stopListSource = _stopListSource.asStateFlow()


    init {
        _searchText
            .debounce(100L)
            .onEach { text ->
                if (text.length < 3 && stopListSource.value != StopListSource.FAVORITES) {
                    setFavoriteStops()
                }
            }
            .filter { it.length >= 3 } // Vvo api only returns results for 3 or more characters
            .onEach { text ->
                _isSearching.update { true }
                setStopsByQuery(text)
                _isSearching.update { false }
            }
            .launchIn(
                coroutineScope
            )
    }


    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    fun startStopMonitor(stop: Stop?) {
        if (stop == null) { return } //TODO(give user feedback that no such stop was found)

        onStopClicked(stop, selectedDateTime.value.toInstant())
    }

    fun toggleFavoriteStop(stop: Stop) {
        coroutineScope.launch {
            useCases.toggleFavoriteStopUseCase(stop)

            _stopList.update { recommendedStops ->

                if (stop.isFavorite) {
                    recommendedStops.filter { it.id != stop.id }
                } else {
                    recommendedStops.map {
                        if (it.id == stop.id) {
                            it.copy(isFavorite = !it.isFavorite)
                        } else {
                            it
                        }
                    }
                }
            }
        }
    }

    fun changeSelectedDate(date: LocalDate) {
        _selectedDateTime.update { it.copy(date = date) }
    }

    fun changeSelectedTime(time: LocalTime) {
        _selectedDateTime.update { it.copy(time = time) }
    }

    private fun dateTimeIsValid() {
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).truncateTo(DateTimeUnit.MINUTE)
        val selected = LocalDateTime(
            selectedDateTime.value.date ?: now.date,
            selectedDateTime.value.time ?: now.time
        )
        if (selected >= now)
            return
        if (selected.time < now.time && selected.date <= now.date)
            _selectedDateTime.update { it.copy(time = null) }
        if (selected.date < now.date)
            _selectedDateTime.update { it.copy(date = null) }
    }

    fun dateTimeIsValid(
        date: LocalDate?,
        time: LocalTime?
    ): Boolean {

        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).truncateTo(DateTimeUnit.MINUTE)
        val selected = LocalDateTime(
            (date ?: selectedDateTime.value.date) ?: now.date,
            (time ?: selectedDateTime.value.time) ?: now.time
        )
        return selected >= now
    }

    fun resetDateTime() {
        _selectedDateTime.update { PickableDateTime() }
    }


    fun reorderFavoriteStop(stopId: String, from: Int, to: Int) {
        coroutineScope.launch {
            useCases.reorderFavoriteStops(stopId, from.toLong(), to.toLong())

            _stopList.update { list ->
                val mutList = list.toMutableList()
                mutList.apply {
                    add(to, removeAt(from))
                }
                mutList
            }
        }
    }

    private suspend fun setStopsByQuery(query: String) {
        when (val recommendedStopsResult = useCases.findStopByQueryUseCase(query)) {
            is Result.Error -> {
                // TODO: display error
                _stopList.update { emptyList() }
            }
            is Result.Success -> {
                _stopList.update { recommendedStopsResult.data.stops }
            }
        }
        _stopListSource.update { StopListSource.SEARCH }
    }

    private suspend fun setFavoriteStops() {
        println("setFavoriteStops")
        when (val favoriteStopsResult = useCases.getFavoriteStopsUseCase()) {
            is Result.Error -> {
                // TODO: display error
                _stopList.update { emptyList() }
            }

            is Result.Success -> {
                _stopList.update { favoriteStopsResult.data }
            }
        }
        _stopListSource.update { StopListSource.FAVORITES }
    }
}