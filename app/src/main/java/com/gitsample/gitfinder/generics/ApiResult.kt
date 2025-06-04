package com.gitsample.gitfinder.generics

sealed class ApiResult<out T> {

    data object Loading : ApiResult<Nothing>()

    data class Success<out T>(val data: T) : ApiResult<T>()

    data class Error(
        val errorMessage: String,
        val throwable: Throwable? = null
    ) : ApiResult<Nothing>()
}