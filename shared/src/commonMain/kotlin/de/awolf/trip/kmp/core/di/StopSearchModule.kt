package de.awolf.trip.kmp.core.di

import de.awolf.trip.kmp.core.data.local.repository.StopRepositoryImpl
import de.awolf.trip.kmp.core.data.remote.repository.StopRemoteSearchRepositoryImpl
import de.awolf.trip.kmp.core.domain.repository.StopRepository
import de.awolf.trip.kmp.core.domain.repository.StopRemoteSearchRepository
import de.awolf.trip.kmp.core.domain.use_cases.FindStopByQueryUseCase
import de.awolf.trip.kmp.core.domain.use_cases.GetFavoriteStopsUseCase
import de.awolf.trip.kmp.core.domain.use_cases.ReorderFavoriteStopsUseCase
import de.awolf.trip.kmp.core.domain.use_cases.SearchUseCases
import de.awolf.trip.kmp.core.domain.use_cases.ToggleFavoriteStopUseCase
import org.koin.dsl.bind
import org.koin.dsl.module

val searchModule = module {
    single {
        StopRemoteSearchRepositoryImpl(get())
    }.bind<StopRemoteSearchRepository>()

    single {
        StopRepositoryImpl(get())
    }.bind<StopRepository>()

    single {
        SearchUseCases(
            findStopByQuery = FindStopByQueryUseCase(get(), get()),
            getFavoriteStops = GetFavoriteStopsUseCase(get()),
            reorderFavoriteStops = ReorderFavoriteStopsUseCase(get()),
            toggleFavoriteStop = ToggleFavoriteStopUseCase(get())
        )
    }
}