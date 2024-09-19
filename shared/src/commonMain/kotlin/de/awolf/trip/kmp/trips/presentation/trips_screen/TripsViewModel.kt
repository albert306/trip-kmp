package de.awolf.trip.kmp.trips.presentation.trips_screen

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import de.awolf.trip.kmp.core.util.CoroutineViewModel
import de.awolf.trip.kmp.core.util.Result
import de.awolf.trip.kmp.trips.domain.models.Route
import de.awolf.trip.kmp.trips.domain.models.TripQuery
import de.awolf.trip.kmp.trips.domain.use_cases.TripsUseCases

class TripsViewModel(
    query: TripQuery,
    private val onCloseClicked: () -> Unit,
) : CoroutineViewModel(), KoinComponent {

    private val useCases: TripsUseCases by inject()

    private val _state = MutableStateFlow(TripsScreenState(query))
    val state = _state.asStateFlow()

    private val _sideEffect = Channel<TripsScreenSideEffect>(Channel.BUFFERED)
    val sideEffect = _sideEffect.receiveAsFlow()

    init {
        coroutineScope.launch {
            updateRoutes(true)
        }
    }

    fun onEvent(event: TripsScreenEvent) {
        coroutineScope.launch {
            when (event) {
                is TripsScreenEvent.Close -> onCloseClicked()

                is TripsScreenEvent.UpdateTripsScreen -> updateRoutes(true)

                is TripsScreenEvent.RouteDetails -> {
                    TODO()
                }
            }
        }
    }


    private suspend fun updateRoutes(showRefreshingIndicator: Boolean = true) {
        if (showRefreshingIndicator)
            _state.value = state.value.copy(isRefreshing = true)

        val routes: List<Route>

        when (val response = useCases.fetchTrips(state.value.tripQuery)) {
            is Result.Error -> {
                _sideEffect.send(
                    TripsScreenSideEffect.ShowError(
                        response.error
                    )
                )
                routes = emptyList()
            }

            is Result.Success -> {
                routes = response.data.routes
            }
        }
        _state.value = state.value.copy(
            routes = routes,
        )

        if (showRefreshingIndicator) {
            delay(300)
            _state.value = state.value.copy(isRefreshing = false)
        }
    }
}