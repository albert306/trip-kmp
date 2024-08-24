package domain.use_case

import domain.models.Stop
import domain.repository.StopDatabaseRepository
import util.Result
import util.error.DatabaseError


class GetFavoriteStopsUseCase(
    private val stopDatabaseRepository: StopDatabaseRepository
) {
    suspend operator fun invoke(): Result<List<Stop>, DatabaseError> {
        return try {
            Result.Success(
                stopDatabaseRepository.getAllFavoriteStops()
            )
        } catch (e: Exception) {
            Result.Error(DatabaseError.UNKNOWN)
        }
    }
}