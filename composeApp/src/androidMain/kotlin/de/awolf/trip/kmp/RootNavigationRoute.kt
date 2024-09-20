package de.awolf.trip.kmp

import kotlinx.serialization.Serializable

@Serializable
sealed interface RootNavigationRoute {
    @Serializable
    data object Departures : RootNavigationRoute
    @Serializable
    data object Trips : RootNavigationRoute
}