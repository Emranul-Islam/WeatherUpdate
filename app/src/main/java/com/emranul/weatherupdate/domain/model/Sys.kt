package com.emranul.weatherupdate.domain.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import androidx.annotation.Keep


data class Sys(
    @Json(name = "country")
    val country: String?
)