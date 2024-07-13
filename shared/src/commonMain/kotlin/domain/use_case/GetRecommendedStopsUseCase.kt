package domain.use_case

import domain.models.Stop
import domain.repository.VvoServiceRepository
import util.Error
import util.Result

class GetRecommendedStopsUseCase(
    private val vvoServiceRepository: VvoServiceRepository,
) {

    suspend operator fun invoke(query: String): Result<List<Stop>, Error> {

        return if (query.length < 3) {
            Result.Success(getFavouriteStops())
        } else {
            when (val response = vvoServiceRepository.getStopByName(
                query = query, // add optional query parameters
            )) {
                is Result.Error -> {
                    response
                }
                is Result.Success -> {
                    // merge two with favourite stop information to show favourites in api response stoplist
//                    val stopListMarkedFavourites = response.data!!.stops.map { stop ->
//                        stop.isFavourite = stopDatabaseRepository.isStopFavourite(stop.id)
//                        stop
//                    }

                    Result.Success(response.data.stops)
                }
            }
        }
    }
    private fun getFavouriteStops(): List<Stop> {
        return emptyList()
    }
}