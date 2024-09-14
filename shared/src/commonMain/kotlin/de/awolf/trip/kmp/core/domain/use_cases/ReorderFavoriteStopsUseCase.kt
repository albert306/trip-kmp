package de.awolf.trip.kmp.core.domain.use_cases

import de.awolf.trip.kmp.core.domain.repository.StopRepository

class ReorderFavoriteStopsUseCase(
    private val stopRepository: StopRepository
) {
    suspend operator fun invoke(stopId: String, oldRank: Long, newRank: Long) {
        stopRepository.reorderFavoriteStop(stopId, oldRank, newRank)
    }
}