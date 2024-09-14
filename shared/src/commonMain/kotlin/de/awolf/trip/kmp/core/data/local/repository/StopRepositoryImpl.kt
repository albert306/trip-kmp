package de.awolf.trip.kmp.core.data.local.repository

import de.awolf.trip.kmp.shared.database.StopDatabase
import de.awolf.trip.kmp.core.domain.models.Stop
import de.awolf.trip.kmp.core.domain.repository.StopRepository

class StopRepositoryImpl(
    private val db: StopDatabase
) : StopRepository {

    private val queries = db.favoriteStopsQueries

    override suspend fun getAllFavoriteStops(): List<Stop> {
        val favorites = queries.getFavoriteStops().executeAsList()

        if (favorites.isEmpty()) return emptyList()
        return favorites.map {
            Stop(
                id = it.id,
                name = it.name,
                region = it.region,
                isFavorite = true
            )
        }
    }

    override suspend fun isStopFavorite(
        stopId: String
    ): Boolean {
        return queries.isStopFavorite(stopId).executeAsOneOrNull() != null
    }

    override suspend fun makeStopFavorite(
        stopId: String,
        name: String,
        region: String
    ) {
        queries.makeStopFavorite(
            id = stopId,
            name = name,
            region = region,
        )
    }

    override suspend fun makeStopNotFavorite(
        stopId: String
    ) {
        queries.makeStopNotFavorite(stopId)
    }

    override suspend fun reorderFavoriteStop(
        stopId: String,
        oldRank: Long,
        newRank: Long
    ) {
        queries.reorderFavoriteStop(
            id = stopId,
            oldRank = oldRank,
            newRank = newRank
        )
    }
}