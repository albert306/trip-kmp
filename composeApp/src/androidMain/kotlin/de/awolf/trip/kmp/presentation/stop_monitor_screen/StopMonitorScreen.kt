package de.awolf.trip.kmp.presentation.stop_monitor_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import de.awolf.trip.kmp.presentation.helper.isFinalItemVisible
import de.awolf.trip.kmp.presentation.stop_monitor_screen.components.DepartureView
import de.awolf.trip.kmp.presentation.stop_monitor_screen.components.ShimmerDepartureItem
import de.awolf.trip.kmp.presentation.stop_monitor_screen.components.StopInfoCard
import presentation.stop_monitor.StopMonitorEvent
import presentation.stop_monitor.StopMonitorViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun StopMonitorScreen(
    viewModel: StopMonitorViewModel,
) {
    val state = viewModel.state.collectAsState()

    Column(
        verticalArrangement = Arrangement.spacedBy((-10).dp),
        modifier = Modifier
            .fillMaxSize()
    ) {
        StopInfoCard(
            stop = viewModel.stop,
            queriedTime = viewModel.queriedTime,
            isStopInfoCardExpanded = state.value.isStopInfoCardExpanded,
            expandStopInfo = { viewModel.onEvent(StopMonitorEvent.ToggleExpandedStopInfo) },
            onCloseButtonClick = { viewModel.onEvent(StopMonitorEvent.Close) },
            modifier = Modifier.zIndex(1f)
        )

        val pullRefreshState = rememberPullRefreshState(
            refreshing = state.value.isRefreshing,
            onRefresh = { viewModel.onEvent(StopMonitorEvent.UpdateDepartures) }
        )
        val lazyListState = rememberLazyListState()
        val isAtBottom = lazyListState.isFinalItemVisible(tolerance = 0)
        LaunchedEffect(isAtBottom) {
            if (isAtBottom && !state.value.isRefreshing && state.value.departures.isNotEmpty()) { //prevent repeated calls
                viewModel.onEvent(StopMonitorEvent.IncreaseDepartureCount)
            }
        }

        Box(
            modifier = Modifier
                .pullRefresh(pullRefreshState)
        ) {
            PullRefreshIndicator(
                refreshing = state.value.isRefreshing,
                state = pullRefreshState,
                backgroundColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .align(Alignment.TopCenter)
            )

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
                    DepartureView(
                        departure = departure,
                        onClick = {
//                            viewModel.toggleVisibilityDetailedStopSchedule(index)
                        },
                    )
                }
                if (!state.value.isRefreshing) {
                    items(3) {
                        ShimmerDepartureItem()
                    }
                }
            }
        }
    }
}