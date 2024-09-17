package de.awolf.trip.kmp.trips.domain.models

import de.awolf.trip.kmp.core.domain.models.Stop

data class Trip(
    val origin: Stop,
    val destination: Stop,
)