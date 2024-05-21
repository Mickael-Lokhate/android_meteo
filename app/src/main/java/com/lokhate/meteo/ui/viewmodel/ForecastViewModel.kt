package com.lokhate.meteo.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lokhate.meteo.data.model.Forecast
import com.lokhate.meteo.data.model.ForecastApi
import com.lokhate.meteo.data.model.ForecastHourly
import com.lokhate.meteo.data.model.ForecastHourlyApi
import com.lokhate.meteo.data.repository.ForecastRepository
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForecastViewModel: ViewModel() {
    private val forecastRepository = ForecastRepository()
    private val _forecastAPI = MutableLiveData<ForecastApi>()
    private val _forecastHourlyAPI = MutableLiveData<ForecastHourlyApi>()
    private val _forecastHourly = MutableLiveData<List<ForecastHourly>>()
    private val _forecast = MutableLiveData<List<Forecast>>()
    private val _city = MutableLiveData<String>()
    private val _country = MutableLiveData<String>()
    val forecastAPI: LiveData<ForecastApi> get() = _forecastAPI
    val forecastHourlyAPI: LiveData<ForecastHourlyApi> get() = _forecastHourlyAPI
    val forecast: LiveData<List<Forecast>> get() = _forecast
    val forecastHourly: LiveData<List<ForecastHourly>> get() = _forecastHourly
    val city: LiveData<String> = _city
    val country: LiveData<String> = _country

    fun fetchForecast(latitude: Double, longitude: Double, timezone: String, city: String, country: String) {
        viewModelScope.launch {
            forecastRepository.getForecast(latitude, longitude, timezone).enqueue(object: Callback<ForecastApi> {
                override fun onResponse(call: Call<ForecastApi>, response: Response<ForecastApi>) {
                    if (response.isSuccessful) {
                        val body: ForecastApi? = response.body()
                        if (body != null) {
                            _forecastAPI.postValue(body as ForecastApi)
                            _forecast.postValue(convertApiData(body))
                            _city.postValue(city)
                            _country.postValue(country)
                        }
                    } else {
                        println("Response not successful")
                    }
                }

                override fun onFailure(call: Call<ForecastApi>, t: Throwable) {
                    println("API call failed ${t.message}")
                }
            })
        }
    }

    fun fetchForecastHourly(latitude: Double, longitude: Double, timezone: String, startDate: String, endDate: String) {
        Log.d("ViewModel", "Fetch hourly forecast")
        viewModelScope.launch {
            forecastRepository.getForecastHourly(latitude, longitude, timezone, startDate, endDate).enqueue(object: Callback<ForecastHourlyApi> {
                override fun onResponse(call: Call<ForecastHourlyApi>, response: Response<ForecastHourlyApi>) {
                    if (response.isSuccessful) {
                        val body: ForecastHourlyApi? = response.body()
                        if (body != null) {
                             _forecastHourlyAPI.postValue(body as ForecastHourlyApi)
                            _forecastHourly.postValue(convertApiDataHourly(body))
                        }
                    } else {
                        println("Response not successful")
                    }
                }

                override fun onFailure(call: Call<ForecastHourlyApi>, t: Throwable) {
                    println("API call failed ${t.message}")
                }
            })
        }
    }

    private fun convertApiData(forecast: ForecastApi): List<Forecast> {
        val forecastResult: MutableList<Forecast> = mutableListOf<Forecast>()
        forecast.daily.time.forEachIndexed { index, time ->
            val daily = forecast.daily
            forecastResult.add(Forecast(day = time, weatherCode = daily.weather_code[index], temperatureMax = daily.temperature_2m_max[index], temperatureMin = daily.temperature_2m_min[index], precipitation = daily.precipitation_sum[index], precipitationProbability = daily.precipitation_probability_max[index]))
        }
        return forecastResult
    }

    private fun convertApiDataHourly(forecast: ForecastHourlyApi): List<ForecastHourly> {
        val forecastResult: MutableList<ForecastHourly> = mutableListOf<ForecastHourly>()
        forecast.hourly.time.forEachIndexed { index, time ->
            val hourly = forecast.hourly
            forecastResult.add(ForecastHourly(time=time, weatherCode = hourly.weather_code[index], temperature = hourly.temperature_2m[index], relativeHumidity = hourly.relative_humidity_2m[index], precipitationProbability = hourly.precipitation_probability[index], precipitation = hourly.precipitation[index]))
        }
        return forecastResult
    }
}