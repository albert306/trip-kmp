package de.awolf.trip.kmp.core.domain.models

enum class Mode(val rawValue: String) {
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
        fun getAllLocal(): List<String> {
            return listOf(
                TRAM.rawValue,
                CITYBUS.rawValue,
                INTERCITYBUS.rawValue,
                PLUSBUS.rawValue,
                SUBURBANRAILWAY.rawValue,
                CABLEWAY.rawValue,
                FERRY.rawValue,
                HAILEDSHAREDTAXI.rawValue
            )
        }

        fun fromString(value: String): Mode {
            return entries.find { it.rawValue == value } ?: UNKNOWN
        }
    }

    fun getIconURL(): String {
        var identifier = rawValue
        if (rawValue == "CityBus" || rawValue == "IntercityBus") {
            identifier = "Bus"
        }
        return "https://www.dvb.de/assets/img/trans-icon/transport-" + identifier + ".svg"
    }

    fun getColorHex(): Long {
        return when (rawValue) {
            "Tram" -> "FFDB0031" //tram red
            "CityBus" -> "FF005D75" //bus blue
            "IntercityBus" -> "FF005D75" //bus blue
            "PlusBus" -> "FFA3177E" //plusbus magenta
            "SuburbanRailway" -> "FF00914D" //sbahn green
            "Train" -> "FF00914D" //train green
            "Ferry" -> "FF00A5DA" //ferry lightblue
            "HailedSharedTaxi" -> "FFFFEC01" //alita taxi yellow
            else -> "FF888888" //gray
        }.toLong(radix = 16)
    }
}