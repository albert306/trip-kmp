package de.awolf.trip.kmp.trips.domain.repository

import de.awolf.trip.kmp.core.util.Result
import de.awolf.trip.kmp.core.util.error.NetworkError
import kotlinx.datetime.Instant

interface TripsRemoteRepository {
    suspend fun getTrips(
        origin: String,
        destination: String,
        shorttermchanges: Boolean,
        time: Instant,
        isArrivalTime: Boolean,
    ): Result<TODO, NetworkError>
}