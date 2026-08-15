package com.omkar.mybucket.navigation


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.ui.graphics.vector.ImageVector

sealed class ScreenRoute(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Dashboard : ScreenRoute("dashboard", "Dashboard", Icons.Default.Dashboard)
    object Responsibilities : ScreenRoute("responsibilities", "Tasks", Icons.Default.ListAlt)
    object Timeline : ScreenRoute("timeline", "Timeline", Icons.Default.Timeline)
    object Insights : ScreenRoute("insights", "Insights", Icons.Default.Analytics)

    // Non-bottom nav screens can be declared here without icons
    object Detail : ScreenRoute("detail/{responsibilityId}", "Detail", Icons.Default.ListAlt) {
        fun createRoute(id: Long) = "detail/$id"
    }

    companion object {
        val bottomNavItems = listOf(
            Dashboard,
            Responsibilities,
            Timeline,
            Insights
        )
    }
}