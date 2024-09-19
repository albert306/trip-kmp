package de.awolf.trip.kmp.core.helper

import de.awolf.trip.kmp.core.util.error.DatabaseError
import de.awolf.trip.kmp.core.util.error.Error
import de.awolf.trip.kmp.core.util.error.NetworkError
import de.awolf.trip.kmp.core.util.error.VvoStatusError

fun Error.message(): String {
    return when (this) {
        is NetworkError -> {
            return when (this) {
                NetworkError.UNKNOWN -> "Unknown network error"
                NetworkError.BAD_REQUEST -> "Bad network request"
                NetworkError.REQUEST_TIMEOUT -> "Request timeout"
                NetworkError.UNAUTHORIZED -> "Network unauthorized error"
                NetworkError.CONFLICT -> "Network conflict"
                NetworkError.TOO_MANY_REQUESTS -> "Too many network requests"
                NetworkError.NO_INTERNET -> "No internet connection"
                NetworkError.PAYLOAD_TOO_LARGE -> "Network payload too large"
                NetworkError.SERVER_ERROR -> "Network server error"
                NetworkError.SERIALIZATION -> "Network serialization error"
            }
        }
        is DatabaseError -> {
            return when (this) {
                DatabaseError.UNKNOWN -> "Unknown database error"
            }
        }
        is VvoStatusError -> {
            return when (this) {
                VvoStatusError.UNKNOWN -> "Unknown VVO status error"
                VvoStatusError.DATE_BEFORE_NOW -> "Time is in past, and was corrected to current time"
            }
        }
        else -> "Unknown error"
    }
}