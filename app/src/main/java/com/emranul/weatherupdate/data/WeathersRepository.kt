package com.emranul.weatherupdate.data

import com.emranul.weatherupdate.api.Result
import com.emranul.weatherupdate.api.ApiInterface
import com.emranul.weatherupdate.api.ControlledRunner
import com.emranul.weatherupdate.api.NetworkResource
import com.emranul.weatherupdate.domain.model.Weathers
import com.emranul.weatherupdate.domain.model.WeathersPayload
import com.emranul.weatherupdate.hilt.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class WeathersRepository @Inject constructor(
    @IoDispatcher val dispatcher: CoroutineDispatcher,
    private val apiInterface: ApiInterface
) {
    private val controlledRunner = ControlledRunner<Flow<Result<Weathers>>>()

    suspend fun fetchOperatingProfile(params: WeathersPayload): Flow<Result<Weathers>> {
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
