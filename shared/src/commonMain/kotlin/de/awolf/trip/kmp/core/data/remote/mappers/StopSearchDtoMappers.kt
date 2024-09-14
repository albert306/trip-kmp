package de.awolf.trip.kmp.core.data.remote.mappers

import data.remote.mappers.dateStringToInstant
import data.remote.mappers.parseStopString
import de.awolf.trip.kmp.core.data.remote.dto.StopSearchResponseDto
import de.awolf.trip.kmp.core.domain.models.StopFinderInfo

fun StopSearchResponseDto.toStopFinderInfo(): StopFinderInfo {
    return StopFinderInfo(
        responseStatus = responseStatusDto.toResponseStatus(),
        pointStatus = pointStatus,
        stops = stops.map { parseStopString(it) },
        expirationTime = dateStringToInstant(expirationTime),
    )
}