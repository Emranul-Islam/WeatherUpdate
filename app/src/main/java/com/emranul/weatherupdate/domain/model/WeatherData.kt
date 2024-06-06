package com.emranul.weatherupdate.domain.model


import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize


@Parcelize
data class WeatherData(
    @Json(name = "clouds")
    val clouds: Clouds?,
    @Json(name = "coord")
    val coord: Coord?,
    @Json(name = "dt")
    val dt: Int?,
    @Json(name = "id")
    val id: Int?,
    @Json(name = "main")
    val main: Main?,
    @Json(name = "name")
    val name: String?,
    @Json(name = "weather")
    val weathers: List<Weather>?,
    @Json(name = "wind")
    val wind: Wind?
):Parcelable{

    fun getWeather() = weathers?.getOrNull(0)
}