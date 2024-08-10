package di

import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import data.local.createStopDatabase
import data.networking.createHttpClient
import de.awolf.trip.kmp.shared.database.StopDatabase
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.bind
import org.koin.dsl.module

actual val platformModule = module {

    single {
        createHttpClient(
            engine = OkHttp.create()
        )
    }.bind<HttpClient>()

    single {
        createStopDatabase(
            driver = AndroidSqliteDriver(StopDatabase.Schema, androidContext(), "STOP_DATABASE")
        )
    }.bind<StopDatabase>()
}