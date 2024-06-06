package com.emranul.weatherupdate.domain.useCases

import com.emranul.weatherupdate.api.FlowUseCase
import com.emranul.weatherupdate.api.Result
import com.emranul.weatherupdate.data.WeathersRepository
import com.emranul.weatherupdate.domain.model.Weathers
import com.emranul.weatherupdate.domain.model.WeathersPayload
import com.emranul.weatherupdate.hilt.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WeathersUseCase @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val repository: WeathersRepository,
) : FlowUseCase<WeathersPayload, Weathers>(ioDispatcher) {
    override suspend fun execute(parameters: WeathersPayload): Flow<Result<Weathers>> =
        repository.fetchOperatingProfile(parameters)
}
