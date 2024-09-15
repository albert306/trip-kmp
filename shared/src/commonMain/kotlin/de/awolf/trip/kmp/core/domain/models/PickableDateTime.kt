package de.awolf.trip.kmp.core.domain.models

import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import de.awolf.trip.kmp.core.util.truncateTo

class PickableDateTime(
    val date: LocalDate? = null,
    val time: LocalTime? = null
) {
    fun hasDate(): Boolean {
        return date != null
    }

    fun hasTime(): Boolean {
        return time != null
    }

    fun dateTimeIsValid(): Boolean {
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).truncateTo(
            DateTimeUnit.MINUTE)
        val selected = LocalDateTime(
            this.date ?: now.date,
            this.time ?: now.time
        )
        return selected >= now
    }

    fun toLocalDateTime(): LocalDateTime {
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

        return LocalDateTime(
            date = date ?: now.date,
            time = time ?: now.time
        )
    }

    fun toInstant(): Instant {
        return toLocalDateTime().toInstant(TimeZone.currentSystemDefault())
    }

    fun copy(
        date: LocalDate? = this.date,
        time: LocalTime? = this.time
    ): PickableDateTime {
        return PickableDateTime(
            date = date,
            time = time
        )
    }
}