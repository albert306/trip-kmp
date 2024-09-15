package de.awolf.trip.kmp.core.domain.use_cases

import de.awolf.trip.kmp.core.domain.models.Stop
import de.awolf.trip.kmp.core.domain.repository.StopRepository
import de.awolf.trip.kmp.core.util.Result
import de.awolf.trip.kmp.core.util.error.DatabaseError

class GetFavoriteStopsUseCase(
    private val stopRepository: StopRepository
) {
    suspend operator fun invoke(): Result<List<Stop>, DatabaseError> {
        return try {
            Result.Success(
                stopRepository.getAllFavoriteStops()
            )
        } catch (e: Exception) {
            Result.Error(DatabaseError.UNKNOWN)
        }
    }
}