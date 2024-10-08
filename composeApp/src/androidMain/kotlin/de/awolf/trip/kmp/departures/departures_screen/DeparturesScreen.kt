package de.awolf.trip.kmp.departures.departures_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import de.awolf.trip.kmp.core.helper.SideEffectListener
import de.awolf.trip.kmp.core.helper.isFinalItemVisible
import de.awolf.trip.kmp.departures.departures_screen.components.DepartureView
import de.awolf.trip.kmp.departures.departures_screen.components.ShimmerDepartureItem
import de.awolf.trip.kmp.departures.departures_screen.components.StopInfoCard
import kotlinx.coroutines.launch
import de.awolf.trip.kmp.core.util.error.NetworkError
import de.awolf.trip.kmp.departures.presentation.departures_screen.DepartureDetailLevel
import de.awolf.trip.kmp.departures.presentation.departures_screen.DeparturesScreenEvent
import de.awolf.trip.kmp.departures.presentation.departures_screen.DeparturesScreenSideEffect
import de.awolf.trip.kmp.departures.presentation.departures_screen.DeparturesViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun StopMonitorScreen(
    viewModel: DeparturesViewModel,
    snackbarHostState: SnackbarHostState,
) {
    val state = viewModel.state.collectAsState()

    val scope = rememberCoroutineScope()

    SideEffectListener(flow = viewModel.sideEffect) { sideEffect ->
        val toastMsg: String
        when (sideEffect) {
            is DeparturesScreenSideEffect.ShowNetworkError -> toastMsg = when (sideEffect.error) {
                NetworkError.UNKNOWN -> "Unknown network error"
                NetworkError.BAD_REQUEST -> "Bad network request"
                NetworkError.REQUEST_TIMEOUT -> "Request timeout"
                NetworkError.UNAUTHORIZED -> "Network unauthorized error"
                NetworkError.CONFLICT -> "Network conflict"
                NetworkError.TOO_MANY_REQUESTS -> "Too many network requests"
                NetworkError.NO_INTERNET -> "No internet connection"
                NetworkError.PAYLOAD_TOO_LARGE -> "Network payload too large"
                NetworkError.SERVER_ERROR -> "Network server error"
                NetworkError.SERIALIZATION -> "Network serialization error"
            }
        }

        scope.launch {
            snackbarHostState.currentSnackbarData?.dismiss()
            snackbarHostState.showSnackbar(
                message = toastMsg,
                duration = SnackbarDuration.Short
            )
        }
    }

    Column(
        verticalArrangement = Arrangement.spacedBy((-10).dp),
        modifier = Modifier
            .fillMaxSize()
    ) {
        StopInfoCard(
            stop = viewModel.stop,
            queriedTime = viewModel.queriedTime,
            isStopInfoCardExpanded = state.value.isStopInfoCardExpanded,
            expandStopInfo = { viewModel.onEvent(DeparturesScreenEvent.ToggleExpandedStopInfo) },
            onCloseButtonClick = { viewModel.onEvent(DeparturesScreenEvent.Close) },
            modifier = Modifier.zIndex(2f)
        )

        val pullRefreshState = rememberPullRefreshState(
            refreshing = state.value.isRefreshing,
            onRefresh = { viewModel.onEvent(DeparturesScreenEvent.UpdateDeparturesScreen) }
        )
        val lazyListState = rememberLazyListState()
        val isAtBottom = lazyListState.isFinalItemVisible(tolerance = 0)
        LaunchedEffect(isAtBottom) {
            if (isAtBottom && !state.value.isRefreshing && state.value.departures.isNotEmpty()) { //prevent repeated calls
                viewModel.onEvent(DeparturesScreenEvent.IncreaseDepartureCount)
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .pullRefresh(pullRefreshState)
        ) {
            PullRefreshIndicator(
                refreshing = state.value.isRefreshing,
                state = pullRefreshState,
                backgroundColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .zIndex(1f)
            )

            if (state.value.maxDepartureCount == 0 && !state.value.isRefreshing) {
                Surface(
                    color = MaterialTheme.colorScheme.error,
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier
                        .padding(top = 24.dp)
                        .align(Alignment.TopCenter)
                ) {
                    Text(
                        text = "No upcoming departures found",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onError,
                        modifier = Modifier
                            .padding(8.dp)
                    )
                }
                return@Box
            }

            LazyColumn(
                state = lazyListState,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .zIndex(0f)
                    .fillMaxSize(),
            ) {
                item {
                    Spacer(modifier = Modifier.height(6.dp))
                }
                items(
                    items = state.value.departures,
                    key = { it.complexId() }
                ) { departure ->
                    val detailLevel = state.value.detailVisibility[departure] ?: DepartureDetailLevel.NONE
                    DepartureView(
                        departure = departure,
                        detailLevel = detailLevel,
                        onClick = {
                            viewModel.onEvent(
                                DeparturesScreenEvent.DepartureDetails(
                                    departure = departure,
                                    detailLevel = if (detailLevel == DepartureDetailLevel.NONE)
                                        DepartureDetailLevel.STOP_SCHEDULE
                                    else
                                        DepartureDetailLevel.NONE
                                )
                            )
                        },
                        onShowWholeScheduleClicked = {
                            viewModel.onEvent(
                                DeparturesScreenEvent.DepartureDetails(
                                    departure = departure,
                                    detailLevel = if (detailLevel == DepartureDetailLevel.STOP_SCHEDULE)
                                        DepartureDetailLevel.WHOLE_STOP_SCHEDULE
                                    else
                                        DepartureDetailLevel.STOP_SCHEDULE
                                )
                            )
                        }
                    )
                }
                if (!state.value.isRefreshing &&
                    state.value.departures.size < state.value.maxDepartureCount
                ) {
                    items(3) {
                        ShimmerDepartureItem()
                    }
                }
            }
        }
    }
}