package di

import data.networking.createHttpClient
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.bind
import org.koin.dsl.module
import viewmodel.HomeScreenViewModel
import viewmodel.StopMonitorViewModel

actual val platformModule = module {

    single {
        createHttpClient(OkHttp.create())
    }.bind<HttpClient>()

    viewModel {
        HomeScreenViewModel(get())
    }

    viewModel {
        parameters -> StopMonitorViewModel(parameters.get(), parameters.get(), get())
    }
}