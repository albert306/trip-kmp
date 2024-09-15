package de.awolf.trip.kmp.core.domain.models

import kotlinx.datetime.Instant

data class StopSearchInfo(
    val responseStatus: ResponseStatus,
    val pointStatus: String,
    val stops: List<Stop>,
    val expirationTime: Instant?,
)