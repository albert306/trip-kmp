package data.networking.repository

import data.networking.HttpRoutes
import data.networking.mappers.toStopFinderInfo
import data.networking.mappers.toStopMonitorInfo
import data.networking.mappers.toStopScheduleItem
import data.networking.dto.stop_finder.StopFinderResponseDto
import data.networking.dto.stop_monitor.StopMonitorResponseDto
import data.networking.dto.stop_monitor.StopScheduleResponseDto
import domain.models.StopFinderInfo
import domain.models.StopMonitorInfo
import domain.models.StopScheduleItem
import domain.repository.VvoServiceRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.utils.io.errors.IOException
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeComponents
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import util.error.NetworkError
import util.Result

class VvoServiceRepositoryImpl(
    private val client: HttpClient
) : VvoServiceRepository {

    override suspend fun monitorStop(
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
                    url(HttpRoutes.STOP_MONITOR)
                    contentType(ContentType.Application.Json)
                    setBody(jsonBody)
                }
            },
            onSuccess = { response ->
                response.body<StopMonitorResponseDto>().toStopMonitorInfo()
            }
        )
    }

    override suspend fun getDetailedStopSchedule(
        stopId: String,
        departureId: String,
        time: Instant
    ): Result<List<StopScheduleItem>, NetworkError> {


        val jsonBody = JsonObject(
            mapOf(
                "tripid" to JsonPrimitive(departureId),
                "time" to JsonPrimitive(time.toLocalDateTime(TimeZone.currentSystemDefault()).format(LocalDateTime.Formats.ISO)),
                "stopid" to JsonPrimitive(stopId)
            )
        )

        return catchNetworkExceptions<List<StopScheduleItem>>(
            request = {
                client.post {
                    url(HttpRoutes.STOP_MONITOR_STOPSCHEDULE)
                    contentType(ContentType.Application.Json)
                    setBody(jsonBody)
                }
            },
            onSuccess = { response ->
                response.body<StopScheduleResponseDto>().scheduledStops.map { stop ->
                    stop.toStopScheduleItem()
                }
            }
        )
    }

    override suspend fun getStopByName(
        query: String,
        limit: Int,
        stopsOnly: Boolean,
        regionalOnly: Boolean,
        stopShortcuts: Boolean
    ): Result<StopFinderInfo, NetworkError> {

        val jsonBody = JsonObject(
            mapOf(
                "query" to JsonPrimitive(query),
                "limit" to JsonPrimitive(limit),
                "stopsOnly" to JsonPrimitive(stopsOnly),
                "regionalOnly" to JsonPrimitive(regionalOnly),
                "stopShortcuts" to JsonPrimitive(stopShortcuts),
            )
        )

        return catchNetworkExceptions<StopFinderInfo>(
            request = {
                client.post {
                    url(HttpRoutes.STOP_FINDER)
                    contentType(ContentType.Application.Json)
                    setBody(jsonBody)
                }
            },
            onSuccess = { response ->
                response.body<StopFinderResponseDto>().toStopFinderInfo()
            }
        )
    }

    private suspend fun <T> catchNetworkExceptions(
        request: suspend () -> HttpResponse,
        onSuccess: suspend (HttpResponse) -> T
    ): Result<T, NetworkError> {

        val response = try {
            request()
        } catch (e: IOException) {
            return Result.Error(NetworkError.NO_INTERNET)
        } catch (e: SerializationException) {
            return Result.Error(NetworkError.SERIALIZATION)
        } catch (e: Exception) {
            return Result.Error(NetworkError.UNKNOWN)
        }

        return when (response.status.value) {
            in 200..299 -> {
                Result.Success(onSuccess(response))
            }
            408 -> Result.Error(NetworkError.REQUEST_TIMEOUT)
            413 -> Result.Error(NetworkError.PAYLOAD_TOO_LARGE)
            429 -> Result.Error(NetworkError.TOO_MANY_REQUESTS)
            in 500..599 -> Result.Error(NetworkError.SERVER_ERROR)
            else -> Result.Error(NetworkError.UNKNOWN)
        }
    }
}