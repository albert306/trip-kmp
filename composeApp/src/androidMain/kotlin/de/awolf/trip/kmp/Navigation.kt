package de.awolf.trip.kmp

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import de.awolf.trip.kmp.presentation.home_screen.HomeScreen
import de.awolf.trip.kmp.presentation.stop_monitor_screen.StopMonitorScreen
import domain.models.Stop
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import viewmodel.HomeScreenViewModel
import viewmodel.StopMonitorViewModel


@Composable
fun Navigation() {

    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.HomeScreen.route,
    ) {
        composable(
            route = Screen.HomeScreen.route,
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
            val homeScreenViewModel = koinViewModel<HomeScreenViewModel>()

            HomeScreen(
                viewModel = homeScreenViewModel
            )
        }

        composable(
            route = Screen.StopMonitorScreen.route + "/{stopId}/{stopName}/{stopRegion}/{stopIsFavourite}/{queriedTime}",
            arguments = listOf(
                navArgument("stopId") {
                    type = NavType.StringType
                },
                navArgument("stopName") {
                    type = NavType.StringType
                },
                navArgument("stopRegion") {
                    type = NavType.StringType
                },
                navArgument("stopIsFavourite") {
                    type = NavType.BoolType
                },
                navArgument("queriedTime") {
                    type = NavType.LongType
                }
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
        ) { entry ->
            val stopMonitorViewModel = koinViewModel<StopMonitorViewModel> {
                parametersOf(
                    Stop(
                        id = entry.arguments?.getString("stopId") ?: "0000000",
                        name = entry.arguments?.getString("stopName") ?: "unknown",
                        region = entry.arguments?.getString("stopRegion") ?: "Dresden",
                    ),
                    entry.arguments?.getLong("queriedTime") ?: 0L,
                )
            }

            StopMonitorScreen(
                viewModel = stopMonitorViewModel
            )
        }
    }
}