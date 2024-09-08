package de.awolf.trip.kmp.presentation.stop_monitor_screen.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
//            .background(
//                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
//            )
    ) {
//        Text(
//            text = "Upcoming Stops:",
//            fontSize = 18.sp,
//            color = MaterialTheme.colorScheme.onSurface,
//            maxLines = 1,
//            fontWeight = FontWeight(400),
//            modifier = Modifier.fillMaxWidth(),
//        )
        StopScheduleItemView(
            stopScheduleItem = departure.stopSchedule!![0],
            isFirst = true,
        )

        for (detailedStop in departure.stopSchedule!!.subList(1, departure.stopSchedule!!.size - 1)) {
            StopScheduleItemView(
                stopScheduleItem = detailedStop,
            )
        }

        StopScheduleItemView(
            stopScheduleItem = departure.stopSchedule!![departure.stopSchedule!!.size - 1],
            isLast = true,
        )
    }
}

@Composable
private fun SpacerWithDivider() {
    Row(
        modifier = Modifier.height(4.dp),
    ) {
        Spacer(modifier = Modifier.width(64.dp))
        VerticalDivider(
            color = Color.Gray,
            thickness = 1.5.dp,
            modifier = Modifier
                .fillMaxHeight()
                .width(6.dp)
        )
    }
}