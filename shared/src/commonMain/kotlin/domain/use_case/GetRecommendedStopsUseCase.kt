package domain.use_case

import domain.models.Stop
import domain.repository.StopDatabaseRepository
import domain.repository.VvoServiceRepository
import util.error.Error
import util.Result

class GetRecommendedStopsUseCase(
    private val vvoServiceRepository: VvoServiceRepository,
    private val stopDatabaseRepository: StopDatabaseRepository
) {

    suspend operator fun invoke(query: String): Result<List<Stop>, Error> {

        return if (query.length < 3) {
            Result.Success(getFavoriteStops())
        } else {
            when (val response = vvoServiceRepository.getStopByName(
                query = query, // add optional query parameters
            )) {
                is Result.Error -> {
                    response
                }
                is Result.Success -> {

                    val stopListMarkedFavorites = response.data.stops.map {
                        it.copy(isFavorite = stopDatabaseRepository.isStopFavorite(it.id))
                    }

                    Result.Success(stopListMarkedFavorites)
                }
            }
        }
    }
    private suspend fun getFavoriteStops(): List<Stop> {
        return stopDatabaseRepository.getAllFavoriteStops()
    }
}