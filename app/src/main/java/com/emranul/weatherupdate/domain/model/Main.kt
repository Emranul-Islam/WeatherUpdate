package com.emranul.weatherupdate.domain.model


import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize


@Parcelize
data class Main(
    @Json(name = "feels_like")
    val feelsLike: Double?,
    @Json(name = "grnd_level")
    val grndLevel: Int?,
    @Json(name = "humidity")
    val humidity: Int?,
    @Json(name = "pressure")
    val pressure: Int?,
    @Json(name = "sea_level")
    val seaLevel: Int?,
    @Json(name = "temp")
    val temp: Double?,
    @Json(name = "temp_max")
    val tempMax: Double?,
    @Json(name = "temp_min")
    val tempMin: Double?
):Parcelable{
    fun temperature()= temp?.toInt()?:0
    fun temperatureMax()= tempMax?.toInt()?:0
    fun temperatureMin()= tempMin?.toInt()?:0
}