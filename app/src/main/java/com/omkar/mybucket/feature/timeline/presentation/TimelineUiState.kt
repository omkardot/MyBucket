package com.omkar.mybucket.feature.timeline.presentation

import com.omkar.mybucket.core.database.entity.LifecycleEventEntity


data class TimelineItem(
    val event: LifecycleEventEntity,
    val responsibilityTitle: String
)

sealed interface TimelineUiState {
    object Loading : TimelineUiState
    data class Success(val items: List<TimelineItem> = emptyList()) : TimelineUiState
    data class Error(val message: String) : TimelineUiState
}