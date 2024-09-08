package de.awolf.trip.kmp.presentation.stop_monitor_screen.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import de.awolf.trip.kmp.theme.AppTheme
import domain.models.Departure
import domain.models.Mode
import domain.models.Platform
import domain.models.StopScheduleItem
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.plus

@Composable
@Preview(showBackground = true)
fun StopScheduleListPreview() {
    AppTheme {
        StopScheduleList(
            departure = Departure(
                id = "",
                dlId = "",
                lineNumber = "66",
                lineDirection = "Mockritz",
                platform = Platform(type = "track", name = "2"),
                mode = Mode.CITYBUS,
                scheduledTime = Clock.System.now(),
                realTime = Clock.System.now(),
                departureState = Departure.DepartureState.INTIME,
                routeChanges = emptyList(),
                diva = null,
                stopSchedule = listOf(
                    StopScheduleItem("", "", "Dresden", "Münzteichweg", "Current", Platform(type = "track", name = "2"), Clock.System.now().plus(2, DateTimeUnit.MINUTE)),
                    StopScheduleItem("", "", "Dresden", "Münzteichweg", "Next", Platform(type = "track", name = "2"), Clock.System.now().plus(2, DateTimeUnit.MINUTE)),
                    StopScheduleItem("", "", "Dresden", "Münzteichweg", "Next", Platform(type = "track", name = "2"), Clock.System.now().plus(2, DateTimeUnit.MINUTE)),
                    StopScheduleItem("", "", "Dresden", "Münzteichweg", "Next", Platform(type = "track", name = "2"), Clock.System.now().plus(2, DateTimeUnit.MINUTE)),
                ),
            )
        )
    }
}

@Composable
fun StopScheduleList(
    departure: Departure,
    modifier: Modifier = Modifier,
) {
    if (departure.stopSchedule == null) return
    Column(
        modifier = modifier
    ) {
        StopScheduleItemView(
            stopScheduleItem = departure.stopSchedule!![0],
            isFirst = true,
        )

        SpacerWithDivider(modifier = Modifier.height(8.dp))

        for (detailedStop in departure.stopSchedule!!.subList(1, departure.stopSchedule!!.size - 1)) {
            StopScheduleItemView(
                stopScheduleItem = detailedStop,
            )
            SpacerWithDivider(modifier = Modifier.height(8.dp))
        }

        StopScheduleItemView(
            stopScheduleItem = departure.stopSchedule!![departure.stopSchedule!!.size - 1],
            isLast = true,
        )
    }
}

@Composable
private fun SpacerWithDivider(
    modifier: Modifier = Modifier,
) {
    ConstraintLayout(
        modifier = modifier
    ) {
        val divider = createRef()
        val dividerGuideline = createGuidelineFromStart(64.dp)

        VerticalDivider(
            color = Color.Gray,
            thickness = 1.5.dp,
            modifier = Modifier
                .constrainAs(divider) {
                    start.linkTo(dividerGuideline)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(dividerGuideline)
                    height = Dimension.fillToConstraints
                }
        )
    }
}