package de.awolf.trip.kmp.presentation.home_screen.components

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
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import de.awolf.trip.kmp.search.presentation.SearchScreenState

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
                    searchScreenState = SearchScreenState(),
                    onSearchTextChange = {},
                    onSearchButtonClick = {},
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
    searchScreenState: SearchScreenState,
    modifier: Modifier = Modifier,
    onSearchTextChange: (newText: String) -> Unit,
    onShowDatePicker: () -> Unit = {},
    onShowTimePicker: () -> Unit = {},
    onResetDateTime: () -> Unit = {},
    onSearchButtonClick: () -> Unit,
) {
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
            value = searchScreenState.searchText,
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
                    contentDescription = "Delete origin text",
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

            val dateString: String = searchScreenState.selectedDateTime.date?.format(LocalDate.Format {
                    dayOfMonth()
                    char('.')
                    monthNumber()
                    char('.')
                }) ?: "Today"

            val timeString: String = searchScreenState.selectedDateTime.time?.format(LocalTime.Format {
                    hour()
                    char(':')
                    minute()
                }) ?: "Now"

            Text(
                text = timeString,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight(400),
                modifier = Modifier
                    .clickableWithoutRipple {
                        onShowTimePicker()
                    }
            )

            Text(
                text = "•",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight(300),
                modifier = Modifier.padding(horizontal = 8.dp),
            )

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

            if (searchScreenState.selectedDateTime.hasDate() || searchScreenState.selectedDateTime.hasTime()) {
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
                onClick = { onSearchButtonClick() },
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