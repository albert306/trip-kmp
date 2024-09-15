package de.awolf.trip.kmp.departures.departures_screen.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import de.awolf.trip.kmp.core.helper.clickableWithoutRipple
import de.awolf.trip.kmp.theme.AppTheme
import de.awolf.trip.kmp.departures.domain.models.Departure
import de.awolf.trip.kmp.core.domain.models.Mode
import de.awolf.trip.kmp.core.domain.models.Platform
import de.awolf.trip.kmp.departures.domain.models.StopScheduleItem
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import de.awolf.trip.kmp.departures.presentation.departures_screen.DepartureDetailLevel
import kotlin.math.absoluteValue

@Composable
@Preview(showBackground = true)
fun TestPreview() {
    AppTheme {
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
                    stopSchedule = listOf(
                        StopScheduleItem("", "", "Dresden", "Münzteichweg", StopScheduleItem.SchedulePosition.PREVIOUS, Platform(type = "track", name = "2"), Clock.System.now().plus(2, DateTimeUnit.MINUTE)),
                        StopScheduleItem("", "", "Dresden", "Münzteichweg", StopScheduleItem.SchedulePosition.CURRENT, Platform(type = "track", name = "2"), Clock.System.now().plus(32, DateTimeUnit.MINUTE)),
                        StopScheduleItem("", "", "Dresden", "Münzteichweg", StopScheduleItem.SchedulePosition.NEXT, Platform(type = "track", name = "2"), Clock.System.now().plus(100, DateTimeUnit.MINUTE)),
                        StopScheduleItem("", "", "Dresden", "Münzteichweg", StopScheduleItem.SchedulePosition.NEXT, Platform(type = "track", name = "2"), Instant.parse("2022-12-24T23:00:00Z")),
                    ),
                ),
                detailLevel = DepartureDetailLevel.STOP_SCHEDULE,
                onClick = {},
                onShowWholeScheduleClicked = {},
            )
        }
    }
}

@Composable
fun DepartureView(
    departure: Departure,
    detailLevel: DepartureDetailLevel,
    onClick: () -> Unit,
    onShowWholeScheduleClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ConstraintLayout(
        modifier = modifier
            .background(
                color = if (detailLevel >= DepartureDetailLevel.STOP_SCHEDULE)
                    MaterialTheme.colorScheme.surfaceContainer
                else
                    MaterialTheme.colorScheme.surface
            )
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .clickableWithoutRipple {
                onClick()
            }
    ) {
        val (lineNumber, lineDirection, eta, statusRow, platform, stopSchedule) = createRefs()

        Box(
            contentAlignment = Alignment.CenterStart,
            modifier = Modifier
                .widthIn(
                    min = 44.dp,
                    max = 100.dp
                )
                .constrainAs(lineNumber) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(
                        if (detailLevel == DepartureDetailLevel.NONE) statusRow.bottom
                        else platform.bottom
                    )
                }
        ) {
            LineNumberView(departure)
        }

        Text(
            text = departure.lineDirection,
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight(400),
            modifier = Modifier
                .constrainAs(lineDirection) {
                    start.linkTo(lineNumber.end, margin = 8.dp)
                    top.linkTo(parent.top)
                    end.linkTo(eta.start, margin = 8.dp)
                    width = Dimension.fillToConstraints
                }
        )
        var etaText = departure.getETA().toString() + " min"
        if (departure.getETA() > 60) {
            etaText = ""
        }
        Text(
            text = etaText,
            textAlign = TextAlign.End,
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            modifier = Modifier
                .constrainAs(eta) {
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                }
        )

        StatusRow(
            departure = departure,
            modifier = Modifier.constrainAs(statusRow) {
                start.linkTo(lineDirection.start)
                top.linkTo(lineDirection.bottom)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
            }
        )

        AnimatedVisibility(
            visible = detailLevel >= DepartureDetailLevel.PLATFORM,
            modifier = Modifier
                .constrainAs(platform) {
                    start.linkTo(lineDirection.start)
                    top.linkTo(statusRow.bottom)
                    bottom.linkTo(
                        if (detailLevel == DepartureDetailLevel.PLATFORM) parent.bottom
                        else stopSchedule.top
                    )
                }
        ) {
            val platformText = if (departure.platform != null)
                departure.platform!!.type + " " +  departure.platform!!.name
            else
                "platform unknown"

            Text(
                text = platformText,
//                    textAlign = TextAlign.End,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
            )
        }

        AnimatedVisibility(
            visible = detailLevel >= DepartureDetailLevel.STOP_SCHEDULE,
            modifier = Modifier
                .constrainAs(stopSchedule) {
                    start.linkTo(parent.start)
                    top.linkTo(platform.bottom)
                    bottom.linkTo(parent.bottom)
                }
                .clickableWithoutRipple { /* intentionally blank */ }
        ) {
            StopScheduleList(
                departure = departure,
                isShowingWholeSchedule = detailLevel == DepartureDetailLevel.WHOLE_STOP_SCHEDULE,
                onShowWholeScheduleClicked = {
                    onShowWholeScheduleClicked()
                },
            )
        }
    }
}

@Composable
fun StatusRow(
    departure: Departure,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
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
        )

        Text(
            text = "•",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(horizontal = 6.dp)
        )

        val delay = departure.getDelay()
        val (red, green, blue) = DelayStateColors.getColors()

        var departureStateDescription = "on time"
        var departureStateDescriptionColor = green
        if (delay > 0) {
            departureStateDescription = "+ $delay"
            departureStateDescriptionColor = red
        }
        if (delay < 0) {
            departureStateDescription = "- ${delay.absoluteValue}"
            departureStateDescriptionColor = blue
        }
        if (departure.departureState == Departure.DepartureState.CANCELLED) {
            departureStateDescription = "cancelled"
            departureStateDescriptionColor = red
        }

        Text(
            text = departureStateDescription,
            fontSize = 14.sp,
            color = departureStateDescriptionColor,
            modifier = modifier
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
            modifier = Modifier
                .weight(1f)
        )
    }
}

@Composable
fun LineNumberView(
    departure: Departure
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
            .background(
                color = Color(departure.mode.getColorHex()),
                shape = RoundedCornerShape(2.dp)
            )
            .padding(horizontal = 4.dp, vertical = 0.dp)
    )
}