package data.remote.mappers

import de.awolf.trip.kmp.core.data.remote.mappers.toResponseStatus
import de.awolf.trip.kmp.search.data.remote.dto.StopSearchResponseDto
import de.awolf.trip.kmp.search.domain.models.StopFinderInfo

fun StopSearchResponseDto.toStopFinderInfo(): StopFinderInfo {
    return StopFinderInfo(
        responseStatus = responseStatusDto.toResponseStatus(),
        pointStatus = pointStatus,
        stops = stops.map { parseStopString(it) },
        expirationTime = dateStringToInstant(expirationTime),
    )
}