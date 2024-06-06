package com.emranul.weatherupdate.api

import com.emranul.weatherupdate.domain.model.Weathers
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    //    @GET("data/2.5/find?lat=23.68&lon=90.35&cnt=50&appid=e384f9ac095b2109c751d95296f8ea76")
    @GET("data/2.5/find")
    fun fetchWeathers(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("cnt") count: Int,
        @Query("appid") appId: String,
        @Query("units") unit: String = "metric"
    ): Flow<ApiResponse<Weathers>>
//
//    @GET("/news-list")
//    fun fetchNewsList(): Flow<ApiResponse<NewsFeed>>
//
//    @POST("api_v2/get-sliders")
//    fun fetchBannerData(): Flow<ApiResponse<Banner>>

}
