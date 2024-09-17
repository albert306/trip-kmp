package de.awolf.trip.kmp.trips.domain.repository

import de.awolf.trip.kmp.core.util.Result
import de.awolf.trip.kmp.core.util.error.NetworkError
import de.awolf.trip.kmp.trips.domain.models.TripsResponse
import kotlinx.datetime.Instant

interface TripsRemoteRepository {
    suspend fun getTrips(
        origin: String,
        destination: String,
        time: Instant,
        isArrivalTime: Boolean,
        shorttermchanges: Boolean,
    ): Result<TripsResponse, NetworkError>
}