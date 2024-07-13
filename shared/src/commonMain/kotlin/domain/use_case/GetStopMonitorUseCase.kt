package domain.use_case

import domain.models.Stop
import domain.models.StopMonitorInfo
import domain.repository.VvoServiceRepository
import kotlinx.datetime.Instant
import util.Error
import util.Result

class GetStopMonitorUseCase(
    private val vvoServiceRepository: VvoServiceRepository
) {

    suspend operator fun invoke(stop: Stop, time: Instant, limit: Int): Result<StopMonitorInfo, Error> {
        return when (val response = vvoServiceRepository.monitorStop(
            stopId = stop.id,
            limit = limit,
            time = time, // add optional query parameters
        )) {
            is Result.Error -> {
                response
            }
            is Result.Success -> {
                Result.Success(response.data)
            }
        }
    }
}