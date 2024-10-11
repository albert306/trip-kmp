package de.awolf.trip.kmp.trips.data.remote.dto

import de.awolf.trip.kmp.core.data.remote.dto.DivaDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MotDto(
    @SerialName("DlId") val dlId: String,
    @SerialName("StatelessId") val lineId: String,
    @SerialName("Type") val mode: String = "Unknown",
    @SerialName("Name") val lineNumber: String,
    @SerialName("Direction") val lineDirection: String? = null,
    @SerialName("TransportationCompany") val transportationCompany: String,
    @SerialName("OperatorCode") val operatorCode: String,
    @SerialName("TrainNumber") val trainNumber: String,
    @SerialName("ProductName") val productName: String? = null,
    @SerialName("Changes") val changes: List<String> = listOf(),
    @SerialName("Diva") val diva: DivaDto? = null,
)
