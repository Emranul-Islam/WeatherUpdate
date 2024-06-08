package com.emranul.weatherupdate.core.base.util

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.graphics.drawable.toBitmap
import coil.ImageLoader
import coil.request.ImageRequest
import com.emranul.weatherupdate.MainActivity
import com.emranul.weatherupdate.R
import com.emranul.weatherupdate.core.domain.model.WeatherData
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import javax.inject.Inject

class NotificationUtil @Inject constructor(
    @ApplicationContext private val context: Context,
    private val notificationManager: NotificationManager
) {

    /**
     * Creating channel for notification
     * */
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel =
                NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, importance)
            channel.description = "This channel is used for update weather notifications."
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showNotification(
        weatherData: WeatherData
    ) {
        Timber.d("NotificationUtil ----------> Show Notification")
        createNotificationChannel()

        val notificationBuilder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
        } else {
            NotificationCompat.Builder(context)
        }

        notificationBuilder.setSmallIcon(R.drawable.ic_weather)
        notificationBuilder.setContentTitle(weatherData.name)
        notificationBuilder.setContentText("Current Temperature ${weatherData.main?.temperature()}°c")
        notificationBuilder.setAutoCancel(true)

        val intent = Intent(context, MainActivity::class.java)

        val pendingIntent =
            PendingIntent.getActivity(
                context,
                100,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )

        notificationBuilder.setContentIntent(pendingIntent)

        try {
            val imageLoader = ImageLoader(context)
            val request = ImageRequest.Builder(context)
                .data("https://openweathermap.org/img/wn/${weatherData.getWeather()?.icon}@4x.png")
                .target { drawable ->
                    Timber.d("NotificationUtil ----------> Image Loaded")
                    notificationBuilder.setLargeIcon(drawable.toBitmap())
                    notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
                }
                .build()
            imageLoader.enqueue(request)
        } catch (e: Exception) {
            Timber.d("NotificationUtil ----------> Image Failed ${e.message}")
            notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
        }

    }

    companion object {
        private const val NOTIFICATION_ID = 1
        private const val NOTIFICATION_CHANNEL_ID = "weather_update"
        private const val NOTIFICATION_CHANNEL_NAME = "Weather Update"
    }
}
