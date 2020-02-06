package com.accedo.codetest.ui.chardetail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.accedo.codetest.data.network.Status
import com.accedo.codetest.data.repository.CharacterRepository
import io.reactivex.disposables.CompositeDisposable

class CharacterDetailViewModel(
    characterId: Long,
    private val characterRepository: CharacterRepository) : ViewModel() {
    private var  characterIdLiveData: MutableLiveData<Long> = MutableLiveData()

    init {
        refresh(characterId)
    }

    val compositeDisposable = CompositeDisposable()

    val comicList = Transformations.switchMap(characterIdLiveData) {
        id  -> characterRepository.fetchLiveComicsList(id, compositeDisposable)
    }

    val emptyListLiveData = Transformations.map(comicList) {
        when (it) {
            is Status.Success<*> -> {
                it.response.data.results.isEmpty()
            }
            else -> false
        }
    }


    fun refresh(idCharacter: Long) {
        characterIdLiveData.value = idCharacter
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}