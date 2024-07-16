package de.awolf.trip.kmp.presentation.stop_monitor_screen.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.awolf.trip.kmp.presentation.helper.clickableWithoutRipple
import domain.models.Departure
import domain.models.Mode
import domain.models.Platform
import domain.models.StopScheduleItem
import java.time.Clock
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.absoluteValue

@Preview()
@Composable
fun DepartureViewPreview() {
    MaterialTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.background
        ) {
            DepartureView(
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
                    isShowingDetailedStopSchedule = true,
                    detailedStopSchedule = listOf(
                        StopScheduleItem("", "", "Dresden", "Münzteichweg", "Next", Platform(type = "track", name = "2"), LocalDateTime.now().plusMinutes(2)),
                        StopScheduleItem("", "", "Dresden", "Münzteichweg", "Next", Platform(type = "track", name = "2"), LocalDateTime.now().plusMinutes(2)),
                        StopScheduleItem("", "", "Dresden", "Münzteichweg", "Next", Platform(type = "track", name = "2"), LocalDateTime.now().plusMinutes(2)),
                        StopScheduleItem("", "", "Dresden", "Münzteichweg", "Next", Platform(type = "track", name = "2"), LocalDateTime.now().plusMinutes(2)),
                    )
                ),
                onClick = {})
        }
    }
}

@Composable
fun DepartureView(
    departure: Departure,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column (
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickableWithoutRipple {
                    onClick()
                }
        ) {
            Box(
                contentAlignment = Alignment.CenterStart,
                modifier = Modifier
                    .widthIn(
                        min = 58.dp,
                        max = 100.dp
                    )
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    text = departure.lineNumber,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 1,
                    fontWeight = FontWeight(500),
                    modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 0.dp)
                        .background(
                            color = departure.mode.getColor(),
                            shape = RoundedCornerShape(2.dp)
                        )
                        .padding(horizontal = 4.dp, vertical = 0.dp)
                )
            }
            Column(
                modifier = Modifier
                    .padding(end = 8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = departure.lineDirection,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight(400),
                        modifier = Modifier.weight(1f),
                    )
                    var etaText = departure.getETA().toString() + " min"
                    if (departure.getETA() > 120) {
                        etaText = ""
                    }
                    Text(
                        text = etaText,
                        textAlign = TextAlign.End,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                        maxLines = 1,
                        fontWeight = FontWeight(400),
                        modifier = Modifier.width(90.dp),
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = departure.sheduledTime.format(DateTimeFormatter.ofPattern("HH:mm")),
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight(300),
                    )
                    Text(
                        text = "•",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight(300),
                        modifier = Modifier.padding(horizontal = 8.dp),
                    )

                    val delay = departure.getDelay()
                    var departureStateDescription = "pünktlich"
                    var departureStateDescriptionColor = Color.Green
                    if (delay > 0) {
                        departureStateDescription = "+ " + delay.toString()
                        departureStateDescriptionColor = Color.Red
                    }
                    if (delay < 0) {
                        departureStateDescription = "- " + delay.absoluteValue.toString()
                        departureStateDescriptionColor = Color.Blue
                    }
                    if (departure.departureState == Departure.DepartureState.CANCELLED) {
                        departureStateDescription = "ausgefallen"
                        departureStateDescriptionColor = Color.Red
                    }

                    Text(
                        text = departureStateDescription,
                        fontSize = 14.sp,
                        color = departureStateDescriptionColor,
                        fontWeight = FontWeight(300),
                        //                    modifier = Modifier.padding(horizontal = 8.dp),
                    )
                    Text(
                        text = departure.realTime.format(DateTimeFormatter.ofPattern("HH:mm")),
                        textAlign = TextAlign.End,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight(300),
                        modifier = Modifier.weight(1f),
                    )
                }
                AnimatedVisibility(visible = departure.isShowingDetailedStopSchedule && departure.platform != null) {
                    if (departure.platform == null) return@AnimatedVisibility
                    Text(
                        text = departure.platform.type + " " +  departure.platform.name,
//                                textAlign = TextAlign.End,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                        maxLines = 1,
                        fontWeight = FontWeight(300),
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }
        AnimatedVisibility(visible = departure.isShowingDetailedStopSchedule && departure.detailedStopSchedule != null) {
            if (departure.detailedStopSchedule == null) return@AnimatedVisibility
            Column (
                modifier = Modifier
                    .padding(start = 4.dp)
//                    .background(
//                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f)
//                    )
            ){
                Text(
                    text = "Upcoming Stops:",
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 1,
                    fontWeight = FontWeight(400),
                    modifier = Modifier.fillMaxWidth(),
                )
                for (detailedStop in departure.detailedStopSchedule!!) {
                    StopScheduleItemView(
                        stopScheduleItem = detailedStop,
//                                    modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
    }
}