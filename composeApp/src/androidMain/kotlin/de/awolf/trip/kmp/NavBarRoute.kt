package de.awolf.trip.kmp

import kotlinx.serialization.Serializable

@Serializable
sealed interface NavBarRoute {
    @Serializable
    data object Departures : NavBarRoute
    @Serializable
    data object Trips : NavBarRoute
}