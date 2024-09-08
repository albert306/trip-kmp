package domain.models

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

    enum class DepartureState(val rawValue: String = "Unknown") {
        INTIME("InTime"),
        DELAYED("Delayed"),
        CANCELLED("Cancelled"),
        UNKNOWN("Unknown");

        companion object {
            fun fromString(value: String): DepartureState {
                return entries.find { it.rawValue == value } ?: UNKNOWN
            }
        }
    }

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