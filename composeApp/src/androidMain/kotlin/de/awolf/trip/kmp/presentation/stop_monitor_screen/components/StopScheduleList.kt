package de.awolf.trip.kmp.presentation.stop_monitor_screen.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.awolf.trip.kmp.presentation.helper.clickableWithoutRipple
import de.awolf.trip.kmp.theme.AppTheme
import de.awolf.trip.kmp.departures.domain.models.Departure
import de.awolf.trip.kmp.core.domain.models.Mode
import de.awolf.trip.kmp.core.domain.models.Platform
import de.awolf.trip.kmp.departures.domain.models.StopScheduleItem
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
                    StopScheduleItem("", "", "Dresden", "Münzteichweg", StopScheduleItem.SchedulePosition.PREVIOUS, Platform(type = "track", name = "2"), Clock.System.now().plus(2, DateTimeUnit.MINUTE)),
                    StopScheduleItem("", "", "Dresden", "Münzteichweg", StopScheduleItem.SchedulePosition.PREVIOUS, Platform(type = "track", name = "2"), Clock.System.now().plus(2, DateTimeUnit.MINUTE)),
                    StopScheduleItem("", "", "Dresden", "Münzteichweg", StopScheduleItem.SchedulePosition.CURRENT, Platform(type = "track", name = "2"), Clock.System.now().plus(2, DateTimeUnit.MINUTE)),
                    StopScheduleItem("", "", "Dresden", "Münzteichweg", StopScheduleItem.SchedulePosition.NEXT, Platform(type = "track", name = "2"), Clock.System.now().plus(2, DateTimeUnit.MINUTE)),
                    StopScheduleItem("", "", "Dresden", "Münzteichweg", StopScheduleItem.SchedulePosition.NEXT, Platform(type = "track", name = "2"), Clock.System.now().plus(2, DateTimeUnit.MINUTE)),
                ),
            ),
            isShowingWholeSchedule = false,
        )
    }
}

@Composable
fun StopScheduleList(
    modifier: Modifier = Modifier,
    departure: Departure,
    isShowingWholeSchedule: Boolean = false,
    onShowWholeScheduleClicked: () -> Unit = {},
) {
    val wholeSchedule = departure.stopSchedule ?: return
    val previousSchedule = wholeSchedule.filter {
        it.schedulePosition == StopScheduleItem.SchedulePosition.PREVIOUS
    }
    val upcomingSchedule = wholeSchedule.filter {
        it.schedulePosition != StopScheduleItem.SchedulePosition.PREVIOUS
    }

    Column(
        modifier = modifier
    ) {
        Spacer(modifier = Modifier.height(4.dp))

        if (previousSchedule.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .clickableWithoutRipple { onShowWholeScheduleClicked() }
            ) {
                Icon(
                    imageVector = if (isShowingWholeSchedule) Icons.Default.KeyboardArrowUp else
                        Icons.Default.KeyboardArrowDown,
                    contentDescription = "Keyboard Arrow",
                )
                Text(
                    text = "${if (isShowingWholeSchedule) "Hide" else "Show"} ${previousSchedule.size} previous stops",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 16.sp,
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
        }

        AnimatedVisibility(visible = isShowingWholeSchedule) {
            Column {
                for (i in previousSchedule.indices) {
                    StopScheduleItemView(
                        stopScheduleItem = previousSchedule[i],
                        isFirst = i == 0,
                    )
                }
            }
        }

        for (i in upcomingSchedule.indices) {
            StopScheduleItemView(
                stopScheduleItem = upcomingSchedule[i],
                isFirst = i == 0 && previousSchedule.isEmpty(),
                isLast = i == upcomingSchedule.size - 1,
            )
        }
    }
}