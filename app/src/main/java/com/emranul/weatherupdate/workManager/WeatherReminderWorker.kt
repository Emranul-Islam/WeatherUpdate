package com.emranul.weatherupdate.workManager

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.emranul.weatherupdate.core.base.util.NotificationUtil
import com.emranul.weatherupdate.core.domain.useCases.CurrentWeatherUseCase
import com.emranul.weatherupdate.features.home.domain.model.WeathersPayload
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltWorker
@OptIn(ExperimentalCoroutinesApi::class)
class WeatherReminderWorker @AssistedInject constructor(
    private val currentWeatherUseCase: CurrentWeatherUseCase,
    private val notificationUtil: NotificationUtil,
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    private val scope = CoroutineScope(Dispatchers.IO)

    private val _currentWeather =
        MutableSharedFlow<WeathersPayload>(
            replay = 1,
            onBufferOverflow = BufferOverflow.DROP_OLDEST
        )

    private val currentWeather = _currentWeather.flatMapLatest {
        currentWeatherUseCase(it)
    }.stateIn(scope, SharingStarted.WhileSubscribed(5_000), null)

    override suspend fun doWork(): Result {

        Timber.d("WeatherReminderWorker -------> Executed")

        val lat = inputData.getString("latitude")
        val lan = inputData.getString("longitude")

        if (lat.isNullOrEmpty() && lan.isNullOrEmpty()) {
            Timber.d("WeatherReminderWorker -------> lat lan is empty")
            return Result.failure()
        }

        scope.launch {
            _currentWeather.tryEmit(
                WeathersPayload(
                    lat!!,
                    lan!!
                )
            )
        }

        var result: Result? = null
        try {
            scope.async {
             currentWeather.collect {

                    when (it) {
                        is com.emranul.weatherupdate.network.Result.Error -> {
                            Timber.d("Current Weather data error ----------> ${it.message}")
                            result = Result.failure()
                        }

                        is com.emranul.weatherupdate.network.Result.Loading -> Unit
                        is com.emranul.weatherupdate.network.Result.Success -> {
                            Timber.d("Current Weather data success ----------> ${it.data}")
                            it.data?.let { it1 -> notificationUtil.showNotification(it1) }
                            result = Result.success()
                            this.cancel(null)

                        }

                        null -> Unit
                    }
                }
            }.await()
        } catch (e: Exception) {
            Timber.d("WeatherReminderWorker Try Catch error ${e.message}")
        }

        Timber.d("WeatherReminderWorker -------> Executed Complete $result")

        return result ?: Result.retry()

    }
}