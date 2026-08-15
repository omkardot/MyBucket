package com.omkar.mybucket.feature.responsibility.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.omkar.mybucket.feature.responsibility.data.ResponsibilityRepository

class ResponsibilityListViewModelFactory(
    private val repository: ResponsibilityRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ResponsibilityListViewModel::class.java)) {
            return ResponsibilityListViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}