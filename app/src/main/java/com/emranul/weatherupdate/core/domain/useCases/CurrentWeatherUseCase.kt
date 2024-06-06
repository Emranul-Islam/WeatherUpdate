package com.emranul.weatherupdate.core.domain.useCases

import com.emranul.weatherupdate.core.data.CurrentWeatherRepository
import com.emranul.weatherupdate.core.domain.model.WeatherData
import com.emranul.weatherupdate.di.IoDispatcher
import com.emranul.weatherupdate.features.home.domain.model.WeathersPayload
import com.emranul.weatherupdate.network.FlowUseCase
import com.emranul.weatherupdate.network.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CurrentWeatherUseCase @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val repository: CurrentWeatherRepository,
) : FlowUseCase<WeathersPayload, WeatherData>(ioDispatcher) {
    override suspend fun execute(parameters: WeathersPayload): Flow<Result<WeatherData>> =
        repository.fetchCurrentWeather(parameters)
}
