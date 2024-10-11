package de.awolf.trip.kmp.core.helper

import de.awolf.trip.kmp.core.domain.models.PickableDateTime
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime

fun LocalDate.formatted(): String {
    return this.format(LocalDate.Format {
        dayOfMonth()
        char('.')
        monthNumber()
        char('.')
    })
}

fun LocalTime.formatted(): String {
    return this.format(LocalTime.Format {
        hour()
        char(':')
        minute()
    })
}

fun Instant.formattedTime(timeZone: TimeZone = TimeZone.currentSystemDefault()): String {
    return this.toLocalDateTime(timeZone).time.formatted()
}

fun Instant.formattedDate(timeZone: TimeZone = TimeZone.currentSystemDefault()): String {
    return this.toLocalDateTime(timeZone).date.formatted()
}

fun PickableDateTime.dateText(): String {
    return this.date?.formatted() ?: "Today"
}

fun PickableDateTime.timeText(): String {
    return this.time?.formatted() ?: "Now"
}