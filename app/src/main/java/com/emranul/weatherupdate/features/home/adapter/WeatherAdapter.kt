package com.emranul.weatherupdate.features.home.adapter

import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.emranul.weatherupdate.core.base.util.layoutInflater
import com.emranul.weatherupdate.core.domain.model.WeatherData
import com.emranul.weatherupdate.databinding.ItemWeatherBinding
import com.emranul.weatherupdate.features.home.presentation.WeatherViewModel

class WeatherAdapter(private val viewModel: WeatherViewModel) :
    ListAdapter<WeatherData, WeatherAdapter.WeatherViewHolder>(
        WeatherDiffer
    ) {

    object WeatherDiffer : DiffUtil.ItemCallback<WeatherData>() {
        override fun areItemsTheSame(oldItem: WeatherData, newItem: WeatherData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: WeatherData, newItem: WeatherData): Boolean {
            return oldItem == newItem
        }
    }

    class WeatherViewHolder(private val binding: ItemWeatherBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(viewModel: WeatherViewModel, item: WeatherData) {
            binding.viewModel = viewModel
            binding.item = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        return WeatherViewHolder(
            ItemWeatherBinding.inflate(
                parent.layoutInflater, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        holder.bindData(viewModel, getItem(position))
    }

}

@BindingAdapter(value = ["bindViewModel", "bindWeatherList"], requireAll = true)
fun RecyclerView.bindWeatherData(viewModel: WeatherViewModel, list: List<WeatherData>?) {
    if (adapter == null) adapter = WeatherAdapter(viewModel)
    val data = list ?: emptyList()
    (adapter as? WeatherAdapter)?.submitList(data)
}
