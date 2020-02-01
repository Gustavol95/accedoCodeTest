package com.accedo.codetest.utils

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