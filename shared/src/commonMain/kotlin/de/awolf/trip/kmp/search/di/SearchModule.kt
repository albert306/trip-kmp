package de.awolf.trip.kmp.search.di

import de.awolf.trip.kmp.search.data.local.repository.SearchLocalRepositoryImpl
import de.awolf.trip.kmp.search.data.remote.repository.SearchRemoteRepositoryImpl
import de.awolf.trip.kmp.search.domain.repository.SearchLocalRepository
import de.awolf.trip.kmp.search.domain.repository.SearchRemoteRepository
import de.awolf.trip.kmp.search.domain.use_cases.FindStopByQueryUseCase
import de.awolf.trip.kmp.search.domain.use_cases.GetFavoriteStopsUseCase
import de.awolf.trip.kmp.search.domain.use_cases.ReorderFavoriteStopsUseCase
import de.awolf.trip.kmp.search.domain.use_cases.SearchUseCases
import de.awolf.trip.kmp.search.domain.use_cases.ToggleFavoriteStopUseCase
import org.koin.dsl.bind
import org.koin.dsl.module

val searchModule = module {
    single {
        SearchRemoteRepositoryImpl(get())
    }.bind<SearchRemoteRepository>()

    single {
        SearchLocalRepositoryImpl(get())
    }.bind<SearchLocalRepository>()

    single {
        SearchUseCases(
            findStopByQuery = FindStopByQueryUseCase(get(), get()),
            getFavoriteStops = GetFavoriteStopsUseCase(get()),
            reorderFavoriteStops = ReorderFavoriteStopsUseCase(get()),
            toggleFavoriteStop = ToggleFavoriteStopUseCase(get())
        )
    }
}