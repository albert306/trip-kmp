package de.awolf.trip.kmp.core.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseStatusDto(
    @SerialName("Code") val code: String,
    @SerialName("Message") val message: String = "",
)