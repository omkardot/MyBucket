package com.omkar.mybucket.feature.dashboard.presentation

import com.omkar.mybucket.core.database.model.ResponsibilityWithEvents



data class DashboardMetrics(
    val totalCount: Int = 0,
    val inProgressCount: Int = 0,
    val blockedCount: Int = 0,
    val completedCount: Int = 0
)

sealed interface DashboardUiState {
    object Loading : DashboardUiState
    data class Success(
        val metrics: DashboardMetrics,
        val activeResponsibilities: List<ResponsibilityWithEvents>
    ) : DashboardUiState
    data class Error(val message: String) : DashboardUiState
}