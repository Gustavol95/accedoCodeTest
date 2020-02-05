package com.accedo.codetest.ui.chardetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.accedo.codetest.data.repository.CharacterRepository

class CharacterViewModelFactory(
    private val characterId: Long,
    private val characterRepository: CharacterRepository
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CharacterDetailViewModel::class.java)) {
            return CharacterDetailViewModel(characterId, characterRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
