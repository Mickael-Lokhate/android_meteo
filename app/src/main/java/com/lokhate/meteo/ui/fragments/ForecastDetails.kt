package com.lokhate.meteo.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import com.lokhate.meteo.R
import com.lokhate.meteo.data.model.Forecast
import com.lokhate.meteo.databinding.FragmentForecastDetailsBinding
import com.lokhate.meteo.ui.viewmodel.ForecastViewModel
import com.lokhate.meteo.utils.ForecastHelper
import com.lokhate.meteo.utils.SharedPreferencesHelper
import java.text.SimpleDateFormat

class ForecastDetails(
    private val forecast: Forecast,
    private val latitude: Double,
    private val longitude: Double,
    private val timezone: String,
) : Fragment() {
    private lateinit var binding: FragmentForecastDetailsBinding
    private lateinit var sharedPrefs: SharedPreferencesHelper
    private val forecastViewModel: ForecastViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("SimpleDateFormat")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("ForecastDetails", "setting up details view")
        sharedPrefs = SharedPreferencesHelper.getInstance(this.requireContext())
        val city = sharedPrefs.getString("city")
        val country = sharedPrefs.getString("country")
        binding = FragmentForecastDetailsBinding.inflate(inflater, container, false)

        val date = SimpleDateFormat("yyyy-MM-dd").parse(forecast.day)
        var outputDate = date?.let { SimpleDateFormat("EEEE, MMMM dd yyyy").format(it) }
        outputDate = outputDate?.let { it.replaceFirstChar { c -> c.uppercase() }}
        binding.locationText.text = "${city}, ${country}"
        binding.dateText.text = outputDate
        binding.temp.text = "Min: ${forecast.temperatureMin}°C Max: ${forecast.temperatureMax}°C"
        binding.weatherText.text = ForecastHelper.getWeatherFromCode(this.requireContext(), forecast.weatherCode)
        binding.precipitation.text = "Precipitation: ${forecast.precipitation}mm (${forecast.precipitationProbability}%)"
        binding.weatherImage.setImageResource(ForecastHelper.getWeatherImage(forecast.weatherCode))
        binding.hourlyButton.setOnClickListener {
            Log.d("ForecastDetails", "Click on see details")
            val fragmentManager = parentFragmentManager
            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(
                R.id.forecastFragment,
                HourlyForecastFragment(latitude, longitude, timezone, forecast.day)
            )
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        // Inflate the layout for this fragment
        return binding.root
    }

}