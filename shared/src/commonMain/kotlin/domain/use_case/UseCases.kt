package domain.use_case

data class UseCases(
    val findStopByQueryUseCase: FindStopByQueryUseCase,
    val getFavoriteStopsUseCase: GetFavoriteStopsUseCase,
    val getStopMonitorUseCase: GetStopMonitorUseCase,
    val getStopSchedule: GetStopScheduleUseCase,
    val toggleFavoriteStopUseCase: ToggleFavoriteStopUseCase,
    val reorderFavoriteStops: ReorderFavoriteStopsUseCase,
)
