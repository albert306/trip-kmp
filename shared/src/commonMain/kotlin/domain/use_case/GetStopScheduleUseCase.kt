package domain.use_case

import domain.models.Departure
import domain.models.StopScheduleItem
import domain.repository.VvoServiceRepository
import util.Result
import util.error.NetworkError
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class GetStopScheduleUseCase(
    val vvoServiceRepository: VvoServiceRepository
) {
    suspend operator fun invoke(
        departure: Departure,
        stopId: String
    ): Result<List<StopScheduleItem>, NetworkError> {
        println("GetStopScheduleUseCase invoked")

        return when (val response = vvoServiceRepository.getStopSchedule(
            departureId = departure.id,
            time = departure.realTime,
            stopId = stopId
        )) {
            is Result.Error -> {
                response
            }

            is Result.Success -> {
                val delay = departure.getDelay()

                Result.Success(response.data.map {
                    it.copy(
                        stopName = it.stopName.removePrefix("Dresden "),
                        arrivalTime = it.arrivalTime.plus(delay.toDuration(DurationUnit.MINUTES))
                    )
                })
            }
        }

    }
}