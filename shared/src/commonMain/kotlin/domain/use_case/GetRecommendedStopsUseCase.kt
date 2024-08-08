package domain.use_case

import domain.models.Stop
import domain.repository.VvoServiceRepository
import util.error.Error
import util.Result

class GetRecommendedStopsUseCase(
    private val vvoServiceRepository: VvoServiceRepository,
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
                    // merge two with favorite stop information to show favorites in api response stoplist
//                    val stopListMarkedFavorites = response.data!!.stops.map { stop ->
//                        stop.isFavorite = stopDatabaseRepository.isStopFavorite(stop.id)
//                        stop
//                    }

                    Result.Success(response.data.stops)
                }
            }
        }
    }
    private fun getFavoriteStops(): List<Stop> {
        return emptyList()
    }
}