package de.awolf.trip.kmp.presentation.helper

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable

/**
 * Returns true if one of the last items of the LazyList becomes visible on screen.
 *
 * Example:
 *
 * tolerance = 0 -> true if last element is visible
 *
 * tolerance = 1 -> true if last or next-to-last element is visible
 *
 * @param tolerance specifies the x-to-last element on which this function triggers
 */
@Composable
fun LazyListState.isFinalItemVisible(tolerance: Int = 0): Boolean {

    val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull() ?: return false
    return lastVisibleItem.index + tolerance >= layoutInfo.totalItemsCount - 1
}