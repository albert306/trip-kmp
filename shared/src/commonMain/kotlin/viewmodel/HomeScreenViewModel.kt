package viewmodel

import domain.models.Stop
import domain.use_case.UseCases
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.datetime.Clock
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeComponents
import util.CoroutineViewModel
import util.Result

@OptIn(FlowPreview::class)
class HomeScreenViewModel(
    private val useCases: UseCases,
    private val navController: NavController
) : CoroutineViewModel() {

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _selectedTime = MutableStateFlow(Clock.System.now())
    val selectedTime = _selectedTime.asStateFlow()

    private val _recommendedStops = MutableStateFlow(listOf<Stop>())
    val recommendedStops = _recommendedStops.asStateFlow()


    init {
        _searchText
            .debounce(100L)
            .onEach { _isSearching.update { true } }
            .combine(_recommendedStops, ) { text, _ ->
                setRecommendedStops(text)
            }
            .onEach { _isSearching.update { false } }
            .launchIn(
                coroutineScope
            )
    }


    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    fun startStopMonitor(stop: Stop?) {
        if (stop == null) { return } //TODO(give user feedback that no such stop was found)

        navController.navigate(Screen.StopMonitorScreen.withArgs(
            stop.id,
            stop.name,
            stop.region,
            stop.isFavourite.toString(),
            Clock.System.now().format(DateTimeComponents.Formats.ISO_DATE_TIME_OFFSET)
        ))
    }

//    fun toggleFavouriteStop(stop: Stop) {
//        coroutineScope.launch {
//            useCases.toggleAndReturnFavouriteStopStatusUseCase(stop)
//            setRecommendedStops(searchText.value)
//        }
//    }

    private suspend fun setRecommendedStops(query: String) {
        when (val recommendedStopsResult = useCases.getRecommendedStopsUseCase(query)) {
            is Result.Error -> {
                println("TOAST: " + recommendedStopsResult.message)
                _recommendedStops.update { emptyList() }
            }
            is Result.Success -> {
                _recommendedStops.update { recommendedStopsResult.data!! }
            }
        }
    }
}