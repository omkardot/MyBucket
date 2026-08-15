package com.omkar.mybucket.feature.timeline.presentation


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.omkar.mybucket.feature.responsibility.data.ResponsibilityRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class TimelineViewModel(
    private val repository: ResponsibilityRepository
) : ViewModel() {

    val uiState: StateFlow<TimelineUiState> = combine(
        repository.timelineEvents,
        repository.allResponsibilities
    ) { events, responsibilities ->
        val titleMap = responsibilities.associate { it.responsibility.id to it.responsibility.title }

        val timelineItems = events.map { event ->
            TimelineItem(
                event = event,
                responsibilityTitle = titleMap[event.responsibilityId] ?: "Unknown Task"
            )
        }

        TimelineUiState.Success(items = timelineItems)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = TimelineUiState.Loading
    )
}

class TimelineViewModelFactory(
    private val repository: ResponsibilityRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TimelineViewModel::class.java)) {
            return TimelineViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}