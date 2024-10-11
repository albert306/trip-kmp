package de.awolf.trip.kmp.core.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import de.awolf.trip.kmp.core.helper.formattedTime
import de.awolf.trip.kmp.departures.departures_screen.components.DelayStateColors
import kotlinx.datetime.Instant
import kotlin.time.Duration.Companion.minutes

@Preview
@Composable
fun RealAndScheduledTimeTextPreview() {
    RealAndScheduledTimeText(
        scheduledTime = Instant.fromEpochSeconds(1630000000),
        realTime = Instant.fromEpochSeconds(1630000000).plus(4.minutes),
        topFontSize = 16.sp,
        bottomFontSize = 12.sp
    )
}

@Composable
fun RealAndScheduledTimeText(
    modifier: Modifier = Modifier,
    scheduledTime: Instant,
    realTime: Instant = scheduledTime,
    topFontSize: TextUnit,
    bottomFontSize: TextUnit,
) {
    ConstraintLayout(
        modifier = modifier
    ) {
        val (scheduledTimeText, realTimeText, delayText) = createRefs()

        Text(
            text = realTime.formattedTime(),
            fontSize = topFontSize,
            maxLines = 1,
            modifier = Modifier.constrainAs(realTimeText) {
                start.linkTo(parent.start)
                top.linkTo(parent.top)
            }
        )
        Text(
            text = scheduledTime.formattedTime(),
            fontSize = bottomFontSize,
            maxLines = 1,
            modifier = Modifier.constrainAs(scheduledTimeText) {
                start.linkTo(parent.start)
                top.linkTo(realTimeText.bottom)
            }
        )
        val (red, green, blue) = DelayStateColors.getColors()
        val delay = realTime.minus(scheduledTime).inWholeMinutes
        Text(
            text = delay.let { if (it > 0) "+$it min" else if (it < 0) "$it min" else "on time" },
            fontSize = bottomFontSize,
            maxLines = 1,
            color = delay.let { if (it > 0) red else if (it < 0) blue else green },
            modifier = Modifier.constrainAs(delayText) {
                start.linkTo(scheduledTimeText.end, margin = 8.dp)
                baseline.linkTo(scheduledTimeText.baseline)
            }
        )

    }
}