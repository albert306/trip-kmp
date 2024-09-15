package de.awolf.trip.kmp.departures.di

import de.awolf.trip.kmp.departures.data.remote.repository.DeparturesRemoteRepositoryImpl
import de.awolf.trip.kmp.departures.domain.repository.DeparturesRemoteRepository
import de.awolf.trip.kmp.departures.domain.use_cases.DeparturesUseCases
import de.awolf.trip.kmp.departures.domain.use_cases.FetchDeparturesUseCase
import de.awolf.trip.kmp.departures.domain.use_cases.FetchStopScheduleUseCase
import org.koin.dsl.bind
import org.koin.dsl.module

val departuresModule = module {
    single {
        DeparturesRemoteRepositoryImpl(get())
    }.bind<DeparturesRemoteRepository>()

    single {
        DeparturesUseCases(
            fetchDepartures = FetchDeparturesUseCase(get()),
            fetchStopSchedule = FetchStopScheduleUseCase(get())
        )
    }
}