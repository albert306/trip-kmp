package de.awolf.trip.kmp.presentation.stop_monitor_screen.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.awolf.trip.kmp.presentation.helper.clickableWithoutRipple
import de.awolf.trip.kmp.theme.AppTheme
import domain.models.Departure
import domain.models.Mode
import domain.models.Platform
import domain.models.StopScheduleItem
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlin.math.absoluteValue

@Preview()
@Composable
fun DepartureViewPreview() {
    AppTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surface
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
                        StopScheduleItem("", "", "Dresden", "Münzteichweg", "Next", Platform(type = "track", name = "2"), Clock.System.now().plus(2, DateTimeUnit.MINUTE)),
                        StopScheduleItem("", "", "Dresden", "Münzteichweg", "Next", Platform(type = "track", name = "2"), Clock.System.now().plus(2, DateTimeUnit.MINUTE)),
                        StopScheduleItem("", "", "Dresden", "Münzteichweg", "Next", Platform(type = "track", name = "2"), Clock.System.now().plus(2, DateTimeUnit.MINUTE)),
                        StopScheduleItem("", "", "Dresden", "Münzteichweg", "Next", Platform(type = "track", name = "2"), Clock.System.now().plus(2, DateTimeUnit.MINUTE)),
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
                val lightTextColor: Color
                val darkTextColor: Color

                if (MaterialTheme.colorScheme.onSurface.luminance() > 0.5f) {
                    lightTextColor = MaterialTheme.colorScheme.onSurface
                    darkTextColor = MaterialTheme.colorScheme.surface
                } else {
                    lightTextColor = MaterialTheme.colorScheme.surface
                    darkTextColor = MaterialTheme.colorScheme.onSurface
                }

                val lineNumberTextColor = if (Color(departure.mode.getColorHex()).luminance() < 0.5f) {
                    lightTextColor
                } else {
                    darkTextColor
                }
                Text(
                    text = departure.lineNumber,
                    fontSize = 18.sp,
                    color = lineNumberTextColor,
                    maxLines = 1,
                    fontWeight = FontWeight(500),
                    modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 0.dp)
                        .background(
                            color = Color(departure.mode.getColorHex()),
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
                        color = MaterialTheme.colorScheme.onSurface,
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
                        color = MaterialTheme.colorScheme.onSurface,
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
                        text = departure.scheduledTime.toLocalDateTime(TimeZone.currentSystemDefault()).format(
                            LocalDateTime.Format {
                            hour()
                            char(':')
                            minute()
                        }),
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight(300),
                    )
                    Text(
                        text = "•",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight(300),
                        modifier = Modifier.padding(horizontal = 8.dp),
                    )

                    val red: Color
                    val green: Color
                    val blue: Color

                    if (isSystemInDarkTheme()) {
                        red = Color(0xFFFF6666)
                        green = Color(0xFF00DD00)
                        blue = Color(0xFF60A0FF)
                    } else {
                        red = Color(0xFF990000)
                        green = Color(0xFF005500)
                        blue = Color(0xFF0000BB)
                    }

                    val delay = departure.getDelay()
                    var departureStateDescription = "pünktlich"
                    var departureStateDescriptionColor = green
                    if (delay > 0) {
                        departureStateDescription = "+ " + delay.toString()
                        departureStateDescriptionColor = red
                    }
                    if (delay < 0) {
                        departureStateDescription = "- " + delay.absoluteValue.toString()
                        departureStateDescriptionColor = blue
                    }
                    if (departure.departureState == Departure.DepartureState.CANCELLED) {
                        departureStateDescription = "ausgefallen"
                        departureStateDescriptionColor = red
                    }

                    Text(
                        text = departureStateDescription,
                        fontSize = 14.sp,
                        color = departureStateDescriptionColor,
                        fontWeight = FontWeight(300),
                        //                    modifier = Modifier.padding(horizontal = 8.dp),
                    )
                    Text(
                        text = departure.realTime.toLocalDateTime(TimeZone.currentSystemDefault()).format(
                            LocalDateTime.Format {
                                hour()
                                char(':')
                                minute()
                            }),
                        textAlign = TextAlign.End,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight(300),
                        modifier = Modifier.weight(1f),
                    )
                }
                AnimatedVisibility(visible = departure.isShowingDetailedStopSchedule && departure.platform != null) {
                    if (departure.platform == null) return@AnimatedVisibility
                    Text(
                        text = departure.platform!!.type + " " +  departure.platform!!.name,
//                                textAlign = TextAlign.End,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface,
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
//                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
//                    )
            ){
                Text(
                    text = "Upcoming Stops:",
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSurface,
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