package domain.repository

import domain.models.Mode
import domain.models.StopFinderInfo
import domain.models.StopMonitorInfo
import domain.models.StopScheduleItem
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import util.error.NetworkError
import util.Result

interface VvoServiceRepository {
    suspend fun monitorStop(
        stopId: String,
        limit: Int = 5,
        time: Instant = Clock.System.now(),
        isArrival: Boolean = false,
        shorttermchanges: Boolean = true,
        modeOfTransport: List<String> = Mode.getAllLocal()
    ): Result<StopMonitorInfo, NetworkError>

    suspend fun getStopByName(
        query: String,
        limit: Int = 15,
        stopsOnly: Boolean = true,
        regionalOnly: Boolean = true,
        stopShortcuts: Boolean = false
    ): Result<StopFinderInfo, NetworkError>

    suspend fun getStopSchedule(
        stopId: String,
        departureId: String,
        time: Instant
    ): Result<List<StopScheduleItem>, NetworkError>
}