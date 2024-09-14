package de.awolf.trip.kmp.departures.data.remote.mappers

import data.remote.mappers.dateStringToInstant
import de.awolf.trip.kmp.core.data.remote.mappers.toResponseStatus
import de.awolf.trip.kmp.departures.data.remote.dto.DepartureDto
import de.awolf.trip.kmp.core.data.remote.mappers.toDiva
import de.awolf.trip.kmp.core.data.remote.mappers.toPlatform
import de.awolf.trip.kmp.departures.data.remote.dto.DepartureMonitorResponseDto
import de.awolf.trip.kmp.departures.data.remote.dto.StopScheduleItemDto
import de.awolf.trip.kmp.departures.domain.models.Departure
import de.awolf.trip.kmp.core.domain.models.Mode
import de.awolf.trip.kmp.departures.domain.models.StopMonitorInfo
import de.awolf.trip.kmp.departures.domain.models.StopScheduleItem

fun DepartureMonitorResponseDto.toStopMonitorInfo(): StopMonitorInfo {
    return StopMonitorInfo(
        name = name,
        responseStatus = responseStatusDto.toResponseStatus(),
        region = region,
        expirationTime = dateStringToInstant(expirationTime),
        departures = departures.map { it.toDeparture() },
    )
}

fun DepartureDto.toDeparture(): Departure {
    val scheduledTime = dateStringToInstant(scheduledTime)
    val realTime = if (realTime != null) dateStringToInstant(realTime) else scheduledTime
    return Departure(
        id = id,
        dlId = dlId,
        lineNumber = lineNumber,
        lineDirection = lineDirection,
        platform = platform?.toPlatform(),
        mode = Mode.fromString(mode),
        scheduledTime = scheduledTime,
        realTime = realTime,
        departureState = Departure.DepartureState.fromString(state),
        routeChanges = routeChanges,
        diva = diva?.toDiva(),
        stopSchedule = null,
    )
}

fun StopScheduleItemDto.toStopScheduleItem(): StopScheduleItem {
    val scheduledTime = dateStringToInstant(scheduledTime)
    val realTime = if (realTime != null) dateStringToInstant(realTime) else scheduledTime
    return StopScheduleItem(
        stopId = stopId,
        dlId = dlId,
        stopRegion = stopRegion,
        stopName = stopName,
        schedulePosition = StopScheduleItem.SchedulePosition.fromString(schedulePosition),
        platform = platform?.toPlatform(),
        scheduledTime = scheduledTime,
        realTime = realTime,
    )
}