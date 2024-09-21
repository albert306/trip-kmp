package de.awolf.trip.kmp.trips

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import de.awolf.trip.kmp.CustomNavType
import de.awolf.trip.kmp.RootNavigationRoute
import de.awolf.trip.kmp.core.helper.viewModelFactory
import de.awolf.trip.kmp.trips.domain.models.TripQuery
import de.awolf.trip.kmp.trips.presentation.trips_entry_screen.TripsEntryScreenViewModel
import de.awolf.trip.kmp.trips.presentation.trips_screen.TripsViewModel
import de.awolf.trip.kmp.trips.trips_entry_screen.TripsEntryScreen
import kotlin.reflect.typeOf

fun NavGraphBuilder.TripsNavigation(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
) {
    navigation<RootNavigationRoute.Trips>(
        startDestination = TripsEntryScreenRoute,
        enterTransition = {
            fadeIn(tween(0))
        },
        exitTransition = {
            fadeOut(tween(0))
        }
    ) {
        composable<TripsEntryScreenRoute> {
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