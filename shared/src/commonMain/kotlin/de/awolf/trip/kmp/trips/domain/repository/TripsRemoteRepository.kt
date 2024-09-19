package de.awolf.trip.kmp.trips.domain.repository

import de.awolf.trip.kmp.core.util.Result
import de.awolf.trip.kmp.core.util.error.NetworkError
import de.awolf.trip.kmp.trips.domain.models.TripQuery
import de.awolf.trip.kmp.trips.domain.models.TripsResponse

interface TripsRemoteRepository {
    suspend fun getTrips(query: TripQuery): Result<TripsResponse, NetworkError>
}