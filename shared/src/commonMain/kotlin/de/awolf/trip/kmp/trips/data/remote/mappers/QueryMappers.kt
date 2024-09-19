package de.awolf.trip.kmp.trips.data.remote.mappers

import de.awolf.trip.kmp.trips.data.remote.dto.TripQueryDto
import de.awolf.trip.kmp.trips.domain.models.TripQuery

fun TripQuery.toTripQueryDto(): TripQueryDto {
    return TripQueryDto(
        origin = origin,
        via = via,
        stayDuration = "${stayDuration?.inWholeHours ?: 0}:${stayDuration?.inWholeMinutes ?: 0}",
        destination = destination,
        time = time.toInstant(),
        isArrivalTime = isArrivalTime,
        shorttermchanges = shorttermchanges,
        settings = settings?.toSettingsDto(),
    )
}

fun TripQuery.Settings.toSettingsDto(): TripQueryDto.SettingsDto {
    return TripQueryDto.SettingsDto(
        footpathToStop = footpathToStop,
        walkingSpeed = walkingSpeed.rawValue,
        maxChanges = maxChanges,
        modeOfTransport = modeOfTransport.map { it.rawValue },
        includeAlternativeStops = includeAlternativeStops,
    )
}