package de.awolf.trip.kmp.trips.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MotDto(
    @SerialName("DlId") val dlId: String,
    @SerialName("StatelessId") val lineId: String,
    @SerialName("Type") val mode: String = "Unknown",
    @SerialName("Name") val lineNumber: String,
    @SerialName("Direction") val lineDirection: String,
    @SerialName("TransportationCompany") val transportationCompany: String,
    @SerialName("OperatorCode") val operatorCode: String,
    @SerialName("TrainNumber") val trainNumber: String,
)
