package de.awolf.trip.kmp.core.data.remote.mappers

import data.remote.mappers.dateStringToInstant
import data.remote.mappers.parseStopString
import de.awolf.trip.kmp.core.data.remote.dto.DivaDto
import de.awolf.trip.kmp.core.data.remote.dto.PlatformDto
import de.awolf.trip.kmp.core.data.remote.dto.ResponseStatusDto
import de.awolf.trip.kmp.core.data.remote.dto.StopSearchResponseDto
import de.awolf.trip.kmp.core.domain.models.Platform
import de.awolf.trip.kmp.core.domain.models.ResponseStatus
import de.awolf.trip.kmp.core.domain.models.Diva
import de.awolf.trip.kmp.core.domain.models.StopSearchInfo

fun ResponseStatusDto.toResponseStatus(): ResponseStatus {
    return ResponseStatus(
        code = code,
        message = message,
    )
}

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

fun StopSearchResponseDto.toStopFinderInfo(): StopSearchInfo {
    return StopSearchInfo(
        responseStatus = responseStatusDto.toResponseStatus(),
        pointStatus = pointStatus,
        stops = stops.map { parseStopString(it) },
        expirationTime = dateStringToInstant(expirationTime),
    )
}