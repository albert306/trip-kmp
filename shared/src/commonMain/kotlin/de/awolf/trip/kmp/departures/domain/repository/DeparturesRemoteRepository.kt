package de.awolf.trip.kmp.departures.domain.repository

import de.awolf.trip.kmp.core.util.Result
import de.awolf.trip.kmp.core.util.error.NetworkError
import de.awolf.trip.kmp.departures.domain.models.StopMonitorInfo
import de.awolf.trip.kmp.departures.domain.models.StopScheduleItem
import kotlinx.datetime.Instant

interface DeparturesRemoteRepository {
    suspend fun getDepartures(
        stopId: String,
        limit: Int,
        time: Instant,
        isArrival: Boolean,
        shorttermchanges: Boolean,
        modeOfTransport: List<String>
    ): Result<StopMonitorInfo, NetworkError>

    suspend fun getStopSchedule(
        stopId: String,
        departureId: String,
        time: Instant
    ): Result<List<StopScheduleItem>, NetworkError>
}