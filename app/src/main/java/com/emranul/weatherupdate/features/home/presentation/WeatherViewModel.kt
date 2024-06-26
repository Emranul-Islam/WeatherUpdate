package com.emranul.weatherupdate.features.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emranul.weatherupdate.core.domain.model.WeatherData
import com.emranul.weatherupdate.features.home.domain.model.WeathersPayload
import com.emranul.weatherupdate.features.home.domain.useCases.WeathersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class WeatherViewModel @Inject constructor(
    private val weathersUseCase: WeathersUseCase
) : ViewModel() {

    private val _navigateTo = Channel<HomeNavigation>(Channel.BUFFERED)
    val navigateTo = _navigateTo.receiveAsFlow()

    private val _weathers =
        MutableSharedFlow<WeathersPayload>(
            replay = 1,
            onBufferOverflow = BufferOverflow.DROP_OLDEST
        )

    val weathers = _weathers.flatMapLatest {
        weathersUseCase(it)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)


    init {
        Timber.d("Api Call -------------------------->")
        _weathers.tryEmit(
            WeathersPayload(
                "23.68",
                "90.35"
            )
        )
    }

    fun navigateTo(homeNavigation: HomeNavigation) {
        _navigateTo.trySend(homeNavigation)
    }

    fun navigateToDetails(item: WeatherData) {
        navigateTo(HomeNavigation.NavigateToWeatherDetails(item))
    }

}

sealed interface HomeNavigation {
    data class NavigateToWeatherDetails(val item: WeatherData) : HomeNavigation
}
