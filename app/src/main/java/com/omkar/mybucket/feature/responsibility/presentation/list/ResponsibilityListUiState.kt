package com.omkar.mybucket.feature.responsibility.presentation.list

import com.omkar.mybucket.core.database.model.ResponsibilityWithEvents

sealed interface ResponsibilityListUiState {
    object Loading : ResponsibilityListUiState

    data class Success(
        val items: List<ResponsibilityWithEvents> = emptyList(),
        val searchQuery: String = "",
        val selectedFilterStage: String? = null
    ) : ResponsibilityListUiState

    data class Error(val message: String) : ResponsibilityListUiState
}