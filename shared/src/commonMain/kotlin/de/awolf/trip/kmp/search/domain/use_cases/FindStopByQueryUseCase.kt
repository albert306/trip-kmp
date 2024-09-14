package de.awolf.trip.kmp.search.domain.use_cases

import de.awolf.trip.kmp.search.domain.models.StopFinderInfo
import de.awolf.trip.kmp.search.domain.repository.SearchLocalRepository
import de.awolf.trip.kmp.core.util.Result
import de.awolf.trip.kmp.core.util.error.NetworkError
import de.awolf.trip.kmp.search.domain.repository.SearchRemoteRepository

class FindStopByQueryUseCase(
    private val searchLocalRepository: SearchLocalRepository,
    private val searchRemoteRepository: SearchRemoteRepository
) {
    suspend operator fun invoke(
        query: String,
        limit: Int = 15,
        stopsOnly: Boolean = true,
        regionalOnly: Boolean = true,
        stopShortcuts: Boolean = false
    ): Result<StopFinderInfo, NetworkError> {

        return when (val response = searchRemoteRepository.getStopByName(
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
                    it.copy(isFavorite = searchLocalRepository.isStopFavorite(it.id))
                }

                Result.Success(response.data.copy(stops = stopListMarkedFavorites))
            }
        }
    }
}