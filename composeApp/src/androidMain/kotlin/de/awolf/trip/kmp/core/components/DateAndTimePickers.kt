package de.awolf.trip.kmp.core.components

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun DateAndTimePickers(
    showDatePicker: MutableState<Boolean>,
    datePickerState: DatePickerState,
    onDateConfirm: (date: LocalDate) -> Unit,
    showTimePicker: MutableState<Boolean>,
    timePickerState: TimePickerState,
    onTimeConfirm: (time: LocalTime) -> Unit
) {
    if (showDatePicker.value) {
        DatePickerDialog(
            onDismissRequest = {
                showDatePicker.value = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (datePickerState.selectedDateMillis == null) {
                            showDatePicker.value = false
                            return@TextButton
                        }

                        showDatePicker.value = false
                        onDateConfirm(
                            Instant
                                .fromEpochMilliseconds(datePickerState.selectedDateMillis!!)
                                .toLocalDateTime(TimeZone.currentSystemDefault())
                                .date
                        )
                    }
                ) { Text("OK") }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDatePicker.value = false
                    }
                ) { Text("Cancel") }
            }
        ) {
            DatePicker(
                state = datePickerState,
            )
        }

    }

    if (showTimePicker.value) {
        TimePickerDialog(
            onDismissRequest = {
                showDatePicker.value = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showTimePicker.value = false
                        onTimeConfirm(
                            LocalTime(timePickerState.hour, timePickerState.minute)
                        )
                    }
                ) { Text("OK") }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showTimePicker.value = false
                    }
                ) { Text("Cancel") }
            }
        ) {
            TimePicker(
                state = timePickerState
            )
        }
    }
}