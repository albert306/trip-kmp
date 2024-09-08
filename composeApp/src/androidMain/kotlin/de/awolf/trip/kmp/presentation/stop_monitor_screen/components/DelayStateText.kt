package de.awolf.trip.kmp.presentation.stop_monitor_screen.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import kotlin.math.absoluteValue

@Composable
fun DelayStateText(
    modifier: Modifier = Modifier,
    delay: Long,
    isCancelled: Boolean = false,
    fontSize: TextUnit = 14.sp
) {
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
    if (isCancelled) {
        departureStateDescription = "cancelled"
        departureStateDescriptionColor = red
    }

    Text(
        text = departureStateDescription,
        fontSize = fontSize,
        color = departureStateDescriptionColor,
        modifier = modifier
    )
}