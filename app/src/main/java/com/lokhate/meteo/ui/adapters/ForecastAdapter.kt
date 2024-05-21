package com.lokhate.meteo.ui.adapters

import android.annotation.SuppressLint
import android.content.DialogInterface.OnClickListener
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.lokhate.meteo.data.model.Forecast
import com.lokhate.meteo.databinding.ListItemForecastBinding
import com.lokhate.meteo.utils.ForecastHelper
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.util.logging.Logger

class ForecastAdapter(
    private val onItemClicked: (Forecast) -> Unit
): ListAdapter<Forecast, ForecastAdapter.ForecastViewHolder>(ForecastDiffCallback()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ForecastAdapter.ForecastViewHolder {
        val binding = ListItemForecastBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ForecastViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        val forecast = getItem(position)
        holder.itemView.setOnClickListener {
            onItemClicked(forecast)
        }
        holder.bind(forecast, position)
    }

    class ForecastViewHolder(private val binding: ListItemForecastBinding): RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SimpleDateFormat")
        fun bind(forecast: Forecast, position: Int) {
            val date = SimpleDateFormat("yyyy-MM-dd").parse(forecast.day)
            var output = date?.let { SimpleDateFormat("EEEE, MMMM dd yyyy").format(it) }
            output = output?.let { it.replaceFirstChar { c -> c.uppercase() } }
            binding.dayTextView.text = output
            binding.maxTempTextView.text = String.format("%.1f°C", forecast.temperatureMax)
            binding.minTempTextView.text = String.format("%.1f°C", forecast.temperatureMin)

            val backgroundColor = if (position % 2 == 0) Color.LTGRAY else Color.GRAY
            binding.constraintLayout.setBackgroundColor(backgroundColor)
            binding.weatherImage.setImageResource(ForecastHelper.getWeatherImage(forecast.weatherCode))
        }
    }
}

class ForecastDiffCallback : DiffUtil.ItemCallback<Forecast>() {
    override fun areItemsTheSame(oldItem: Forecast, newItem: Forecast): Boolean {
        return oldItem.day == newItem.day
    }

    override fun areContentsTheSame(oldItem: Forecast, newItem: Forecast): Boolean {
        return oldItem == newItem
    }
}