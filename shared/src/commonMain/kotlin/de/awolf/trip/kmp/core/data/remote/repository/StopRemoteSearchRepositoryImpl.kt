package de.awolf.trip.kmp.core.data.remote.repository

import de.awolf.trip.kmp.core.data.remote.mappers.toStopFinderInfo
import de.awolf.trip.kmp.core.data.remote.HttpRoutes
import de.awolf.trip.kmp.core.util.Result
import de.awolf.trip.kmp.core.util.error.NetworkError
import de.awolf.trip.kmp.core.data.remote.dto.StopSearchResponseDto
import de.awolf.trip.kmp.core.domain.models.StopFinderInfo
import de.awolf.trip.kmp.core.domain.repository.StopRemoteSearchRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

class StopRemoteSearchRepositoryImpl(
    private val client: HttpClient
): BaseVvoRepository(), StopRemoteSearchRepository {
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
                    url(HttpRoutes.STOP_SEARCH)
                    contentType(ContentType.Application.Json)
                    setBody(jsonBody)
                }
            },
            onSuccessMapper = { response ->
                response.body<StopSearchResponseDto>().toStopFinderInfo()
            }
        )
    }
}