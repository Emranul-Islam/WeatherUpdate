package com.emranul.weatherupdate.core.domain.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import androidx.annotation.Keep

data class Rain(
    @Json(name = "1h")
    val h: Double?
)