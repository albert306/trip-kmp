package de.awolf.trip.kmp.search.domain.use_cases

data class SearchUseCases(
    val findStopByQuery: FindStopByQueryUseCase,
    val getFavoriteStops: GetFavoriteStopsUseCase,
    val reorderFavoriteStops: ReorderFavoriteStopsUseCase,
    val toggleFavoriteStop: ToggleFavoriteStopUseCase,
)