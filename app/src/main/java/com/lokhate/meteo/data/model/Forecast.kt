package com.lokhate.meteo.data.model

data class Forecast(
    val day: String,
    val weatherCode: Int,
    val temperatureMax: Double,
    val temperatureMin: Double,
    val precipitation: Double,
    val precipitationProbability: Int,
)

data class ForecastHourly(
    val time: String,
    val weatherCode: Int,
    val temperature: Double,
    val relativeHumidity: Int,
    val precipitationProbability: Int,
    val precipitation: Double
)