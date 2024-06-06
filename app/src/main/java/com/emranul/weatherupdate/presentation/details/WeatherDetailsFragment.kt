package com.emranul.weatherupdate.presentation.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.emranul.weatherupdate.R
import com.emranul.weatherupdate.base.ui.BaseFragment
import com.emranul.weatherupdate.base.util.launchAndRepeatWithViewLifecycle
import com.emranul.weatherupdate.databinding.FragmentWeatherDetailsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.ktx.awaitMap
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WeatherDetailsFragment : BaseFragment<FragmentWeatherDetailsBinding>(R.layout.fragment_weather_details) {

    private val args : WeatherDetailsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.item = args.weatherData


        val supportMapFragment =
            childFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment?
        supportMapFragment?.getMapAsync {map->
            val latLng = LatLng(
                args.weatherData.coord?.lat?:0.0,
                args.weatherData.coord?.lon?:0.0,
            )
            map.addMarker(
                MarkerOptions().position(latLng)
            )
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,13f))
        }

    }

}