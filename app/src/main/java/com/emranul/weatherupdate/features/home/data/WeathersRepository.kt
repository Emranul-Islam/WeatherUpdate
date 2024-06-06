package com.emranul.weatherupdate.features.home.data

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


class WeathersRepository @Inject constructor(
    @IoDispatcher val dispatcher: CoroutineDispatcher,
    private val apiInterface: ApiInterface
) {
    private val controlledRunner = ControlledRunner<Flow<Result<Weathers>>>()

    suspend fun fetchWeathers(params: WeathersPayload): Flow<Result<Weathers>> {
        return controlledRunner.cancelPreviousThenRun {
            object : NetworkResource<Weathers>(dispatcher) {
                override suspend fun createCall() = apiInterface.fetchWeathers(
                    params.lat,
                    params.lon,
                    50,
                    "e384f9ac095b2109c751d95296f8ea76"
                )
            }.asFlow()
        }
    }

}
