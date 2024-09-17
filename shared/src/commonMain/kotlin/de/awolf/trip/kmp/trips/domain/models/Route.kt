package de.awolf.trip.kmp.trips.domain.models

data class Route(
    val priceLevel: Int,
    val price: String,
    val priceDayTicket: String,
    val network: String,
    val duration: Int,
    val interchanges: Int,
    val numberOfFareZones: String,
    val numberOfFareZonesDayTicket: String,
    val fareZonesNames: String,
    val fareZonesNamesDayTicket: String,
    val fareZoneOrigin: Int,
    val fareZoneDestination: Int,
    val routeIndex: Int,
    val mapData: List<String>,
    val motChain: List<Mot>,
    val partialRoutes: List<PartialRoute>
)
