package de.awolf.trip.kmp.trips.di

import de.awolf.trip.kmp.trips.data.remote.repository.TripsRemoteRepositoryImpl
import de.awolf.trip.kmp.trips.domain.repository.TripsRemoteRepository
import de.awolf.trip.kmp.trips.domain.use_cases.FetchTripsUseCase
import de.awolf.trip.kmp.trips.domain.use_cases.TripsUseCases
import org.koin.dsl.bind
import org.koin.dsl.module

val tripsModule = module {
    single {
        TripsRemoteRepositoryImpl(get())
    }.bind<TripsRemoteRepository>()

    single {
        TripsUseCases(
            fetchTrips = FetchTripsUseCase(get())
        )
    }
}