package com.omkar.mybucket.feature.dashboard.presentation


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.omkar.mybucket.feature.responsibility.data.ResponsibilityRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class DashboardViewModel(
    private val repository: ResponsibilityRepository
) : ViewModel() {

    val uiState: StateFlow<DashboardUiState> = repository.allResponsibilities
        .map { items ->
            val total = items.size
            val inProgress = items.count { it.responsibility.currentStage == "In Progress" }
            val blocked = items.count { it.responsibility.currentStage == "Blocked" }
            val completed = items.count { it.responsibility.currentStage == "Completed" }

            // Active items exclude completed items
            val active = items.filter { it.responsibility.currentStage != "Completed" }

            DashboardUiState.Success(
                metrics = DashboardMetrics(
                    totalCount = total,
                    inProgressCount = inProgress,
                    blockedCount = blocked,
                    completedCount = completed
                ),
                activeResponsibilities = active
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = DashboardUiState.Loading
        )
}

class DashboardViewModelFactory(
    private val repository: ResponsibilityRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
            return DashboardViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}