package de.awolf.trip.kmp.trips.domain.models

import de.awolf.trip.kmp.core.domain.models.ResponseStatus

data class TripsResponse(
    val sessionId: String?,
    val status: ResponseStatus,
    val routes: List<Route>
)