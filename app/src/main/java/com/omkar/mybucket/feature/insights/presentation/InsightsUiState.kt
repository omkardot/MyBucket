package com.omkar.mybucket.feature.insights.presentation

data class ProjectTimeDistribution(
    val projectName: String,
    val totalMinutes: Int,
    val percentage: Float
)

data class BottleneckSummary(
    val stageName: String,
    val count: Int
)

data class InsightsData(
    val totalTimeSpentMinutes: Int = 0,
    val completionRatePercentage: Int = 0,
    val projectDistributions: List<ProjectTimeDistribution> = emptyList(),
    val topBottlenecks: List<BottleneckSummary> = emptyList(),
    val totalLoggedEvents: Int = 0
)

sealed interface InsightsUiState {
    object Loading : InsightsUiState
    data class Success(val data: InsightsData) : InsightsUiState
    data class Error(val message: String) : InsightsUiState
}