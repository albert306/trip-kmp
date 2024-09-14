package de.awolf.trip.kmp.search.domain.models

import de.awolf.trip.kmp.core.domain.models.ResponseStatus
import de.awolf.trip.kmp.core.domain.models.Stop
import kotlinx.datetime.Instant

data class StopFinderInfo(
    val responseStatus: ResponseStatus,
    val pointStatus: String,
    val stops: List<Stop>,
    val expirationTime: Instant?,
)