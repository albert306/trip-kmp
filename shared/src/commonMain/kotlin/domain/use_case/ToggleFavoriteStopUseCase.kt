package domain.use_case

import domain.models.Stop
import domain.repository.StopDatabaseRepository

class ToggleFavoriteStopUseCase(
    private val stopDatabaseRepository: StopDatabaseRepository
) {
    suspend operator fun invoke(stop: Stop) {

        if (stopDatabaseRepository.isStopFavorite(stop.id)) {
            stopDatabaseRepository.makeStopNotFavorite(stop.id)
        } else {
            stopDatabaseRepository.makeStopFavorite(stop.id, stop.name, stop.region)
        }
    }
}