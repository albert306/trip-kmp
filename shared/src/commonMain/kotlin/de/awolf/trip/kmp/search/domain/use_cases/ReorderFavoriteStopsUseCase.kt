package de.awolf.trip.kmp.search.domain.use_cases

import de.awolf.trip.kmp.search.domain.repository.SearchLocalRepository

class ReorderFavoriteStopsUseCase(
    private val searchLocalRepository: SearchLocalRepository
) {
    suspend operator fun invoke(stopId: String, oldRank: Long, newRank: Long) {
        searchLocalRepository.reorderFavoriteStop(stopId, oldRank, newRank)
    }
}