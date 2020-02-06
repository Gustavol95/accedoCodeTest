package com.accedo.codetest.data.network

import com.accedo.codetest.utils.RetryListener

data class ApiResponse<T>(val data : Data<T>)

data class Data<T>(
    val offset : Int,
    val limit : Int,
    val total : Int,
    val count : Int,
    val results : List<T>
)

data class Character(
    val id : Long,
    val name : String,
    val description : String?,
    val thumbnail: Thumbnail

)

data class Thumbnail(
    val path: String,
    val extension: String
) {
    fun getUrl() : String {
        return "$path.$extension"
    }
}

data class Comic(
    val id: Long,
    val title: String,
    val description: String?,
    val thumbnail: Thumbnail
)

sealed class Status {
    class Success<T>(val response: ApiResponse<T>) : Status()
    class Failure(val throwable: Throwable, val retryListener: RetryListener?) : Status()
    object Loading : Status()
}