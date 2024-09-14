package de.awolf.trip.kmp.core.util

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime

fun LocalTime.truncateTo(unit: DateTimeUnit.TimeBased): LocalTime =
    LocalTime.fromNanosecondOfDay(toNanosecondOfDay().let { it - it % unit.nanoseconds })

fun LocalDateTime.truncateTo(unit: DateTimeUnit.TimeBased): LocalDateTime =
    LocalDateTime(date, time.truncateTo(unit))