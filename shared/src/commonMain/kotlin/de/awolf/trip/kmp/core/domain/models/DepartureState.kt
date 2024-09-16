package de.awolf.trip.kmp.core.domain.models

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