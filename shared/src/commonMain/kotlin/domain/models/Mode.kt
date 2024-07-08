package domain.models

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

    fun getColorHex(): String {
        return when (rawValue) {
            "Tram" -> "0xDB0031" //tram red
            "CityBus" -> "0x005D75" //bus blue
            "IntercityBus" -> "0x005D75" //bus blue
            "PlusBus" -> "0xA3177E" //plusbus magenta
            "SuburbanRailway" -> "0x00914D" //sbahn green
            "Train" -> "0x00914D" //train green
            "Ferry" -> "0x00A5DA" //ferry lightblue
            "HailedSharedTaxi" -> "0xEC01" //alita taxi yellow
            else -> "0x888888" //gray
        }
    }
}