package com.emranul.weatherupdate.network

sealed class Result< out T>(
    val status: Status,
    val data: T?,
    val message: String?
) {

    class Success<T>(data: T?) : Result<T>(Status.SUCCESS,data,null)
    class Error<T>(error:String?) : Result<T>(Status.ERROR,null,error)
    class Loading<T> : Result<T>(Status.LOADING,null,null)

}
