package com.accedo.codetest.utils

import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.accedo.codetest.R
import com.google.android.material.snackbar.Snackbar
import retrofit2.HttpException
import java.io.IOException
import java.net.UnknownHostException

fun Throwable.getSimpleMessage() : String {
       return  when (this) {
            is HttpException -> "There was a problem with the response"
           is  IOException -> "There was a problem with the connection"
           is UnknownHostException -> "There was a problem with the host"
           else -> "An Error Ocurred."}
}

fun Snackbar.makeRounded() : Snackbar{
    this.view.layoutParams = (view.layoutParams as CoordinatorLayout.LayoutParams).apply {
        setMargins(
            12,
            12,
            12,
            12
        )
    }
    view.background = view.context.resources.getDrawable(R.drawable.round_corner, null)
    return this
}