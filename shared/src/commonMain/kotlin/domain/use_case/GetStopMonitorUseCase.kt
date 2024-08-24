package domain.use_case

import domain.models.Stop
import domain.models.StopMonitorInfo
import domain.repository.VvoServiceRepository
import kotlinx.datetime.Instant
import util.Result
import util.error.NetworkError

class GetStopMonitorUseCase(
    private val vvoServiceRepository: VvoServiceRepository
) {

    suspend operator fun invoke(stop: Stop, time: Instant, limit: Int): Result<StopMonitorInfo, NetworkError> {
        return when (val response = vvoServiceRepository.monitorStop(
            stopId = stop.id,
            limit = limit,
            time = time, // add optional query parameters
        )) {
            is Result.Error -> {
                response
            }
            is Result.Success -> {
                val sortedUniqueDepartures = response.data.departures.distinct().sorted()
                Result.Success(response.data.copy(departures = sortedUniqueDepartures))
            }
        }
    }
}