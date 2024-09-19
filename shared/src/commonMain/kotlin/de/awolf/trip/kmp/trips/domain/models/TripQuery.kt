package de.awolf.trip.kmp.trips.domain.models

import de.awolf.trip.kmp.core.domain.models.Mode
import de.awolf.trip.kmp.core.domain.models.PickableDateTime
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

data class TripQuery(
    val origin: String,
    val via: String? = null,
    val stayDuration: Duration? = null,
    val destination: String,
    val time: PickableDateTime = PickableDateTime(),
    val isArrivalTime: Boolean = false,
    val shorttermchanges: Boolean = true,
    val settings: Settings? = Settings(),
) {
    data class Settings(
        val footpathToStop: Int = 5,
        val walkingSpeed: WalkingSpeed = WalkingSpeed.NORMAL,
        val maxChanges: Int = Int.MAX_VALUE,
        val modeOfTransport: List<Mode> = Mode.getAllLocal(),
        val includeAlternativeStops: Boolean = true,
    ) {
        enum class WalkingSpeed(val rawValue: String = "Normal") {
            VERY_SLOW("VerySlow"),
            SLOW("Slow"),
            NORMAL("Normal"),
            FAST("Fast"),
            VERY_FAST("VeryFast"),
        }
    }
}
