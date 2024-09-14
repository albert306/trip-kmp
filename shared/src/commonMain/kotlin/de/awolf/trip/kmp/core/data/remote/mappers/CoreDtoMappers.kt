package de.awolf.trip.kmp.core.data.remote.mappers

import de.awolf.trip.kmp.core.data.remote.dto.DivaDto
import de.awolf.trip.kmp.core.data.remote.dto.PlatformDto
import de.awolf.trip.kmp.core.data.remote.dto.ResponseStatusDto
import de.awolf.trip.kmp.core.domain.models.Platform
import de.awolf.trip.kmp.core.domain.models.ResponseStatus
import de.awolf.trip.kmp.core.domain.models.Diva

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