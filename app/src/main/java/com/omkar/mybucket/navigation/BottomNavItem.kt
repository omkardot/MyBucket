package com.omkar.mybucket.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Dashboard : BottomNavItem(
        route = ScreenRoute.Dashboard.route,
        title = "Dashboard",
        icon = Icons.Default.Dashboard
    )

    object Responsibilities : BottomNavItem(
        route = ScreenRoute.Responsibilities.route,
        title = "Tasks",
        icon = Icons.Default.ListAlt
    )

    object Timeline : BottomNavItem(
        route = ScreenRoute.Timeline.route,
        title = "Timeline",
        icon = Icons.Default.Timeline
    )

    object Insights : BottomNavItem(
        route = ScreenRoute.Insights.route,
        title = "Insights",
        icon = Icons.Default.Analytics
    )

    companion object {
        val items = listOf(Dashboard, Responsibilities, Timeline, Insights)
    }
}