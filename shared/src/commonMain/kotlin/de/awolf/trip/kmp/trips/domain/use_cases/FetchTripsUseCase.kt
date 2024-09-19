package de.awolf.trip.kmp.trips.domain.use_cases

import de.awolf.trip.kmp.core.util.Result
import de.awolf.trip.kmp.core.util.error.NetworkError
import de.awolf.trip.kmp.trips.domain.models.TripQuery
import de.awolf.trip.kmp.trips.domain.models.TripsResponse
import de.awolf.trip.kmp.trips.domain.repository.TripsRemoteRepository

class FetchTripsUseCase(
    private val tripsRemoteRepository: TripsRemoteRepository
) {
    suspend operator fun invoke(query: TripQuery): Result<TripsResponse, NetworkError> {

        return when (val response = tripsRemoteRepository.getTrips(query)) {
            is Result.Error -> {
                response
            }
            is Result.Success -> {
                response
            }
        }
    }
}