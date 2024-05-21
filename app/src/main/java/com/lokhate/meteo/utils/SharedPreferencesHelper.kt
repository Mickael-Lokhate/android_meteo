package com.lokhate.meteo.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lokhate.meteo.data.model.Forecast

class SharedPreferencesHelper private constructor(context: Context){

    companion object {
        private const val NAME = "forecast_app"
        private const val FORECAST_LIST_KEY = "forecast_list"
        private var instance: SharedPreferencesHelper? = null

        fun getInstance(context: Context): SharedPreferencesHelper {
            if (instance == null) {
                instance = SharedPreferencesHelper(context.applicationContext)
            }
            return instance!!
        }
    }

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE)
    private val gson = Gson()

    fun putForecastList(forecastList: List<Forecast>) {
        val json = gson.toJson(forecastList)
        sharedPreferences.edit().putString(FORECAST_LIST_KEY, json).apply()
    }

    fun getForecastList(): List<Forecast> {
        val json = sharedPreferences.getString(FORECAST_LIST_KEY, null) ?: return emptyList()
        val type = object : TypeToken<List<Forecast>>() {}.type
        return gson.fromJson(json, type)
    }

    fun putString(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }
    fun getString(key: String, defaultValue: String? = null): String? {
        return sharedPreferences.getString(key, defaultValue)
    }

    fun putDouble(key: String, value: Double) {
        sharedPreferences.edit().putLong(key, java.lang.Double.doubleToRawLongBits(value)).apply()
    }
    fun getDouble(key: String, defaultValue: Double = 0.0): Double {
        return java.lang.Double.longBitsToDouble(sharedPreferences.getLong(key, java.lang.Double.doubleToRawLongBits(defaultValue)))
    }

    fun remove(key: String) {
        sharedPreferences.edit().remove(key).apply()
    }
    fun clear() {
        sharedPreferences.edit().clear().apply()
    }
    fun clearForecastList() {
        sharedPreferences.edit().remove(FORECAST_LIST_KEY).apply()
    }
}