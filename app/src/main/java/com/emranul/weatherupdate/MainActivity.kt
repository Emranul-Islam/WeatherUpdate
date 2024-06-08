package com.emranul.weatherupdate

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.emranul.weatherupdate.core.base.ui.BaseActivity
import com.emranul.weatherupdate.core.base.util.findNavControllerByFragmentContainerView
import com.emranul.weatherupdate.databinding.ActivityMainBinding
import com.emranul.weatherupdate.workManager.WeatherReminderWorker
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {


    private val mNavController by lazy { findNavControllerByFragmentContainerView(R.id.fragmentContainerView) }



    private val requestPermissionContract =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {results ->
            val allAllowed = results.all { it.value }
            if (allAllowed) {
                getCurrentLatLon()
            }else{
                Toast.makeText(
                    this,
                    "Give all permission to got weather update notification",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        handlePermission()

    }

    private fun handlePermission() {

        val listOfPermission = mutableListOf<String>()

        listOfPermission.add(Manifest.permission.ACCESS_FINE_LOCATION)
        listOfPermission.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            listOfPermission.add(Manifest.permission.POST_NOTIFICATIONS)
        }

        val notAllowedPermission =  listOfPermission.filter {
            ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_DENIED
        }

        if (notAllowedPermission.isNotEmpty()) {
            Toast.makeText(
                this,
                "Give all permission to got weather update notification",
                Toast.LENGTH_SHORT
            ).show()
            requestPermissionContract.launch(notAllowedPermission.toTypedArray())
        }else{
            getCurrentLatLon()
        }

    }
    private fun getCurrentLatLon() {

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }

        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
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


        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

//        WorkManager.getInstance(this).cancelAllWorkByTag("weatherReminderWork")

        val periodicWorkRequest =
            PeriodicWorkRequestBuilder<WeatherReminderWorker>(1, TimeUnit.HOURS)
                .setInputData(data)
                .setConstraints(constraints)
                .setBackoffCriteria(
                    BackoffPolicy.LINEAR,
                    WorkRequest.MIN_BACKOFF_MILLIS,
                    TimeUnit.MILLISECONDS
                )
                .addTag("weatherReminderWork")
                .build()

//        WorkManager.getInstance(applicationContext).enqueue(periodicWorkRequest)
        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            "weatherReminderWork",
            ExistingPeriodicWorkPolicy.KEEP,
            periodicWorkRequest
        )

    }


    companion object {
        private val TOP_LEVEL_DESTINATIONS = setOf(
            R.id.homeFragment,
        )
    }

    override fun registerToolbarWithNavigation(toolbar: Toolbar) {
        val appBarConfiguration = AppBarConfiguration(TOP_LEVEL_DESTINATIONS)
        toolbar.setupWithNavController(mNavController, appBarConfiguration)
    }


}