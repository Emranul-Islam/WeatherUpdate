package com.emranul.weatherupdate.api

//data class Result<out T>(val status: Status, val data: T?, val message: String?) : Flow<Any> {
//    companion object {
//        fun <T> success(data: T?): Result<T> =
//            Result(Status.SUCCESS, data, null)
//
//        fun <T> error(msg: String, data: T? = null): Result<T> =
//            Result(Status.ERROR, data, msg)
//
//        fun <T> error(exception: Exception, data: T? = null): Result<T> =
//            Result(Status.ERROR, data, exception.message)
//
//        fun <T> loading(data: T? = null): Result<T> =
//            Result(Status.LOADING, data, null)
//
//        fun <T> nothing(): Result<T> =
//            Result(Status.NOTHING, null, null)
//    }
//
//    override suspend fun collect(collector: FlowCollector<Any>) {
//
//    }
//}

sealed class Result< out T>(
    val status: Status,
    val data: T?,
    val message: String?
) {

    class Success<T>(data: T?) : Result<T>(Status.SUCCESS,data,null)
    class Error<T>(error:String?) : Result<T>(Status.ERROR,null,error)
    class Loading<T> : Result<T>(Status.LOADING,null,null)

}
