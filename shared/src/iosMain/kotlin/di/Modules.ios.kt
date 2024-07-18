package di

import data.networking.createHttpClient
import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin
import org.koin.dsl.bind
import org.koin.dsl.module
import viewmodel.HomeScreenViewModel
import viewmodel.StopMonitorViewModel

actual val platformModule = module {

    single {
        createHttpClient(Darwin.create())
    }.bind<HttpClient>()

    single {
        HomeScreenViewModel(get())
    }

    single {
        parameters -> StopMonitorViewModel(parameters.get(), parameters.get(), get())
    }
}