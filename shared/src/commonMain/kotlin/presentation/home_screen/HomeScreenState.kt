package presentation.home_screen

import domain.models.PickableDateTime
import domain.models.Stop
import domain.models.StopListSource

data class HomeScreenState(
    val searchText: String = "",
    val selectedDateTime: PickableDateTime = PickableDateTime(),
    val stopList: List<Stop> = emptyList(),
    val stopListSource: StopListSource = StopListSource.NONE,
)