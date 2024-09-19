package de.awolf.trip.kmp.core.domain.models

enum class Mode(val rawValue: String) {
    FOOTPATH("Footpath"),
    TRAM("Tram"),
    CITYBUS("CityBus"),
    INTERCITYBUS("IntercityBus"),
    PLUSBUS("PlusBus"),
    SUBURBANRAILWAY("SuburbanRailway"),
    TRAIN("Train"),
    CABLEWAY("Cableway"),
    FERRY("Ferry"),
    HAILEDSHAREDTAXI("HailedSharedTaxi"),
    UNKNOWN("Unknown");

    companion object {
        fun getAllLocal(): List<Mode> {
            return listOf(
                TRAM,
                CITYBUS,
                INTERCITYBUS,
                PLUSBUS,
                SUBURBANRAILWAY,
                CABLEWAY,
                FERRY,
                HAILEDSHAREDTAXI
            )
        }

        fun fromString(value: String): Mode {
            return entries.find { it.rawValue == value } ?: UNKNOWN
        }
    }

    fun getIconURL(): String {
        if (this == UNKNOWN || this == FOOTPATH) return ""

        val identifier = if (this == CITYBUS || this == INTERCITYBUS) "Bus" else rawValue

        return "https://www.dvb.de/assets/img/trans-icon/transport-$identifier.svg"
    }

    fun getColorHex(): Long {
        return when (this) {
            FOOTPATH -> "FF888888" //gray
            TRAM -> "FFDB0031" //tram red
            CITYBUS -> "FF005D75" //bus blue
            INTERCITYBUS -> "FF005D75" //bus blue
            PLUSBUS -> "FFA3177E" //plusbus magenta
            SUBURBANRAILWAY -> "FF00914D" //sbahn green
            TRAIN -> "FF00914D" //train green
            FERRY -> "FF00A5DA" //ferry lightblue
            HAILEDSHAREDTAXI -> "FFFFEC01" //alita taxi yellow
            else -> "FF888888" //gray
        }.toLong(radix = 16)
    }
}