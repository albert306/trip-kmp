package de.awolf.trip.kmp.di

import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import de.awolf.trip.kmp.core.data.local.createStopDatabase
import de.awolf.trip.kmp.core.data.remote.createHttpClient
import de.awolf.trip.kmp.shared.database.StopDatabase
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.bind
import org.koin.dsl.module

actual val httpClientModule = module {
    single {
        createHttpClient(
            engine = OkHttp.create()
        )
    }.bind<HttpClient>()
}

actual val databaseModule = module {
    single {
        createStopDatabase(
            driver = AndroidSqliteDriver(StopDatabase.Schema, androidContext(), "STOP_DATABASE")
        )
    }.bind<StopDatabase>()
}
