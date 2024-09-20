package de.awolf.trip.kmp.departures

import de.awolf.trip.kmp.core.domain.models.PickableDateTime
import de.awolf.trip.kmp.core.domain.models.Stop
import kotlinx.serialization.Serializable

@Serializable
object DeparturesEntryScreenRoute

@Serializable
data class DeparturesScreenRoute(
    val stop: Stop,
    val queriedTime: PickableDateTime
)