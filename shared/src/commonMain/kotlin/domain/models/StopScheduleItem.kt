package domain.models

import kotlinx.datetime.Instant

data class StopScheduleItem(
    val stopId: String,
    val dlId: String,
    val stopRegion: String,
    val stopName: String,
    val shedulePosition: String,
    val platform: Platform?,
    val arrivalTime: Instant
)