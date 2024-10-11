package de.awolf.trip.kmp.trips.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RouteDto(
    @SerialName("PriceLevel") val priceLevel: Int,
    @SerialName("Price") val price: String,
    @SerialName("PriceDayTicket") val priceDayTicket: String,
    @SerialName("Net") val network: String,
    @SerialName("Duration") val duration: Int,
    @SerialName("Interchanges") val interchanges: Int,
    @SerialName("NumberOfFareZones") val numberOfFareZones: String,
    @SerialName("NumberOfFareZonesDayTicket") val numberOfFareZonesDayTicket: String,
    @SerialName("FareZoneNames") val fareZonesNames: String,
    @SerialName("FareZoneNamesDayTicket") val fareZonesNamesDayTicket: String,
    @SerialName("FareZoneOrigin") val fareZoneOrigin: Int,
    @SerialName("FareZoneDestination") val fareZoneDestination: Int,
    @SerialName("RouteId") val routeIndex: Int,
    @SerialName("MapData") val mapData: List<String>,
    @SerialName("MotChain") val motChain: List<MotDto>,
    @SerialName("PartialRoutes") val partialRoutes: List<PartialRouteDto>,
)