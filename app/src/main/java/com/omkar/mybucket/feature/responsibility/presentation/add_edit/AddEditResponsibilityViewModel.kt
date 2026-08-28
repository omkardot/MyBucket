package com.omkar.mybucket.feature.responsibility.presentation.add_edit

import androidx.compose.animation.AnimatedContentScope
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import com.omkar.mybucket.feature.responsibility.data.ResponsibilityRepository
import kotlinx.coroutines.launch

class AddEditResponsibilityViewModel(
    val repo: ResponsibilityRepository
) : ViewModel() {
    fun createResponsibility(
        title: String,
        description: String,
        codeStack: String,
        assignedBy: String,
        priority: String,
        selectedcomplexity: String,
        selectedestimatedTime: String,
        dateText: String
    ) {
        viewModelScope.launch {
            repo.createResponsibility(
                title = title,
                description = description,
                codeStack = codeStack, assignedBy = assignedBy, priority = priority,
                complexity = selectedcomplexity,
                estimatedTime = selectedestimatedTime,
                dateText = dateText
            )
        }
    }
}
