package de.awolf.trip.kmp.core.di

import de.awolf.trip.kmp.core.data.local.repository.StopRepositoryImpl
import de.awolf.trip.kmp.core.data.remote.repository.StopSearchRemoteRepositoryImpl
import de.awolf.trip.kmp.core.domain.repository.StopRepository
import de.awolf.trip.kmp.core.domain.repository.StopSearchRemoteRepository
import de.awolf.trip.kmp.core.domain.use_cases.FindStopByQueryUseCase
import de.awolf.trip.kmp.core.domain.use_cases.GetFavoriteStopsUseCase
import de.awolf.trip.kmp.core.domain.use_cases.ReorderFavoriteStopsUseCase
import de.awolf.trip.kmp.core.domain.use_cases.CoreUseCases
import de.awolf.trip.kmp.core.domain.use_cases.ToggleFavoriteStopUseCase
import org.koin.dsl.bind
import org.koin.dsl.module

val coreModule = module {
    single {
        StopSearchRemoteRepositoryImpl(get())
    }.bind<StopSearchRemoteRepository>()

    single {
        StopRepositoryImpl(get())
    }.bind<StopRepository>()

    single {
        CoreUseCases(
            findStopByQuery = FindStopByQueryUseCase(get(), get()),
            getFavoriteStops = GetFavoriteStopsUseCase(get()),
            reorderFavoriteStops = ReorderFavoriteStopsUseCase(get()),
            toggleFavoriteStop = ToggleFavoriteStopUseCase(get())
        )
    }
}