package de.awolf.trip.kmp.presentation.stop_monitor_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import de.awolf.trip.kmp.theme.AppTheme
import domain.models.Platform
import domain.models.StopScheduleItem
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime

@Preview(showBackground = true)
@Composable
private fun StopScheduleItemViewPreview() {
    AppTheme {
        Surface(
//            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.surface
        ) {
            StopScheduleItemView(StopScheduleItem("", "", "Dresden", "Münzteichweg", "Next", Platform(type = "track", name = "2"), Clock.System.now().plus(2, DateTimeUnit.MINUTE)))
            StopScheduleItemView(StopScheduleItem("", "", "Dresden", "Münzteichweg", "Next", Platform(type = "track", name = "2"), Clock.System.now().plus(2, DateTimeUnit.MINUTE)))
        }
    }
}

@Composable
fun StopScheduleItemView(
    stopScheduleItem: StopScheduleItem,
    modifier: Modifier = Modifier,
    isFirst: Boolean = false,
    isLast: Boolean = false
) {
    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
    ) {
        val (time, dividerLine, dividerDot , stopName) = createRefs()
        val dividerGuideline = createGuidelineFromStart(64.dp)

        Text(
            text = stopScheduleItem.realTime.toLocalDateTime(TimeZone.currentSystemDefault()).format(
                kotlinx.datetime.LocalDateTime.Format {
                    hour()
                    char(':')
                    minute()
                }),
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            modifier = Modifier
                .constrainAs(time) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    end.linkTo(dividerGuideline)
                    width = Dimension.fillToConstraints
                }
        )

        VerticalDivider(
            color = Color.Gray,
            thickness = 1.5.dp,
            modifier = Modifier
                .constrainAs(dividerLine) {
                    start.linkTo(dividerGuideline)
                    if (isFirst) {
                        top.linkTo(dividerDot.bottom)
                    } else {
                        top.linkTo(parent.top)
                    }
                    if (isLast) {
                        bottom.linkTo(dividerDot.top)
                    } else {
                        bottom.linkTo(parent.bottom)
                    }
                    end.linkTo(dividerGuideline)
                    height = Dimension.fillToConstraints
                }
        )
        Box(
            modifier = Modifier
                .size(6.dp)
                .background(
                    color = Color.Gray,
                    shape = CircleShape
                )
                .constrainAs(dividerDot) {
                    start.linkTo(dividerGuideline)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(dividerGuideline)
                }
        )

        Text(
            text = stopScheduleItem.stopName,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .constrainAs(stopName) {
                    start.linkTo(dividerGuideline, margin = 10.dp)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                }
        )
    }
}