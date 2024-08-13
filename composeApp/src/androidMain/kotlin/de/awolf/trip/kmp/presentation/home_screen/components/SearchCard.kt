package de.awolf.trip.kmp.presentation.home_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.awolf.trip.kmp.theme.AppTheme
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
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
                    selectedTime = Clock.System.now(),
                    onShowDatePicker = {},
                    onShowTimePicker = {},
                    onSearchButtonClick = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
            }

        }
    }
}

@Composable
fun SearchCard(
    searchText: String,
    onSearchTextChange: (newText: String) -> Unit,
    selectedTime: Instant,
    onShowDatePicker: () -> Unit,
    onShowTimePicker: () -> Unit,
    onSearchButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                shape = RoundedCornerShape(0.dp, 0.dp, 20.dp, 20.dp)
            )
            .padding(top = 8.dp, bottom = 0.dp, start = 12.dp, end = 12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth()
        ) {
            BasicTextField(
                value = searchText,
                onValueChange = { newText -> onSearchTextChange(newText) },
                decorationBox = { innerTextField ->
                    Box(
                        contentAlignment = Alignment.CenterStart,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        if (searchText.isBlank()) {
                            Text(
                                text = "Enter stop name",
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6F),
                                fontSize = 22.sp
                            )
                        }
                    }
                    innerTextField()
                },
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    autoCorrectEnabled = false,
                    imeAction = ImeAction.Done
                ),
                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 22.sp,
                ),
                modifier = Modifier
                    .weight(1f)
            )
            IconButton(
                onClick = { onSearchTextChange("") },
                modifier = Modifier
                    .size(26.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "delete text",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(26.dp)
                )
            }
        }
        
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .align(Alignment.CenterStart)
            ) {
                TextButton(
                    onClick = { onShowDatePicker() },
                    contentPadding = PaddingValues(0.dp),
                ) {
                    Text(
                        text = selectedTime.toLocalDateTime(TimeZone.currentSystemDefault()).format(LocalDateTime.Format {
                            dayOfMonth()
                            char('.')
                            monthNumber()
                            char('.')
                        }),
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight(400),
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                TextButton(
                    onClick = { onShowTimePicker() },
                    contentPadding = PaddingValues(0.dp),
                ) {
                    Text(
                        text = selectedTime.toLocalDateTime(TimeZone.currentSystemDefault()).format(LocalDateTime.Format {
                            hour()
                            char(':')
                            minute()
                        }),
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight(400),
                    )
                }
            }


            Button(
                onClick = { onSearchButtonClick() },
                contentPadding = PaddingValues(0.dp),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .size(width = 110.dp, height = 30.dp)
                    .align(Alignment.CenterEnd)
                    .padding(end = 4.dp)
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