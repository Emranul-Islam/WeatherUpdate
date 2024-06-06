package com.emranul.weatherupdate.core.data

import com.emranul.weatherupdate.core.domain.model.WeatherData
import com.emranul.weatherupdate.network.Result
import com.emranul.weatherupdate.network.ApiInterface
import com.emranul.weatherupdate.network.ControlledRunner
import com.emranul.weatherupdate.network.NetworkResource
import com.emranul.weatherupdate.core.domain.model.Weathers
import com.emranul.weatherupdate.features.home.domain.model.WeathersPayload
import com.emranul.weatherupdate.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class CurrentWeatherRepository @Inject constructor(
    @IoDispatcher val dispatcher: CoroutineDispatcher,
    private val apiInterface: ApiInterface
) {
    private val controlledRunner = ControlledRunner<Flow<Result<WeatherData>>>()

    suspend fun fetchCurrentWeather(params: WeathersPayload): Flow<Result<WeatherData>> {
        return controlledRunner.cancelPreviousThenRun {
            object : NetworkResource<WeatherData>(dispatcher) {
                override suspend fun createCall() = apiInterface.fetchCurrentWeather(
                    params.lat,
                    params.lon,
                    "e384f9ac095b2109c751d95296f8ea76"
                )
            }.asFlow()
        }
    }

}
