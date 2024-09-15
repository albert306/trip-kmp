package de.awolf.trip.kmp.core.domain.use_cases

data class CoreUseCases(
    val findStopByQuery: FindStopByQueryUseCase,
    val getFavoriteStops: GetFavoriteStopsUseCase,
    val reorderFavoriteStops: ReorderFavoriteStopsUseCase,
    val toggleFavoriteStop: ToggleFavoriteStopUseCase,
)