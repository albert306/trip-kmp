package de.awolf.trip.kmp.core.data.remote.repository

import de.awolf.trip.kmp.core.util.Result
import de.awolf.trip.kmp.core.util.error.NetworkError
import io.ktor.client.statement.HttpResponse
import io.ktor.utils.io.errors.IOException
import kotlinx.serialization.SerializationException

abstract class BaseHttpRepository {

    protected suspend fun <T> catchNetworkExceptions(
        request: suspend () -> HttpResponse,
        onSuccessMapper: suspend (HttpResponse) -> T
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
                Result.Success(onSuccessMapper(response))
            }
            400 -> Result.Error(NetworkError.BAD_REQUEST)
            408 -> Result.Error(NetworkError.REQUEST_TIMEOUT)
            413 -> Result.Error(NetworkError.PAYLOAD_TOO_LARGE)
            429 -> Result.Error(NetworkError.TOO_MANY_REQUESTS)
            in 500..599 -> Result.Error(NetworkError.SERVER_ERROR)
            else -> Result.Error(NetworkError.UNKNOWN)
        }
    }
}