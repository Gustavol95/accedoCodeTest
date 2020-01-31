package com.accedo.codetest.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.accedo.codetest.data.network.Character
import com.accedo.codetest.data.network.MarvelService
import io.reactivex.disposables.CompositeDisposable

class CharacterRepository (private val marvelService: MarvelService) {

    lateinit var characterPagedList: LiveData<PagedList<Character>>
    lateinit var characterDataSourceFactory: CharacterDataSourceFactory

    fun fetchLiveCharacterPagedList(compositeDisposable: CompositeDisposable) : LiveData<PagedList<Character>> {
        characterDataSourceFactory = CharacterDataSourceFactory(marvelService, compositeDisposable)

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(10)
            .build()

        characterPagedList = LivePagedListBuilder(characterDataSourceFactory, config).build()

        return characterPagedList
    }

}