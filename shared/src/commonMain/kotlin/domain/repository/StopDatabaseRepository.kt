package domain.repository

import domain.models.Stop

interface StopDatabaseRepository {
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