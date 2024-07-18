package de.awolf.trip.kmp

sealed class Screen(val route: String) {
    data object HomeScreen : Screen("home_screen")
    data object StopMonitorScreen : Screen("stop_monitor_screen")

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}
