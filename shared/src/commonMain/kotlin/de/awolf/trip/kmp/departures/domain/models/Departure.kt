package de.awolf.trip.kmp.departures.domain.models

import de.awolf.trip.kmp.core.domain.models.DepartureState
import de.awolf.trip.kmp.core.domain.models.Diva
import de.awolf.trip.kmp.core.domain.models.Mode
import de.awolf.trip.kmp.core.domain.models.Platform
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

data class Departure(
    val id: String,
    val dlId: String?,
    val lineNumber: String,
    val lineDirection: String,
    val platform: Platform?,
    val mode: Mode,
    val scheduledTime: Instant,
    val realTime: Instant = scheduledTime,
    val departureState: DepartureState,
    val routeChanges: List<String>,
    val diva: Diva?,
    val stopSchedule: List<StopScheduleItem>?,
) : Comparable<Departure> {

    fun getETA(): Long {
        val diff = realTime.minus(Clock.System.now())
        return diff.inWholeSeconds.plus(60).div(60)
    }

    fun getDelay(): Long {
        val diff = realTime.minus(scheduledTime)
        return diff.inWholeMinutes
    }

    override fun compareTo(other: Departure): Int {
        return realTime.compareTo(other.realTime)
    }

    fun complexId(): String {
        return "$id,${scheduledTime.toEpochMilliseconds()},$lineDirection"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Departure) return false

        return complexId() == other.complexId()
    }

    override fun hashCode(): Int {
        return complexId().hashCode()
    }
}