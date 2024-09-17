package de.awolf.trip.kmp.trips.domain.use_cases

import de.awolf.trip.kmp.core.util.Result
import de.awolf.trip.kmp.core.util.error.NetworkError
import de.awolf.trip.kmp.trips.domain.models.TripsResponse
import de.awolf.trip.kmp.trips.domain.repository.TripsRemoteRepository
import kotlinx.datetime.Instant

class FetchTripsUseCase(
    private val tripsRemoteRepository: TripsRemoteRepository
) {
    suspend operator fun invoke(
        origin: String,
        destination: String,
        time: Instant,
        isArrivalTime: Boolean = false,
        shorttermchanges: Boolean = true,
    ): Result<TripsResponse, NetworkError> {

        return when (val response = tripsRemoteRepository.getTrips(
            origin = origin,
            destination = destination,
            time = time,
            isArrivalTime = isArrivalTime,
            shorttermchanges = shorttermchanges,
        )) {
            is Result.Error -> {
                response
            }
            is Result.Success -> {
                response
            }
        }
    }
}