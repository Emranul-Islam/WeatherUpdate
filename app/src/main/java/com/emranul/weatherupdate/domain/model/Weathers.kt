package com.emranul.weatherupdate.domain.model


import com.squareup.moshi.Json


data class Weathers(
    @Json(name = "cod")
    val cod: String?,
    @Json(name = "count")
    val count: Int?,
    @Json(name = "list")
    val list: List<WeatherData>?,
    @Json(name = "message")
    val message: String?
)