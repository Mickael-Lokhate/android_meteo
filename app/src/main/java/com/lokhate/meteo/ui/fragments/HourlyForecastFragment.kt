package com.lokhate.meteo.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.lokhate.meteo.R
import com.lokhate.meteo.data.model.Forecast
import com.lokhate.meteo.data.model.ForecastHourly
import com.lokhate.meteo.databinding.FragmentHourlyForecastBinding
import com.lokhate.meteo.ui.adapters.ForecastHourlyAdapter
import com.lokhate.meteo.ui.viewmodel.ForecastViewModel
import com.lokhate.meteo.utils.SharedPreferencesHelper
import java.util.TimeZone

class HourlyForecastFragment(
    private val latitude: Double,
    private val longitude: Double,
    private val timezone: String,
    private val day: String
) : Fragment() {
    private lateinit var sharedPrefs: SharedPreferencesHelper
    private lateinit var binding: FragmentHourlyForecastBinding
    private val forecastViewModel: ForecastViewModel by viewModels()
    private var forecastHourly: List<ForecastHourly> = emptyList()
    private lateinit var adapter: ForecastHourlyAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        forecastViewModel.forecastHourly.observe(this, Observer { forecast: List<ForecastHourly> ->
            forecastHourly = forecast
        })

        forecastViewModel.fetchForecastHourly(latitude, longitude, timezone, day, day)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedPrefs = SharedPreferencesHelper.getInstance(this.requireContext())
        val city = sharedPrefs.getString("city")
        val country = sharedPrefs.getString("country")
        binding = FragmentHourlyForecastBinding.inflate(inflater, container, false)

        adapter = ForecastHourlyAdapter(this.requireContext())
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)

        binding.cityCountryText.text = if (city != "Unknown" ) "$city" else "$country"

        forecastViewModel.forecastHourly.observe(viewLifecycleOwner, Observer {
            it?.let { adapter.submitList(it) }
        })

        return binding.root
    }
}