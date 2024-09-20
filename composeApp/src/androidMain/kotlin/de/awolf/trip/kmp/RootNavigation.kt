package de.awolf.trip.kmp

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import de.awolf.trip.kmp.departures.DeparturesNavigation
import de.awolf.trip.kmp.trips.TripsNavigation

data class BottomNavigationItem(
    val title: String,
    val selectedIcon: Painter,
    val unselectedIcon: Painter,
    val route: RootNavigationRoute,
)

@Composable
fun RootNavigation() {
    val navigationItems = listOf(
        BottomNavigationItem(
            title = "Departures",
            selectedIcon = painterResource(id = R.drawable.baseline_departure_board_24),
            unselectedIcon = painterResource(id = R.drawable.outline_departure_board_24),
            route = RootNavigationRoute.Departures
        ),
        BottomNavigationItem(
            title = "Trips",
            selectedIcon = painterResource(id = R.drawable.baseline_route_24),
            unselectedIcon = painterResource(id = R.drawable.outline_route_24),
            route = RootNavigationRoute.Trips
        ),
        // Future navigation items:
        // Settings
        // Route changes
    )

    var selectedIconIndex by rememberSaveable {
        mutableIntStateOf(0)
    }

    val navController = rememberNavController()

    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .imePadding()
            )
        },
        containerColor = MaterialTheme.colorScheme.surface,
        bottomBar = {
            NavigationBar {
                navigationItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        label = { Text(item.title) },
                        selected = selectedIconIndex == index,
                        icon = {
                            Icon(
                                painter = if (selectedIconIndex == index) item.selectedIcon else item.unselectedIcon,
                                contentDescription = item.title
                            )
                        },
                        onClick = {
                            selectedIconIndex = index
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        },
        modifier = Modifier
            .fillMaxSize()
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = RootNavigationRoute.Departures,
            modifier = Modifier.padding(innerPadding)
        ) {
            DeparturesNavigation(
                navController = navController,
                snackbarHostState = snackbarHostState
            )
            TripsNavigation(
                navController = navController,
                snackbarHostState = snackbarHostState
            )
        }
    }
}