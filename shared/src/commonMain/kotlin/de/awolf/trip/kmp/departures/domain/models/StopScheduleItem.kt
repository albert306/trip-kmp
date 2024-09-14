package de.awolf.trip.kmp.departures.domain.models

import de.awolf.trip.kmp.core.domain.models.Platform
import kotlinx.datetime.Instant

data class StopScheduleItem(
    val stopId: String,
    val dlId: String,
    val stopRegion: String,
    val stopName: String,
    val schedulePosition: SchedulePosition,
    val platform: Platform?,
    val scheduledTime: Instant,
    val realTime: Instant = scheduledTime,
) {
    enum class SchedulePosition(val rawValue: String = "Unknown") {
        PREVIOUS("Previous"),
        CURRENT("Current"),
        NEXT("Next"),
        UNKNOWN("Unknown");

        companion object {
            fun fromString(value: String): SchedulePosition {
                return entries.find { it.rawValue == value } ?: UNKNOWN
            }
        }
    }

    fun getDelay(): Long {
        val diff = realTime.minus(scheduledTime)
        return diff.inWholeMinutes
    }
}