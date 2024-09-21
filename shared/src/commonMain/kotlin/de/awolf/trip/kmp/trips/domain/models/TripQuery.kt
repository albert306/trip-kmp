package de.awolf.trip.kmp.trips.domain.models

import de.awolf.trip.kmp.core.domain.models.Mode
import de.awolf.trip.kmp.core.domain.models.PickableDateTime
import de.awolf.trip.kmp.core.domain.models.Stop
import kotlinx.serialization.Serializable
import kotlin.time.Duration

@Serializable
data class TripQuery(
    val origin: Stop?,
    val via: Stop? = null,
    val stayDuration: Duration? = null,
    val destination: Stop?,
    val time: PickableDateTime = PickableDateTime(),
    val isArrivalTime: Boolean = false,
    val shorttermchanges: Boolean = true,
    val settings: Settings? = Settings(),
) {
    @Serializable
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
