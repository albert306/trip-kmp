package de.awolf.trip.kmp

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import de.awolf.trip.kmp.core.helper.viewModelFactory
import de.awolf.trip.kmp.departures.search_screen.HomeScreen
import de.awolf.trip.kmp.departures.departures_screen.StopMonitorScreen
import de.awolf.trip.kmp.core.domain.models.Stop
import de.awolf.trip.kmp.departures.DeparturesScreen
import de.awolf.trip.kmp.departures.DeparturesSearchScreen
import de.awolf.trip.kmp.departures.presentation.search_screen.SearchScreenViewModel
import de.awolf.trip.kmp.departures.presentation.departures_screen.DeparturesViewModel
import kotlinx.datetime.Instant


@Composable
fun Navigation(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {

    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = DeparturesSearchScreen,
        modifier = modifier
    ) {
        composable<DeparturesSearchScreen>(
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(200)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(200)
                )
            }
        ) {
            val searchScreenViewModel = viewModel<SearchScreenViewModel>(
                factory = viewModelFactory {
                    SearchScreenViewModel(
                        onStopClicked = { stop: Stop, queriedTime: Instant ->
                            navController.navigate(DeparturesScreen(
                                stop = stop,
                                queriedTime = queriedTime
                            ))
                        }
                    )
                }
            )

            HomeScreen(
                viewModel = searchScreenViewModel,
                snackbarHostState = snackbarHostState
            )
        }

        composable<DeparturesScreen>(
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(200)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(200)
                )
            }
        ) {
            val args = it.toRoute<DeparturesScreen>()

            val departuresViewModel = viewModel<DeparturesViewModel>(
                factory = viewModelFactory {
                    DeparturesViewModel(
                        stop = args.stop,
                        queriedTime = args.queriedTime,
                        onCloseClicked = navController::navigateUp
                    )
                }
            )

            StopMonitorScreen(
                viewModel = departuresViewModel,
                snackbarHostState = snackbarHostState
            )
        }
    }
}