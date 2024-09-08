package domain.models

import kotlinx.datetime.Instant

data class StopScheduleItem(
    val stopId: String,
    val dlId: String,
    val stopRegion: String,
    val stopName: String,
    val schedulePosition: String,
    val platform: Platform?,
    val scheduledTime: Instant,
    val realTime: Instant = scheduledTime
) {
    fun getDelay(): Long {
        val diff = realTime.minus(scheduledTime)
        return diff.inWholeMinutes
    }
}