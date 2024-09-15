package de.awolf.trip.kmp.core.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DivaDto(
    @SerialName("Number") val number: String,
    @SerialName("Network") val network: String
)
