package com.emranul.weatherupdate.core.base.util

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load
import com.emranul.weatherupdate.R

@BindingAdapter("bindWeatherImage")
fun ImageView.bindWeatherImage(status:String?){
    load("https://openweathermap.org/img/wn/$status@4x.png"){     //this is url from OpenWeather API for load image of current weather
        placeholder(R.drawable.ic_weather)
        error(R.drawable.ic_weather)
    }
}

@BindingAdapter("goneUnless")
fun View.goneUnless(condition: Boolean) {
    if (condition.not()) this.visibility = View.GONE
    else this.visibility = View.VISIBLE
}
