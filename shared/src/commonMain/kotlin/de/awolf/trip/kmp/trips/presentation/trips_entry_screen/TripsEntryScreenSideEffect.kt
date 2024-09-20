package de.awolf.trip.kmp.trips.presentation.trips_entry_screen

import de.awolf.trip.kmp.core.util.error.Error

sealed interface TripsEntryScreenSideEffect {
    data object ShowNoOriginSelectedMsg : TripsEntryScreenSideEffect
    data object ShowNoDestinationSelectedMsg : TripsEntryScreenSideEffect
    data object ShowInvalidDateTimeMsg : TripsEntryScreenSideEffect
    data class ShowError(val error: Error) : TripsEntryScreenSideEffect
}