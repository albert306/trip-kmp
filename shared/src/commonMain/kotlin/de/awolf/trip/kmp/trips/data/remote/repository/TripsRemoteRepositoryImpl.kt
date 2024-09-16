package de.awolf.trip.kmp.trips.data.remote.repository

import de.awolf.trip.kmp.core.data.remote.repository.BaseHttpRepository
import de.awolf.trip.kmp.core.util.Result
import de.awolf.trip.kmp.core.util.error.NetworkError
import de.awolf.trip.kmp.trips.data.remote.HttpRoutes
import de.awolf.trip.kmp.trips.domain.repository.TripsRemoteRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.datetime.Instant
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeComponents
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

class TripsRemoteRepositoryImpl(
    private val client: HttpClient
): BaseHttpRepository(), TripsRemoteRepository {

    override suspend fun getTrips(
        origin: String,
        destination: String,
        time: Instant,
        isArrivalTime: Boolean,
        shorttermchanges: Boolean,
    ): Result<TODO, NetworkError> {

        val jsonBody = JsonObject(
            mapOf(
                "origin" to JsonPrimitive(origin),
                "destination" to JsonPrimitive(destination),
                "time" to JsonPrimitive(time.format(DateTimeComponents.Formats.ISO_DATE_TIME_OFFSET)),
                "isarrival" to JsonPrimitive(isArrivalTime),
                "shorttermchanges" to JsonPrimitive(shorttermchanges),
            )
        )

        return catchNetworkExceptions<TODO>(
            request = {
                client.post {
                    url(HttpRoutes.TRIPS)
                    contentType(ContentType.Application.Json)
                    setBody(jsonBody)
                }
            },
            onSuccessMapper = { response ->
                TODO()
            }
        )
    }
}