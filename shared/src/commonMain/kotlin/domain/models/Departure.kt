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
    val realTime: Instant,
    val departureState: DepartureState,
    val routeChanges: List<String>,
    val diva: Diva?,
    var isShowingDetailedStopSchedule: Boolean = false,
    var detailedStopSchedule: List<StopScheduleItem>? = null,
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
        return diff.inWholeMinutes
    }

    fun getDelay(): Long {
        val diff = scheduledTime.minus(realTime)
        return diff.inWholeMinutes
    }

    override fun compareTo(other: Departure): Int {
        return realTime.compareTo(other.realTime)
    }
}