package com.accedo.codetest.data.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.accedo.codetest.App.Companion.PAGE_SIZE
import com.accedo.codetest.data.network.Character
import com.accedo.codetest.data.network.MarvelService
import com.accedo.codetest.data.network.Status
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class CharacterDataSource(
    private val marvelService: MarvelService,
    private val compositeDisposable: CompositeDisposable
) : PageKeyedDataSource<Int, Character>() {


    val networkStatusLiveData = MutableLiveData<Status>()

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Character>
    ) {
        Timber.i("PAGE_SIZE = $PAGE_SIZE")
        networkStatusLiveData.postValue(Status.Loading)
        compositeDisposable.add(
            marvelService.getCharacters(PAGE_SIZE,0)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        callback.onResult(it.data.results, null, PAGE_SIZE)
                        networkStatusLiveData.postValue(Status.Success(it))
                    },
                    {
                        networkStatusLiveData.postValue(Status.Failure(it))
                    }
                )
        )

    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Character>) {
        networkStatusLiveData.postValue(Status.Loading)
        compositeDisposable.add(
            marvelService.getCharacters(PAGE_SIZE,params.key)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        if (it.data.limit + it.data.offset <= it.data.total) {
                            callback.onResult(it.data.results, params.key+PAGE_SIZE)
                            networkStatusLiveData.postValue(Status.Success(it))
                        } else {
                            Timber.i("No more data")
                        }
                    },
                    {
                        networkStatusLiveData.postValue(Status.Failure(it))
                    }
                )
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Character>) {
    }

}