package de.awolf.trip.kmp.di

import app.cash.sqldelight.driver.native.NativeSqliteDriver
import de.awolf.trip.kmp.core.data.local.createStopDatabase
import de.awolf.trip.kmp.core.data.remote.createHttpClient
import de.awolf.trip.kmp.shared.database.StopDatabase
import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin
import org.koin.dsl.bind
import org.koin.dsl.module

actual val httpClientModule = module {
    single {
        createHttpClient(
            engine = Darwin.create()
        )
    }.bind<HttpClient>()
}

actual val databaseModule = module {
    single {
        createStopDatabase(
            driver = NativeSqliteDriver(StopDatabase.Schema, "STOP_DATABASE")
        )
    }.bind<StopDatabase>()
}