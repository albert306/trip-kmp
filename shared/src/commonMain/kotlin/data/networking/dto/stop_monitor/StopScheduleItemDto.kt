package data.networking.dto.stop_monitor

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StopScheduleItemDto(
    @SerialName("Id") val stopId: String,
    @SerialName("DlId") val dlId: String = "",
    @SerialName("Place") val stopRegion: String,
    @SerialName("Name") val stopName: String,
    @SerialName("Position") val schedulePosition: String,
    @SerialName("Platform") val platform: PlatformDto? = null,
    @SerialName("Time") val scheduledTime: String,
    @SerialName("RealTime") val realTime: String
)