package de.awolf.trip.kmp.core.domain.use_cases

import de.awolf.trip.kmp.core.domain.models.Stop
import de.awolf.trip.kmp.core.domain.repository.StopRepository

class ToggleFavoriteStopUseCase(
    private val stopRepository: StopRepository
) {
    suspend operator fun invoke(stop: Stop) {

        if (stopRepository.isStopFavorite(stop.id)) {
            stopRepository.makeStopNotFavorite(stop.id)
        } else {
            stopRepository.makeStopFavorite(stop.id, stop.name, stop.region)
        }
    }
}