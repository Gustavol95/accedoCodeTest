package com.accedo.codetest.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.accedo.codetest.data.repository.CharacterRepository


class HomeViewModelFactory(
    private val pagedListRepository: CharacterRepository
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(pagedListRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
