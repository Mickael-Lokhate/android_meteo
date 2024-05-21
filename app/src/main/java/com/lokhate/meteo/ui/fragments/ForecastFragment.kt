package com.lokhate.meteo.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.lokhate.meteo.R
import com.lokhate.meteo.data.model.Forecast
import com.lokhate.meteo.databinding.FragmentForecastBinding
import com.lokhate.meteo.ui.adapters.ForecastAdapter
import com.lokhate.meteo.ui.viewmodel.ForecastViewModel
import com.lokhate.meteo.utils.SharedPreferencesHelper

class ForecastFragment: Fragment() {
    private lateinit var viewModel: ForecastViewModel
    private lateinit var binding: FragmentForecastBinding
    private lateinit var adapter: ForecastAdapter
    private lateinit var sharedPrefs: SharedPreferencesHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("ForecastFragment", "Create view")
        binding = FragmentForecastBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity()).get(ForecastViewModel::class.java)
        if (this.context != null) {
            sharedPrefs = SharedPreferencesHelper.getInstance(this.requireContext())
        }

        adapter = ForecastAdapter {
            val fragmentManager = parentFragmentManager
            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(
                R.id.forecastFragment,
                ForecastDetails(it, sharedPrefs.getDouble("latitude"), sharedPrefs.getDouble("longitude"), sharedPrefs.getString("timezone") ?: "Europe/Paris")
            )
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)

        viewModel.forecast.observe(viewLifecycleOwner, Observer {
            Log.d("ForecastFragment", "forecast updated")
            it?.let { adapter.submitList(it) }
        })

        val city = sharedPrefs.getString("city")
        val country = sharedPrefs.getString("country")
        binding.locationText.text = "$city, $country"

        return binding.root
    }
}