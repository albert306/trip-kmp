package de.awolf.trip.kmp.departures.search_screen

import android.os.Build
import android.view.HapticFeedbackConstants
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import de.awolf.trip.kmp.core.helper.SideEffectListener
import de.awolf.trip.kmp.departures.search_screen.components.SearchCard
import de.awolf.trip.kmp.departures.search_screen.components.StopView
import de.awolf.trip.kmp.departures.search_screen.components.TimePickerDialog
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState
import de.awolf.trip.kmp.core.domain.models.StopListSource
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import de.awolf.trip.kmp.core.util.error.DatabaseError
import de.awolf.trip.kmp.core.util.error.NetworkError
import de.awolf.trip.kmp.departures.presentation.search_screen.SearchScreenEvent
import de.awolf.trip.kmp.departures.presentation.search_screen.SearchScreenSideEffect
import de.awolf.trip.kmp.departures.presentation.search_screen.SearchScreenViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    viewModel: SearchScreenViewModel,
    snackbarHostState: SnackbarHostState
) {
    val state by viewModel.state.collectAsState()

    val view = LocalView.current
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

    val lazyListState = rememberLazyListState()

    val reorderableLazyListState = rememberReorderableLazyListState(
        lazyListState = lazyListState,
        scrollThresholdPadding = PaddingValues(top = 100.dp)
    ) { from, to ->
        viewModel.onEvent(
            SearchScreenEvent
                .ReorderFavoriteStop(from.key.toString(), from.index - 1, to.index - 1)
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            view.performHapticFeedback(HapticFeedbackConstants.SEGMENT_TICK)
        }
    }

    SideEffectListener(flow = viewModel.sideEffect) { sideEffect ->
        val toastMsg: String
        when (sideEffect) {
            is SearchScreenSideEffect.ShowNoStopFoundMsg -> toastMsg = "No stop selected"
            is SearchScreenSideEffect.ShowInvalidDateTimeMsg -> toastMsg = "Selected date and time is in the past"
            is SearchScreenSideEffect.ShowDatabaseError -> toastMsg = when (sideEffect.error) {
                DatabaseError.UNKNOWN -> "Unknown database error"
            }
            is SearchScreenSideEffect.ShowNetworkError -> toastMsg = when (sideEffect.error) {
                NetworkError.UNKNOWN -> "Unknown network error"
                NetworkError.BAD_REQUEST -> "Bad network request"
                NetworkError.REQUEST_TIMEOUT -> "Request timeout"
                NetworkError.UNAUTHORIZED -> "Network unauthorized error"
                NetworkError.CONFLICT -> "Network conflict"
                NetworkError.TOO_MANY_REQUESTS -> "Too many network requests"
                NetworkError.NO_INTERNET -> "No internet connection"
                NetworkError.PAYLOAD_TOO_LARGE -> "Network payload too large"
                NetworkError.SERVER_ERROR -> "Network server error"
                NetworkError.SERIALIZATION -> "Network serialization error"
            }
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
        onDateConfirm = { viewModel.onEvent(SearchScreenEvent.ChangeSelectedDate(it)) },
        onTimeConfirm = { viewModel.onEvent(SearchScreenEvent.ChangeSelectedTime(it)) }
    )

    Column(
        verticalArrangement = Arrangement.spacedBy((-10).dp),
        modifier = Modifier
            .fillMaxSize()
    ) {
        SearchCard(
            searchScreenState = state,
            onSearchTextChange = { viewModel.onEvent(SearchScreenEvent.Search(it)) },
            onShowDatePicker = { showDatePicker.value = true },
            onShowTimePicker = { showTimePicker.value = true },
            onResetDateTime = { viewModel.onEvent(SearchScreenEvent.ResetSelectedDateTime) },
            onSearchButtonClick = {
                viewModel.onEvent(SearchScreenEvent.StartStopMonitor(null))
            },
            modifier = Modifier
                .zIndex(1f)
                .fillMaxWidth()
        )
        LazyColumn(
            state = lazyListState,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .zIndex(0f)
                .fillMaxSize(),
        ) {
            item {
                Spacer(modifier = Modifier.height(6.dp))
            }
            items(items = state.stopList, key = { it.id }) { stop ->
                if (state.stopListSource == StopListSource.FAVORITES) {
                    ReorderableItem(
                        state = reorderableLazyListState,
                        key = stop.id
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            StopView(
                                stop = stop,
                                onFavoriteStarClick = {
                                    viewModel.onEvent(SearchScreenEvent.ToggleFavorite(stop))
                                },
                                onNameClick = { viewModel.onEvent(SearchScreenEvent.StartStopMonitor(stop)) },
                                modifier = Modifier
                                    .animateItem(fadeInSpec = null, fadeOutSpec = null)
                                    .fillMaxWidth(fraction = 0.9f)
                            )
                            IconButton(
                                onClick = {},
                                modifier = Modifier
                                    .align(Alignment.CenterEnd)
                                    .draggableHandle()
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Menu,
                                    contentDescription = "Reorder",
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                } else {
                    StopView(
                        stop = stop,
                        onFavoriteStarClick = {
                            viewModel.onEvent(SearchScreenEvent.ToggleFavorite(stop))
                        },
                        onNameClick = { viewModel.onEvent(SearchScreenEvent.StartStopMonitor(stop)) },
                        modifier = Modifier
                            .animateItem(fadeInSpec = null, fadeOutSpec = null)
                            .fillMaxWidth()
                    )
                }

            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun DateAndTimePickers(
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