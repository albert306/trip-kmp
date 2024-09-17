package de.awolf.trip.kmp.trips.domain.models


data class PartialRoute(
    val partialRouteId: Int,
    val duration: Int,
    val mot: Mot,
    val mapDataIndex: Int?,
    val shift: String,
    val regularStops: List<PartialRouteStop>,
    val changeoverEndangered: Boolean,
    val nextDepartureTimes: List<String>,
    val previousDepartureTimes: List<String>
)