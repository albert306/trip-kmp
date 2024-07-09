package data.networking.dto.stop_monitor

import domain.models.Platform
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StopScheduleItemDto(
    @SerialName("Id") val stopId: String,
    @SerialName("DlId") val dlId: String = "",
    @SerialName("Place") val stopRegion: String,
    @SerialName("Name") val stopName: String,
    @SerialName("Position") val shedulePosition: String,
    @SerialName("Platform") val platform: PlatformDto? = null,
    @SerialName("Time") val arrivalTime: String

)