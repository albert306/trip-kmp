package de.awolf.trip.kmp.trips.data.remote.mappers

import de.awolf.trip.kmp.core.data.remote.mappers.dateStringToInstant
import de.awolf.trip.kmp.core.data.remote.mappers.toDiva
import de.awolf.trip.kmp.core.data.remote.mappers.toPlatform
import de.awolf.trip.kmp.core.data.remote.mappers.toResponseStatus
import de.awolf.trip.kmp.core.domain.models.DepartureState
import de.awolf.trip.kmp.core.domain.models.Mode
import de.awolf.trip.kmp.trips.data.remote.dto.MotDto
import de.awolf.trip.kmp.trips.data.remote.dto.PartialRouteDto
import de.awolf.trip.kmp.trips.data.remote.dto.PartialRouteStopDto
import de.awolf.trip.kmp.trips.data.remote.dto.RouteDto
import de.awolf.trip.kmp.trips.data.remote.dto.TripsResponseDto
import de.awolf.trip.kmp.trips.domain.models.Mot
import de.awolf.trip.kmp.trips.domain.models.PartialRoute
import de.awolf.trip.kmp.trips.domain.models.PartialRouteStop
import de.awolf.trip.kmp.trips.domain.models.Route
import de.awolf.trip.kmp.trips.domain.models.TripsResponse

fun TripsResponseDto.toTripsResponse(): TripsResponse {
    return TripsResponse(
        sessionId = sessionId,
        status = responseStatusDto.toResponseStatus(),
        routes = routes.map { it.toRoute() }
    )
}

fun RouteDto.toRoute(): Route {
    return Route(
        priceLevel = priceLevel,
        price = price,
        priceDayTicket = priceDayTicket,
        network = network,
        duration = duration,
        interchanges = interchanges,
        numberOfFareZones = numberOfFareZones,
        numberOfFareZonesDayTicket = numberOfFareZonesDayTicket,
        fareZonesNames = fareZonesNames,
        fareZonesNamesDayTicket = fareZonesNamesDayTicket,
        fareZoneOrigin = fareZoneOrigin,
        fareZoneDestination = fareZoneDestination,
        routeIndex = routeIndex,
        mapData = mapData,
        motChain = motChain.map { it.toMot() },
        partialRoutes = partialRoutes.map { it.toPartialRoute() }
    )
}

fun MotDto.toMot(): Mot {
    return Mot(
        dlId = dlId,
        lineId = lineId,
        mode = Mode.fromString(mode),
        lineNumber = lineNumber,
        lineDirection = lineDirection,
        transportationCompany = transportationCompany,
        operatorCode = operatorCode,
        trainNumber = trainNumber,
        productName = productName,
        changes = changes,
        diva = diva?.toDiva()
    )
}

fun PartialRouteDto.toPartialRoute(): PartialRoute {
    return PartialRoute(
        partialRouteId = partialRouteId,
        duration = duration,
        mot = mot.toMot(),
        mapDataIndex = mapDataIndex,
        shift = shift,
        regularStops = regularStops.map { it.toPartialRouteStop() },
        changeoverEndangered = changeoverEndangered,
        nextDepartureTimes = nextDepartureTimes,
        previousDepartureTimes = previousDepartureTimes
    )
}

fun PartialRouteStopDto.toPartialRouteStop(): PartialRouteStop {
    val arrivalScheduledTime = dateStringToInstant(arrivalScheduledTime)
    val departureScheduledTime = dateStringToInstant(departureScheduledTime)
    val arrivalRealTime =
        if (arrivalRealTime != null) dateStringToInstant(arrivalRealTime) else arrivalScheduledTime
    val departureRealTime =
        if (departureRealTime != null) dateStringToInstant(departureRealTime) else departureScheduledTime

    return PartialRouteStop(
        arrivalScheduledTime = arrivalScheduledTime,
        departureScheduledTime = departureScheduledTime,
        arrivalRealTime = arrivalRealTime,
        departureRealTime = departureRealTime,
        stopRegion = stopRegion,
        stopName = stopName,
        type = type,
        stopId = stopId,
        dhId = dhId,
        platform = platform?.toPlatform(),
        latitude = latitude,
        longitude = longitude,
        departureState = DepartureState.fromString(departureState),
        arrivalState = DepartureState.fromString(arrivalState),
        cancelReasons = cancelReasons,
        parkAndRail = parkAndRail,
        occupancy = occupancy
    )
}