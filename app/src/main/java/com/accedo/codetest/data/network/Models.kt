package com.accedo.codetest.data.network

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
    val description : String,
    val thumbnail: Thumbnail

)

data class Thumbnail(
    val path: String,
    val extension: String
)