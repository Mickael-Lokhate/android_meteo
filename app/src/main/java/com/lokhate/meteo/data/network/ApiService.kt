package com.lokhate.meteo.data.network

import com.lokhate.meteo.data.model.ForecastApi
import com.lokhate.meteo.data.model.ForecastHourlyApi
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("forecast")
    fun getForecast(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("daily") daily: String,
        @Query("timezone") timezone: String,
    ): Call<ForecastApi>

    @GET("forecast")
    fun getForecastHourly(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("hourly") hourly: String,
        @Query("timezone") timezone: String,
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String
    ): Call<ForecastHourlyApi>
}