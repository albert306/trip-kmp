package de.awolf.trip.kmp.trips.trips_entry_screen.components

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
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import de.awolf.trip.kmp.R
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
                    state = TripsEntryScreenState(),
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
    state: TripsEntryScreenState,
    modifier: Modifier = Modifier,
    onFocusChange: (SearchField) -> Unit = {},
    onTextChange: (newText: String, field: SearchField) -> Unit,
    onSwap: () -> Unit = {},
    onToggleShowVia: () -> Unit = {},
    onShowDatePicker: () -> Unit = {},
    onShowTimePicker: () -> Unit = {},
    onResetDateTime: () -> Unit = {},
    onSubmitButtonClick: () -> Unit,
) {
    ConstraintLayout(
        constraintSet = constraints(state.showVia),
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                shape = RoundedCornerShape(0.dp, 0.dp, 20.dp, 20.dp)
            )
            .padding(top = 4.dp, bottom = 12.dp, start = 12.dp, end = 12.dp)
    ) {
        OutlinedTextField(
            value = state.tripQuery.origin?.let {
                "${it.name}, ${it.region}"
            } ?: state.originText,
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
            maxLines = 1,
            keyboardOptions = KeyboardOptions(
                autoCorrectEnabled = false,
                imeAction = ImeAction.Done
            ),
            textStyle = TextStyle(
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 20.sp,
            ),
            modifier = Modifier
                .layoutId("origin")
                .onFocusChanged {
                    if (it.isFocused) onFocusChange(SearchField.ORIGIN)
                }
        )

        if (state.showVia) {
            OutlinedTextField(
                value = state.tripQuery.via?.let {
                    "${it.name}, ${it.region}"
                } ?: state.viaText,
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
                maxLines = 1,
                keyboardOptions = KeyboardOptions(
                    autoCorrectEnabled = false,
                    imeAction = ImeAction.Done
                ),
                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 20.sp,
                ),
                modifier = Modifier
                    .layoutId("via")
                    .onFocusChanged {
                        if (it.isFocused) onFocusChange(SearchField.VIA)
                    }
            )
        }

        OutlinedTextField(
            value = state.tripQuery.destination?.let {
                "${it.name}, ${it.region}"
            } ?: state.destinationText,
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
            maxLines = 1,
            keyboardOptions = KeyboardOptions(
                autoCorrectEnabled = false,
                imeAction = ImeAction.Done
            ),
            textStyle = TextStyle(
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 20.sp,
            ),
            modifier = Modifier
                .layoutId("destination")
                .onFocusChanged {
                    if (it.isFocused) onFocusChange(SearchField.DESTINATION)
                }
        )

        IconButton(
            onClick = { onSwap() },
            modifier = Modifier
                .layoutId("swap")
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_swap_vertical_circle_24),
                contentDescription = "switch origin and destination",
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(30.dp)
            )
        }

        IconButton(
            onClick = { onToggleShowVia() },
            modifier = Modifier
                .layoutId("toggleVia")
        ) {
            Icon(
                painter = painterResource(
                    id = if (state.showVia) R.drawable.baseline_remove_circle_24 else R.drawable.baseline_add_circle_24
                ),
                contentDescription = "toggle via",
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(30.dp)
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .layoutId("dateTimeSelection")
        ) {
            Text(
                text = state.tripQuery.time.timeText(),
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
            )

            Text(
                text = state.tripQuery.time.dateText(),
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .clickableWithoutRipple {
                        onShowDatePicker()
                    }
            )

            if (state.tripQuery.time.hasDate() || state.tripQuery.time.hasTime()) {
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
                ) {
                    Text(
                        text = "Reset",
                        fontSize = 16.sp,
                    )
                }
            }
        }

//        IconButton(
//            onClick = { /*TODO*/ },
//            colors = IconButtonDefaults.iconButtonColors(
//                containerColor = MaterialTheme.colorScheme.primary,
//                contentColor = MaterialTheme.colorScheme.onPrimary
//            ),
//        ) {
//            Icon(
//                imageVector = Icons.Default.Search,
//                contentDescription = "start search",
//                tint = MaterialTheme.colorScheme.onPrimary,
//                modifier = Modifier.size(20.dp)
//            )
//        }

        Button(
            onClick = { onSubmitButtonClick() },
            contentPadding = PaddingValues(0.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            modifier = Modifier
                .layoutId("submit")
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

private fun constraints(showVia: Boolean): ConstraintSet {
    return ConstraintSet {
        val origin = createRefFor("origin")
        val via = createRefFor("via")
        val destination = createRefFor("destination")
        val swap = createRefFor("swap")
        val toggleVia = createRefFor("toggleVia")
        val dateTimeSelection = createRefFor("dateTimeSelection")
        val submit = createRefFor("submit")

        val textBoxEndGuideline = createGuidelineFromStart(0.85f)

        constrain(origin) {
            top.linkTo(parent.top)
            start.linkTo(parent.start)
            end.linkTo(textBoxEndGuideline)
            width = Dimension.fillToConstraints
        }
        constrain(swap) {
            top.linkTo(origin.top, margin = 8.dp) // 8dp is the non removable top padding of the text field
            start.linkTo(textBoxEndGuideline, margin = 8.dp)
            bottom.linkTo(origin.bottom)
            end.linkTo(parent.end)
            width = Dimension.fillToConstraints
        }
        if (showVia) {
            constrain(via) {
                top.linkTo(origin.bottom)
                start.linkTo(parent.start)
                end.linkTo(textBoxEndGuideline)
                width = Dimension.fillToConstraints
            }
            constrain(toggleVia) {
                top.linkTo(via.top, margin = 8.dp) // 8dp is the non removable top padding of the text field
                start.linkTo(swap.start)
                bottom.linkTo(via.bottom)
                end.linkTo(swap.end)
            }
        } else {
            constrain(toggleVia) {
                top.linkTo(destination.top, margin = 8.dp) // 8dp is the non removable top padding of the text field
                start.linkTo(swap.start)
                bottom.linkTo(destination.bottom)
                end.linkTo(swap.end)
            }
        }

        constrain(destination) {
            top.linkTo(if (showVia) via.bottom else origin.bottom)
            start.linkTo(parent.start)
            end.linkTo(textBoxEndGuideline)
            width = Dimension.fillToConstraints
        }
        constrain(dateTimeSelection) {
            top.linkTo(destination.bottom, margin = 8.dp)
            start.linkTo(parent.start)
        }
        constrain(submit) {
            top.linkTo(dateTimeSelection.top)
            bottom.linkTo(dateTimeSelection.bottom)
            end.linkTo(parent.end)
        }

    }
}