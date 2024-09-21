package de.awolf.trip.kmp.departures

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
import de.awolf.trip.kmp.core.domain.models.PickableDateTime
import de.awolf.trip.kmp.core.domain.models.Stop
import de.awolf.trip.kmp.core.helper.viewModelFactory
import de.awolf.trip.kmp.departures.departures_entry_screen.DeparturesEntryScreen
import de.awolf.trip.kmp.departures.departures_screen.DeparturesScreen
import de.awolf.trip.kmp.departures.presentation.departures_entry_screen.DeparturesEntryScreenViewModel
import de.awolf.trip.kmp.departures.presentation.departures_screen.DeparturesViewModel
import kotlin.reflect.typeOf

fun NavGraphBuilder.DeparturesNavigation(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
) {
    navigation<RootNavigationRoute.Departures>(
        startDestination = DeparturesEntryScreenRoute,
        enterTransition = {
            fadeIn(tween(0))
        },
        exitTransition = {
            fadeOut(tween(0))
        }
    ) {
        composable<DeparturesEntryScreenRoute> {
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