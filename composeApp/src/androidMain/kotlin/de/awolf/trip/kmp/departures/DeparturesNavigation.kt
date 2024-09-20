package de.awolf.trip.kmp.departures

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
import de.awolf.trip.kmp.CustomNavType
import de.awolf.trip.kmp.core.domain.models.PickableDateTime
import de.awolf.trip.kmp.core.domain.models.Stop
import de.awolf.trip.kmp.core.helper.viewModelFactory
import de.awolf.trip.kmp.departures.departures_entry_screen.DeparturesEntryScreen
import de.awolf.trip.kmp.departures.departures_screen.DeparturesScreen
import de.awolf.trip.kmp.departures.presentation.departures_entry_screen.DeparturesEntryScreenViewModel
import de.awolf.trip.kmp.departures.presentation.departures_screen.DeparturesViewModel
import kotlin.reflect.typeOf

@Composable
fun DeparturesNavigation(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = DeparturesEntryScreenRoute,
        modifier = modifier
    ) {
        composable<DeparturesEntryScreenRoute>(
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
            val departuresEntryScreenViewModel = viewModel<DeparturesEntryScreenViewModel>(
                factory = viewModelFactory {
                    DeparturesEntryScreenViewModel(
                        onStopClicked = { stop: Stop, queriedTime: PickableDateTime ->
                            navController.navigate(
                                DeparturesScreenRoute(
                                    stop = stop,
                                    queriedTime = queriedTime
                                )
                            )
                        }
                    )
                }
            )

            DeparturesEntryScreen(
                viewModel = departuresEntryScreenViewModel,
                snackbarHostState = snackbarHostState
            )
        }

        composable<DeparturesScreenRoute>(
            typeMap = mapOf(
                typeOf<Stop>() to CustomNavType.StopType,
                typeOf<PickableDateTime>() to CustomNavType.PickableDateTimeType
            ),
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
            val args = it.toRoute<DeparturesScreenRoute>()

            val departuresViewModel = viewModel<DeparturesViewModel>(
                factory = viewModelFactory {
                    DeparturesViewModel(
                        stop = args.stop,
                        queriedTime = args.queriedTime,
                        onCloseClicked = navController::navigateUp
                    )
                }
            )

            DeparturesScreen(
                viewModel = departuresViewModel,
                snackbarHostState = snackbarHostState
            )
        }
    }
}