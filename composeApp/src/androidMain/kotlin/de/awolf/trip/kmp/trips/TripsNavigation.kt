package de.awolf.trip.kmp.trips

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
import de.awolf.trip.kmp.core.helper.viewModelFactory
import de.awolf.trip.kmp.trips.domain.models.TripQuery
import de.awolf.trip.kmp.trips.presentation.trips_entry_screen.TripsEntryScreenViewModel
import de.awolf.trip.kmp.trips.presentation.trips_screen.TripsViewModel
import de.awolf.trip.kmp.trips.trips_entry_screen.TripsEntryScreen
import kotlin.reflect.typeOf


@Composable
fun TripsNavigation(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = TripsEntryScreenRoute,
        modifier = modifier
    ) {
        composable<TripsEntryScreenRoute>(
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
            val tripsEntryScreenViewModel = viewModel<TripsEntryScreenViewModel>(
                factory = viewModelFactory {
                    TripsEntryScreenViewModel(
                        onSubmitClicked = {
                            navController.navigate(
                                TripsScreenRoute(tripQuery = it)
                            )
                        }
                    )
                }
            )

            TripsEntryScreen(
                viewModel = tripsEntryScreenViewModel,
                snackbarHostState = snackbarHostState
            )
        }

        composable<TripsScreenRoute>(
            typeMap = mapOf(
                typeOf<TripQuery>() to CustomNavType.TripQueryType,
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
            val args = it.toRoute<TripsScreenRoute>()

            val tripsViewModel = viewModel<TripsViewModel>(
                factory = viewModelFactory {
                    TripsViewModel(
                        query = args.tripQuery,
                        onCloseClicked = navController::navigateUp
                    )
                }
            )

            TODO()
        }
    }
}