package com.accedo.codetest.ui.chardetail

import androidx.lifecycle.ViewModel
import com.accedo.codetest.data.repository.CharacterRepository
import io.reactivex.disposables.CompositeDisposable

class CharacterDetailViewModel(val characterRepository: CharacterRepository) : ViewModel() {

    val compositeDisposable = CompositeDisposable()
    fun comicLiveList(idCharacter : Long) = characterRepository.fetchLiveComicsList(idCharacter,compositeDisposable)

    fun refresh(idCharacter: Long) {
        characterRepository.fetchLiveComicsList(idCharacter,compositeDisposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}