package com.omkar.mybucket.feature.insights.presentation



import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.omkar.mybucket.feature.responsibility.data.ResponsibilityRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class InsightsViewModel(
    private val repository: ResponsibilityRepository
) : ViewModel() {

    val uiState: StateFlow<InsightsUiState> = combine(
        repository.allResponsibilities,
        repository.timelineEvents
    ) { items, events ->
        val totalItems = items.size
        val completedItems = items.count { it.responsibility.currentStage == "Completed" }
        val completionRate = if (totalItems > 0) (completedItems * 100) / totalItems else 0

        val totalMinutes = events.sumOf { it.timeSpentMinutes }

        // Time distribution by project
        val resProjectMap = items.associate { it.responsibility.id to it.responsibility.project }
        val projectTimeMap = mutableMapOf<String, Int>()

        events.forEach { event ->
            val project = resProjectMap[event.responsibilityId] ?: "Unassigned"
            projectTimeMap[project] = (projectTimeMap[project] ?: 0) + event.timeSpentMinutes
        }

        val projectDistributions = projectTimeMap.map { (project, minutes) ->
            val percentage = if (totalMinutes > 0) (minutes.toFloat() / totalMinutes) * 100f else 0f
            ProjectTimeDistribution(project, minutes, percentage)
        }.sortedByDescending { it.totalMinutes }

        // Top bottlenecks calculation (events logged with issues or in "Blocked" stage)
        val blockedCounts = events
            .filter { it.stage == "Blocked" || !it.issuesFound.isNullOrBlank() }
            .groupBy { it.stage }
            .map { (stage, list) -> BottleneckSummary(stage, list.size) }
            .sortedByDescending { it.count }

        InsightsUiState.Success(
            data = InsightsData(
                totalTimeSpentMinutes = totalMinutes,
                completionRatePercentage = completionRate,
                projectDistributions = projectDistributions,
                topBottlenecks = blockedCounts,
                totalLoggedEvents = events.size
            )
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = InsightsUiState.Loading
    )
}

class InsightsViewModelFactory(
    private val repository: ResponsibilityRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InsightsViewModel::class.java)) {
            return InsightsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}