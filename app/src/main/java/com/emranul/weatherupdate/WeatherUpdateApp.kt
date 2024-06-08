package com.emranul.weatherupdate

import android.app.Application
import android.util.Log
import androidx.work.Configuration
import com.emranul.weatherupdate.workManager.WeatherReminderWorkerFactory
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class WeatherUpdateApp : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: WeatherReminderWorkerFactory
    override fun onCreate() {
        super.onCreate()


        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree()) //initialize Timber for just debug build

    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setMinimumLoggingLevel(Log.DEBUG)
            .setWorkerFactory(workerFactory)
            .build()
}

