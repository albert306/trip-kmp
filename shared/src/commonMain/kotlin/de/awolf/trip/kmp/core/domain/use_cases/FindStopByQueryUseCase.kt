package de.awolf.trip.kmp.core.domain.use_cases

import de.awolf.trip.kmp.core.domain.models.StopSearchInfo
import de.awolf.trip.kmp.core.domain.repository.StopRepository
import de.awolf.trip.kmp.core.domain.repository.StopSearchRemoteRepository
import de.awolf.trip.kmp.core.util.Result
import de.awolf.trip.kmp.core.util.error.NetworkError

class FindStopByQueryUseCase(
    private val stopRepository: StopRepository,
    private val stopSearchRemoteRepository: StopSearchRemoteRepository
) {
    suspend operator fun invoke(
        query: String,
        limit: Int = 15,
        stopsOnly: Boolean = true,
        regionalOnly: Boolean = true,
        stopShortcuts: Boolean = false
    ): Result<StopSearchInfo, NetworkError> {

        return when (val response = stopSearchRemoteRepository.getStopByName(
            query = query,
            limit = limit,
            stopsOnly = stopsOnly,
            regionalOnly = regionalOnly,
            stopShortcuts = stopShortcuts
        )) {
            is Result.Error -> {
                response
            }
            is Result.Success -> {

                val stopListMarkedFavorites = response.data.stops.map {
                    it.copy(isFavorite = stopRepository.isStopFavorite(it.id))
                }

                Result.Success(response.data.copy(stops = stopListMarkedFavorites))
            }
        }
    }
}