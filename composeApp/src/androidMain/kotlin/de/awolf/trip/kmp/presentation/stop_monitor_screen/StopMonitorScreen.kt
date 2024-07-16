package de.awolf.trip.kmp.presentation.stop_monitor_screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import de.awolf.trip.kmp.presentation.helper.isFinalItemVisible
import de.awolf.trip.kmp.presentation.stop_monitor_screen.components.DepartureView
import de.awolf.trip_compose.presentation.stop_monitor_screen.components.ShimmerDepartureItem
import de.awolf.trip.kmp.presentation.stop_monitor_screen.components.StopInfoCard
import viewmodel.StopMonitorViewModel
import java.time.LocalDateTime

@OptIn(ExperimentalFoundationApi::class)
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
            queriedTime = LocalDateTime.now(), //TODO(pass actual queried time)
            isStopInfoCardExpanded = isStopInfoCardExpanded,
            expandStopInfo = viewModel::expandStopInfo,
            onCloseButtonClick = viewModel::close,
            modifier = Modifier.zIndex(1f)
        )

        val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isRefreshing)
        val lazyListState = rememberLazyListState()
        val isAtBottom = lazyListState.isFinalItemVisible(tolerance = 0)
        LaunchedEffect(isAtBottom) {
            if (isAtBottom && !isRefreshing && departures.isNotEmpty()) { //prevent repeated calls
                viewModel.increaseDepartureCount()
            }
        }

        SwipeRefresh(
            state = swipeRefreshState,
            onRefresh = viewModel::updateDepartures,
            indicator = { state, refreshTrigger ->
                SwipeRefreshIndicator(
                    state = state,
                    refreshTriggerDistance = refreshTrigger,
                    backgroundColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.Black,
                )
            }
        ) {
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
                itemsIndexed(departures, key = { _, departure -> departure.id + "||" + departure.lineDirection + "||" + departure.sheduledTime.toString() + "||"}) { index, departure ->
                    DepartureView(
                        departure = departure,
                        onClick = {
                            viewModel.toggleVisibilityDetailedStopSchedule(index)
                        },
                        modifier = Modifier.animateItemPlacement()
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