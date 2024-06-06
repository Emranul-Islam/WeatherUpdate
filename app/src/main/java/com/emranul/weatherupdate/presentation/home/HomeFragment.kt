package com.emranul.weatherupdate.presentation.home


import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.emranul.weatherupdate.R
import com.emranul.weatherupdate.base.ui.BaseFragment
import com.emranul.weatherupdate.base.util.launchAndRepeatWithViewLifecycle
import com.emranul.weatherupdate.base.util.navigate
import com.emranul.weatherupdate.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    private val viewModel: WeatherViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel


        launchAndRepeatWithViewLifecycle {
            launch {
                viewModel.navigateTo.collect {
                    when (it) {
                        is HomeNavigation.NavigateToWeatherDetails -> navigate(
                            HomeFragmentDirections.actionHomeFragmentToWeatherDetailsFragment(it.item)
                        )
                    }
                }
            }
        }

    }

}