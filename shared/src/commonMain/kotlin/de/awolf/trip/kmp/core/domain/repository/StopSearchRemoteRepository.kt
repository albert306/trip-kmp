package de.awolf.trip.kmp.core.domain.repository

import de.awolf.trip.kmp.core.util.Result
import de.awolf.trip.kmp.core.util.error.NetworkError
import de.awolf.trip.kmp.core.domain.models.StopSearchInfo

interface StopSearchRemoteRepository {
    suspend fun getStopByName(
        query: String,
        limit: Int,
        stopsOnly: Boolean,
        regionalOnly: Boolean,
        stopShortcuts: Boolean
    ): Result<StopSearchInfo, NetworkError>
}