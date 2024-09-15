package de.awolf.trip.kmp.core.domain.repository

import de.awolf.trip.kmp.core.domain.models.Stop

interface StopRepository {
    suspend fun getAllFavoriteStops(): List<Stop>

    suspend fun isStopFavorite(
        stopId: String
    ): Boolean

    suspend fun makeStopFavorite(
        stopId: String,
        name: String,
        region: String
    )

    suspend fun makeStopNotFavorite(
        stopId: String
    )

    suspend fun reorderFavoriteStop(
        stopId: String,
        oldRank: Long,
        newRank: Long
    )
}