package de.awolf.trip.kmp.search.domain.use_cases

import de.awolf.trip.kmp.core.domain.models.Stop
import de.awolf.trip.kmp.search.domain.repository.SearchLocalRepository
import de.awolf.trip.kmp.core.util.Result
import de.awolf.trip.kmp.core.util.error.DatabaseError

class GetFavoriteStopsUseCase(
    private val searchLocalRepository: SearchLocalRepository
) {
    suspend operator fun invoke(): Result<List<Stop>, DatabaseError> {
        return try {
            Result.Success(
                searchLocalRepository.getAllFavoriteStops()
            )
        } catch (e: Exception) {
            Result.Error(DatabaseError.UNKNOWN)
        }
    }
}