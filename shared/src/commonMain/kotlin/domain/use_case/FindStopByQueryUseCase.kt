package domain.use_case

import domain.models.StopFinderInfo
import domain.repository.StopDatabaseRepository
import domain.repository.VvoServiceRepository
import util.Result
import util.error.NetworkError

class FindStopByQueryUseCase(
    private val vvoServiceRepository: VvoServiceRepository,
    private val stopDatabaseRepository: StopDatabaseRepository
) {
    suspend operator fun invoke(query: String): Result<StopFinderInfo, NetworkError> {

        return when (val response = vvoServiceRepository.getStopByName(
            query = query, // add optional query parameters
        )) {
            is Result.Error -> {
                response
            }
            is Result.Success -> {

                val stopListMarkedFavorites = response.data.stops.map {
                    it.copy(isFavorite = stopDatabaseRepository.isStopFavorite(it.id))
                }

                Result.Success(response.data.copy(stops = stopListMarkedFavorites))
            }
        }
    }
}