package com.accedo.codetest

import android.app.Application
import timber.log.Timber

class App : Application() {

    companion object {

        var PAGE_SIZE: Int = 10
    }

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        PAGE_SIZE = resources.getInteger(R.integer.character_page_size)
    }
}