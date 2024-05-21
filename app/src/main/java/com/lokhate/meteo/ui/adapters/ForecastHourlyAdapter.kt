package com.lokhate.meteo.ui.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lokhate.meteo.data.model.Forecast
import com.lokhate.meteo.data.model.ForecastHourly
import com.lokhate.meteo.databinding.ListItemForecastHourlyBinding
import com.lokhate.meteo.utils.ForecastHelper
import java.text.SimpleDateFormat

class ForecastHourlyAdapter(private val context: Context): ListAdapter<ForecastHourly, ForecastHourlyAdapter.ForecastHourlyViewHolder>(ForecastHourlyDiffCallback()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ForecastHourlyAdapter.ForecastHourlyViewHolder {
        val binding = ListItemForecastHourlyBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ForecastHourlyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ForecastHourlyViewHolder, position: Int) {
        val forecastHourly = getItem(position)
        holder.bind(forecastHourly, position, context)
    }

    class ForecastHourlyViewHolder(private val binding: ListItemForecastHourlyBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(forecastHourly: ForecastHourly, position: Int, context: Context) {
            Log.d("ForecastHourlyAdapter", forecastHourly.time)
            val date = SimpleDateFormat("yyyy-MM-dd HH:mm").parse(forecastHourly.time.replace("T", " "))
            val dateResult = date?.let { SimpleDateFormat("dd/MM/YYYY HH:mm").format(it) }
            binding.timeText.text = dateResult
            binding.humidityText.text = "Humidity: ${forecastHourly.relativeHumidity}%"
            binding.precipitationText.text = "Precipitation: ${forecastHourly.precipitation}mm (${forecastHourly.precipitationProbability}%)"
            binding.weatherText.text = ForecastHelper.getWeatherFromCode(context, forecastHourly.weatherCode)
            binding.weatherImage.setImageResource(ForecastHelper.getWeatherImage(forecastHourly.weatherCode))
            binding.tempText.text = "${forecastHourly.temperature}°C"
        }
    }
}

class ForecastHourlyDiffCallback : DiffUtil.ItemCallback<ForecastHourly>() {
    override fun areItemsTheSame(oldItem: ForecastHourly, newItem: ForecastHourly): Boolean {
        return oldItem.time == newItem.time
    }

    override fun areContentsTheSame(oldItem: ForecastHourly, newItem: ForecastHourly): Boolean {
        return oldItem == newItem
    }
}