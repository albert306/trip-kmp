package data.networking.mappers

import domain.models.Stop
import kotlinx.datetime.Instant

/**
 * Utility function that converts a string of the form `\/Date(1487859556456+0200)\/`
 * to an Instant using the epoch time and system default time zone.
 */
fun dateStringToInstant(s: String?): Instant {
    if (s == null) return Instant.DISTANT_FUTURE

    val epochTime = s.substringAfter('(').substringBefore('+').toLong()
    return Instant.fromEpochMilliseconds(epochTime)
}


/**
 * Utility function that parses a stop string of the form
 * `33000742|||Helmholtzstraße|5655904|4621157|0||` into a [Stop] object.
 * For parsing details see [vvo-webapi doc](https://github.com/kiliankoe/vvo/blob/main/documentation/webapi.md#pointfinder).
 */
fun parseStopString(stopString: String): Stop {
    val stopData = stopString.split('|')
    val id = stopData[0]
    val region = when (stopData[2]) {
        "" -> "Dresden"
        else -> stopData[2]
    }
    val name = stopData[3]

    return Stop(id, name, region)
}