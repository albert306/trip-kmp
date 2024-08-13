package domain.use_case

import domain.repository.StopDatabaseRepository

class ReorderFavoriteStopsUseCase(
    private val stopDatabaseRepository: StopDatabaseRepository
) {
    suspend operator fun invoke(stopId: String, oldRank: Long, newRank: Long) {
        stopDatabaseRepository.reorderFavoriteStop(stopId, oldRank, newRank)
    }

}