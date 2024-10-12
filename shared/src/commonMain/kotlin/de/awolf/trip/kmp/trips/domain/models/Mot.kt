package de.awolf.trip.kmp.trips.domain.models

import de.awolf.trip.kmp.core.domain.models.Diva
import de.awolf.trip.kmp.core.domain.models.Mode

sealed class Mot {
    data class Line(
        val dlId: String,
        val lineId: String,
        val mode: Mode,
        val lineNumber: String,
        val lineDirection: String?,
        val transportationCompany: String?,
        val operatorCode: String?,
        val trainNumber: String?,
        val productName: String?,
        val changes: List<String> = listOf(),
        val diva: Diva?
    ) : Mot()

    data object Footpath : Mot()
}
