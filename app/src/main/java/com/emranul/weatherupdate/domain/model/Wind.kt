package com.emranul.weatherupdate.domain.model


import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Parcelize
data class Wind(
    @Json(name = "deg")
    val deg: Int?,
    @Json(name = "speed")
    val speed: Double?
):Parcelable