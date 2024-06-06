package com.emranul.weatherupdate.workManager

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class WeatherReminderWorker(
     appContext: Context,
      workerParams: WorkerParameters
):CoroutineWorker(appContext,workerParams) {

    override suspend fun doWork(): Result {
        TODO("Not yet implemented")
    }
}