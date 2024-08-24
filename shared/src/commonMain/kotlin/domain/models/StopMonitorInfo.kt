package domain.models

import kotlinx.datetime.Instant

data class StopMonitorInfo(
    val responseStatus: ResponseStatus,
    val name: String,
    val region: String,
    val expirationTime: Instant?,
    val departures: List<Departure>,
    val maxDepartureCount: Int = Int.MAX_VALUE,
)