package com.omkar.mybucket.feature.responsibility.presentation.add_edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.omkar.mybucket.feature.insights.presentation.InsightsViewModel
import com.omkar.mybucket.feature.responsibility.data.ResponsibilityRepository


class AddTaskViewModelFacttory(
    private val repository: ResponsibilityRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddEditResponsibilityViewModel::class.java)) {
            return AddEditResponsibilityViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}