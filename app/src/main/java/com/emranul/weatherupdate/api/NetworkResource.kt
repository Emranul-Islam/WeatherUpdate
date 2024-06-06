package com.emranul.weatherupdate.api
import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import com.emranul.weatherupdate.hilt.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.withContext

@OptIn(ExperimentalCoroutinesApi::class)
abstract class NetworkResource<RequestType>(
    @IoDispatcher val dispatcher: CoroutineDispatcher
) {
    suspend fun asFlow(): Flow<Result<RequestType>> {
        return createCall().transformLatest {
            when (it) {
                is ApiSuccessResponse -> {
                    val data = processResponse(it)
                    withContext(dispatcher) {
                        saveCallResult(data)
                        emit(Result.Success(data))
                    }
                }
                is ApiEmptyResponse -> emit(Result.Success(null))
                is ApiErrorResponse -> {
                    onFetchFailed()
                    emit(Result.Error(it.errorMessage))
                }
            }
        }.onStart {
            emit(Result.Loading())
        }
    }

    protected open fun onFetchFailed() {
    }

    @WorkerThread
    protected open fun processResponse(response: ApiSuccessResponse<RequestType>) = response.body

    @WorkerThread
    protected open suspend fun saveCallResult(data: RequestType) {
    }

    @MainThread
    protected abstract suspend fun createCall(): Flow<ApiResponse<RequestType>>
}
