package de.awolf.trip.kmp.departures.domain.use_cases

data class DeparturesUseCases(
    val fetchDepartures: FetchDeparturesUseCase,
    val fetchStopSchedule: FetchStopScheduleUseCase,
)