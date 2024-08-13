package de.awolf.trip.kmp.presentation.home_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.awolf.trip.kmp.presentation.helper.clickableWithoutRipple
import de.awolf.trip.kmp.theme.AppTheme
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime

@Preview(showBackground = true)
@Composable
private fun SearchCardPreview() {
    AppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column() {
                SearchCard(
                    searchText = "",
                    onSearchTextChange = {},
                    selectedDateTime = LocalDateTime(2024, 1, 1, 12, 0),
                    onShowDatePicker = {},
                    onShowTimePicker = {},
                    onSearchButtonClick = {},
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Spacer(modifier = Modifier.weight(1f))
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchCard(
    searchText: String,
    onSearchTextChange: (newText: String) -> Unit,
    selectedDateTime: LocalDateTime,
    onShowDatePicker: () -> Unit,
    onShowTimePicker: () -> Unit,
    onSearchButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                shape = RoundedCornerShape(0.dp, 0.dp, 20.dp, 20.dp)
            )
            .padding(top = 8.dp, bottom = 12.dp, start = 12.dp, end = 12.dp)
    ) {
        OutlinedTextField(
            value = searchText,
            onValueChange = { newText: String -> onSearchTextChange(newText) },
            label = {
                Text(
                    text = "Origin",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6F),
                    fontSize = 20.sp
                )
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "delete text",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .size(26.dp)
                        .clickableWithoutRipple {
                            onSearchTextChange("")
                        }
                )
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                autoCorrectEnabled = false,
                imeAction = ImeAction.Done
            ),
            textStyle = TextStyle(
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 20.sp,
            ),
            modifier = Modifier
                .fillMaxWidth()
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
        ) {
            val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

            val dateString = if (selectedDateTime.date == today.date) {
                "Today"
            } else {
                selectedDateTime.format(LocalDateTime.Format {
                    dayOfMonth()
                    char('.')
                    monthNumber()
                    char('.')
                })
            }
            Text(
                text = selectedDateTime.format(LocalDateTime.Format {
                    hour()
                    char(':')
                    minute()
                }),
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight(400),
                modifier = Modifier
                    .clickableWithoutRipple {
                        onShowTimePicker()
                    }
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = dateString,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight(400),
                modifier = Modifier
                    .clickableWithoutRipple {
                        onShowDatePicker()
                    }
            )


            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { onSearchButtonClick() },
                contentPadding = PaddingValues(0.dp),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .size(width = 110.dp, height = 30.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "start search",
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}