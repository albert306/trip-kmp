package data.networking.mappers

import data.networking.dto.ResponseStatusDto
import data.networking.dto.stop_finder.StopFinderResponseDto
import data.networking.dto.stop_monitor.DepartureDto
import data.networking.dto.stop_monitor.DivaDto
import data.networking.dto.stop_monitor.PlatformDto
import data.networking.dto.stop_monitor.StopMonitorResponseDto
import data.networking.dto.stop_monitor.StopScheduleItemDto
import domain.models.Departure
import domain.models.Diva
import domain.models.Mode
import domain.models.Platform
import domain.models.ResponseStatus
import domain.models.StopFinderInfo
import domain.models.StopMonitorInfo
import domain.models.StopScheduleItem

fun PlatformDto.toPlatform(): Platform {
    return Platform(
        type = type,
        name = name,
    )
}

fun DivaDto.toDiva(): Diva {
    return Diva(
        number = number,
        network = network,
    )
}

fun ResponseStatusDto.toResponseStatus(): ResponseStatus {
    return ResponseStatus(
        code = code,
        message = message,
    )
}

fun StopMonitorResponseDto.toStopMonitorInfo(): StopMonitorInfo {
    return StopMonitorInfo(
        name = name,
        responseStatus = responseStatusDto.toResponseStatus(),
        region = region,
        expirationTime = dateStringToInstant(expirationTime),
        departures = departures.map { it.toDeparture() },
    )
}


fun DepartureDto.toDeparture(): Departure {
    return Departure(
        id = id,
        dlId = dlId,
        lineNumber = lineNumber,
        lineDirection = lineDirection,
        platform = platform?.toPlatform(),
        mode = Mode.fromString(mode),
        scheduledTime = dateStringToInstant(scheduledTime),
        realTime = dateStringToInstant(realTime),
        departureState = Departure.DepartureState.fromString(state),
        routeChanges = routeChanges,
        diva = diva?.toDiva(),
    )
}

fun StopFinderResponseDto.toStopFinderInfo(): StopFinderInfo {
    return StopFinderInfo(
        responseStatus = responseStatusDto.toResponseStatus(),
        pointStatus = pointStatus,
        stops = stops.map {
            parseStopString(it)
        },
        expirationTime = dateStringToInstant(expirationTime),
    )
}

fun StopScheduleItemDto.toStopScheduleItem(): StopScheduleItem {
    return StopScheduleItem(
        stopId = stopId,
        dlId = dlId,
        stopRegion = stopRegion,
        stopName = stopName,
        shedulePosition = shedulePosition,
        platform = platform?.toPlatform(),
        arrivalTime = dateStringToInstant(arrivalTime)
    )
}