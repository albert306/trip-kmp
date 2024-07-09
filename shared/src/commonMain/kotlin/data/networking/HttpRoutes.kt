package data.networking

object HttpRoutes {
    private const val BASE_URL = "https://webapi.vvo-online.de"
    const val STOP_FINDER = "$BASE_URL/tr/pointfinder"
    const val STOP_MONITOR = "$BASE_URL/dm"
    const val STOP_MONITOR_STOPSCHEDULE = "$BASE_URL/dm/trip"
}
