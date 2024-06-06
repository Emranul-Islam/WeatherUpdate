package com.emranul.weatherupdate.domain.model


import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Parcelize
data class Clouds(
    @Json(name = "all")
    val all: Int?
):Parcelable