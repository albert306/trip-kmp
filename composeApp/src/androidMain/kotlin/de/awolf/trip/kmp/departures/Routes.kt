package de.awolf.trip.kmp.departures

import de.awolf.trip.kmp.core.domain.models.PickableDateTime
import de.awolf.trip.kmp.core.domain.models.Stop
import kotlinx.serialization.Serializable

@Serializable
object DeparturesSearchRoute

@Serializable
data class DeparturesRoute(
    val stop: Stop,
    val queriedTime: PickableDateTime
)