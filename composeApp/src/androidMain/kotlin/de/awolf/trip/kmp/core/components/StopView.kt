package de.awolf.trip.kmp.core.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.awolf.trip.kmp.core.helper.clickableWithoutRipple
import de.awolf.trip.kmp.theme.AppTheme
import de.awolf.trip.kmp.core.domain.models.Stop

@Preview(showBackground = true)
@Composable
private fun Preview() {
    AppTheme {
        Surface(
//            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.surface
        ) {
            StopView(stop = Stop("33000028", "Haupbahnhof", "Dresden"), {}, {})
        }
    }
}

@Composable
fun StopView(
    stop: Stop,
    onFavoriteStarClick: () -> Unit,
    onNameClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
    ) {

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .width(46.dp)
        ) {
            Icon(
                imageVector = if (stop.isFavorite) Icons.Filled.Star else Icons.Outlined.Star,
                contentDescription = if (stop.isFavorite) "filled star" else "outlined star",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(26.dp)
                    .clickableWithoutRipple {
                        onFavoriteStarClick()
                    }
            )
        }
        Column(
            modifier = Modifier
                .clickableWithoutRipple {
                    onNameClick()
                }
                .weight(1f)
        ) {
            Text(
                text = stop.name,
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight(400),
            )
            Text(
                text = stop.region,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                fontWeight = FontWeight(200)
            )
        }
    }

}