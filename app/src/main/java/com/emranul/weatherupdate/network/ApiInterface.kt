package com.emranul.weatherupdate.network

import com.emranul.weatherupdate.core.domain.model.WeatherData
import com.emranul.weatherupdate.core.domain.model.Weathers
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("data/2.5/find")
    fun fetchWeathers(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("cnt") count: Int,
        @Query("appid") appId: String,
        @Query("units") unit: String = "metric"
    ): Flow<ApiResponse<Weathers>>

    @GET("data/2.5/weather")
    fun fetchCurrentWeather(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("appid") appId: String,
        @Query("units") unit: String = "metric"
    ): Flow<ApiResponse<WeatherData>>


}
