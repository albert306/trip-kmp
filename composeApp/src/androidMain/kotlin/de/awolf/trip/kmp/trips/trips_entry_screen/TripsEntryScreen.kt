package de.awolf.trip.kmp.trips.trips_entry_screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import de.awolf.trip.kmp.core.components.StopView
import de.awolf.trip.kmp.core.helper.SideEffectListener
import de.awolf.trip.kmp.core.helper.message
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import de.awolf.trip.kmp.trips.presentation.trips_entry_screen.TripsEntryScreenEvent
import de.awolf.trip.kmp.trips.presentation.trips_entry_screen.TripsEntryScreenSideEffect
import de.awolf.trip.kmp.trips.presentation.trips_entry_screen.TripsEntryScreenViewModel
import de.awolf.trip.kmp.trips.trips_entry_screen.components.SearchCard
import de.awolf.trip.kmp.core.components.DateAndTimePickers

@Preview
@Composable
fun TripsEntryScreenPreview() {
    TripsEntryScreen(
        viewModel = TripsEntryScreenViewModel {},
        snackbarHostState = SnackbarHostState()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripsEntryScreen(
    viewModel: TripsEntryScreenViewModel,
    snackbarHostState: SnackbarHostState
) {
    val state by viewModel.state.collectAsState()

    val scope = rememberCoroutineScope()

    val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

    val showDatePicker = remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val showTimePicker = remember { mutableStateOf(false) }
    val timePickerState = rememberTimePickerState(
        initialHour = now.hour,
        initialMinute = now.minute,
        is24Hour = true,
    )

    SideEffectListener(flow = viewModel.sideEffect) { sideEffect ->
        val toastMsg = when (sideEffect) {
            is TripsEntryScreenSideEffect.ShowNoOriginSelectedMsg -> "Please select a valid origin"
            is TripsEntryScreenSideEffect.ShowNoDestinationSelectedMsg -> "Please select a valid destination"
            is TripsEntryScreenSideEffect.ShowInvalidDateTimeMsg -> "Selected date and time is in the past"
            is TripsEntryScreenSideEffect.ShowError -> sideEffect.error.message()
        }

        scope.launch {
            snackbarHostState.currentSnackbarData?.dismiss()
            snackbarHostState.showSnackbar(
                message = toastMsg,
                duration = SnackbarDuration.Short
            )
        }
    }

    DateAndTimePickers(
        showDatePicker = showDatePicker,
        datePickerState = datePickerState,
        showTimePicker = showTimePicker,
        timePickerState = timePickerState,
        onDateConfirm = { viewModel.onEvent(TripsEntryScreenEvent.ChangeSelectedDate(it)) },
        onTimeConfirm = { viewModel.onEvent(TripsEntryScreenEvent.ChangeSelectedTime(it)) }
    )

    Column(
        verticalArrangement = Arrangement.spacedBy((-10).dp),
        modifier = Modifier
            .fillMaxSize()
    ) {
        SearchCard(
            state = state,
            onTextChange = { text, field ->
                viewModel.onEvent(TripsEntryScreenEvent.TextChange(text, field))
            },
            onFocusChange = { viewModel.onEvent(TripsEntryScreenEvent.FocusChange(it)) },
            onShowDatePicker = { showDatePicker.value = true },
            onShowTimePicker = { showTimePicker.value = true },
            onResetDateTime = { viewModel.onEvent(TripsEntryScreenEvent.ResetSelectedDateTime) },
            onSubmitButtonClick = { viewModel.onEvent(TripsEntryScreenEvent.Submit) },
            modifier = Modifier
                .zIndex(1f)
                .fillMaxWidth()
        )
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .zIndex(0f)
                .fillMaxSize(),
        ) {
            item {
                Spacer(modifier = Modifier.height(6.dp))
            }
            items(items = state.searchResultList, key = { it.id }) { stop ->
                StopView(
                    stop = stop,
                    onFavoriteStarClick = {
                        viewModel.onEvent(TripsEntryScreenEvent.ToggleFavoriteStop(stop))
                    },
                    onNameClick = { viewModel.onEvent(TripsEntryScreenEvent.SetStop(stop)) },
                    modifier = Modifier
                        .animateItem(fadeInSpec = null, fadeOutSpec = null)
                        .fillMaxWidth()
                )
            }
        }
    }
}