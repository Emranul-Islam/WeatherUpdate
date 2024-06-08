package com.emranul.weatherupdate.features.details

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.emranul.weatherupdate.R
import com.emranul.weatherupdate.core.base.ui.BaseFragment
import com.emranul.weatherupdate.databinding.FragmentWeatherDetailsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WeatherDetailsFragment : BaseFragment<FragmentWeatherDetailsBinding>(R.layout.fragment_weather_details) {

    override val hasToolbar: Boolean = true
    override val resToolbarId = R.id.toolbar

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