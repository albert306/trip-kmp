package de.awolf.trip.kmp.trips.data.remote.repository

import de.awolf.trip.kmp.core.data.remote.repository.BaseHttpRepository
import de.awolf.trip.kmp.core.util.Result
import de.awolf.trip.kmp.core.util.error.NetworkError
import de.awolf.trip.kmp.trips.data.remote.HttpRoutes
import de.awolf.trip.kmp.trips.data.remote.dto.TripsResponseDto
import de.awolf.trip.kmp.trips.data.remote.mappers.toTripsResponse
import de.awolf.trip.kmp.trips.domain.models.TripQuery
import de.awolf.trip.kmp.trips.domain.models.TripsResponse
import de.awolf.trip.kmp.trips.domain.repository.TripsRemoteRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class TripsRemoteRepositoryImpl(
    private val client: HttpClient
): BaseHttpRepository(), TripsRemoteRepository {

    override suspend fun getTrips(query: TripQuery, ): Result<TripsResponse, NetworkError> {

        return catchNetworkExceptions<TripsResponse>(
            request = {
                client.post {
                    url(HttpRoutes.TRIPS)
                    contentType(ContentType.Application.Json)
                    setBody(Json.encodeToString(query))
                }
            },
            onSuccessMapper = { response ->
                response.body<TripsResponseDto>().toTripsResponse()
            }
        )
    }
}