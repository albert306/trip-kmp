package de.awolf.trip.kmp.core.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Stop(
    val id: String,
    val name: String,
    val region: String = "Dresden",
    val isFavorite: Boolean = false
)