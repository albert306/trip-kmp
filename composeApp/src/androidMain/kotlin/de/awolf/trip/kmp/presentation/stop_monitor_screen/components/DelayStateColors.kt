package de.awolf.trip.kmp.presentation.stop_monitor_screen.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

data class DelayStateColors(
    val red: Color,
    val green: Color,
    val blue: Color
) {
    companion object {
        @Composable
        fun getColors(): DelayStateColors {
            return if (isSystemInDarkTheme()) {
                DelayStateColors(
                    red = Color(0xFFFF6666),
                    green = Color(0xFF00DD00),
                    blue = Color(0xFF60A0FF),
                )
            } else {
                DelayStateColors(
                    red = Color(0xFF990000),
                    green = Color(0xFF005500),
                    blue = Color(0xFF0000BB),
                )
            }
        }
    }
}