package data.local.repository

import de.awolf.trip.kmp.shared.database.StopDatabase
import domain.models.Stop
import domain.repository.StopDatabaseRepository

class StopDatabaseRepositoryImpl(
    private val db: StopDatabase
) : StopDatabaseRepository {

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
            rank = null
        )
    }

    override suspend fun makeStopNotFavorite(
        stopId: String
    ) {
        queries.makeStopNotFavorite(stopId)
    }
}