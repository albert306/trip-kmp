package de.awolf.trip.kmp.departures.domain.use_cases

import de.awolf.trip.kmp.core.domain.models.Mode
import de.awolf.trip.kmp.core.domain.models.Stop
import de.awolf.trip.kmp.departures.domain.models.StopMonitorInfo
import de.awolf.trip.kmp.core.util.Result
import de.awolf.trip.kmp.core.util.error.NetworkError
import de.awolf.trip.kmp.departures.domain.repository.DeparturesRemoteRepository
import kotlinx.datetime.Instant

class FetchDeparturesUseCase(
    private val departuresRemoteRepository: DeparturesRemoteRepository
) {
    suspend operator fun invoke(
        stop: Stop,
        time: Instant,
        limit: Int,
        isArrival: Boolean = false,
        shorttermchanges: Boolean = true,
        modeOfTransport: List<String> = Mode.getAllLocal()
    ): Result<StopMonitorInfo, NetworkError> {

        return when (val response = departuresRemoteRepository.getDepartures(
            stopId = stop.id,
            limit = limit,
            time = time,
            isArrival = isArrival,
            shorttermchanges = shorttermchanges,
            modeOfTransport = modeOfTransport
        )) {
            is Result.Error -> {
                response
            }
            is Result.Success -> {
                val maxDepartureCount = if (response.data.departures.size < limit) {
                    response.data.departures.size
                } else {
                    Int.MAX_VALUE
                }
                val sortedUniqueDepartures = response.data.departures.distinct().sorted()

                Result.Success(
                    response.data.copy(
                        departures = sortedUniqueDepartures,
                        maxDepartureCount = maxDepartureCount
                    )
                )
            }
        }
    }
}