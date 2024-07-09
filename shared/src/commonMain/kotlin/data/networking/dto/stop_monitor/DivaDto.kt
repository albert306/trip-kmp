package data.networking.dto.stop_monitor

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DivaDto(
    @SerialName("Number") val number: String,
    @SerialName("Network") val network: String
)
