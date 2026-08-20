package com.omkar.mybucket.feature.responsibility.presentation.list

import com.omkar.mybucket.feature.responsibility.data.ResponsibilityRepository


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omkar.mybucket.feature.responsibility.presentation.detail.DetailUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
class ResponsibilityListViewModel(
    private val repository: ResponsibilityRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    private val _selectedStageFilter = MutableStateFlow<String?>(null)

    val uiState: StateFlow<ResponsibilityListUiState> = combine(
        repository.allResponsibilities,
        _searchQuery,
        _selectedStageFilter
    ) { items, query, stageFilter ->
        val filteredItems = items.filter { item ->
            val matchesQuery = item.responsibility.title.contains(query, ignoreCase = true) ||
                    item.responsibility.description.contains(query, ignoreCase = true) ||
                    item.responsibility.project.contains(query, ignoreCase = true)

            val matchesStage = stageFilter == null ||
                    item.responsibility.currentStage.equals(stageFilter, ignoreCase = true)

            matchesQuery && matchesStage
        }

        ResponsibilityListUiState.Success(
            items = filteredItems,
            searchQuery = query,
            selectedFilterStage = stageFilter
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ResponsibilityListUiState.Loading
    )

    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun onStageFilterSelect(stage: String?) {
        // Reset to null if "All Tasks" is selected or if the same stage chip is clicked again
        _selectedStageFilter.value = if (stage == "All Tasks" || _selectedStageFilter.value == stage) {
            null
        } else {
            stage
        }
    }
}