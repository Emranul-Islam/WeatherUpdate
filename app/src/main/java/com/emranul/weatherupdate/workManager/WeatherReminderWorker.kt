package com.emranul.weatherupdate.workManager

import android.content.Context
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
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import timber.log.Timber

@HiltWorker
class WeatherReminderWorker @AssistedInject constructor(
    private val currentWeatherUseCase: CurrentWeatherUseCase,
    private val notificationUtil: NotificationUtil,
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    private val scope = CoroutineScope(Dispatchers.IO)

    override suspend fun doWork(): Result {

        Timber.d("WeatherReminderWorker -------> Executed")

        val lat = inputData.getString("latitude")
        val lan = inputData.getString("longitude")

        if (lat.isNullOrEmpty() && lan.isNullOrEmpty()) {
            Timber.d("WeatherReminderWorker -------> lat lan is empty")
            return Result.failure()
        }

        var result: Result? = null


        try {
            scope.async {
                return@async currentWeatherUseCase(
                    WeathersPayload(
                        lat ?: "0.0",
                        lan ?: "0.0"
                    )
                ).collect {
                    when (it) {
                        is com.emranul.weatherupdate.network.Result.Error -> {
                            Timber.d("Current Weather data error ----------> ${it.message}")
                            result = Result.failure()
                            this.cancel()
                        }

                        is com.emranul.weatherupdate.network.Result.Loading -> Unit
                        is com.emranul.weatherupdate.network.Result.Success -> {
                            Timber.d("Current Weather data success ----------> ${it.data}")
                            it.data?.let { it1 -> notificationUtil.showNotification(it1) }
                            result = Result.success()
                            this.cancel()
                        }
                    }
                }

            }.await()

        } catch (e: Exception) {
            Timber.d("WeatherReminderWorker catch --->${e.message}")
        }


        Timber.d("WeatherReminderWorker -------> Executed Complete $result")

        return result ?: Result.retry()

    }
}