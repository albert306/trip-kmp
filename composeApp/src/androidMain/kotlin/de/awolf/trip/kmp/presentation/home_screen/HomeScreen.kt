package de.awolf.trip.kmp.presentation.home_screen

import domain.models.Stop
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import de.awolf.trip.kmp.presentation.home_screen.components.SearchCard
import de.awolf.trip.kmp.presentation.home_screen.components.StopView
import viewmodel.HomeScreenViewModel

@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel,
) {

    val searchText by viewModel.searchText.collectAsState()
//    val isSearching by viewModel.isSearching.collectAsState() not currently in use
    val recommendedStops by viewModel.recommendedStops.collectAsState()

    Column(
        verticalArrangement = Arrangement.spacedBy((-10).dp),
        modifier = Modifier
            .fillMaxSize()
    ) {
        SearchCard(
            searchText = searchText,
            onSearchTextChange = viewModel::onSearchTextChange,
            onSearchButtonClick = { viewModel.startStopMonitor(recommendedStops.firstOrNull()) },
            modifier = Modifier
                .height(100.dp)
                .zIndex(2f)
                .fillMaxWidth()
        )
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .zIndex(1f)
                .fillMaxSize(),
        ) {
            item {
                Spacer(modifier = Modifier.height(6.dp))
            }
            items(
                items = recommendedStops,
                key = { it.id })
            { stop ->
                StopView(
                    stop = stop,
                    onFavouriteStarClick = {},
                    onNameClick = { viewModel.startStopMonitor(stop) },
                    modifier = Modifier.animateItem(fadeInSpec = null, fadeOutSpec = null)
                )
            }
        }
    }
}