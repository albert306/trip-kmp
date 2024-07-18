package di

import data.networking.repository.VvoServiceRepositoryImpl
import domain.repository.VvoServiceRepository
import domain.use_case.GetRecommendedStopsUseCase
import domain.use_case.GetStopMonitorUseCase
import domain.use_case.UseCases
import org.koin.core.module.Module
import org.koin.dsl.bind
import org.koin.dsl.module

expect val platformModule: Module

val sharedModule = module {

    single {
        VvoServiceRepositoryImpl(get())
    }.bind<VvoServiceRepository>()

    single {
        UseCases(
            GetRecommendedStopsUseCase(get()),
            GetStopMonitorUseCase(get())
        )
    }
}