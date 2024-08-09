package data.local

import app.cash.sqldelight.db.SqlDriver
import de.awolf.trip.kmp.shared.database.StopDatabase

fun createStopDatabase(driver: SqlDriver): StopDatabase {
    return StopDatabase(
        driver = driver
    )
}