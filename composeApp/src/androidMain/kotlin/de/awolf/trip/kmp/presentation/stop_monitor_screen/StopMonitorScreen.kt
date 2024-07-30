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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import de.awolf.trip.kmp.presentation.helper.isFinalItemVisible
import de.awolf.trip.kmp.presentation.stop_monitor_screen.components.DepartureView
import de.awolf.trip.kmp.presentation.stop_monitor_screen.components.ShimmerDepartureItem
import de.awolf.trip.kmp.presentation.stop_monitor_screen.components.StopInfoCard
import kotlinx.datetime.Clock
import viewmodel.StopMonitorViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun StopMonitorScreen(
    viewModel: StopMonitorViewModel,
) {

    val stop = viewModel.stop
    val isStopInfoCardExpanded by viewModel.isStopInfoCardExpanded.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val departures by viewModel.departures.collectAsState()

    Column(
        verticalArrangement = Arrangement.spacedBy((-10).dp),
        modifier = Modifier
            .fillMaxSize()
    ) {
        StopInfoCard(
            stop = stop,
            queriedTime = Clock.System.now(), //TODO(pass actual queried time)
            isStopInfoCardExpanded = isStopInfoCardExpanded,
            expandStopInfo = viewModel::expandStopInfo,
            onCloseButtonClick = viewModel::close,
            modifier = Modifier.zIndex(1f)
        )

        val pullRefreshState = rememberPullRefreshState(refreshing = isRefreshing, onRefresh = viewModel::updateDepartures)
        val lazyListState = rememberLazyListState()
        val isAtBottom = lazyListState.isFinalItemVisible(tolerance = 0)
        LaunchedEffect(isAtBottom) {
            if (isAtBottom && !isRefreshing && departures.isNotEmpty()) { //prevent repeated calls
                viewModel.increaseDepartureCount()
            }
        }

        Box(
            modifier = Modifier
                .pullRefresh(pullRefreshState)
        ) {
            PullRefreshIndicator(isRefreshing, pullRefreshState, Modifier.align(Alignment.TopCenter))

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
                items(items = departures) { departure ->
                    DepartureView(
                        departure = departure,
                        onClick = {
//                            viewModel.toggleVisibilityDetailedStopSchedule(index)
                        },
                    )
                }
                if (!isRefreshing) {
                    items(3) {
                        ShimmerDepartureItem()
                    }
                }
            }
        }
    }
}