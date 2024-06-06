package com.emranul.weatherupdate.features.home.domain.useCases

import com.emranul.weatherupdate.network.FlowUseCase
import com.emranul.weatherupdate.network.Result
import com.emranul.weatherupdate.features.home.data.WeathersRepository
import com.emranul.weatherupdate.core.domain.model.Weathers
import com.emranul.weatherupdate.features.home.domain.model.WeathersPayload
import com.emranul.weatherupdate.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WeathersUseCase @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val repository: WeathersRepository,
) : FlowUseCase<WeathersPayload, Weathers>(ioDispatcher) {
    override suspend fun execute(parameters: WeathersPayload): Flow<Result<Weathers>> =
        repository.fetchWeathers(parameters)
}
