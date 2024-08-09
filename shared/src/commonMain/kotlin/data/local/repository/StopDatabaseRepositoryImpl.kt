package data.local.repository

import de.awolf.trip.kmp.shared.database.StopDatabase
import domain.models.Stop
import domain.repository.StopDatabaseRepository

class StopDatabaseRepositoryImpl(
    private val db: StopDatabase
) : StopDatabaseRepository {

    private val queries = db.favoriteStopsQueries

    override suspend fun getAllFavoriteStops(): List<Stop> {
        return queries.getFavoriteStops().executeAsList().map {
            Stop(
                id = it.id,
                name = it.name,
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
            region = region
        )
    }

    override suspend fun makeStopNotFavorite(
        stopId: String
    ) {
        queries.makeStopNotFavorite(stopId)
    }
}