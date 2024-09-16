package de.awolf.trip.kmp.core.helper

import de.awolf.trip.kmp.core.domain.models.PickableDateTime
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.format
import kotlinx.datetime.format.char

fun PickableDateTime.dateText(): String {
    return this.date?.format(LocalDate.Format {
        dayOfMonth()
        char('.')
        monthNumber()
        char('.')
    }) ?: "Today"
}

fun PickableDateTime.timeText(): String {
    return this.time?.format(LocalTime.Format {
        hour()
        char(':')
        minute()
    }) ?: "Now"
}