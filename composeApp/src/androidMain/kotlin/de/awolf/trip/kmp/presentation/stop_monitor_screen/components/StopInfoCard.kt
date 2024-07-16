package de.awolf.trip.kmp.presentation.stop_monitor_screen.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.awolf.trip.kmp.presentation.helper.clickableWithoutRipple
import domain.models.Stop
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun StopInfoCard(
    stop: Stop,
    queriedTime: LocalDateTime,
    isStopInfoCardExpanded: Boolean,
    expandStopInfo: () -> Unit,
    onCloseButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickableWithoutRipple { expandStopInfo() }
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(0.dp, 0.dp, 24.dp, 24.dp)
            )
            .padding(12.dp)
    ) {
        Spacer(modifier = Modifier.height(2.dp))
        Row {
            Text(
                text = stop.name,
                fontSize = 22.sp,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight(400),
                modifier = Modifier
                    .weight(1f)
            )
            TextButton(
                onClick = { onCloseButtonClick() },
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier
                    .size(26.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "close StopMonitor",
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.size(26.dp)
                )
            }
        }
        AnimatedVisibility(
            visible = isStopInfoCardExpanded,
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
            ) {
                Text(
                    text = stop.region,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight(200),
                )
                Text(
                    text = "•",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight(200),
                    modifier = Modifier.padding(horizontal = 8.dp),
                )
                Text(
                    text = queriedTime.format(DateTimeFormatter.ofPattern("dd.MM. // HH:mm")),
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight(200),
                )
            }
        }
    }
}