package data.networking.dto.stop_monitor

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DepartureDto(
    @SerialName("Id") val id: String,
    @SerialName("DlId") val dlId: String? = null,
    @SerialName("LineName") val lineNumber: String,
    @SerialName("Direction") val lineDirection: String,
    @SerialName("Platform") val platform: PlatformDto? = null,
    @SerialName("Mot") val mode: String = "Unknown",
    @SerialName("ScheduledTime") val scheduledTime: String,
    @SerialName("RealTime") val realTime: String? = null,
    @SerialName("State") val state: String = "InTime",
    @SerialName("RouteChanges") val routeChanges: List<String> = emptyList(),
    @SerialName("Diva") val diva: DivaDto? = null,
)
