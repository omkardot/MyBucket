package com.omkar.mybucket.feature.responsibility.presentation.detail


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.omkar.mybucket.core.database.entity.LifecycleEventEntity
import com.omkar.mybucket.core.database.model.ResponsibilityWithEvents
import com.omkar.mybucket.feature.responsibility.data.ResponsibilityRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

sealed interface DetailUiState {
    object Loading : DetailUiState
    data class Success(
        val itemWithEvents: ResponsibilityWithEvents,
        val isEditing: Boolean = false
    ) : DetailUiState
    data class Error(val message: String) : DetailUiState
}

class ResponsibilityDetailViewModel(
    private val responsibilityId: Long,
    private val repository: ResponsibilityRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            repository.getResponsibilityWithEvents(responsibilityId).collect { item ->
                if (item != null) {
                    val currentIsEditing = (_uiState.value as? DetailUiState.Success)?.isEditing ?: false
                    _uiState.value = DetailUiState.Success(itemWithEvents = item, isEditing = currentIsEditing)
                } else {
                    _uiState.value = DetailUiState.Error("Task not found.")
                }
            }
        }
    }

    fun toggleEditMode() {
        val currentState = _uiState.value as? DetailUiState.Success ?: return
        _uiState.value = currentState.copy(isEditing = !currentState.isEditing)
    }

    fun updateStatus(newStage: String) {
        viewModelScope.launch {
            val currentState = _uiState.value as? DetailUiState.Success ?: return@launch
            val updatedEntity = currentState.itemWithEvents.responsibility.copy(
                currentStage = newStage,
                updatedAt = System.currentTimeMillis()
            )
            repository.updateResponsibility(updatedEntity)
        }
    }

    fun saveTaskDetails(
        title: String,
        description: String,
        project: String,
        priority: String,
        estimatedHours: Int
    ) {
        viewModelScope.launch {
            val currentState = _uiState.value as? DetailUiState.Success ?: return@launch
            val updatedEntity = currentState.itemWithEvents.responsibility.copy(
                title = title,
                description = description,
                project = project,
                priority = priority,
                estimatedHours = estimatedHours,
                updatedAt = System.currentTimeMillis()
            )
            repository.updateResponsibility(updatedEntity)
            _uiState.value = currentState.copy(isEditing = false)
        }
    }

    fun addTimelineEvent(stage: String, timeSpentMinutes: Int, notes: String, issuesFound: String?) {
        viewModelScope.launch {
            val event = LifecycleEventEntity(
                responsibilityId = responsibilityId,
                stage = stage,
                timestamp = System.currentTimeMillis(),
                timeSpentMinutes = timeSpentMinutes,
                notes = notes,
                issuesFound = issuesFound
            )
            repository.insertTimelineEvent(event)
        }
    }
}

class ResponsibilityDetailViewModelFactory(
    private val responsibilityId: Long,
    private val repository: ResponsibilityRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ResponsibilityDetailViewModel::class.java)) {
            return ResponsibilityDetailViewModel(responsibilityId, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}