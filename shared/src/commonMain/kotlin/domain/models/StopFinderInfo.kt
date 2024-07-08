package domain.models

import kotlinx.datetime.Instant

data class StopFinderInfo(
    val pointStatus: String,
    val stops: List<Stop>,
    val expirationTime: Instant?,
)
