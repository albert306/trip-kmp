package de.awolf.trip.kmp.departures.domain.use_cases

import de.awolf.trip.kmp.departures.domain.models.Departure
import de.awolf.trip.kmp.departures.domain.models.StopScheduleItem
import de.awolf.trip.kmp.core.util.Result
import de.awolf.trip.kmp.core.util.error.NetworkError
import de.awolf.trip.kmp.departures.domain.repository.DeparturesRemoteRepository

class FetchStopScheduleUseCase(
    private val departuresRemoteRepository: DeparturesRemoteRepository
) {
    suspend operator fun invoke(
        departure: Departure,
        stopId: String
    ): Result<List<StopScheduleItem>, NetworkError> {

        return when (val response = departuresRemoteRepository.getStopSchedule(
            departureId = departure.id,
            time = departure.realTime,
            stopId = stopId
        )) {
            is Result.Error -> {
                response
            }

            is Result.Success -> {
                Result.Success(response.data.map {
                    it.copy(
                        stopName = it.stopName.removePrefix("Dresden "),
                    )
                })
            }
        }
    }
}