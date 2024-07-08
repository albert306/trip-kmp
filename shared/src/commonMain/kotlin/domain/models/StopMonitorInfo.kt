package domain.models

import kotlinx.datetime.Instant

data class StopMonitorInfo(
    val name: String,
    val region: String,
    val expirationTime: Instant?,
    val departures: List<Departure>,
)