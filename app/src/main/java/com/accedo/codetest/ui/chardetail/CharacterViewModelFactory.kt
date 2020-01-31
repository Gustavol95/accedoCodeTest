package com.accedo.codetest.ui.chardetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.accedo.codetest.data.repository.CharacterRepository

class CharacterViewModelFactory(
    private val characterRepository: CharacterRepository
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CharacterDetailViewModel::class.java)) {
            return CharacterDetailViewModel(characterRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
