package de.awolf.trip.kmp.departures.data.remote

object HttpRoutes {
    private const val BASE_URL = "https://webapi.vvo-online.de"
    const val DEPARTURE_MONITOR = "$BASE_URL/dm"
    const val STOP_SCHEDULE = "$BASE_URL/dm/trip"
}
