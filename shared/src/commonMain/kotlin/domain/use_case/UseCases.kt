package domain.use_case

data class UseCases(
    val getRecommendedStopsUseCase: GetRecommendedStopsUseCase,
    val getStopMonitorUseCase: GetStopMonitorUseCase,
    val toggleFavoriteStopUseCase: ToggleFavoriteStopUseCase,
)
