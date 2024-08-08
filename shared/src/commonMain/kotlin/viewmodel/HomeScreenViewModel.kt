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
import kotlinx.datetime.Instant
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import util.CoroutineViewModel
import util.Result

@OptIn(FlowPreview::class)
class HomeScreenViewModel(
    val onStopClicked: (Stop, Instant) -> Unit,
) : CoroutineViewModel(), KoinComponent {

    private val useCases: UseCases by inject()

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

        onStopClicked(stop, selectedTime.value)
    }

//    fun toggleFavoriteStop(stop: Stop) {
//        coroutineScope.launch {
//            useCases.toggleAndReturnFavoriteStopStatusUseCase(stop)
//            setRecommendedStops(searchText.value)
//        }
//    }

    private suspend fun setRecommendedStops(query: String) {
        when (val recommendedStopsResult = useCases.getRecommendedStopsUseCase(query)) {
            is Result.Error -> {
                // TODO: display error
                _recommendedStops.update { emptyList() }
            }
            is Result.Success -> {
                _recommendedStops.update { recommendedStopsResult.data }
            }
        }
    }
}