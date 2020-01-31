package com.accedo.codetest.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.accedo.codetest.data.network.Character
import com.accedo.codetest.data.network.MarvelService
import com.accedo.codetest.data.network.Status
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class CharacterRepository(private val marvelService: MarvelService) {

    lateinit var characterPagedList: LiveData<PagedList<Character>>
    lateinit var characterDataSourceFactory: CharacterDataSourceFactory

    fun fetchLiveCharacterPagedList(compositeDisposable: CompositeDisposable): LiveData<PagedList<Character>> {
        characterDataSourceFactory = CharacterDataSourceFactory(marvelService, compositeDisposable)

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(10)
            .build()

        characterPagedList = LivePagedListBuilder(characterDataSourceFactory, config).build()

        return characterPagedList
    }

    fun fetchLiveComicsList(idCharacter: Long, compositeDisposable: CompositeDisposable): MutableLiveData<Status> {
        val comicListLiveData = MutableLiveData<Status>()
        comicListLiveData.value = Status.Loading
        compositeDisposable.add(
            marvelService.getCharacterComics(idCharacter)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        comicListLiveData.postValue(Status.Success(it))
                    },
                    {
                        comicListLiveData.postValue(Status.Failure(it))
                    })
        )
        return comicListLiveData
    }

}