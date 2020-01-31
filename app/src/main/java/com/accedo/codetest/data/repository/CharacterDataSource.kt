package com.accedo.codetest.data.repository

import androidx.paging.PageKeyedDataSource
import com.accedo.codetest.data.network.Character
import com.accedo.codetest.data.network.MarvelService
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber


const val ITEMS_PER_PAGE = 10

class CharacterDataSource(
    private val marvelService: MarvelService,
    private val compositeDisposable: CompositeDisposable
) : PageKeyedDataSource<Int, Character>() {

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Character>
    ) {
        compositeDisposable.add(
            marvelService.getCharacters(ITEMS_PER_PAGE,0)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        callback.onResult(it.data.results, null, ITEMS_PER_PAGE)
                    },
                    {

                    }
                )
        )

    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Character>) {
        compositeDisposable.add(
            marvelService.getCharacters(10,params.key)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        if (it.data.limit + it.data.offset <= it.data.total) {
                            callback.onResult(it.data.results, params.key+ITEMS_PER_PAGE)
                        } else {
                            Timber.i("No more data")
                        }
                    },
                    {

                    }
                )
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Character>) {
    }

}