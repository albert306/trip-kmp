package de.awolf.trip.kmp.departures

import de.awolf.trip.kmp.core.domain.models.Stop
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
object DeparturesSearchScreen

@Serializable
data class DeparturesScreen(
    val stop: Stop,
    val queriedTime: Instant
)