package com.lokhate.meteo.data.repository

import com.lokhate.meteo.data.model.ForecastApi
import com.lokhate.meteo.data.model.ForecastHourly
import com.lokhate.meteo.data.model.ForecastHourlyApi
import com.lokhate.meteo.data.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback

class ForecastRepository {
    private val api = RetrofitClient.ApiClient.apiService

    fun getForecast(latitude: Double, longitude: Double, timezone: String): Call<ForecastApi> {
        return api.getForecast(
            latitude = latitude,
            longitude = longitude,
            daily = "weather_code,temperature_2m_max,temperature_2m_min,precipitation_sum,precipitation_probability_max",
            timezone = timezone
        )
    }

    fun getForecastHourly(latitude: Double, longitude: Double, timezone: String, startDate: String, endDate: String): Call<ForecastHourlyApi> {
        return api.getForecastHourly(
            latitude = latitude,
            longitude = longitude,
            hourly = "temperature_2m,relative_humidity_2m,precipitation_probability,precipitation,weather_code",
            timezone = timezone,
            startDate = startDate,
            endDate = endDate
        )
    }
}