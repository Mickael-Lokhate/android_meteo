package com.lokhate.meteo.utils

import android.content.Context
import android.location.Geocoder
import android.util.Log
import com.lokhate.meteo.R
import java.util.Locale

object LocationHelper {
    fun getAddress(context: Context, latitude: Double, longitude: Double): Pair<String, String> {
        val geocoder = Geocoder(context, Locale.getDefault())
        val addresses = geocoder.getFromLocation(latitude, longitude, 1)

        return if (!addresses.isNullOrEmpty()) {
            val address = addresses[0]
            val city = address.locality ?: "Unknown"
            val country = address.countryName ?: "Unknown"
            Pair(city, country)
        } else {
            Pair(context.getString(R.string.unknown_city), context.getString(R.string.unknown_country))
        }
    }
}