package com.emranul.weatherupdate

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.work.Configuration
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.emranul.weatherupdate.core.base.util.NotificationUtil
import com.emranul.weatherupdate.core.domain.useCases.CurrentWeatherUseCase
import com.emranul.weatherupdate.workManager.WeatherReminderWorker
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class WeatherUpdateApp : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: WeatherReminderWorkerFactory
    override fun onCreate() {
        super.onCreate()


        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())

    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setMinimumLoggingLevel(Log.DEBUG)
            .setWorkerFactory(workerFactory)
            .build()
}

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