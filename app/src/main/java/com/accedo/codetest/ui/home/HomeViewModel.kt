package com.accedo.codetest.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.accedo.codetest.data.network.Character
import com.accedo.codetest.data.repository.CharacterRepository
import io.reactivex.disposables.CompositeDisposable

class HomeViewModel(private val characterRepository: CharacterRepository) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()


    val characterPagedList : LiveData<PagedList<Character>> by lazy {
        characterRepository.fetchLiveCharacterPagedList(compositeDisposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}