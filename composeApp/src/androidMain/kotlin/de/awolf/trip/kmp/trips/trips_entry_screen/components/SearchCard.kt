package de.awolf.trip.kmp.trips.trips_entry_screen.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.awolf.trip.kmp.core.helper.clickableWithoutRipple
import de.awolf.trip.kmp.core.helper.dateText
import de.awolf.trip.kmp.core.helper.timeText
import de.awolf.trip.kmp.theme.AppTheme
import de.awolf.trip.kmp.trips.presentation.trips_entry_screen.SearchField
import de.awolf.trip.kmp.trips.presentation.trips_entry_screen.TripsEntryScreenState

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
                    tripsEntryScreenState = TripsEntryScreenState(),
                    onTextChange = { _, _ -> },
                    onSubmitButtonClick = {},
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun SearchCard(
    tripsEntryScreenState: TripsEntryScreenState,
    modifier: Modifier = Modifier,
    onFocusChange: (SearchField) -> Unit = {},
    onTextChange: (newText: String, field: SearchField) -> Unit,
    onShowDatePicker: () -> Unit = {},
    onShowTimePicker: () -> Unit = {},
    onResetDateTime: () -> Unit = {},
    onSubmitButtonClick: () -> Unit,
) {
    val showVia = remember {
        mutableStateOf(false)
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                shape = RoundedCornerShape(0.dp, 0.dp, 20.dp, 20.dp)
            )
            .padding(top = 4.dp, bottom = 12.dp, start = 12.dp, end = 12.dp)
    ) {
        OutlinedTextField(
            value = tripsEntryScreenState.originText,
            onValueChange = { newText: String -> onTextChange(newText, SearchField.ORIGIN) },
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
                    contentDescription = "Delete origin text",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .size(26.dp)
                        .clickableWithoutRipple {
                            onTextChange("", SearchField.ORIGIN)
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
                .onFocusChanged {
                    onFocusChange(if (it.isFocused) SearchField.ORIGIN else SearchField.NONE)
                }
        )

        AnimatedVisibility(visible = showVia.value) {
            OutlinedTextField(
                value = tripsEntryScreenState.viaText,
                onValueChange = { newText: String -> onTextChange(newText, SearchField.VIA) },
                label = {
                    Text(
                        text = "Via",
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6F),
                        fontSize = 20.sp
                    )
                },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Delete via text",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .size(26.dp)
                            .clickableWithoutRipple {
                                onTextChange("", SearchField.VIA)
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
                    .onFocusChanged {
                        onFocusChange(if (it.isFocused) SearchField.VIA else SearchField.NONE)
                    }
            )
        }

        OutlinedTextField(
            value = tripsEntryScreenState.destinationText,
            onValueChange = { newText: String -> onTextChange(newText, SearchField.DESTINATION) },
            label = {
                Text(
                    text = "Destination",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6F),
                    fontSize = 20.sp
                )
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Delete destination text",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .size(26.dp)
                        .clickableWithoutRipple {
                            onTextChange("", SearchField.DESTINATION)
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
                .onFocusChanged {
                    onFocusChange(if (it.isFocused) SearchField.DESTINATION else SearchField.NONE)
                }
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
        ) {
            Text(
                text = tripsEntryScreenState.tripQuery.time.timeText(),
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .clickableWithoutRipple {
                        onShowTimePicker()
                    }
            )

            Text(
                text = "•",
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .padding(horizontal = 8.dp),
            )

            Text(
                text = tripsEntryScreenState.tripQuery.time.dateText(),
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .clickableWithoutRipple {
                        onShowDatePicker()
                    }
            )

            if (tripsEntryScreenState.tripQuery.time.hasDate() || tripsEntryScreenState.tripQuery.time.hasTime()) {
                Button(
                    onClick = { onResetDateTime() },
                    contentPadding = PaddingValues(0.dp),
                    shape = RoundedCornerShape(4.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary
                    ),
                    modifier = Modifier
                        .height(24.dp)
                        .padding(start = 8.dp)
                ) {
                    Text(
                        text = "Reset",
                        fontSize = 16.sp,
                        fontWeight = FontWeight(400),
                    )
                }
            }


            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { onSubmitButtonClick() },
                contentPadding = PaddingValues(0.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
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