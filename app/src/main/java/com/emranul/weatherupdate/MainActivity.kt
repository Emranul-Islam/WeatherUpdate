package com.emranul.weatherupdate

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.emranul.weatherupdate.core.base.ui.BaseActivity
import com.emranul.weatherupdate.core.base.util.findNavControllerByFragmentContainerView
import com.emranul.weatherupdate.core.domain.useCases.DailyWeatherNotifyUseCase
import com.emranul.weatherupdate.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {


    private val mNavController by lazy { findNavControllerByFragmentContainerView(R.id.fragmentContainerView) }


    val dailyWeatherNotifyUseCase = DailyWeatherNotifyUseCase(this)



    private val requestPermissionContract =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {results ->
            val allAllowed = results.all { it.value }
            if (allAllowed) {
                dailyWeatherNotifyUseCase.invoke()
            }else{
                Toast.makeText(
                    this,
                    "Give all permission to got weather update notification",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
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
            dailyWeatherNotifyUseCase.invoke()
        }

    }

//    private fun getCurrentLatLon() {
//
//        if (ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//
//            return
//        }
//
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
//        fusedLocationClient.lastLocation.addOnSuccessListener {
//            Timber.d("fusedLocationClient success ${it.latitude}")
//            executeWorkManager(it)
//        }.addOnFailureListener {
//            Timber.d("fusedLocationClient error ----------> ${it.message}")
//        }
//    }


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