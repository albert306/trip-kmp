package de.awolf.trip.kmp.search.domain.use_cases

import de.awolf.trip.kmp.core.domain.models.Stop
import de.awolf.trip.kmp.search.domain.repository.SearchLocalRepository

class ToggleFavoriteStopUseCase(
    private val searchLocalRepository: SearchLocalRepository
) {
    suspend operator fun invoke(stop: Stop) {

        if (searchLocalRepository.isStopFavorite(stop.id)) {
            searchLocalRepository.makeStopNotFavorite(stop.id)
        } else {
            searchLocalRepository.makeStopFavorite(stop.id, stop.name, stop.region)
        }
    }
}