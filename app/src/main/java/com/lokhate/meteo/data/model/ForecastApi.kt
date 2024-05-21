package com.lokhate.meteo.data.model

data class ForecastApi(
    val daily: DailyWeather,
    val daily_units: DailyUnits
)

data class ForecastHourlyApi(
    val hourly: HourlyWeather
)

data class DailyWeather(
    val time: List<String>,
    val weather_code: List<Int>,
    val temperature_2m_max: List<Double>,
    val temperature_2m_min: List<Double>,
    val precipitation_sum: List<Double>,
    val precipitation_probability_max: List<Int>,
)

data class DailyUnits(
    val time: String,
    val weather_code: String,
    val temperature_2m_max: String,
    val temperature_2m_min: String,
    val precipitation_sum: String,
    val precipitation_probability_max: String
)

data class HourlyWeather(
    val time: List<String>,
    val weather_code: List<Int>,
    val temperature_2m: List<Double>,
    val relative_humidity_2m: List<Int>,
    val precipitation_probability: List<Int>,
    val precipitation: List<Double>
)