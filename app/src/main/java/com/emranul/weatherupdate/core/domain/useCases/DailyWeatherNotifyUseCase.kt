package com.emranul.weatherupdate.core.domain.useCases

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.emranul.weatherupdate.workManager.WeatherReminderWorker
import com.google.android.gms.location.LocationServices
import timber.log.Timber
import java.time.Duration
import java.util.concurrent.TimeUnit

class DailyWeatherNotifyUseCase constructor(val context: Context) {

    operator fun invoke() {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }

        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        fusedLocationClient.lastLocation.addOnSuccessListener {
            Timber.d("fusedLocationClient success ${it.latitude}")
            executeWorkManager(it)
        }.addOnFailureListener {
            Timber.d("fusedLocationClient error ----------> ${it.message}")
        }

    }

    private fun executeWorkManager(location: Location) {
        val data = Data.Builder().apply {
            putString("latitude", location.latitude.toString())
            putString("longitude", location.longitude.toString())
        }.build()
//        val parallelWorkRequest = OneTimeWorkRequestBuilder<WeatherReminderWorker>()
//            .setBackoffCriteria(
//                BackoffPolicy.LINEAR,
//                WorkRequest.MIN_BACKOFF_MILLIS,
//                TimeUnit.MILLISECONDS
//            )
//            .setInputData(data)
//            .addTag("weatherReminderWork")
//            .build()
//
//        val workManager = WorkManager.getInstance(context)
//
//        workManager.enqueueUniqueWork(
//            "weatherReminderWork",
//            ExistingWorkPolicy.REPLACE,
//            parallelWorkRequest
//        )
//


        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val periodicWorkRequest =
            PeriodicWorkRequestBuilder<WeatherReminderWorker>(2, TimeUnit.MINUTES)
                .setInputData(data)
                .setConstraints(constraints)
                .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "weatherReminderWork",
            ExistingPeriodicWorkPolicy.KEEP,
            periodicWorkRequest
        )
    }
}