package com.emranul.weatherupdate.workManager

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.emranul.weatherupdate.core.base.util.NotificationUtil
import com.emranul.weatherupdate.core.domain.useCases.CurrentWeatherUseCase
import javax.inject.Inject

class WeatherReminderWorkerFactory @Inject constructor(
    private val currentWeatherUseCase: CurrentWeatherUseCase,
    private val notificationUtil: NotificationUtil,
) : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker =
        WeatherReminderWorker(currentWeatherUseCase, notificationUtil, appContext, workerParameters)

}