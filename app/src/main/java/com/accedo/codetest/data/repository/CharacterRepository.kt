package com.accedo.codetest.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.accedo.codetest.App.Companion.PAGE_SIZE
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
            .setPageSize(PAGE_SIZE)
            .build()

        characterPagedList = LivePagedListBuilder(characterDataSourceFactory, config).build()

        return characterPagedList
    }

    fun fetchLiveComicsList(idCharacter: Long, compositeDisposable: CompositeDisposable): LiveData<Status> {
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
                        comicListLiveData.postValue(Status.Failure(it,null))
                    })
        )
        return comicListLiveData
    }

    fun getNetworkState() : LiveData<Status> {
        return Transformations.switchMap<CharacterDataSource, Status>(
            characterDataSourceFactory.chatacterLiveDataSource, CharacterDataSource::networkStatusLiveData)
    }

}