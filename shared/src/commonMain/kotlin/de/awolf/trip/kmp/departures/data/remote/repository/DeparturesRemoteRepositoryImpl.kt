package de.awolf.trip.kmp.departures.data.remote.repository

import de.awolf.trip.kmp.core.data.remote.repository.BaseHttpRepository
import de.awolf.trip.kmp.core.util.Result
import de.awolf.trip.kmp.core.util.error.NetworkError
import de.awolf.trip.kmp.departures.data.remote.HttpRoutes
import de.awolf.trip.kmp.departures.data.remote.dto.DepartureMonitorResponseDto
import de.awolf.trip.kmp.departures.data.remote.dto.StopScheduleResponseDto
import de.awolf.trip.kmp.departures.data.remote.mappers.toStopMonitorInfo
import de.awolf.trip.kmp.departures.data.remote.mappers.toStopScheduleItem
import de.awolf.trip.kmp.departures.domain.models.StopMonitorInfo
import de.awolf.trip.kmp.departures.domain.models.StopScheduleItem
import de.awolf.trip.kmp.departures.domain.repository.DeparturesRemoteRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeComponents
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

class DeparturesRemoteRepositoryImpl(
    private val client: HttpClient
): BaseHttpRepository(), DeparturesRemoteRepository {
    override suspend fun getDepartures(
        stopId: String,
        limit: Int,
        time: Instant,
        isArrival: Boolean,
        shorttermchanges: Boolean,
        modeOfTransport: List<String>
    ): Result<StopMonitorInfo, NetworkError> {

        val jsonBody = JsonObject(
            mapOf(
                "stopid" to JsonPrimitive(stopId),
                "limit" to JsonPrimitive(limit),
//                "time" to JsonPrimitive(time.toLocalDateTime(TimeZone.currentSystemDefault()).format(LocalDateTime.Formats.ISO)),
                "time" to JsonPrimitive(time.format(DateTimeComponents.Formats.ISO_DATE_TIME_OFFSET)),
                "isarrival" to JsonPrimitive(isArrival),
                "shorttermchanges" to JsonPrimitive(shorttermchanges),
                "mot" to JsonPrimitive(modeOfTransport.toString())
            )
        )

        return catchNetworkExceptions<StopMonitorInfo>(
            request = {
                client.post {
                    url(HttpRoutes.DEPARTURE_MONITOR)
                    contentType(ContentType.Application.Json)
                    setBody(jsonBody)
                }
            },
            onSuccessMapper = { response ->
                response.body<DepartureMonitorResponseDto>().toStopMonitorInfo()
            }
        )
    }

    override suspend fun getStopSchedule(
        stopId: String,
        departureId: String,
        time: Instant
    ): Result<List<StopScheduleItem>, NetworkError> {

        val jsonBody = JsonObject(
            mapOf(
                "tripid" to JsonPrimitive(departureId),
                "time" to JsonPrimitive(time.toLocalDateTime(TimeZone.currentSystemDefault()).format(
                    LocalDateTime.Formats.ISO)),
                "stopid" to JsonPrimitive(stopId)
            )
        )

        return catchNetworkExceptions<List<StopScheduleItem>>(
            request = {
                client.post {
                    url(HttpRoutes.STOP_SCHEDULE)
                    contentType(ContentType.Application.Json)
                    setBody(jsonBody)
                }
            },
            onSuccessMapper = { response ->
                response.body<StopScheduleResponseDto>().scheduledStops.map { stop ->
                    stop.toStopScheduleItem()
                }
            }
        )
    }
}