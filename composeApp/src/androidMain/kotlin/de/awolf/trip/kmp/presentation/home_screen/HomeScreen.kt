package de.awolf.trip.kmp.presentation.home_screen

import android.os.Build
import android.view.HapticFeedbackConstants
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import de.awolf.trip.kmp.presentation.home_screen.components.SearchCard
import de.awolf.trip.kmp.presentation.home_screen.components.StopView
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState
import viewmodel.HomeScreenViewModel
import domain.models.StopListSource

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel,
) {

    val searchText by viewModel.searchText.collectAsState()
//    val isSearching by viewModel.isSearching.collectAsState() not currently in use
    val stopList by viewModel.stopList.collectAsState()
    val stopListSource by viewModel.stopListSource.collectAsState()


    val view = LocalView.current

    val lazyListState = rememberLazyListState()

    val reorderableLazyListState = rememberReorderableLazyListState(
        lazyListState = lazyListState,
        scrollThresholdPadding = PaddingValues(top = 100.dp)
    ) { from, to ->
        viewModel.reorderFavoriteStop(from.key.toString(), from.index - 1, to.index - 1)
        view.performHapticFeedback(HapticFeedbackConstants.SEGMENT_TICK)
    }

    Column(
        verticalArrangement = Arrangement.spacedBy((-10).dp),
        modifier = Modifier
            .fillMaxSize()
    ) {
        SearchCard(
            searchText = searchText,
            onSearchTextChange = viewModel::onSearchTextChange,
            onSearchButtonClick = { viewModel.startStopMonitor(stopList.firstOrNull()) },
            modifier = Modifier
                .height(100.dp)
                .zIndex(2f)
                .fillMaxWidth()
        )
        LazyColumn(
            state = lazyListState,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .zIndex(1f)
                .fillMaxSize(),
        ) {
            item {
                Spacer(modifier = Modifier.height(6.dp))
            }
            items(items = stopList, key = { it.id }) { stop ->
                if (stopListSource == StopListSource.FAVORITES) {
                    ReorderableItem(
                        state = reorderableLazyListState,
                        key = stop.id
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            StopView(
                                stop = stop,
                                onFavoriteStarClick = { viewModel.toggleFavoriteStop(stop) },
                                onNameClick = { viewModel.startStopMonitor(stop) },
                                modifier = Modifier
                                    .animateItem(fadeInSpec = null, fadeOutSpec = null)
                                    .fillMaxWidth(fraction = 0.9f)
                            )
                            IconButton(
                                onClick = {},
                                modifier = Modifier
                                    .align(Alignment.CenterEnd)
                                    .draggableHandle()
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Menu,
                                    contentDescription = "Reorder",
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                } else {
                    StopView(
                        stop = stop,
                        onFavoriteStarClick = { viewModel.toggleFavoriteStop(stop) },
                        onNameClick = { viewModel.startStopMonitor(stop) },
                        modifier = Modifier
                            .animateItem(fadeInSpec = null, fadeOutSpec = null)
                            .fillMaxWidth()
                    )
                }

            }
        }
    }
}