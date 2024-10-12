package de.awolf.trip.kmp.trips.trips_screen.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import de.awolf.trip.kmp.core.components.RealAndScheduledTimeText
import de.awolf.trip.kmp.core.domain.models.Mode
import de.awolf.trip.kmp.core.helper.formatted
import de.awolf.trip.kmp.core.helper.formattedTime
import de.awolf.trip.kmp.trips.domain.models.Mot
import de.awolf.trip.kmp.trips.domain.models.Route
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Preview
@Composable
fun RouteViewPreview() {
    RouteView(
        Route(
            priceLevel = 1,
            price = "2.50",
            priceDayTicket = "5.00",
            network = "DVB",
            duration = 30,
            interchanges = 1,
            numberOfFareZones = "1",
            numberOfFareZonesDayTicket = "2",
            fareZonesNames = "Dresden",
            fareZonesNamesDayTicket = "Dresden",
            fareZoneOrigin = 1,
            fareZoneDestination = 2,
            routeIndex = 1,
            mapData = listOf(),
            motChain = listOf(
                Mot.Line(
                    dlId = "id",
                    lineId = "id",
                    mode = Mode.CITYBUS,
                    lineNumber = "66",
                    lineDirection = "Nickern",
                    transportationCompany = "DVB",
                    operatorCode = "DVB",
                    trainNumber = "",
                    productName = "Bus",
                    changes = listOf(),
                    diva = null
                ),
                Mot.Line(
                    dlId = "id",
                    lineId = "id",
                    mode = Mode.TRAM,
                    lineNumber = "7",
                    lineDirection = "Weixdorf",
                    transportationCompany = "DVB",
                    operatorCode = "DVB",
                    trainNumber = "",
                    productName = "Tram",
                    changes = listOf(),
                    diva = null
                )
            ),
            partialRoutes = listOf()
        )
    )
}

@Composable
fun RouteView(
    route: Route,
    modifier: Modifier = Modifier
) {
    if (route.partialRoutes.isEmpty()) {
        return
    }
    ConstraintLayout(
        constraintSet = constraints(),
        modifier = modifier
    ) {
        RealAndScheduledTimeText(
            scheduledTime = route.firstStop.departureScheduledTime,
            realTime = route.firstStop.departureRealTime,
            topFontSize = 16.sp,
            bottomFontSize = 12.sp,
            modifier = Modifier.layoutId("startTime")
        )

        Text(
            text = "${route.duration} min",
            fontSize = 12.sp,
            modifier = Modifier.layoutId("duration")
        )

        RealAndScheduledTimeText(
            scheduledTime = route.lastStop.arrivalScheduledTime,
            realTime = route.lastStop.arrivalRealTime,
            topFontSize = 16.sp,
            bottomFontSize = 12.sp,
            modifier = Modifier.layoutId("endTime")
        )

        Canvas(
            modifier = Modifier
                .layoutId("visual")
                .fillMaxWidth()
        ) {
        }

    }
}

private fun constraints() = ConstraintSet {
    val startTime = createRefFor("startTime")
    val endTime = createRefFor("endTime")
    val duration = createRefFor("duration")
    val visual = createRefFor("visual")
}
