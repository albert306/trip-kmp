package di

import app.cash.sqldelight.driver.native.NativeSqliteDriver
import data.local.createStopDatabase
import data.networking.createHttpClient
import de.awolf.trip.kmp.shared.database.StopDatabase
import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin
import org.koin.dsl.bind
import org.koin.dsl.module

actual val platformModule = module {

    single {
        createHttpClient(
            engine = Darwin.create()
        )
    }.bind<HttpClient>()

    single {
        createStopDatabase(
            driver = NativeSqliteDriver(StopDatabase.Schema, "STOP_DATABASE")
        )
    }.bind<StopDatabase>()
}